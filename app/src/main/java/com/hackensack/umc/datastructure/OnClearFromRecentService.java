package com.hackensack.umc.datastructure;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hackensack.umc.activity.BaseActivity;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;


public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    public void onTaskRemoved(Intent rootIntent) {
//        if (this instanceof HackensackUMCActivity) {
//            ((HackensackUMCActivity) context).updateLogoutUI();
//        }

        BaseActivity.isUserLogout = true;
        Util.stopUserSession(this);
//        if (Util.getLoggedInType(this) >= Constant.GUEST_LOGIN) {
//            BaseActivity.showSessionTimeOutPopUp = true;
//        }
        Util.setToken(this, "", "", 0);
        Util.setToken(this, "", "", 0, Constant.UNKNOWN_LOGIN);
        Util.setPatientMRNID(this, "");
        Util.setPatientJSON(this, "");
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }

}