package com.hackensack.umc.listener;

import android.view.View;
import android.widget.RadioGroup;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class OnCheckButtonClickListener implements View.OnClickListener {

    private int position;
    private String mStartDate;
    private String mType;
    private String mID;
    private OnCheckButtonClickCallback mOnItemClickCallback;
    private RadioGroup mRadioButton;

    public OnCheckButtonClickListener(int position, OnCheckButtonClickCallback onItemClickCallback, String startDate, String type, String id, RadioGroup radiobutton) {
        this.position = position;
        this.mOnItemClickCallback = onItemClickCallback;
        mStartDate = startDate;
        mType = type;
        mID = id;
        mRadioButton = radiobutton;
    }

    @Override
    public void onClick(View view) {
        mOnItemClickCallback.onCheckButtonClicked(view, position, mStartDate, mType, mID, mRadioButton);
    }

    public interface OnCheckButtonClickCallback {
        void onCheckButtonClicked(View view, int position, String startDate, String type, String id, RadioGroup radioButton);
    }

}

