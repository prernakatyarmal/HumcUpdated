package com.hackensack.umc.fragment;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.ScheduleDetails;
import com.hackensack.umc.datastructure.TimeSlotDetails;
import com.hackensack.umc.listener.OnCheckButtonClickListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gaurav_salunkhe on 10/6/2015.
 */
public class DoctorDetailPageFragment extends Fragment {

    private Context mContext;
    private ScheduleDetails mScheduleDetails;
    private OnCheckButtonClickListener.OnCheckButtonClickCallback mOnCheckButtonClickCallback;

    private RadioButton[] rb;
    private RadioGroup radioGroupAnswer;
    private int mID;


    public DoctorDetailPageFragment(){

    }

    public DoctorDetailPageFragment(ScheduleDetails scheduleDetails, Context context, OnCheckButtonClickListener.OnCheckButtonClickCallback checkButtonClickCallback, int id) {

        mContext = context ;
        mScheduleDetails = scheduleDetails;
        mOnCheckButtonClickCallback = checkButtonClickCallback;
        mID = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_time_slot, container, false);

        LinearLayout radioLinear = (LinearLayout) rootView.findViewById(R.id.radio_linear_layout);

        if(mScheduleDetails != null) {

            int numberOfTimeSlot = mScheduleDetails.getSlots().size();

            rb = new RadioButton[numberOfTimeSlot];

            radioGroupAnswer = new RadioGroup(mContext);
            radioGroupAnswer.setOrientation(RadioGroup.VERTICAL);
            radioGroupAnswer.setPadding(0, 0, 0, 10);

            for (int j = 0; j < numberOfTimeSlot; j++) {

                TimeSlotDetails slotItem = mScheduleDetails.getSlots().get(j);
                String startDate = slotItem.getStart();
//            Log.e("Doctor Detail", ""+startDate);

                DateFormat formatter = new SimpleDateFormat("hh:mm a");
                Date d = null;
                try {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//                d = sdf.parse("2015-11-19T08:00:00.000-05:00Z");
                    d = sdf.parse(slotItem.getStart());
                } catch (Exception e) {
                    Log.e("Date", " " + e);
                }

//            Log.e("Date", " " + d);
                String ans = formatter.format(d.getTime());

                rb[j] = new RadioButton(mContext);
//            rb[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                rb[j].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                rb[j].setTag(startDate);
                rb[j].setText(ans);
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{android.R.attr.state_checked}, mContext.getResources().getDrawable(R.drawable.radio_btn_active));
                stateListDrawable.addState(new int[]{android.R.attr.state_focused}, mContext.getResources().getDrawable(R.drawable.radio_btn_active));
                stateListDrawable.addState(new int[]{}, mContext.getResources().getDrawable(R.drawable.radio_btn_normal));
                rb[j].setButtonDrawable(stateListDrawable);
//                rb[j].setPadding(35, 15, 15, 15);

                if(((Util.getDeviceWidth(mContext) - 100) <= Constant.SCREEN_MIN_WIDTH && (Util.getDeviceHeight(mContext) - 100) <= Constant.SCREEN_MIN_HEIGHT )
                        ||  ((Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN))) {
                    rb[j].setPadding(50, 10, 10, 10);
                }else {
                    rb[j].setPadding(10, 10, 10, 10);
                }

//                ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                radioGroupAnswer.addView(rb[j]);

                View view = new View(getActivity());
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
                view.setBackgroundColor(getResources().getColor(R.color.white));
                radioGroupAnswer.addView(view);

                radioGroupAnswer.setId(mID);
                rb[j].setOnClickListener(new OnCheckButtonClickListener(j, mOnCheckButtonClickCallback, startDate, mScheduleDetails.getSystem(), mScheduleDetails.getCode(), radioGroupAnswer));
            }

            if (radioGroupAnswer != null) {
                ViewGroup parent = (ViewGroup) radioGroupAnswer.getParent();
                if (parent != null) {
                    parent.removeView(radioGroupAnswer);
                }
                radioLinear.addView(radioGroupAnswer);

            }

        }

        return rootView;
    }

//    @Override
//    public void setMenuVisibility(boolean menuVisible) {
//
//        if(menuVisible && rb != null) {
////            for (int count = 0; count < mScheduleDetails.getSlots().size() ; count++) {
////
////                rb[count].setChecked(false);
////
////            }
//
////            if(radioGroupAnswer != null)
////                radioGroupAnswer.clearCheck();
////
////            if(mContext != null)
////                ((DoctorDetailActivity) mContext).onCheckButtonClicked(null, 0, "", "", "");
//
//        }
//
//        super.setMenuVisibility(menuVisible);
//    }

}

