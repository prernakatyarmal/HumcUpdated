package com.hackensack.umc.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import epic.mychart.android.library.open.WPOpen;

/**
 * Created by bhagyashree_kumawat on 2/8/2016.
 */
public class DeviceTimedOutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("mychart", "DeviceTimedOutReceiver onreceive");
        WPOpen.initialize(context);
        WPOpen.delayDeviceTimeOut();
    }
}
