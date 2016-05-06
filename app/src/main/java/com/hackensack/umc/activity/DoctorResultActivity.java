package com.hackensack.umc.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.DoctorResultGridAdaptor;
import com.hackensack.umc.adaptor.MenuItemHolder;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.db.MyDoctorDataSource;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.listener.OnGridItemClickListener;
import com.hackensack.umc.listener.OnLastItemReachListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorResultActivity extends BaseActivity implements OnGridItemClickListener.OnGridItemClickCallback, OnLastItemReachListener.OnLastItemReachCallback, HttpDownloadCompleteListener {

    /**
     * UI view variables
     */
    ImageView mUserImageView;

    ArrayList<MenuItemHolder> mMenuItems = new ArrayList<MenuItemHolder>();

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private MyDoctorDataSource myDoctorDataSource;
    ArrayList<DoctorDetails> mDoctorsResult = new ArrayList<DoctorDetails>();

//    private ProgressBar mProgressBar;

    private String mNextUrl;

    private TextView mEmptyView;
    private String errorMsg;
    private View mLoadingProgressBar;
    private FrameLayout frameLayout;
    private int filterbottom, filterleft;
    private float heartRight, bottom;
    private LayoutInflater layoutInflater;
    private boolean isFromFilter, isOverlayRemoved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_doctor_result);
        frameLayout = new FrameLayout(this);
        // creating LayoutParams
        FrameLayout.LayoutParams frameLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(frameLayout, frameLayoutParam);
        layoutInflater = LayoutInflater.from(this);
        View mainView = layoutInflater.inflate(R.layout.activity_doctor_result, null, false);
        frameLayout.addView(mainView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        filterbottom = getSupportActionBar().getHeight();
        mSelectedSpecialty = getIntent().getStringExtra(Constant.DOCTOR_SEARCH_SPECIALTY_QUERY);
        mSelectedGender = getIntent().getStringExtra(Constant.DOCTOR_SEARCH_GENDER_QUERY);
        mSelectedZipcode = getIntent().getStringExtra(Constant.DOCTOR_SEARCH_ZIPCODE_QUERY);
        mSelectedFirstName = getIntent().getStringExtra(Constant.DOCTOR_SEARCH_FIRST_NAME_QUERY);
        mSelectedLastName = getIntent().getStringExtra(Constant.DOCTOR_SEARCH_LAST_NAME_QUERY);
        isFromFilter = getIntent().getBooleanExtra(Constant.DOCTOR_IS_FROM_FILTER, false);

        mDoctorSearchQuery = null;
        mEmptyView = (TextView) findViewById(R.id.text_view_favorite);
//        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // mProgressBar.setVisibility(View.VISIBLE);

        /**
         * Favorite Code
         */
        myDoctorDataSource = new MyDoctorDataSource(DoctorResultActivity.this);
        try {
            myDoctorDataSource.open();
        } catch (SQLException e) {
            Log.e("DoctorResultActivity", "Exception Occured" + e);
        }

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //check if result is populated then onli show filter option
        invalidateOptionsMenu();
//        HttpDownloader http = new HttpDownloader(this, Constant.PRACTITIONER_URL+"?specialty="+mSelectedSpecialty, Constant.DOCTOR_RESULT_DATA, this);
        if (Util.ifNetworkAvailableShowProgressDialog(DoctorResultActivity.this, getString(R.string.loading_text), true)) {
            HttpDownloader http = new HttpDownloader(this, ServerConstants.PRACTITIONER_URL + getDoctorSearchQuery(), Constant.DOCTOR_RESULT_DATA, this);
            http.startDownloading();
        }

        //See if the location service is on
        Location currentLocation = Util.getLocation(this);
        if (currentLocation == null) {
            //show alert if the Location service is not ON
            Util.createLocationServiceError(this);
        }

        mAdapter = new DoctorResultGridAdaptor(this, mDoctorsResult, this, this, false, myDoctorDataSource, mSelectedZipcode);
        mRecyclerView.setAdapter(mAdapter);

//        recyclerView.setItemAnimator(new SlideInUpAnimator());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String gender = intent.getStringExtra(Constant.DOCTOR_SEARCH_GENDER_QUERY);
        boolean isGenderAvialable = gender != null ;
        if (isGenderAvialable) {
            mSelectedGender = gender;
        }
        mSelectedSearchIn = intent.getStringExtra(Constant.DOCTOR_SEARCH_SEARCH_WITHIN);
        mSelectedLocation = intent.getStringExtra(Constant.DOCTOR_SEARCH_LOCATION);
        isFromFilter = intent.getBooleanExtra(Constant.DOCTOR_IS_FROM_FILTER, false);

        if (isGenderAvialable || (!TextUtils.isEmpty(mSelectedSearchIn) && mSelectedSearchIn.length() > 0) || (!TextUtils.isEmpty(mSelectedLocation) && mSelectedLocation.length() > 0)) {

            mDoctorSearchQuery = null;
            mDoctorsResult = new ArrayList<DoctorDetails>();

            mAdapter = new DoctorResultGridAdaptor(this, mDoctorsResult, this, this, false, myDoctorDataSource, mSelectedZipcode);
            mRecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();

            //   mProgressBar.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            if (Util.ifNetworkAvailableShowProgressDialog(DoctorResultActivity.this, getString(R.string.loading_text), true)) {
                HttpDownloader http = new HttpDownloader(this, ServerConstants.PRACTITIONER_URL + getDoctorSearchQuery(), Constant.DOCTOR_RESULT_DATA, this);
                boolean isNetworkAvailable = http.startDownloading();
            }
            /*if (irogressBar.setVisibility(View.GONE);
                shsNetworkAvailable == false) {
                mPowDialogBox("Network Error", "Connectivity error, please try again later");
            }*/

        }
    }

    private String mDoctorSearchQuery;

    private String mSelectedSpecialty;
    private String mSelectedGender;
    private String mSelectedZipcode;
    private String mSelectedFirstName;
    private String mSelectedLastName;
    private String mSelectedSearchIn;
    private String mSelectedLocation;

    private String getDoctorSearchQuery() {

        if (mDoctorSearchQuery == null) {

            try {

                if (mSelectedSpecialty != null && mSelectedSpecialty.length() > 1) {
                    mDoctorSearchQuery = "?specialty=" + URLEncoder.encode(mSelectedSpecialty.trim().replaceAll("&amp;", "&").trim(), "UTF-8");
                }

            } catch (Exception e) {

            }
        }

        if (mSelectedGender != null && mSelectedGender.length() > 1) {
            try {
                if (mDoctorSearchQuery == null) {
                    mDoctorSearchQuery = "?gender=" + mSelectedGender.toString().trim();
                } else {
                    mDoctorSearchQuery = mDoctorSearchQuery + "&gender=" + URLEncoder.encode(mSelectedGender.toString().trim(), "UTF-8");
                }
            } catch (Exception e) {

            }
        }

        if (mSelectedZipcode != null && mSelectedZipcode.length() > 0) {
            try {
                if (mDoctorSearchQuery == null) {
                    mDoctorSearchQuery = "?zipcode=" + URLEncoder.encode(mSelectedZipcode, "UTF-8");
                } else {
                    mDoctorSearchQuery = mDoctorSearchQuery + "&zipcode=" + URLEncoder.encode(mSelectedZipcode, "UTF-8");
                }
            } catch (Exception e) {

            }
        }

        if (mSelectedFirstName != null && mSelectedFirstName.length() > 0) {
            try {
                if (mDoctorSearchQuery == null) {
                    mDoctorSearchQuery = "?firstName=" + URLEncoder.encode(mSelectedFirstName, "UTF-8");
                } else {
                    mDoctorSearchQuery = mDoctorSearchQuery + "&firstName=" + URLEncoder.encode(mSelectedFirstName, "UTF-8");
                }
            } catch (Exception e) {

            }
        }

        if (mSelectedLastName != null && mSelectedLastName.length() > 0) {
            try {
                if (mDoctorSearchQuery == null) {
                    mDoctorSearchQuery = "?lastName=" + URLEncoder.encode(mSelectedLastName, "UTF-8");
                } else {
                    mDoctorSearchQuery = mDoctorSearchQuery + "&lastName=" + URLEncoder.encode(mSelectedLastName, "UTF-8");
                }
            } catch (Exception e) {

            }
        }

//        if(mSelectedSearchIn != null && mSelectedSearchIn.length() > 0) {
//            try {
//                if (mDoctorSearchQuery == null) {
//                    mDoctorSearchQuery = "?searchwithin=" + URLEncoder.encode(mSelectedSearchIn, "UTF-8");
//                } else {
//                    mDoctorSearchQuery = mDoctorSearchQuery + "&searchwithin=" + URLEncoder.encode(mSelectedSearchIn, "UTF-8");
//                }
//            }catch(Exception e) {
//
//            }
//        }
//
//        if(mSelectedLocation != null && mSelectedLocation.length() > 0) {
//            try {
//                if (mDoctorSearchQuery == null) {
//                    mDoctorSearchQuery = "?location=" + URLEncoder.encode(mSelectedLocation, "UTF-8");
//                } else {
//                    mDoctorSearchQuery = mDoctorSearchQuery + "&location=" + URLEncoder.encode(mSelectedLocation, "UTF-8");
//                }
//            }catch(Exception e) {
//
//            }
//        }

        if (mDoctorSearchQuery == null)
            return "";
        else
            return mDoctorSearchQuery;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        if (Util.isActivityFinished || isUserLogout) {
            finish();
        }

        try {
            myDoctorDataSource.open();
        } catch (SQLException e) {
            Log.e("DoctorResultActivity", "Exception Occured" + e);
        }

        mDoctorSearchQuery = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDoctorDataSource.close();
//        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_result, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mDoctorsResult != null && mDoctorsResult.isEmpty()) {
            menu.getItem(0).setVisible(false);
        } else {
            menu.getItem(0).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isOverlayRemoved) {
            Util.removeHelpViewFromParent(this, R.id.doc_result_help_rl);
            isOverlayRemoved = true;
        }else{
        switch (item.getItemId()) {
            case android.R.id.home:
                isBackPressed = true;
                super.onBackPressed();
                //finish();
                return true;

            case R.id.action_filter_search:
                Util.removeHelpViewFromParent(this, R.id.doc_result_help_rl);
                Intent filterIntent = new Intent(DoctorResultActivity.this, DoctorFilterActivity.class);
                filterIntent.putExtra(Constant.DOCTOR_SEARCH_GENDER_QUERY, mSelectedGender);
                filterIntent.putExtra(Constant.DOCTOR_IS_FROM_FILTER, isFromFilter);
                startActivity(filterIntent);

                return true;

//            case R.id.action_settings:
//                finish();
//                return true;



        }}
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGridItemClicked(View view, final int position) {

        if (mDoctorsResult.size() > 0) {
            Intent intent = new Intent(DoctorResultActivity.this, DoctorOfficeDetailActivity.class);
            intent.putExtra(Constant.DOCTOR_SELECTED, mDoctorsResult.get(position));
            startActivity(intent);
        }

    }


    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        Object obj = data.getDataObject();
        mNextUrl = data.getNextHttpUrl();

        if (obj != null && !isFinishing()) {

//            mRelativeLoading.setVisibility(View.GONE);

            if (((ArrayList<DoctorDetails>) obj).size() > 0) {

                try {

                    mDoctorsResult.addAll((ArrayList<DoctorDetails>) obj);
                    mAdapter.notifyDataSetChanged();
                    if (mDoctorsResult.size() > 0) {
                        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                int left = mLayoutManager.getDecoratedLeft(mRecyclerView.getChildAt(0));
                                heartRight = left + ((mLayoutManager.getDecoratedRight(mRecyclerView.getChildAt(0)) - left) / 4);
                                int top = mLayoutManager.getDecoratedTop(mRecyclerView.getChildAt(0));
                                bottom = (mRecyclerView.getChildAt(0).findViewById(R.id.doctor_img)).getBottom() + ((mLayoutManager.getDecoratedBottom(mRecyclerView.getChildAt(0)) - top) / 4);
                                showHelpOverlay(layoutInflater);
                            }
                        });
                    }

                } catch (Exception e) {
//                    showDialogBox("Parsing Error", "Something wrong, please try again later");
                }
            } else {

                Util.showNetworkOfflineDialog(DoctorResultActivity.this, getString(R.string.no_matching_title), getString(R.string.no_matching_text));
//                showDialogBox("No Match", "No Matching Physicians found, Please alter the search criteria");
                mEmptyView.setText(getString(R.string.no_matching_text));
                mEmptyView.setVisibility(View.VISIBLE);

            }

            CountDownTimer myCountDown = new CountDownTimer(20000, 2000) {

                public void onTick(long millisUntilFinished) {

                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();

                }

                public void onFinish() {

                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();

                }
            };
            myCountDown.start();

        } else {
            //Show Popup
//            showDialogBox("Network Error", "Connectivity error, please try again later");
            Util.showNetworkOfflineDialog(DoctorResultActivity.this, getString(R.string.error_text), getString(R.string.error_description));
        }
        stopProgress();

