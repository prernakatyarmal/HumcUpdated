package com.hackensack.umc.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.hackensack.umc.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by gaurav_salunkhe on 9/15/2015.
 */

public class DoctorResultGridAdaptor extends RecyclerView.Adapter<DoctorResultGridAdaptor.ViewHolder> {


    private OnGridItemClickListener.OnGridItemClickCallback onItemClickCallback;
    private OnLastItemReachListener.OnLastItemReachCallback onLastItemReachCallback;
    private Context mContext;
    private boolean isFavorite;
    private MyDoctorDataSource myDoctorDataSource;
    // public static int FAVORITE_COUNT = 5;
    private Location mCurrentLocation;

    List<DoctorDetails> mItems;
    private DirectionList directionListDTO;
    private AlertDialog alert;

    private int lastPosition = -1;

    private ArrayList<Integer> mPages = new ArrayList<Integer>();
    private ArrayList<Integer> mImageUrl = new ArrayList<Integer>();

    String mSelectedZipcode = null;

    public DoctorResultGridAdaptor(Context context, List<DoctorDetails> list, OnGridItemClickListener.OnGridItemClickCallback onItemClickCallback, OnLastItemReachListener.OnLastItemReachCallback onLastItemReachCallback, boolean isfav, MyDoctorDataSource dataSource, String zipcode) {

        super();
        mItems = list;
        this.onItemClickCallback = onItemClickCallback;
        this.onLastItemReachCallback = onLastItemReachCallback;
        mContext = context;
        myDoctorDataSource = dataSource;
        isFavorite = isfav;
        mCurrentLocation = Util.getLocation(mContext);

        mSelectedZipcode = zipcode;

        mPages = new ArrayList<Integer>();
        mImageUrl = new ArrayList<Integer>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_result_item, parent, false);
        v.setTag(R.id.dist_text, v.findViewById(R.id.dist_text));
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final DoctorDetails doctorItem = mItems.get(position);

        if ((mItems.size() - 1) == position) {
            viewHolder.itemLoading.setVisibility(View.VISIBLE);
            if (!mPages.contains(position)) {
                if (onLastItemReachCallback.onLastItemReach(viewHolder.itemLoading, position)) {
//                    viewHolder.itemLoading.setVisibility(View.VISIBLE);
                    mPages.add(position);
                } else {
                    viewHolder.itemLoading.setVisibility(View.GONE);
                }
            }
        } else {
            viewHolder.itemLoading.setVisibility(View.GONE);
        }

        viewHolder.specialityText.setText(doctorItem.getDoctorSpeciality());
        viewHolder.nameText.setText(doctorItem.getDoctorFullName());

        Editable phoneNum = null;

        if (isFavorite) {
//            viewHolder.addressText.setText(doctorItem.getmFirstAddress());
            phoneNum = new SpannableStringBuilder(doctorItem.getmPhoneNum());
        } else {
//            viewHolder.addressText.setText(doctorItem.getDoctorFirstAddress());
            phoneNum = new SpannableStringBuilder(doctorItem.getDoctorFirstPhone());
        }

        if (phoneNum != null && phoneNum.length() > 0) {
            PhoneNumberUtils.formatNumber(phoneNum, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
            viewHolder.telephoneText.setText(phoneNum);
            viewHolder.telephoneText.setVisibility(View.VISIBLE);
            viewHolder.phoneIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.telephoneText.setVisibility(View.INVISIBLE);
            viewHolder.phoneIcon.setVisibility(View.INVISIBLE);
        }

        doctorItem.getDoctorImage().setDoctorImg(mContext, viewHolder.doctorImg, mImageUrl);

        /**
         * Favorite Code
         */
        boolean isfav = myDoctorDataSource.isFavoriteDoc(doctorItem.getDoctorHumcId());
        if (isFavorite || isfav) {
            viewHolder.favIcon.setImageResource(R.drawable.favorite_icon_active);
        } else {
            viewHolder.favIcon.setImageResource(R.drawable.favorite_icon_normal);
        }


        if (!isFavorite) {
            viewHolder.favlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isfavorite = myDoctorDataSource.isFavoriteDoc(doctorItem.getDoctorHumcId());
                    if (isfavorite) {

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
                                alert.dismiss();
                            }
                        });

                        Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_cancel);
                        btnDismiss.setVisibility(View.VISIBLE);
                        btnDismiss.setText(mContext.getString(R.string.button_no));
                        btnDismiss.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                viewHolder.favIcon.setImageResource(R.drawable.favorite_icon_active);
                                alert.dismiss();
                            }
                        });

                        alert = builder.show();

                        viewHolder.favIcon.setImageResource(R.drawable.favorite_icon_normal);
                    } else {
                        //Code to limit favorites to 5
                        // int favCount = myDoctorDataSource.getMyDoctorCount();
                        // if (favCount < FAVORITE_COUNT) {
                        long id = myDoctorDataSource.addDoctorToFavorite(doctorItem);
                        doctorItem.setDbEntryId(id);
                        notifyDataSetChanged();
                        viewHolder.favIcon.setImageResource(R.drawable.favorite_icon_active);

                    }
                }
            });
        }


        final String number = phoneNum.toString();
        viewHolder.itemArea.setOnClickListener(new OnGridItemClickListener(position, onItemClickCallback));
        viewHolder.telephoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                mContext.startActivity(callIntent);

            }
        });

        String address = null;
        if (isFavorite) {
            address = doctorItem.getmFirstAddress();
        } else {
            if (mSelectedZipcode != null && mSelectedZipcode.length() > 1) {
                address = doctorItem.getDoctorFirstAddressWithZipcode(mSelectedZipcode);
            } else {
                address = doctorItem.getDoctorFirstAddress();
            }
        }

        viewHolder.addressText.setText(address);

        if (address != null && address.length() > 0) {

            viewHolder.directionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String addrStr = "";
                    if (isFavorite) {
                        addrStr = doctorItem.getmFirstAddress();
                    } else {
                        addrStr = doctorItem.getDoctorFirstAddress();
                    }
                    addrStr = addrStr.replaceAll("[\n\r]", "");
                    LatLng addr = Util.getLocationFromAddress(addrStr, mContext);
                    directionListDTO = new DirectionList(doctorItem.getDoctorFullName(), addrStr, "", addr.latitude, addr.longitude);
                    Intent intent = new Intent(mContext, DirectionMapsActivity.class);
                    intent.putExtra(DirectionsListActivity.EXTRA_DIRECTION_OBJ, directionListDTO);
                    mContext.startActivity(intent);

                }
            });

            //Code to set Distance in miles for the doctor address
            if (TextUtils.isEmpty(doctorItem.getmDocDistance())) {
                String addr = isFavorite ? doctorItem.getmFirstAddress() : doctorItem.getDoctorFirstAddress();
                DistanceLoader mDistanceLoader = new DistanceLoader(viewHolder.mileText, position);
                mDistanceLoader.execute(viewHolder.mileText, addr, mContext);
            } else {
                viewHolder.mileText.setText(doctorItem.getmDocDistance());
            }
            viewHolder.mileText.setVisibility(View.VISIBLE);
            viewHolder.directionText.setVisibility(View.VISIBLE);
            viewHolder.directionIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mileText.setVisibility(View.INVISIBLE);
            viewHolder.directionText.setVisibility(View.INVISIBLE);
            viewHolder.directionIcon.setVisibility(View.INVISIBLE);
        }


