package com.hackensack.umc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hackensack.umc.R;
import com.hackensack.umc.customview.SlidingTabLayout;
import com.hackensack.umc.datastructure.DirectionList;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.DoctorOfficeDetail;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.db.MyDoctorDataSource;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class DoctorAppointmentSummaryActivity extends BaseActivity implements HttpDownloadCompleteListener {

    private DoctorDetails mDoctorDetail;
    private DoctorOfficeDetail mDoctorOfficeSelected;

    private ImageView mDoctorImage;
    private TextView mDoctorSpeciality;
    private TextView mDoctorFullName;
    private TextView mDoctorAddress;
    private TextView mDoctorTelephone;
    private FrameLayout frameLayout;
    ViewPager mViewPager;
    SlidingTabLayout mSlidingTabLayout;

    String mDoctorFullAddress, mDoctorPhoneNum, mAppointmentTime, mAppointmentType, mAppointmentID, mDistInMile;

    Button mChangeAppointmentButton, mMakeAppointmentButton, mDoneButton;

    ImageView mBackButton, mFavIcon;

    LinearLayout mChangeMakeLayout, mDoneLayout;

    private AlertDialog alert;
    private float top = 0;

    private ProgressDialog mProgressDialog;
    private MyDoctorDataSource myDoctorDataSource;

    boolean isReschedulingAppointment = false;
    private String mRescheduleAppointmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout = new FrameLayout(this);
        // creating LayoutParams
        FrameLayout.LayoutParams frameLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(frameLayout, frameLayoutParam);
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        View mainView = layoutInflater.inflate(R.layout.activity_doctor_appointement_summary, null, false);
        frameLayout.addView(mainView);

        //setContentView(R.layout.activity_doctor_appointement_summary);

//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayShowHomeEnabled(true);
//        bar.setIcon(R.drawable.filter_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().hide();
//        getSupportActionBar().setCustomView(R.layout.activity_doctor_detail);

        mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (top == 0) {
                    top = mMakeAppointmentButton.getBottom();
                    showHelpView(layoutInflater);
                }
            }
        });
        /**
         * Favorite Code
         */
        myDoctorDataSource = new MyDoctorDataSource(DoctorAppointmentSummaryActivity.this);
        try {
            myDoctorDataSource.open();
        } catch (SQLException e) {
            Log.e("DoctorResultActivity", "Exception Occured" + e);
        }
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(Constant.DOCTOR_APPOINTMENT, false)) {
            isReschedulingAppointment = true;
            mRescheduleAppointmentID = intent.getStringExtra(Constant.RESCHEDULE_APPOINTMENT_ID);
        }
        mDoctorDetail = (DoctorDetails) intent.getSerializableExtra(Constant.DOCTOR_SELECTED);
        mDoctorOfficeSelected = (DoctorOfficeDetail) intent.getSerializableExtra(Constant.DOCTOR_ADDRESS_SELECTED);
        Util.saveLocationIdinSharePreferances(DoctorAppointmentSummaryActivity.this, mDoctorOfficeSelected.getID());
        mDoctorFullAddress = intent.getStringExtra(Constant.DOCTOR_ADDRESS);
        mDoctorPhoneNum = intent.getStringExtra(Constant.DOCTOR_TELEPHONE);
        mAppointmentTime = intent.getStringExtra(Constant.DOCTOR_APPOINTMENT_TIME);
        mAppointmentType = intent.getStringExtra(Constant.DOCTOR_APPOINTMENT_TYPE);
        mAppointmentID = intent.getStringExtra(Constant.DOCTOR_APPOINTMENT_ID);
        mDistInMile = intent.getStringExtra(Constant.DOCTOR_DISTANCE_MILE);

        mDoctorImage = (ImageView) findViewById(R.id.doctor_img);
        mDoctorSpeciality = (TextView) findViewById(R.id.speciality_text);
        mDoctorFullName = (TextView) findViewById(R.id.name_text);
        mDoctorAddress = (TextView) findViewById(R.id.address_one_text);
        mDoctorTelephone = (TextView) findViewById(R.id.call);
        mFavIcon = (ImageView) findViewById(R.id.fav_icon);
        ((TextView) findViewById(R.id.dist_mile_tv)).setText(mDistInMile);

        if (mDoctorDetail != null) {

            if (mDoctorDetail.getDoctorSpeciality() != null && mDoctorDetail.getDoctorSpeciality().length() > 1)
                mDoctorSpeciality.setText(mDoctorDetail.getDoctorSpeciality());

            if (mDoctorDetail.getDoctorFullName() != null && mDoctorDetail.getDoctorFullName().length() > 1)
                mDoctorFullName.setText(mDoctorDetail.getDoctorFullName());

            mDoctorDetail.getDoctorImage().setDoctorImg(this, mDoctorImage, new ArrayList<Integer>());
        }
        mDoctorAddress.setText(mDoctorFullAddress);
        mDoctorTelephone.setText(mDoctorPhoneNum);
        mDoctorTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mDoctorPhoneNum));
                startActivity(callIntent);

            }
        });

        ( findViewById(R.id.fav_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Everytime isfavdoc value in db changes hence the below call has to be kept in onclick
                final boolean isfavdoc = myDoctorDataSource.isFavoriteDoc(mDoctorDetail.getDoctorHumcId());
                if (isfavdoc) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(DoctorAppointmentSummaryActivity.this);

                    LayoutInflater inflater = DoctorAppointmentSummaryActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
                    builder.setView(dialogView);

                    ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(DoctorAppointmentSummaryActivity.this.getString(R.string.remove_fav_title));
                    ((TextView) dialogView.findViewById(R.id.text_message)).setText(DoctorAppointmentSummaryActivity.this.getString(R.string.remove_fav_msg_first) + " " + mDoctorDetail.getDoctorFullName() + " " + DoctorAppointmentSummaryActivity.this.getString(R.string.remove_fav_msg_second));

                    Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
                    btnOk.setText(DoctorAppointmentSummaryActivity.this.getString(R.string.button_yes));
                    btnOk.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            myDoctorDataSource.deleteMyDoctor(mDoctorDetail);
                            alert.dismiss();
                        }
                    });

                    Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_cancel);
                    btnDismiss.setVisibility(View.VISIBLE);
                    btnDismiss.setText(DoctorAppointmentSummaryActivity.this.getString(R.string.button_no));
                    btnDismiss.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mFavIcon.setImageResource(R.drawable.favorite_icon_active);
                            alert.dismiss();
                        }
                    });

                    alert = builder.show();

                    mFavIcon.setImageResource(R.drawable.favorite_icon_normal);
                } else {

                    long id = myDoctorDataSource.addDoctorToFavorite(mDoctorDetail);
                    mDoctorDetail.setDbEntryId(id);
                    mFavIcon.setImageResource(R.drawable.favorite_icon_active);

                }
            }
        });
        (findViewById(R.id.direction)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng addr = Util.getLocationFromAddress(mDoctorFullAddress, DoctorAppointmentSummaryActivity.this);
                mDoctorFullAddress = mDoctorFullAddress.replaceAll("[\n\r]", "");
                DirectionList directionListDTO = new DirectionList(mDoctorDetail.getDoctorFullName(), mDoctorFullAddress, "", addr.latitude, addr.longitude);
                Intent intent = new Intent(DoctorAppointmentSummaryActivity.this, DirectionMapsActivity.class);
                intent.putExtra(DirectionsListActivity.EXTRA_DIRECTION_OBJ, directionListDTO);
                startActivity(intent);
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
            d = sdf.parse(mAppointmentTime);
        } catch (Exception e) {
            Log.e("Date", " " + e);
        }

        //Bug is there : Null Pointer : Appointment Date
        ((TextView) findViewById(R.id.time_text)).setText(formatterTime.format(d.getTime()) + ", " + formatterDate.format(d.getTime()));