//        mProgressBar.setVisibility(View.GONE);

        if (mLoadingProgressBar != null)
            mLoadingProgressBar.setVisibility(View.GONE);

        invalidateOptionsMenu();


    }

//    public void showDialogBox(final String title, String message) {
//
//        if (!isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorResultActivity.this);
//            builder.setTitle(title);
//            builder.setMessage(message);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    if (title.equalsIgnoreCase("Network Error")) {
//                        mEmptyView.setVisibility(View.VISIBLE);
//                        mEmptyView.setGravity(Gravity.CENTER);
//                        mEmptyView.setText(getString(R.string.no_network_text));
//                    } else {
//                        finish();
//                    }
//
//                }
//            });
//            builder.show();
//        }
//
//
//    }

    @Override
    public boolean onLastItemReach(View view, int position) {

        boolean callSuccess = false;
        mLoadingProgressBar = view;

        if (mNextUrl != null && mNextUrl.length() > 0 && !(mNextUrl.equals("null"))) {

            mNextUrl = mNextUrl.replaceAll(" ", "%20");

            Util.ifNetworkAvailableShowProgressDialog(DoctorResultActivity.this, getString(R.string.loading_text), false);
            {
                HttpDownloader http = new HttpDownloader(this, ServerConstants.PRACTITIONER_BASE_SERVER + mNextUrl, Constant.DOCTOR_RESULT_DATA, this);
                callSuccess = http.startDownloading();
            }

        } else {
            callSuccess = false;
        }

        return callSuccess;
    }

    @Override
    public void OnAllListItemDeleted() {

    }

    public void showHelpOverlay(LayoutInflater inflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_DOCTOR_RESULT)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_DOCTOR_RESULT, false);
            Typeface arrowFont = Util.getArrowFont(this), segoeFont = Util.getFontSegoe(this);
            final View helpView = inflater.inflate(R.layout.doctor_result_help, null, false);
            ((TextView) helpView.findViewById(R.id.filter_arrow)).setTypeface(arrowFont);
            ((TextView) helpView.findViewById(R.id.filter_tv)).setTypeface(segoeFont);
            (helpView.findViewById(R.id.filter_ll)).setY(filterbottom);
            ((TextView) helpView.findViewById(R.id.fav_arrow)).setTypeface(arrowFont);
            ((TextView) helpView.findViewById(R.id.fav_tv)).setTypeface(segoeFont);
            ((TextView) helpView.findViewById(R.id.tap_tv)).setTypeface(segoeFont);
            (helpView.findViewById(R.id.fav_ll)).setY(bottom);
            (helpView.findViewById(R.id.fav_ll)).setX(heartRight);
            isOverlayRemoved = false;
            //for dismissing anywhere you touch
            (helpView.findViewById(R.id.doc_result_help_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                    isOverlayRemoved = true;
                }
            });
            frameLayout.addView(helpView);
        }
    }

}
