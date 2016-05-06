package com.hackensack.umc.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.AppointmentGridAdapter;
import com.hackensack.umc.datastructure.AppointmentDetails;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.listener.OnAppointmentButtonListener;
import com.hackensack.umc.listener.OnGridItemClickListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewAppointmentActivity extends BaseActivity implements OnGridItemClickListener.OnGridItemClickCallback, OnAppointmentButtonListener.OnAppointmentButtonCallback, HttpDownloadCompleteListener {

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;

    RecyclerView.Adapter mAdapter;
    ArrayList<AppointmentDetails> mAppointmentResult = new ArrayList<AppointmentDetails>();
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<DoctorDetails> mDoctorDetails;

//    private ProgressBar mProgressBar;
    private Button mFindDoctor;

    RelativeLayout mButtonRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_appointment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mButtonRelativeLayout = (RelativeLayout) findViewById(R.id.relative_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.appointment_recycler_view);
        mEmptyView = (TextView) findViewById(R.id.appointment_empty_view);
        mEmptyView.setGravity(Gravity.CENTER);

        mAdapter = new AppointmentGridAdapter(this, mAppointmentResult, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mProgressBar = (ProgressBar) findViewById(R.id.appointment_pb);
//        mProgressBar.setVisibility(View.GONE);

        if (Util.isUserLogin(this) && Util.isPatientIdValid(this)) {
            if (Util.ifNetworkAvailableShowProgressDialog(ViewAppointmentActivity.this, getString(R.string.getting_upcoming_appointment), true)) {
                HttpDownloader http = new HttpDownloader(this, ServerConstants.VIEW_APPOINTMENT_URL + Util.getPatientMRNID(this), Constant.VIEW_APPOINTMENT, this);
                boolean isNetworkAvailable = http.startDownloading();
//                mProgressBar.setVisibility(View.VISIBLE);
            }else{

                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

            }
        } else {
//            if(Util.isNetworkAvailable(this)) {
//                Util.showNetworkOfflineDialog(this, "Network Error", "The internet connection appears to be offline.");
//            }else {
//            showAlertForLoggedOutUsr(ViewAppointmentActivity.this, "Please login to see appointments.", getString(R.string.alert_title));
            mButtonRelativeLayout.setVisibility(View.VISIBLE);
            if (mAppointmentResult.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);

            }
//            }
        }

        mFindDoctor = (Button) findViewById(R.id.register_button);

        mFindDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ViewAppointmentActivity.this, DoctorSearchActivity.class));

            }
        });




    }

    public void showAlertForLoggedOutUsr(Context context, String message, String title) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message).setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_view_appointment, menu);
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

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isActivityFinished || isUserLogout /*|| isAppointmentDone*/) {
            finish();
            return;
        }

        if(isAppointmentDone) {
            if (Util.ifNetworkAvailableShowProgressDialog(ViewAppointmentActivity.this, getString(R.string.getting_upcoming_appointment), true)) {

                mAppointmentResult.clear();
                mAdapter.notifyDataSetChanged();
                HttpDownloader http = new HttpDownloader(this, ServerConstants.VIEW_APPOINTMENT_URL + Util.getPatientMRNID(this), Constant.VIEW_APPOINTMENT, this);
                boolean isNetworkAvailable = http.startDownloading();
            }
            isAppointmentDone = false;
        }else {
            if (mAppointmentResult != null)
                mAdapter.notifyDataSetChanged();
        }

    }


