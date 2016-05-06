package com.hackensack.umc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.DoctorDetailPagerAdapter;
import com.hackensack.umc.customview.SlidingTabLayout;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.DoctorOfficeDetail;
import com.hackensack.umc.db.MyDoctorDataSource;
import com.hackensack.umc.listener.OnCheckButtonClickListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class DoctorDetailActivity extends BaseActivity implements OnCheckButtonClickListener.OnCheckButtonClickCallback {

    private DoctorDetails mDoctorDetail;
    private DoctorOfficeDetail mDoctorOfficeSelected;

    private ImageView mDoctorImage/*, mFavIcon*/;
    private TextView mDoctorSpeciality;
    private TextView mDoctorFullName;
    private TextView mDoctorAddress;
    private TextView mDoctorTelephone;

    private ImageView mBackButton, mDoneButton;

    ViewPager mViewPager;
    SlidingTabLayout mSlidingTabLayout;

    String mDoctorFullAddress, mDoctorPhoneNum, mAppointmentTime, mAppointmentType, mAppointmentID, mDistInMile;
    String mRescheduleAppointmentID;
    private MyDoctorDataSource myDoctorDataSource;

    private ArrayList<String> mFilter;

    boolean isReschedulingAppointment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_detail);

//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayShowHomeEnabled(true);
//        bar.setIcon(R.drawable.filter_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().hide();
//        getSupportActionBar().setCustomView(R.layout.activity_doctor_detail);

        /**
         * Favorite Code
         */
        myDoctorDataSource = new MyDoctorDataSource(DoctorDetailActivity.this);
        try {
            myDoctorDataSource.open();
        } catch (SQLException e) {
            Log.e("DoctorResultActivity", "Exception Occured" + e);
        }

        Intent intent = getIntent();

        if(intent != null && intent.getBooleanExtra(Constant.DOCTOR_APPOINTMENT, false)) {
            isReschedulingAppointment = true;
            mRescheduleAppointmentID = intent.getStringExtra(Constant.RESCHEDULE_APPOINTMENT_ID);
        }

        mDoctorDetail = (DoctorDetails) intent.getSerializableExtra(Constant.DOCTOR_SELECTED);
        mDoctorOfficeSelected = (DoctorOfficeDetail) intent.getSerializableExtra(Constant.DOCTOR_ADDRESS_SELECTED);
        mDoctorFullAddress = intent.getStringExtra(Constant.DOCTOR_ADDRESS);
        mDoctorPhoneNum = intent.getStringExtra(Constant.DOCTOR_TELEPHONE);
        mDistInMile = intent.getStringExtra(Constant.DOCTOR_DISTANCE_MILE);
        mFilter = (ArrayList<String>) intent.getSerializableExtra(Constant.APPOINTMENT_FILTER);

        ((TextView) findViewById(R.id.direction_text)).setText(mDistInMile);
        mDoctorImage = (ImageView) findViewById(R.id.doctor_img);
        mDoctorSpeciality = (TextView) findViewById(R.id.speciality_text);
        mDoctorFullName = (TextView) findViewById(R.id.name_text);
        mDoctorAddress = (TextView) findViewById(R.id.address_one_text);
        mDoctorTelephone = (TextView) findViewById(R.id.dd_call);

       // mFavIcon = (ImageView) findViewById(R.id.fav_icon);
        if(mDoctorDetail != null) {

            if(mDoctorDetail.getDoctorSpeciality() != null && mDoctorDetail.getDoctorSpeciality().length() > 1)
                mDoctorSpeciality.setText(mDoctorDetail.getDoctorSpeciality());

            if(mDoctorDetail.getDoctorFullName() != null && mDoctorDetail.getDoctorFullName().length() > 1)
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

        CountDownTimer myCountDown = new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                FragmentManager mFragmentManager = getSupportFragmentManager();
                DoctorDetailPagerAdapter mDoctorDetailPagerAdapter = new DoctorDetailPagerAdapter(mDoctorOfficeSelected, mFragmentManager, DoctorDetailActivity.this, DoctorDetailActivity.this);

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.pager);
                mViewPager.setAdapter(mDoctorDetailPagerAdapter);
                mViewPager.setOffscreenPageLimit(30);

                mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
                mSlidingTabLayout.setViewPager(mViewPager);

                mDoctorDetailPagerAdapter.notifyDataSetChanged();

                stopProgress();
            }
        };
        myCountDown.start();

        startProgress(DoctorDetailActivity.this, getResources().getString(R.string.loading_text));

    }

    /*private void updateFavorite() {
        final boolean isfavdoc = myDoctorDataSource.isFavoriteDoc(mDoctorDetail.getDoctorHumcId());
        if (isfavdoc) {
            mFavIcon.setImageResource(R.drawable.favorite_icon_active);
        } else {
            mFavIcon.setImageResource(R.drawable.favorite_icon_normal);
        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_doctor_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;

            case R.id.action_done:

                if (mAppointmentTime != null && mAppointmentTime.length() > 0) {

                    Intent intent = new Intent(DoctorDetailActivity.this, DoctorAppointmentSummaryActivity.class);
                    intent.putExtra(Constant.DOCTOR_SELECTED, mDoctorDetail);
                    intent.putExtra(Constant.DOCTOR_ADDRESS_SELECTED, mDoctorOfficeSelected);
                    intent.putExtra(Constant.DOCTOR_ADDRESS, mDoctorFullAddress);
                    intent.putExtra(Constant.DOCTOR_TELEPHONE, mDoctorPhoneNum.toString());
                    intent.putExtra(Constant.DOCTOR_APPOINTMENT_TIME, mAppointmentTime);
                    intent.putExtra(Constant.DOCTOR_APPOINTMENT_TYPE, mAppointmentType);
                    intent.putExtra(Constant.DOCTOR_APPOINTMENT_ID, mAppointmentID);
                    intent.putExtra(Constant.DOCTOR_DISTANCE_MILE,mDistInMile);
                    if(isReschedulingAppointment) {
                        intent.putExtra(Constant.DOCTOR_APPOINTMENT, true);
                        intent.putExtra(Constant.RESCHEDULE_APPOINTMENT_ID, mRescheduleAppointmentID);
                    }
                    startActivity(intent);
                } else {

                    Util.showNetworkOfflineDialog(DoctorDetailActivity.this, getString(R.string.error_text), getString(R.string.select_slot_text));

                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private RadioGroup mRadioGroupButton;
//    private static ArrayList<RadioGroup> mRadioGroupArray;

    @Override
    public void onCheckButtonClicked(View view, int position, String startDate, String type, String id, RadioGroup renderRadioGroup) {

        if(mRadioGroupButton != null)
            Log.e("DOCTOR DETAIL", "mRadioGroupButton.getId() -> "+mRadioGroupButton.getId()+", renderRadioGroup.getId() -> "+renderRadioGroup.getId());

        if(mRadioGroupButton != null && !(mRadioGroupButton.getId() == renderRadioGroup.getId())) {
            mRadioGroupButton.clearCheck();
            Log.e("DOCTOR DETAIL", "Radio Button Clear");
        }
        mRadioGroupButton = renderRadioGroup;

//        RadioGroup item = mRadioGroupArray.get(renderRadioGroup.getId());
//        for(RadioGroup radio : mRadioGroupArray) {
//
//            if(radio != null && !(radio.getId() == renderRadioGroup.getId())) {
//                radio.clearCheck();
//            }else {
//                mRadioGroupArray.add(renderRadioGroup.getId(), renderRadioGroup);
//            }
//
//        }








//        if(radioGroupButton != null)
//            radioGroupButton.clearCheck();

        mAppointmentTime = startDate;
        mAppointmentType = type;
        mAppointmentID = id;

    }

    @Override
    protected void onResume() {
        super.onResume();
        //update the Favorite status
       // updateFavorite();
//        mAppointmentTime = "";
//        mAppointmentType = "";
//        mAppointmentID = "";
        if (Util.isActivityFinished || isUserLogout || isAppointmentDone) {
            finish();
        }

    }

}
