package com.hackensack.umc.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.Address;
import com.hackensack.umc.datastructure.DirectionList;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.DoctorOfficeDetail;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.ScheduleDetails;
import com.hackensack.umc.datastructure.TimeSlotDetails;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.patient_data.Telecom;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.DistanceLoader;
import com.hackensack.umc.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class DoctorOfficeDetailActivity extends BaseActivity implements HttpDownloadCompleteListener {

    DoctorDetails mDoctorDetail;

    private ImageView mDoctorImage;
    private TextView mDoctorSpeciality;
    private TextView mDoctorFullName;

//    private ProgressBar mProgressBar;

    private ImageView mBackButton, mDoneButton;

    private LinearLayout mAddressLinearLayout;

    private ArrayList<DoctorDetails.Address> mDoctorAddressList;
    //get current location
    Location mCurrentLocation;


    private boolean isReschedulingAppointment = false;
    private String mRescheduleAppointmentID;

    private ArrayList<String> mFilter = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_office_detail);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentLocation = Util.getLocation(DoctorOfficeDetailActivity.this);
        mDoctorImage = (ImageView) findViewById(R.id.doctor_img);
        mDoctorSpeciality = (TextView) findViewById(R.id.speciality_text);
        mDoctorFullName = (TextView) findViewById(R.id.name_text);

        Intent intent = getIntent();

        String doctorID = null;

        if(intent != null && intent.getBooleanExtra(Constant.DOCTOR_APPOINTMENT, false)) {

//            doctorID = intent.getStringExtra(Constant.DOCTOR_SELECTED);
            isReschedulingAppointment = true;
//            mDoctorDetail = (DoctorDetails) intent.getSerializableExtra(Constant.DOCTOR_SELECTED);
            mRescheduleAppointmentID = intent.getStringExtra(Constant.RESCHEDULE_APPOINTMENT_ID);
        }

        mDoctorDetail = (DoctorDetails) intent.getSerializableExtra(Constant.DOCTOR_SELECTED);

        mDoctorSpeciality.setText(mDoctorDetail.getDoctorSpeciality());
        mDoctorFullName.setText(mDoctorDetail.getDoctorFullName());
        mDoctorDetail.getDoctorImage().setDoctorImg(this, mDoctorImage, new ArrayList<Integer>());
//            doctorID = mDoctorDetail.getDoctorNpi();

        doctorID = mDoctorDetail.getDoctorNpi();
