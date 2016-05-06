package com.hackensack.umc.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.fragment.SymptomChildFragment;
import com.hackensack.umc.fragment.SymptomFemaleFragment;
import com.hackensack.umc.fragment.SymptomMaleFragment;
import com.hackensack.umc.listener.TabListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class SymptomCheckerActivity extends BaseActivity {
    public static boolean isBodyFront = true;

    Fragment mMaleFragment = new SymptomMaleFragment();
    Fragment mFemaleFragment = new SymptomFemaleFragment();
    Fragment mChildFragment = new SymptomChildFragment();
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
        View mainView = layoutInflater.inflate(R.layout.activity_symptom_checker, null, false);
        frameLayout.addView(mainView);
        // setContentView(R.layout.activity_symptom_checker);
        showHelpView(layoutInflater);
        ActionBar bar = getSupportActionBar();
//        bar.setDisplayShowHomeEnabled(true);
//        bar.setIcon(R.drawable.filter_icon);
        bar.setDisplayHomeAsUpEnabled(true);


        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.addTab(bar.newTab().setText(getString(R.string.male)).setTabListener(new TabListener(mMaleFragment)));
        bar.addTab(bar.newTab().setText(getString(R.string.female)).setTabListener(new TabListener(mFemaleFragment)));
        bar.addTab(bar.newTab().setText(getString(R.string.child)).setTabListener(new TabListener(mChildFragment)));

//        TabHost tab_host = (TabHost) findViewById(R.id.tabHost);
//        LocalActivityManager lm = new LocalActivityManager(SymptomCheckerActivity.this, false);
//        lm.dispatchCreate(savedInstanceState);
//        tab_host.setup(lm);
//
//        TabHost.TabSpec ts1 = tab_host.newTabSpec("MALE");
//        ts1.setIndicator("MALE");
//        tab_host.setBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
//        ts1.setContent(new Intent(this, LoginActivity.class));
//        tab_host.addTab(ts1);
//
//        TabHost.TabSpec ts2 = tab_host.newTabSpec("FEMALE");
//        ts2.setIndicator("FEMALE");
//        ts2.setContent(new Intent(this, DoctorFilterActivity.class));
//        tab_host.addTab(ts2);
//
//        TabHost.TabSpec ts3 = tab_host.newTabSpec("CHILD");
//        ts3.setIndicator("CHILD");
//        ts3.setContent(new Intent(this, DoctorSearchActivity.class));
//        tab_host.addTab(ts3);
//
//        for(int i=0; i < tab_host.getChildCount(); i++) {
//            tab_host.getChildAt(i).setBackgroundResource(R.drawable.tab_selector);
//        }
//
//        tab_host.setCurrentTab(0);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_doctor_search, menu);

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

            case R.id.action_search:

//                    Toast.makeText(DoctorSearchActivity.this, "click on search : ", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(DoctorFilterActivity.this, DoctorResultActivity.class));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isBodyFront = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isActivityFinished || isUserLogout) {
            finish();
        }
    }

    private void showHelpView(LayoutInflater layoutInflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_SYMPTOM_CHECKER)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_SYMPTOM_CHECKER, false);
            Typeface arrowFont = Util.getArrowFont(this), segoeFont = Util.getFontSegoe(this);
            final View helpView = layoutInflater.inflate(R.layout.symtom_help, null, false);
            ((TextView) helpView.findViewById(R.id.body_help_arrow)).setTypeface(arrowFont);
            ((TextView) helpView.findViewById(R.id.body_help_tv)).setTypeface(segoeFont);
            ((TextView) helpView.findViewById(R.id.tab_arrow)).setTypeface(arrowFont);
            ((TextView) helpView.findViewById(R.id.tab_tv)).setTypeface(segoeFont);
            ((TextView) helpView.findViewById(R.id.tap_tv)).setTypeface(segoeFont);
            //for dismissing anywhere you touch
            ( helpView.findViewById(R.id.symtom_help_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                }
            });
            frameLayout.addView(helpView);
        }
    }
}