//        ((TextView) findViewById(R.id.date_text)).setText(formatterDate.format(d.getTime()));

        mChangeMakeLayout = (LinearLayout) findViewById(R.id.change_make_layout);
        mDoneLayout = (LinearLayout) findViewById(R.id.done_layout);

        mChangeAppointmentButton = (Button) findViewById(R.id.change_appointment_button);
        mChangeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mMakeAppointmentButton = (Button) findViewById(R.id.make_appointment_button);
        mMakeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (Util.isUserLogin(DoctorAppointmentSummaryActivity.this) && Util.isPatientIdValid(DoctorAppointmentSummaryActivity.this)) {

                    mChangeMakeLayout.setVisibility(View.GONE);

                    if (isReschedulingAppointment) {

                        if (Util.ifNetworkAvailableShowProgressDialog(DoctorAppointmentSummaryActivity.this, getString(R.string.reschedule_appointment), true)) {
                            HttpDownloader http = new HttpDownloader(DoctorAppointmentSummaryActivity.this, ServerConstants.CANCEL_APPOINTMENT_URL + mRescheduleAppointmentID, Constant.RESCHEDULE_APPOINTMENT, DoctorAppointmentSummaryActivity.this);
                            http.startDownloading();
                        }

                    } else {
                        if (Util.ifNetworkAvailableShowProgressDialog(DoctorAppointmentSummaryActivity.this, getString(R.string.getting_appointment), true)) {
                            HttpDownloader http = new HttpDownloader(DoctorAppointmentSummaryActivity.this, ServerConstants.SCHEDULE_APPOINTMENT_URL, Constant.SCHEDULE_APPOINTMENT, DoctorAppointmentSummaryActivity.this);
                            http.startDownloading();
                        }
                    }

                }else if(isMyChartLogin) {
                    mChangeMakeLayout.setVisibility(View.GONE);
                    (findViewById(R.id.display_text)).setVisibility(View.INVISIBLE);

                    Util.showNetworkOfflineDialog(DoctorAppointmentSummaryActivity.this, getResources().getString(R.string.error_str), getResources().getString(R.string.non_mrn_message));

                    mDoneLayout.setVisibility(View.VISIBLE);

                }else {
                    Intent intent = new Intent();
                    intent.setClass(DoctorAppointmentSummaryActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.ACTIVITY_FLOW, Constant.APPOINTMENT_SUMMARY_FLOW);
                    startActivity(intent);
                }

            }
        });

        mDoneButton = (Button) findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int regForProxy = Util.getRegistrationIsForFromSharePref(DoctorAppointmentSummaryActivity.this);
                if(regForProxy == Constant.REGISTRATION_FOR_DEPENDENT){
                    Intent intent = new Intent(DoctorAppointmentSummaryActivity.this,ProxyAccessNavActivity.class);
                    startActivity(intent);
                }
                Util.isActivityFinished = true;
                finish();

            }
        });

