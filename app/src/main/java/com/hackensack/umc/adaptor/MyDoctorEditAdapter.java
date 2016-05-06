package com.hackensack.umc.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hackensack.umc.R;
import com.hackensack.umc.activity.DirectionMapsActivity;
import com.hackensack.umc.activity.DirectionsListActivity;
import com.hackensack.umc.datastructure.DirectionList;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.db.MyDoctorDataSource;
import com.hackensack.umc.listener.OnGridItemClickListener;
import com.hackensack.umc.listener.OnLastItemReachListener;
import com.hackensack.umc.util.DistanceLoader;
import com.hackensack.umc.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by bhagyashree_kumawat on 10/21/2015.
 */
public class MyDoctorEditAdapter extends RecyclerView.Adapter<MyDoctorEditAdapter.ViewHolder> {
    private Context mContext;
    List<DoctorDetails> mFavorites;
    private MyDoctorDataSource myDoctorDataSource;
    private OnLastItemReachListener.OnLastItemReachCallback onLastItemReachCallback;
    private OnGridItemClickListener.OnGridItemClickCallback onItemClickCallback;
    private AlertDialog alert;
    private Location mCurrentLocation;

    public MyDoctorEditAdapter(List<DoctorDetails> doctorDetails, MyDoctorDataSource dataSource, Context context, OnLastItemReachListener.OnLastItemReachCallback lastItemCallback, OnGridItemClickListener.OnGridItemClickCallback onItemClickedCallback) {
        myDoctorDataSource = dataSource;
        mFavorites = doctorDetails;
        mContext = context;
        onLastItemReachCallback = lastItemCallback;
        onItemClickCallback = onItemClickedCallback;
        mCurrentLocation = Util.getLocation(mContext);
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mydoc_edit, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        {

            final DoctorDetails doctorItem = mFavorites.get(position);
            holder.specialityText.setText(doctorItem.getDoctorSpeciality());
            holder.nameText.setText(doctorItem.getDoctorFullName());
            final Editable phoneNum;

            holder.addressText.setText(doctorItem.getmFirstAddress());
            phoneNum = new SpannableStringBuilder(doctorItem.getmPhoneNum());

            if (phoneNum != null && phoneNum.length() > 0) {
                PhoneNumberUtils.formatNumber(phoneNum, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                holder.telephoneText.setText(phoneNum);
                holder.telephoneText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                        mContext.startActivity(callIntent);

                    }
                });
                holder.telephoneText.setVisibility(View.VISIBLE);
                holder.phoneIcon.setVisibility(View.VISIBLE);
            } else {
                holder.telephoneText.setVisibility(View.INVISIBLE);
                holder.phoneIcon.setVisibility(View.INVISIBLE);
            }

            doctorItem.getDoctorImage().setDoctorImg(mContext, holder.doctorImg, new ArrayList<Integer>());

            String address = null;
            address = doctorItem.getmFirstAddress();

            if (address != null && address.length() > 0) {
                holder.directionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String addrStr = doctorItem.getmFirstAddress();
                        addrStr = addrStr.replaceAll("[\n\r]", "");
                        LatLng addr = Util.getLocationFromAddress(addrStr, mContext);
                        DirectionList directionListDTO = new DirectionList(doctorItem.getDoctorFullName(), addrStr, "", addr.latitude, addr.longitude);
                        Intent intent = new Intent(mContext, DirectionMapsActivity.class);
                        intent.putExtra(DirectionsListActivity.EXTRA_DIRECTION_OBJ, directionListDTO);
                        mContext.startActivity(intent);

                    }
                });
                if (TextUtils.isEmpty(doctorItem.getmDocDistance())) {
                    String addr = doctorItem.getmFirstAddress();
                    DistanceLoader mDistanceLoader = new DistanceLoader(mContext, holder.mileTv, mCurrentLocation, addr);
                    mDistanceLoader.execute(holder.mileTv, addr, mContext);
                } else {
                    holder.mileTv.setText(doctorItem.getmDocDistance());
                }
                holder.mileTv.setVisibility(View.VISIBLE);
                holder.directionText.setVisibility(View.VISIBLE);
                holder.directionIcon.setVisibility(View.VISIBLE);
            } else {
                holder.mileTv.setVisibility(View.INVISIBLE);
                holder.directionText.setVisibility(View.INVISIBLE);
                holder.directionIcon.setVisibility(View.INVISIBLE);
            }

            holder.itemArea.setOnClickListener(new OnGridItemClickListener(position, onItemClickCallback));
            holder.deleteLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
                    builder.setView(dialogView);

                    ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(mContext.getString(R.string.remove_fav_title));
                    ((TextView) dialogView.findViewById(R.id.text_message)).setText(mContext.getString(R.string.remove_fav_msg_first) + " " + doctorItem.getDoctorFullName() + " " + mContext.getString(R.string.remove_fav_msg_second));

                    Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
                    btnOk.setText(mContext.getString(R.string.button_yes));
                    btnOk.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            myDoctorDataSource.deleteMyDoctor(doctorItem);
                            if (mFavorites != null && mFavorites.contains(doctorItem)) {
                                if (mFavorites.size() == 1) {
                                    onLastItemReachCallback.OnAllListItemDeleted();
                                }
                                mFavorites.remove(position);
                                notifyDataSetChanged();
                            }
                            alert.dismiss();
                        }
                    });

                    Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_cancel);
                    btnDismiss.setVisibility(View.VISIBLE);
                    btnDismiss.setText(mContext.getString(R.string.button_no));
                    btnDismiss.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });

                    alert = builder.show();

                }
            });


        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorImg;
        private TextView specialityText;
        private TextView nameText;
        private TextView addressText;
        private TextView telephoneText;
        private LinearLayout deleteLL;
        private TextView directionText, mileTv;
        private ImageView phoneIcon;
        private ImageView directionIcon;
        private RelativeLayout itemArea;

        public ViewHolder(View itemView) {

            super(itemView);
            doctorImg = (ImageView) itemView.findViewById(R.id.edit_doctor_img);
            specialityText = (TextView) itemView.findViewById(R.id.speciality_tv);
            nameText = (TextView) itemView.findViewById(R.id.name_tv);
            addressText = (TextView) itemView.findViewById(R.id.address_one_tv);
            telephoneText = (TextView) itemView.findViewById(R.id.call_tv);
            deleteLL = (LinearLayout) itemView.findViewById(R.id.delete_ll);
            directionText = (TextView) itemView.findViewById(R.id.direction);
            mileTv = (TextView) itemView.findViewById(R.id.dist_text_tv);
            itemArea = (RelativeLayout) itemView.findViewById(R.id.top_layout_edit);
            phoneIcon = (ImageView) itemView.findViewById(R.id.phone_img);
            directionIcon = (ImageView) itemView.findViewById(R.id.direction_img);
        }
    }
}
