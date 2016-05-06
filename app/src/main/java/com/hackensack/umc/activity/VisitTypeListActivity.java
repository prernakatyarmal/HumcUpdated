package com.hackensack.umc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.VisitTypeListAdapter;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.DoctorOfficeDetail;
import com.hackensack.umc.datastructure.ScheduleDetails;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.util.ArrayList;

public class VisitTypeListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private VisitTypeListAdapter mAdapter;

    private SearchView mSearchView;
    private TextView mEmptyView;

    private DoctorDetails mDoctorDetail;
    private DoctorOfficeDetail mDoctorOfficeSelected;

    String mDoctorFullAddress, mDoctorPhoneNum, mDistInMile;
    String mRescheduleAppointmentID;
    boolean isReschedulingAppointment = false;
    private ArrayList<String> mVisitType = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mVisitType = mDoctorOfficeSelected.getVisitTypeFilter();

        setContentView(R.layout.activity_list);
        mEmptyView = (TextView) findViewById(android.R.id.empty);
        mEmptyView.setGravity(Gravity.CENTER);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        (findViewById(R.id.find_doc_ll)).setVisibility(View.GONE);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setIconified(false);
        mSearchView.setBackgroundColor(getResources().getColor(R.color.white));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText.toString());
                return true;
            }
        });

        listView = (ListView) findViewById(R.id.directionList);
        listView.setEmptyView(mEmptyView);
        listView.setOnItemClickListener(this);

        mAdapter = new VisitTypeListAdapter(this, mVisitType);

        if (mAdapter != null)
            listView.setAdapter(mAdapter);

        hideSoftKeyboard(mSearchView);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                startProgress(VisitTypeListActivity.this, getResources().getString(R.string.loading_text));

            }
        });


        String visitTypeSelected = mVisitType.get(position);

        DoctorOfficeDetail filterdoctorOffice = new DoctorOfficeDetail();

        ArrayList<ScheduleDetails> schedules = mDoctorOfficeSelected.getSchedules();
        filterdoctorOffice.setID(mDoctorOfficeSelected.getID());
        filterdoctorOffice.setAddress(mDoctorOfficeSelected.getAddress());
        filterdoctorOffice.setTelephone(mDoctorOfficeSelected.getTelephone());
        filterdoctorOffice.setVisitTypeFilter(mDoctorOfficeSelected.getVisitTypeFilter());

        for(ScheduleDetails schedulesItem : schedules) {

            if(schedulesItem.getDisplay().equals(visitTypeSelected)) {

                filterdoctorOffice.addSchedule(schedulesItem);

            }

        }

        Intent intent = new Intent(VisitTypeListActivity.this, DoctorDetailActivity.class);
        intent.putExtra(Constant.DOCTOR_SELECTED, mDoctorDetail);
//        intent.putExtra(Constant.DOCTOR_ADDRESS_SELECTED, mDoctorOfficeSelected);
        intent.putExtra(Constant.DOCTOR_ADDRESS_SELECTED, filterdoctorOffice);
        intent.putExtra(Constant.DOCTOR_ADDRESS, mDoctorFullAddress);
        intent.putExtra(Constant.DOCTOR_TELEPHONE, mDoctorPhoneNum);
        intent.putExtra(Constant.DOCTOR_DISTANCE_MILE, mDistInMile);

        if (isReschedulingAppointment) {
            intent.putExtra(Constant.DOCTOR_APPOINTMENT, true);
            intent.putExtra(Constant.RESCHEDULE_APPOINTMENT_ID, mRescheduleAppointmentID);
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                stopProgress();

            }
        });

        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

        if (Util.isActivityFinished || isUserLogout || isAppointmentDone) {
            finish();
        }

    }

    public void hideSoftKeyboard(View view) {
        if(getCurrentFocus()!= null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        }
    }

}
