package com.hackensack.umc.adaptor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hackensack.umc.datastructure.DoctorOfficeDetail;
import com.hackensack.umc.fragment.DoctorDetailPageFragment;
import com.hackensack.umc.listener.OnCheckButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gaurav_salunkhe on 10/6/2015.
 */
public class DoctorDetailPagerAdapter extends FragmentPagerAdapter {


//    @Override
//    public Fragment getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public int getCount() {
//        return 0;
//    }

    private Context mContext;
    private DoctorOfficeDetail mDoctorOfficeSelected;
    private OnCheckButtonClickListener.OnCheckButtonClickCallback mOnCheckButtonClickCallback;
    private ArrayList<Fragment> mFragment = new ArrayList<Fragment>();


    public DoctorDetailPagerAdapter(DoctorOfficeDetail doctorOfficeSelected, FragmentManager fm, Context context, OnCheckButtonClickListener.OnCheckButtonClickCallback checkButtonClickCallback) {
        super(fm);

        mContext = context;
        mDoctorOfficeSelected = doctorOfficeSelected ;

        mOnCheckButtonClickCallback = checkButtonClickCallback;

    }



    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        Fragment fragment = new DoctorDetailPageFragment(mDoctorOfficeSelected.getSchedules().get(position), mContext, mOnCheckButtonClickCallback, position);
//        Bundle args = new Bundle();
//        args.putInt(DoctorDetailPageFragment.ARG_SECTION_NUMBER, position + 1);
//        fragment.setArguments(args);
        mFragment.add(fragment);

        return fragment;
    }

    @Override
    public int getCount() {
        return mDoctorOfficeSelected.getSchedules().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String title = "";

        try {

            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date MyDate = newDateFormat.parse(mDoctorOfficeSelected.getSchedules().get(position).getDate());
            newDateFormat.applyPattern("EEE MMM d");
            title = newDateFormat.format(MyDate);

        }catch(Exception e){

        }

        return title;

    }

}
