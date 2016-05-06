package com.hackensack.umc.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.DoctorResultGridAdaptor;
import com.hackensack.umc.adaptor.MyDoctorEditAdapter;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.db.MyDoctorDataSource;
import com.hackensack.umc.listener.OnGridItemClickListener;
import com.hackensack.umc.listener.OnLastItemReachListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhagyashree_kumawat on 10/20/2015.
 */
public class FavoriteDoctorList extends BaseActivity implements OnGridItemClickListener.OnGridItemClickCallback, OnLastItemReachListener.OnLastItemReachCallback {
    private List<DoctorDetails> mFavDoctorList = new ArrayList<DoctorDetails>();
    private OnGridItemClickListener.OnGridItemClickCallback onItemClickCallback;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private MyDoctorDataSource myDoctorDataSource;
    private boolean isEditMode, isListEmpty = false, isOverlayRemoved =true;
    private Menu menu;
    private TextView mEmptyView;
    private FrameLayout frameLayout;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout = new FrameLayout(this);
        // creating LayoutParams
        FrameLayout.LayoutParams frameLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(frameLayout, frameLayoutParam);
        layoutInflater = LayoutInflater.from(this);
        View mainView = layoutInflater.inflate(R.layout.activity_doctor_result, null, false);
        frameLayout.addView(mainView);
        //setContentView(R.layout.activity_doctor_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isEditMode = true;
        mEmptyView = (TextView) findViewById(R.id.text_view_favorite);
//        ((ProgressBar) findViewById(R.id.progress_bar)).setVisibility(View.GONE);
        (findViewById(R.id.fav_find_doc_ll)).setVisibility(View.VISIBLE);
        (findViewById(R.id.fav_find_doc_ll)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FavoriteDoctorList.this, DoctorSearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        //get database handler
        myDoctorDataSource = new MyDoctorDataSource(FavoriteDoctorList.this);
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

        mFavDoctorList = myDoctorDataSource.getAllMyDoctors();
        if (mFavDoctorList.isEmpty()) {
            OnAllListItemDeleted();
        } else {
            showHelpView(layoutInflater);
        }
        mAdapter = new DoctorResultGridAdaptor(this, mFavDoctorList, this, this, true, myDoctorDataSource, null);
        mRecyclerView.setAdapter(mAdapter);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mydoctor, menu);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isListEmpty) {
            (menu.findItem(R.id.action_edit)).setVisible(false);
        } else {
            (menu.findItem(R.id.action_edit)).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!isOverlayRemoved){
            Util.removeHelpViewFromParent(this, R.id.doc_result_help_rl);
            isOverlayRemoved = true;
        }else{
        switch (item.getItemId()) {
            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;
            case R.id.action_edit:
                if (isEditMode) {
                    if (mFavDoctorList != null && mFavDoctorList.size() > 0) {
                        setTitle(getString(R.string.title_activity_Edit));
                        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.done_icon));
                        isEditMode = false;
                        mAdapter = new MyDoctorEditAdapter(mFavDoctorList, myDoctorDataSource, this, this,this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    setTitle(getString(R.string.title_activity_favourite));
                    isEditMode = true;
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.edit_icon_fav));
                    mAdapter = new DoctorResultGridAdaptor(this, mFavDoctorList, this, this, true, myDoctorDataSource, null);
                    mRecyclerView.setAdapter(mAdapter);
                }
                return true;
        }}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnAllListItemDeleted() {
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setGravity(Gravity.CENTER);
        mEmptyView.setText(getString(R.string.fav_empty_text));
        setTitle(getString(R.string.title_activity_favourite));
        isListEmpty = true;
        invalidateOptionsMenu();
    }

    @Override
    public void onGridItemClicked(View view, int position) {
        if (mFavDoctorList.size() > 0) {
            Intent intent = new Intent(FavoriteDoctorList.this, DoctorOfficeDetailActivity.class);
            intent.putExtra(Constant.DOCTOR_SELECTED, mFavDoctorList.get(position));
            startActivity(intent);
        }
    }

    @Override
    public boolean onLastItemReach(View view, int position) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isActivityFinished || isUserLogout) {
            finish();
        }
    }

    private void showHelpView(LayoutInflater layoutInflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_FAVORITE_DOCTOR)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_FAVORITE_DOCTOR, false);
            Typeface arrowFont = Util.getArrowFont(this), segoeFont = Util.getFontSegoe(this);
            final View helpView = layoutInflater.inflate(R.layout.doctor_result_help, null, false);
            ((TextView) helpView.findViewById(R.id.filter_arrow)).setTypeface(arrowFont);
            ((TextView) helpView.findViewById(R.id.filter_tv)).setTypeface(segoeFont);
            ((TextView) helpView.findViewById(R.id.filter_tv)).setText(getString(R.string.fav_list_help_text));
            ((TextView) helpView.findViewById(R.id.tap_tv)).setTypeface(segoeFont);
            (helpView.findViewById(R.id.fav_ll)).setVisibility(View.GONE);
            //for dismissing anywhere you touch
            (helpView.findViewById(R.id.doc_result_help_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                    isOverlayRemoved = true;
                }
            });
            isOverlayRemoved =false;
            frameLayout.addView(helpView);
        }
    }

}