//        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (Util.ifNetworkAvailableShowProgressDialog(DoctorOfficeDetailActivity.this, getString(R.string.getting_office_text), true)) {
            HttpDownloader http = new HttpDownloader(this, ServerConstants.PRACTITIONER_TIME_SLOT_URL + doctorID + ServerConstants.PRACTITIONER_TIME_SLOT_URL_PART, Constant.DOCTOR_OFFICE_DATA, this);
            http.startDownloading();
        }else {
//            mProgressBar.setVisibility(View.GONE);
        }

    }


    public void updateUI(ArrayList<DoctorOfficeDetail> doctorOfficeDetail) {

        mAddressLinearLayout = (LinearLayout) findViewById(R.id.address_linear_layout);

//        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
//        Slide slide = new Slide();
//        slide.setSlideEdge(Gravity.TOP);
//        TransitionManager.beginDelayedTransition(root, slide);

        int noAddress = doctorOfficeDetail.size();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (noAddress > 0) {

            for (int count = 0; count < noAddress; count++) {

                final int addressSelected = count;

                View cardView = inflater.inflate(R.layout.activity_doctor_office_cardview, null, false);
                TextView addressFirst = (TextView) cardView.findViewById(R.id.address_text1);
                TextView phoneTextView = (TextView) cardView.findViewById(R.id.call_display);
                TextView firstTimeTextView = (TextView) cardView.findViewById(R.id.first_available_time);
                TextView moreTimeTextView = (TextView) cardView.findViewById(R.id.more_available_time);
                final TextView distanceTv = (TextView) cardView.findViewById(R.id.distance_text);
                TextView directionTv = (TextView) cardView.findViewById(R.id.direction_display);
                final DoctorOfficeDetail doctorOfficeDetailItem = doctorOfficeDetail.get(count);

                ArrayList<ScheduleDetails> schedules = doctorOfficeDetailItem.getSchedules();
                ScheduleDetails firstSchedule = null;

                int firstScheduleTime = 999;
                int totalTimeSlots = 0;

                for (ScheduleDetails scheduleItem : schedules) {

                    if (firstScheduleTime >= Integer.parseInt(scheduleItem.getID())) {

                        firstScheduleTime = Integer.parseInt(scheduleItem.getID());
                        firstSchedule = scheduleItem;

                    }

                    totalTimeSlots = totalTimeSlots + scheduleItem.getSlots().size();

                }


                if(firstSchedule != null) {

                    TimeSlotDetails firstTimeSlot = null;

                    ArrayList<TimeSlotDetails> slotArray = firstSchedule.getSlots();
                    for (TimeSlotDetails slotItem : slotArray) {

                        if (slotItem.getFreeBusyType().equals(Constant.KEY_FREE)) {

                            if (firstTimeSlot == null /*|| Long.parseLong(firstTimeSlot.getID()) < Long.parseLong(slotItem.getID())*/) {

                                firstTimeSlot = slotItem;

                            }

                        }

                    }

//                DateFormat formatter = new SimpleDateFormat("hh:mm a, MMM dd, EEE");
                    DateFormat formatter = new SimpleDateFormat("hh:mm a, EEE MMM dd ");

                    Date d = null;
                    try {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        //                d = sdf.parse("2015-11-19T08:00:00.000-05:00Z");
                        d = sdf.parse(firstTimeSlot.getStart());
                    } catch (Exception e) {
                        Log.e("Date", " " + e);
                    }

                    Log.e("Date", " " + d);
                    firstTimeTextView.setText(formatter.format(d.getTime()));
                    moreTimeTextView.setText((totalTimeSlots - 1) + " " + getResources().getString(R.string.more_available_time_text));

                }else {
                    firstTimeTextView.setVisibility(View.GONE);
                    (cardView.findViewById(R.id.first_available_time_label)).setVisibility(View.INVISIBLE);
                    moreTimeTextView.setText("No time slots");
//                    ((View) cardView.findViewById(R.id.line_view)).setVisibility(View.GONE);
                }
                //            Date d = new Date(milliSeconds);
                //            Date d = new Date("2015-11-19T08:00:00.000-05:00");
                //            formatter.setTimeZone(TimeZone.getTimeZone("UTC-5"));
                //            firstTimeTextView.setText(formatter.format(d.getTime()));

                //            String date = DateFormat.getDateInstance(DateFormat.LONG).format(Long.parseLong((firstTimeSlot.getID())));

                //            String strCurrentDate = firstTimeSlot.getStart();
                //            String date = (strCurrentDate.split("T"))[0] ;
                //            try {
                //
                //                SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //                Date MyDate = newDateFormat.parse(date);
                //                newDateFormat.applyPattern("MMM d, EEE");
                //                String MyNewDate = newDateFormat.format(MyDate);
                //
                //            }catch(Exception e) {
                //
                //                Log.i("Date", "" + e.toString());
                //
                //            }



                String mDoctorFullAddress = "";

                Address address = doctorOfficeDetailItem.getAddress();
                String street1 = address.getStreet1();
                String street2 = address.getStreet2();
                String city = address.getCity();
                String state = address.getState();
                String zipcode = address.getZip();

               if (street1 != null && street1.length() > 0) {
                    mDoctorFullAddress = street1;
                }

                if (street2 != null && street2.length() > 0) {
                    mDoctorFullAddress = mDoctorFullAddress + ", " + street2;
                }

//                if (city != null && city.length() > 0) {
//                    mDoctorFullAddress = mDoctorFullAddress + "\n" + city;
//                }

                if(state != null && state.length() > 0) {
                    mDoctorFullAddress = mDoctorFullAddress + "\n" +state;
                }

                if (zipcode != null && zipcode.length() > 0) {
                    mDoctorFullAddress = mDoctorFullAddress + ", " + zipcode;
                }

                addressFirst.setText(mDoctorFullAddress);

                final String passingAddress = mDoctorFullAddress;
                //Get distance in miles Context context, TextView mileText, Location currentLocation,String address
                DistanceLoader mDistanceLoader = new DistanceLoader(DoctorOfficeDetailActivity.this, distanceTv, mCurrentLocation, passingAddress);
                mDistanceLoader.execute();
                //Direction code
                directionTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng addr = Util.getLocationFromAddress(passingAddress, DoctorOfficeDetailActivity.this);
                        DirectionList directionListDTO = new DirectionList(mDoctorDetail.getDoctorFullName(), passingAddress, "", addr.latitude, addr.longitude);
                        Intent intent = new Intent(DoctorOfficeDetailActivity.this, DirectionMapsActivity.class);
                        intent.putExtra(DirectionsListActivity.EXTRA_DIRECTION_OBJ, directionListDTO);
                        startActivity(intent);
                    }
                });
                ArrayList<Telecom> telecom = doctorOfficeDetailItem.getTelephone();
                String phonestr = null;
                if (telecom.size() > 0)
                    phonestr = telecom.get(0).getValue();

                final String phone = phonestr;

                final Editable doctorPhoneNum = new SpannableStringBuilder(phone);
                PhoneNumberUtils.formatNumber(doctorPhoneNum, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                phoneTextView.setText(doctorPhoneNum);
                phoneTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        startActivity(callIntent);

                    }
                });


                final ScheduleDetails schedule = firstSchedule;
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(schedule == null) {

                            Util.showNetworkOfflineDialog(DoctorOfficeDetailActivity.this, getString(R.string.office_title), getString(R.string.office_description));

                        }else {

//                            Intent intent = new Intent(DoctorOfficeDetailActivity.this, DoctorDetailActivity.class);
                            Intent intent = new Intent(DoctorOfficeDetailActivity.this, VisitTypeListActivity.class);
                            intent.putExtra(Constant.DOCTOR_SELECTED, mDoctorDetail);
                            intent.putExtra(Constant.DOCTOR_ADDRESS_SELECTED, doctorOfficeDetailItem);
                            intent.putExtra(Constant.DOCTOR_ADDRESS, passingAddress);
                            intent.putExtra(Constant.DOCTOR_TELEPHONE, doctorPhoneNum.toString());
                            intent.putExtra(Constant.DOCTOR_DISTANCE_MILE, distanceTv.getText());
//                            intent.putExtra(Constant.APPOINTMENT_FILTER, doctorOfficeDetailItem.getVisitTypeFilter());
                            if (isReschedulingAppointment) {
                                intent.putExtra(Constant.DOCTOR_APPOINTMENT, true);
                                intent.putExtra(Constant.RESCHEDULE_APPOINTMENT_ID, mRescheduleAppointmentID);
                            }
                            startActivity(intent);
                        }
                    }
                });

                mAddressLinearLayout.addView(cardView);
            }

        } else {

            ArrayList<DoctorDetails.Address> doctor = mDoctorDetail.getAddress();
            noAddress = doctor.size();

            for (int count = 0; count < noAddress; count++) {

                final int addressSelected = count;

                View cardView = inflater.inflate(R.layout.activity_doctor_office_cardview, null, false);
                TextView addressFirst = (TextView) cardView.findViewById(R.id.address_text1);
                TextView phoneTextView = (TextView) cardView.findViewById(R.id.call_display);
                final TextView distanceTv = (TextView) cardView.findViewById(R.id.distance_text);
                TextView directionText = (TextView) cardView.findViewById(R.id.direction_display);

                (cardView.findViewById(R.id.line_view)).setVisibility(View.GONE);
                (cardView.findViewById(R.id.first_available_time_label)).setVisibility(View.GONE);
                (cardView.findViewById(R.id.first_available_time)).setVisibility(View.GONE);
                (cardView.findViewById(R.id.more_available_time)).setVisibility(View.GONE);


                DoctorDetails.Address doctorOfficeDetailItem = doctor.get(count);

                String mDoctorFullAddress = doctorOfficeDetailItem.getAddress();

                if (mDoctorFullAddress == null || mDoctorFullAddress.length() <= 4) {
                    continue;
                }

                addressFirst.setText(mDoctorFullAddress);

                final String passingAddress = mDoctorFullAddress;
                //Get distance in miles Context context, TextView mileText, Location currentLocation,String address
                DistanceLoader mDistanceLoader = new DistanceLoader(DoctorOfficeDetailActivity.this, distanceTv, mCurrentLocation, passingAddress);
                mDistanceLoader.execute();

                final String phone = doctorOfficeDetailItem.getPhone();

                final Editable doctorPhoneNum = new SpannableStringBuilder(phone);
                PhoneNumberUtils.formatNumber(doctorPhoneNum, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                phoneTextView.setText(doctorPhoneNum);
                phoneTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        startActivity(callIntent);

                    }
                });

                directionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng addr = Util.getLocationFromAddress(passingAddress, DoctorOfficeDetailActivity.this);
                        String str = passingAddress.replaceAll("[\n\r]", "");
                        DirectionList directionListDTO = new DirectionList(mDoctorDetail.getDoctorFullName(), str, "", addr.latitude, addr.longitude);
                        Intent intent = new Intent(DoctorOfficeDetailActivity.this, DirectionMapsActivity.class);
                        intent.putExtra(DirectionsListActivity.EXTRA_DIRECTION_OBJ, directionListDTO);
                        startActivity(intent);
                    }
                });

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.showNetworkOfflineDialog(DoctorOfficeDetailActivity.this, getString(R.string.office_title), getString(R.string.office_description));
                    }
                });

                mAddressLinearLayout.addView(cardView);

            }

            Util.showNetworkOfflineDialog(DoctorOfficeDetailActivity.this, getString(R.string.office_title), getString(R.string.office_description));

        }