//        mBackButton = (ImageView) findViewById(R.id.back_img);
//        mBackButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//
//            }
//        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        if (isReschedulingAppointment) {
            ((TextView) findViewById(R.id.display_text)).setText(getString(R.string.rescheduling_text));
            mMakeAppointmentButton.setText(getString(R.string.rescheduling_button));
        }

        showHelpView(layoutInflater);
    }

    private void updateFavorite() {
        final boolean isfavdoc = myDoctorDataSource.isFavoriteDoc(mDoctorDetail.getDoctorHumcId());
        if (isfavdoc) {
            mFavIcon.setImageResource(R.drawable.favorite_icon_active);
        } else {
            mFavIcon.setImageResource(R.drawable.favorite_icon_normal);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_doctor_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                isBackPressed = true;
                super.onBackPressed();
                //finish();
                return true;

//            case R.id.action_done:
//
//                Intent intent = new Intent(DoctorAppointmentSummaryActivity.this, ProfileActivity.class);
//                startActivity(intent);
//
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//
//        super.onNewIntent(intent);
//
//        boolean success = intent.getBooleanExtra(Constant.KEY_LOGIN_SUCCESSFUL, false);
//
//        if(success) {
//            mChangeMakeLayout.setVisibility(View.GONE);
//        }
//
//        startProgress();
//
//        HttpDownloader http = new HttpDownloader(DoctorAppointmentSummaryActivity.this, Constant.SCHEDULE_APPOINTMENT_URL, Constant.SCHEDULE_APPOINTMENT, this);
//        http.startDownloading();
//
//    }

    public String getAppointmentData(int type) {

        String body = null;

        if (type == Constant.RESCHEDULE_APPOINTMENT) {

            body = "{\"resourceType\": \"Appointment\",\"id\": \"$$APPOINTMENT$$\",\"status\": \"cancelled\",\"participant\": [{\"type\": [{\"coding\": [{\"system\": \"http://www.hl7.org/fhir/v3/ParticipationType/index.html\",\"code\": \"SBJ\"}]}],\"actor\": {\"reference\": \"Patient/$$MRN$$\"}}]}";

            body = body.replace("$$APPOINTMENT$$", mRescheduleAppointmentID);
            body = body.replace("$$MRN$$", Util.getPatientMRNID(this));


        } else {

            body = "{ \"resourceType\": \"Appointment\", \"start\": \"S$$$T\", \"extension\": [ { \"url\": \"VisitTypeID\", \"valueString\": \"ID$$$$\" }, { \"url\": \"VisitTypeIDType\", \"valueString\": \"TYPE$$$$\" } ], \"participant\": [ { \"type\": [ { \"coding\": [ { \"system\": \"http://www.hl7.org/fhir/v3/ParticipationType/index.html\", \"code\": \"SBJ\" } ] } ], \"actor\": { \"reference\": \"Patient/P$$$T\" } }, { \"type\": [ { \"coding\": [ { \"system\": \"http://www.hl7.org/fhir/v3/ParticipationType/index.html\", \"code\": \"PPRF\" } ] } ], \"actor\": { \"reference\": \"Practitioner/P$$$R\" } }, { \"type\": [ { \"coding\": [ { \"system\": \"http://www.hl7.org/fhir/v3/ParticipationType/index.html\", \"code\": \"LOC\" } ] } ], \"actor\": { \"reference\": \"Location/L$$$N\" } } ] }";

            body = body.replace("S$$$T", mAppointmentTime);
            body = body.replace("P$$$T", Util.getPatientMRNID(this));
            body = body.replace("P$$$R", mDoctorDetail.getDoctorNpi());
            body = body.replace("L$$$N", mDoctorOfficeSelected.getID());
            body = body.replace("TYPE$$$$", mAppointmentType);
            body = body.replace("ID$$$$", mAppointmentID);

        }

        return body;

    }

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        if (data != null && !isFinishing()) {

            int httpsServerResponse = data.getResponseCode();

            String title = "";
            String message = "";

            if (data.getRequestType() == Constant.RESCHEDULE_APPOINTMENT) {

                if (httpsServerResponse >= Constant.HTTP_OK && httpsServerResponse < Constant.HTTP_REDIRECT) {

                    HttpDownloader http = new HttpDownloader(DoctorAppointmentSummaryActivity.this, ServerConstants.SCHEDULE_APPOINTMENT_URL, Constant.SCHEDULE_APPOINTMENT, this);
                    http.startDownloading();

                    return;

                } else {

                    title = "Error";
                    message = "Appointment cannot be rescheduled, please call the doctor's office for rescheduling.";

                    (findViewById(R.id.display_text)).setVisibility(View.INVISIBLE);
                    mDoneLayout.setVisibility(View.VISIBLE);
                    isAppointmentDone = true;

                }

            } else {

                if (httpsServerResponse >= Constant.HTTP_OK && httpsServerResponse < Constant.HTTP_REDIRECT) {

                    if (isReschedulingAppointment) {

                        (findViewById(R.id.progress_bar)).setVisibility(View.GONE);
                        mDoneLayout.setVisibility(View.VISIBLE);

                        title = "Success!";
                        message = "Appointment rescheduled successfully.";
                        ((TextView) findViewById(R.id.display_text)).setText("You have successfully rescheduled the appointment");
                        mDoneLayout.setVisibility(View.VISIBLE);
                        isAppointmentDone = true;

                    } else {

                        (findViewById(R.id.progress_bar)).setVisibility(View.GONE);
                        mDoneLayout.setVisibility(View.VISIBLE);

                        title = "Success!";
                        message = "Appointment Scheduled.";
                        ((TextView) findViewById(R.id.display_text)).setText("You have successfully booked this appointment");
                        mDoneLayout.setVisibility(View.VISIBLE);
                        isAppointmentDone = true;
                        if(!isMyChartLogin){
                            Util.setPatientMRNID(this, "");

                        }
                    }

                } else {

                    ( findViewById(R.id.display_text)).setVisibility(View.INVISIBLE);
                    mDoneLayout.setVisibility(View.VISIBLE);
                    isAppointmentDone = true;

                    if (isReschedulingAppointment) {

                        title = "Error";
                        message = "Appointment cannot be rescheduled, please call the doctor's office for rescheduling.";

                    } else {

                        title = "Error";
                        message = "Appointment cannot be booked, Please try a different time.";

//                        ((TextView) findViewById(R.id.display_text)).setVisibility(View.INVISIBLE);
//                        mDoneLayout.setVisibility(View.VISIBLE);
//                        isAppointmentDone = true;
                    }
                }
            }

            stopProgress();

            if (!isFinishing()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorAppointmentSummaryActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
                builder.setView(dialogView);

                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);

                ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

                TextView btnDismiss = (TextView) dialogView.findViewById(R.id.button_dialog_ok);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert = builder.show();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //update the favorite status
        updateFavorite();
        if (isUserLogout) {
            finish();
        }

        if (isLoginDone) {

            isLoginDone = false;
            mChangeMakeLayout.setVisibility(View.GONE);
            if (Util.ifNetworkAvailableShowProgressDialog(DoctorAppointmentSummaryActivity.this, getString(R.string.getting_appointment), true)) {
                HttpDownloader http = new HttpDownloader(DoctorAppointmentSummaryActivity.this, ServerConstants.SCHEDULE_APPOINTMENT_URL, Constant.SCHEDULE_APPOINTMENT, this);
                http.startDownloading();
            }

        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void showHelpView(LayoutInflater layoutInflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_APPIONTMENT_SUMMARY)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_APPIONTMENT_SUMMARY, false);
            final View helpView = layoutInflater.inflate(R.layout.apt_summary_help, null, false);
            (helpView.findViewById(R.id.apt_sum_ll)).setBottom(mMakeAppointmentButton.getTop());
            ((TextView) helpView.findViewById(R.id.apt_sum_arrow)).setTypeface(Util.getArrowFont(this));
            ((TextView) helpView.findViewById(R.id.apt_sum_tv)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.tap_tv)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.apt_sum_tv)).setText(R.string.apt_sum_help_text);

            (helpView.findViewById(R.id.apt_sum_help_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                }
            });
            frameLayout.addView(helpView);
        }
    }


}

