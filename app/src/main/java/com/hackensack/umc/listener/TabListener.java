package com.hackensack.umc.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.hackensack.umc.R;

/**
 * Created by gaurav_salunkhe on 9/28/2015.
 */
public class TabListener implements ActionBar.TabListener {

    private Fragment mFragment;


    public TabListener(Fragment fragment) {

        mFragment = fragment;

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        fragmentTransaction.replace(R.id.activity_frame_layout, mFragment);

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        fragmentTransaction.remove(mFragment);

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