//        viewHolder.itemLoading.setOnClickListener(new OnGridItemClickListener(position, onItemClickCallback));
        viewHolder.itemLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        viewHolder.startAnimation(animation);
//        lastPosition = position;

//        viewHolder.itemArea.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(mContext, DoctorDetailActivity.class);
//                Bundle b = new Bundle();
////        b.putSerializable(Constant.DOCTOR_SELECTED,(Serializable) mDoctorsResult.get(position));
//                intent.putExtra(Constant.DOCTOR_SELECTED, (Serializable) mItems.get(position));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        intent.putExtras(b);
//                mContext.startActivity(intent);
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorImg;
        private TextView specialityText;
        private TextView nameText;
        private TextView addressText;
        private TextView telephoneText;
        private TextView directionText;
        private RelativeLayout itemArea;
        private RelativeLayout itemLoading;
        private ImageView favIcon;
        private TextView favText, mileText;
        private ImageView phoneIcon;
        private ImageView directionIcon;
        private LinearLayout favlayout;

        public ViewHolder(View itemView) {

            super(itemView);

            doctorImg = (ImageView) itemView.findViewById(R.id.doctor_img);
            specialityText = (TextView) itemView.findViewById(R.id.speciality_text);
            nameText = (TextView) itemView.findViewById(R.id.name_text);
            addressText = (TextView) itemView.findViewById(R.id.address_one_text);
            telephoneText = (TextView) itemView.findViewById(R.id.call);
            directionText = (TextView) itemView.findViewById(R.id.direction);
            itemArea = (RelativeLayout) itemView.findViewById(R.id.top_layout);
            itemLoading = (RelativeLayout) itemView.findViewById(R.id.bottom_footer);
            favIcon = (ImageView) itemView.findViewById(R.id.fav_icon);
            favText = (TextView) itemView.findViewById(R.id.favorite_tv);
            mileText = (TextView) itemView.findViewById(R.id.dist_text);
            favlayout = (LinearLayout) itemView.findViewById(R.id.fav_layout);
            phoneIcon = (ImageView) itemView.findViewById(R.id.phone_img);
            directionIcon = (ImageView) itemView.findViewById(R.id.direction_img);

        }

    }

    //AsyncTask to calculate the Distance between current position and doctors address
    private class DistanceLoader extends AsyncTask<Object, String, String> {

        private View view;
        private String distance = null;
        private Context context;
        private int position;

        public DistanceLoader(TextView mileText, int pos) {
            view = mileText;
            position = pos;
        }

        @Override
        protected void onPreExecute() {

//            if(view != null)
            ((TextView) view).setText("( " + mContext.getString(R.string.mile_str) + " )");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... parameters) {
            view = (View) parameters[0];
            String addr = (String) parameters[1];
            context = (Context) parameters[2];
            LatLng latLng = Util.getLocationFromAddress(addr, context);
            if (mCurrentLocation != null) {
                double distanceInMile = Util.getDistanceInMiles(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), latLng.latitude, latLng.longitude);
                distance = String.format("%.1f", distanceInMile);

            }

            return distance;
        }

        @Override
        protected void onPostExecute(String distance) {
            if (distance != null && view != null) {
                String dist = "( " + distance + " " + mContext.getString(R.string.mile_str) + " )";
                ((TextView) view).setText(dist);
                if (mItems != null && mItems.size() >= position) {
                    DoctorDetails doctorDetails = mItems.get(position);
                    doctorDetails.setmDocDistance(dist);
                    mItems.set(position, doctorDetails);
                }


            }
        }
    }

}