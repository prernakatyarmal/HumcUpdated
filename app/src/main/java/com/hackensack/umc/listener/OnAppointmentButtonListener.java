package com.hackensack.umc.listener;

import android.view.View;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class OnAppointmentButtonListener {

    private int position;
    private OnAppointmentButtonCallback mOnItemClickCallback;

    public interface OnAppointmentButtonCallback {
        boolean onCancelButtonCallback(View view, int position);
        boolean onRescheduleButtonCallback(View view, int position);

    }

}
