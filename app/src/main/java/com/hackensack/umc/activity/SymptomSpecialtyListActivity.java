package com.hackensack.umc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.SymptomSpecialtyListAdapter;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SymptomSpecialtyListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<String> mSymptomListResult = new ArrayList<String>();
    private SymptomSpecialtyListAdapter mAdapter;
    private SearchView mSearchView;
//    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSymptomListResult = getIntent().getStringArrayListExtra(Constant.DOCTOR_SPECIALTY);

        setContentView(R.layout.activity_list);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(getResources().getString(R.string.suggested_specialties));

        ( findViewById(R.id.find_doc_ll)).setVisibility(View.GONE);
        //SearchView code
        mSearchView = (SearchView) findViewById(R.id.search_view);
//        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setIconified(false);
        mSearchView.setBackgroundColor(getResources().getColor(R.color.white));
        mSearchView.clearFocus();
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
        listView.setOnItemClickListener(this);

//        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
//        mProgressBar.setVisibility(View.GONE);

        mAdapter = new SymptomSpecialtyListAdapter(this, mSymptomListResult);

        if (mAdapter != null)
            listView.setAdapter(mAdapter);
        else
            Log.e("Test", "adapter is null");

        CountDownTimer delayCounter = new CountDownTimer(200, 200) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.down_from_top);
                animation.setStartOffset(0);
                mSearchView.startAnimation(animation);
                mSearchView.setVisibility(View.VISIBLE);

            }
        };
        delayCounter.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String specialty = mSymptomListResult.get(position).replaceAll("&amp;", "&").trim();

//        try {
//            specialty = URLEncoder.encode(specialty, "UTF-8");
//        } catch (Exception e) {
//
//        }

        Intent intent = new Intent(SymptomSpecialtyListActivity.this, DoctorResultActivity.class);
//        intent.putExtra(Constant.DOCTOR_SEARCH_QUERY, "?specialty=" + specialty);
        intent.putExtra(Constant.DOCTOR_SEARCH_SPECIALTY_QUERY, specialty.replaceAll("&amp;", "&").trim());
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

        if(Util.isActivityFinished || isUserLogout) {
            finish();
        }

    }
}
