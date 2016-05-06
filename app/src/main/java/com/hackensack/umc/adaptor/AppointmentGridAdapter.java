package com.hackensack.umc.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hackensack.umc.R;
import com.hackensack.umc.activity.DirectionMapsActivity;
import com.hackensack.umc.activity.DirectionsListActivity;
import com.hackensack.umc.datastructure.AppointmentDetails;
import com.hackensack.umc.datastructure.DirectionList;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.LocationDetails;
import com.hackensack.umc.listener.OnAppointmentButtonListener;
import com.hackensack.umc.util.DistanceLoader;
import com.hackensack.umc.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bhagyashree_kumawat on 11/24/2015.
 */
public class AppointmentGridAdapter extends RecyclerView.Adapter<AppointmentGridAdapter.ViewHolder> {

    List<AppointmentDetails> mItems;
//    private OnGridItemClickListener.OnGridItemClickCallback onItemClickCallback;
    private OnAppointmentButtonListener.OnAppointmentButtonCallback onAppointmentButtonCallback;
    private Context mContext;
    //get current location
    private Location mCurrentLocation;

    public AppointmentGridAdapter(Context context, List<AppointmentDetails> list, OnAppointmentButtonListener.OnAppointmentButtonCallback appointmentButtonCallback) {

        super();
        mContext = context;
        mItems = list;
        this.onAppointmentButtonCallback = appointmentButtonCallback;
        mCurrentLocation = Util.getLocation(mContext);

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final AppointmentDetails appointmentItem = mItems.get(position);
        final DoctorDetails doctor = appointmentItem.getDoctorDetail();

        LocationDetails location = appointmentItem.getLocation();

        Editable phoneNum = null;

        phoneNum = new SpannableStringBuilder(location.getTelephone().get(0).getValue());

        PhoneNumberUtils.formatNumber(phoneNum, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));

        if(doctor != null) {
            viewHolder.specialityText.setText(doctor.getDoctorSpeciality());
            viewHolder.nameText.setText(doctor.getDoctorFullName());
            doctor.getDoctorImage().setDoctorImg(mContext, viewHolder.doctorImg, new ArrayList<Integer>());
        }

        viewHolder.telephoneText.setText(phoneNum);
        final String number = phoneNum.toString();
        viewHolder.telephoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                mContext.startActivity(callIntent);

            }
        });
        final String addrStr = location.getAddress().toString();
        viewHolder.addressText.setText(location.getAddress().getAppointmentAddress());
        //Need to set doctor name
        viewHolder.directImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addrString = addrStr.replaceAll("[\n\r]", "");
                LatLng addr = Util.getLocationFromAddress(addrString, mContext);
                DirectionList directionListDTO = new DirectionList(doctor.getDoctorFullName(), addrString, "", addr.latitude, addr.longitude);
                Intent intent = new Intent(mContext, DirectionMapsActivity.class);
                intent.putExtra(DirectionsListActivity.EXTRA_DIRECTION_OBJ, directionListDTO);
                mContext.startActivity(intent);
            }
        });
        DateFormat formatterTime = new SimpleDateFormat("hh:mm a");
        DateFormat formatterDate = new SimpleDateFormat("EEE, MMM dd yyyy");

        Date d = null;
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//                d = sdf.parse("2015-11-19T08:00:00.000-05:00Z");
            d = sdf.parse(appointmentItem.getStartDate());
        }catch(Exception e) {
            Log.e("Date", " " + e);
        }


        long currenttime = System.currentTimeMillis();
        final long appointmentTime = d.getTime();
        long one_hour = currenttime + 3600000;

//        Log.i("Item -> "+position, "Current Time -> "+currenttime+", Appointment Time -> "+appointmentTime+", +1 Hour -> "+one_hour);

        if(appointmentTime < currenttime) {
            viewHolder.scheduleText.setText(mContext.getResources().getString(R.string.completed_text));
            viewHolder.scheduleText.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.cancelImg.setVisibility(View.GONE);
            viewHolder.editImg.setVisibility(View.GONE);
            viewHolder.directImg.setVisibility(View.GONE);
        }else {
            if(appointmentTime < one_hour) {
                viewHolder.scheduleText.setText(mContext.getResources().getString(R.string.arrived_text));
                viewHolder.scheduleText.setTextColor(mContext.getResources().getColor(R.color.accentColor));
                viewHolder.cancelImg.setVisibility(View.GONE);
                viewHolder.editImg.setVisibility(View.GONE);
                viewHolder.directImg.setVisibility(View.VISIBLE);
            }else {
                viewHolder.scheduleText.setText(mContext.getResources().getString(R.string.schedule_text));
                viewHolder.scheduleText.setTextColor(mContext.getResources().getColor(R.color.light_blue));
                viewHolder.cancelImg.setVisibility(View.VISIBLE);
                viewHolder.editImg.setVisibility(View.VISIBLE);
                viewHolder.directImg.setVisibility(View.VISIBLE);
            }
        }

        //Appointment Date
        viewHolder.timeTv.setText(formatterTime.format(appointmentTime));
        viewHolder.dateTv.setText(formatterDate.format(appointmentTime));
        //Get distance in miles Context context, TextView mileText, Location currentLocation,String address
        DistanceLoader mDistanceLoader = new DistanceLoader(mContext, viewHolder.milesText, mCurrentLocation, addrStr);
        mDistanceLoader.execute();


        final long one_day = currenttime + 86400000;

        final int pos = position;
        viewHolder.cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(appointmentTime > one_day) {
                    onAppointmentButtonCallback.onCancelButtonCallback(view, pos);
                }else {
                    Util.showNetworkOfflineDialog((Activity) mContext, "Error", "Cannot cancel appointment scheduled to happen within next 24 hours, Please call front desk to cancel appointment.");
                }

            }
        });
        viewHolder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(appointmentTime > one_day) {
                    onAppointmentButtonCallback.onRescheduleButtonCallback(view, pos);
                }else {
                    Util.showNetworkOfflineDialog((Activity) mContext, "Error", "Cannot reschedule appointment scheduled to happen within next 24 hours, Please call front desk to reschedule appointment.");
                }
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorImg;
        private TextView specialityText;
        private TextView nameText;
        private TextView addressText;
        private TextView telephoneText;
        private TextView milesText;
        private RelativeLayout itemArea;
        private ImageView directImg;
        private TextView dateTv,timeTv;
        private ImageView cancelImg, editImg;
        private TextView scheduleText;

        public ViewHolder(View itemView) {
            super(itemView);
            doctorImg = (ImageView) itemView.findViewById(R.id.doctor_img);
            specialityText = (TextView) itemView.findViewById(R.id.speciality_text);
            nameText = (TextView) itemView.findViewById(R.id.name_text);
            addressText = (TextView) itemView.findViewById(R.id.address_one_text);
            telephoneText = (TextView) itemView.findViewById(R.id.phone_num_tv);
            milesText = (TextView) itemView.findViewById(R.id.mile_tv);
            directImg = (ImageView) itemView.findViewById(R.id.direct_img);
            dateTv = (TextView)itemView.findViewById(R.id.app_date_tv);
            timeTv = (TextView)itemView.findViewById(R.id.app_time_tv);

            cancelImg = (ImageView) itemView.findViewById(R.id.delete_img);
            editImg = (ImageView) itemView.findViewById(R.id.edit_img);

            scheduleText = (TextView) itemView.findViewById(R.id.schedule_text);
        }
    }
}
