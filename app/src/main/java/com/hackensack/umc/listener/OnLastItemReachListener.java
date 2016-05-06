package com.hackensack.umc.listener;

import android.view.View;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class OnLastItemReachListener {

    private int position;
    private OnLastItemReachCallback mOnItemClickCallback;

    public interface OnLastItemReachCallback {
        boolean onLastItemReach(View view, int position);
       void OnAllListItemDeleted();
    }

}