//        mProgressBar.setVisibility(View.GONE);


    }

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        Object obj = data.getDataObject();

        if (obj != null && !isFinishing()) {

            updateUI((ArrayList<DoctorOfficeDetail>) obj);
//            updateUI((ArrayList<DoctorOfficeDetail>) (obj.getDataObject()));
//            mFilter = (ArrayList<String>) obj.getFilter();

        }else {
            Util.showNetworkOfflineDialog(DoctorOfficeDetailActivity.this, getString(R.string.error_text), getString(R.string.error_description));
        }
        stopProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("Doctor Office onResume", "isUserLogout -> "+isUserLogout);

        if (Util.isActivityFinished || isUserLogout) {
            finish();
            return ;
        }

        if(isAppointmentDone) {

            if(isReschedulingAppointment) {
                finish();
                return;
            }else {
                isAppointmentDone = false;

                if(mAddressLinearLayout != null)
                    mAddressLinearLayout.removeAllViews();

                if (Util.ifNetworkAvailableShowProgressDialog(DoctorOfficeDetailActivity.this, getString(R.string.loading_text), true)) {
                    HttpDownloader http = new HttpDownloader(this, ServerConstants.PRACTITIONER_TIME_SLOT_URL + mDoctorDetail.getDoctorNpi() + ServerConstants.PRACTITIONER_TIME_SLOT_URL_PART, Constant.DOCTOR_OFFICE_DATA, this);
                    http.startDownloading();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_doctor_result, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