//    ArrayList<Integer> mDoctorID;
    int mNoOfDoctor = 0;

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        Object obj = data.getDataObject();

        if (data.getRequestType() == Constant.VIEW_APPOINTMENT) {

            if ((data.getResponseCode() >= Constant.HTTP_OK && data.getResponseCode() < Constant.HTTP_REDIRECT)) {

                if (obj != null) {

                    mAppointmentResult.addAll((ArrayList<AppointmentDetails>) obj);

                    mAdapter.notifyDataSetChanged();
                    CountDownTimer myCountDown = new CountDownTimer(20000, 1000) {

                        public void onTick(long millisUntilFinished) {

                            if(mAdapter != null)
                                mAdapter.notifyDataSetChanged();

                        }

                        public void onFinish() {

                            if(mAdapter != null)
                                mAdapter.notifyDataSetChanged();

                        }
                    };
                    myCountDown.start();

                } else {
                    Util.showNetworkOfflineDialog(this, getString(R.string.appointment_title), getString(R.string.no_pending_appointment_text));
                }

            } else {
                Util.showNetworkOfflineDialog(this, getString(R.string.error_text), getString(R.string.error_description));
            }

        } else if (data.getRequestType() == Constant.CANCEL_APPOINTMENT) {

            if ((data.getResponseCode() >= Constant.HTTP_OK && data.getResponseCode() < Constant.HTTP_REDIRECT)) {
                mAppointmentResult.remove(mSelectedItem);
                mAdapter.notifyDataSetChanged();
                Util.showNetworkOfflineDialog(this, getString(R.string.success), getString(R.string.appointment_cancel));
            } else {
                Util.showNetworkOfflineDialog(this, getString(R.string.error_text), getString(R.string.appointment_not_cancel));
            }

        }

        stopProgress();

        mButtonRelativeLayout.setVisibility(View.VISIBLE);
        if (mAppointmentResult.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onGridItemClicked(View view, int position) {


    }

    int mSelectedItem = 0;
    AlertDialog alert;

    @Override
    public boolean onCancelButtonCallback(View view, int position) {

        cancelAppointment(position, Constant.CANCEL_APPOINTMENT);

        return false;
    }

    @Override
    public boolean onRescheduleButtonCallback(View view, int position) {

        cancelAppointment(position, Constant.RESCHEDULE_APPOINTMENT);

        return false;
    }

    private void cancelAppointment(int position, final int type) {

            mSelectedItem = position;

            if (!isFinishing()) {


                DateFormat formatterTime = new SimpleDateFormat("hh:mm a");
                DateFormat formatterDate = new SimpleDateFormat("EEE, MMM dd yyyy");


                Date d = null;
                try {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    d = sdf.parse(mAppointmentResult.get(mSelectedItem).getStartDate());
                } catch (Exception e) {
                    Log.e("Date", " " + e);
                }

                //Appointment Date
                String dateTime = formatterTime.format(d.getTime()) + ", " + formatterDate.format(d.getTime());


                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
                builder.setView(dialogView);

                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getString(R.string.confirmation_title));
                if (type == Constant.CANCEL_APPOINTMENT)
                    ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.confirmation_cancel_appointment) + "\n\n" + mAppointmentResult.get(mSelectedItem).getDoctorDetail().getDoctorFullName() + "\n\n" + dateTime);
                else
                    ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.confirmation_reschedule) + "\n\n" + "" + dateTime);

                TextView btnNo = (TextView) dialogView.findViewById(R.id.button_dialog_cancel);
                btnNo.setVisibility(View.VISIBLE);
                btnNo.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                TextView btnOk = (TextView) dialogView.findViewById(R.id.button_dialog_ok);
                btnOk.setText(getString(R.string.button_yes));
                btnOk.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {

//                    HttpDownloader http = new HttpDownloader(ViewAppointmentActivity.this, Constant.CANCEL_APPOINTMENT_URL + mAppointmentResult.get(mSelectedItem).getID(), type, ViewAppointmentActivity.this);
//                    boolean isNetworkAvailable = http.startDownloading();

                        if (type == Constant.CANCEL_APPOINTMENT) {
//                            startProgress(ViewAppointmentActivity.this, "Cancelling appointment...");
                            if (Util.ifNetworkAvailableShowProgressDialog(ViewAppointmentActivity.this, getString(R.string.cancel_appointment), true)) {
                                HttpDownloader http = new HttpDownloader(ViewAppointmentActivity.this, ServerConstants.CANCEL_APPOINTMENT_URL + mAppointmentResult.get(mSelectedItem).getID(), type, ViewAppointmentActivity.this);
                                boolean isNetworkAvailable = http.startDownloading();
                            }
                        } else {

//                            DoctorDetails doctorItems = new DoctorDetails();
//                            doctorItems.setDoctorNpi(mAppointmentResult.get(mSelectedItem).getPractitioner());

                            Intent intent = new Intent(ViewAppointmentActivity.this, DoctorOfficeDetailActivity.class);
                            intent.putExtra(Constant.DOCTOR_SELECTED, mAppointmentResult.get(mSelectedItem).getDoctorDetail());
                            intent.putExtra(Constant.DOCTOR_APPOINTMENT, true);
                            intent.putExtra(Constant.RESCHEDULE_APPOINTMENT_ID, mAppointmentResult.get(mSelectedItem).getID());
                            startActivity(intent);
                        }

                        alert.dismiss();
                    }
                });

                alert = builder.show();
            }

    }

    public String getAppointmentData() {

        String body = "{\"resourceType\": \"Appointment\",\"id\": \"$$APPOINTMENT$$\",\"status\": \"cancelled\",\"participant\": [{\"type\": [{\"coding\": [{\"system\": \"http://www.hl7.org/fhir/v3/ParticipationType/index.html\",\"code\": \"SBJ\"}]}],\"actor\": {\"reference\": \"Patient/$$MRN$$\"}}]}";

        body = body.replace("$$APPOINTMENT$$", mAppointmentResult.get(mSelectedItem).getID());
        body = body.replace("$$MRN$$", Util.getPatientMRNID(this));

        return body;
    }
}
