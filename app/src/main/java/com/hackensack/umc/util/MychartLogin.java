package com.hackensack.umc.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hackensack.umc.listener.ILoginInteface;

import java.util.Map;

import epic.mychart.android.library.open.WPLoginResultType;
import epic.mychart.android.library.open.WPOpen;

/**
 * Created by bhagyashree_kumawat on 2/1/2016.
 */
public class MychartLogin extends AsyncTask<Object, Void, WPLoginResultType>{
    Context context;
    ILoginInteface listener;

    public MychartLogin(Context context, ILoginInteface listener) {
        //To initialize mychart Library
        WPOpen.initialize(context);
        this.context = context;
        this.listener = listener;
    }
    @Override
    protected WPLoginResultType doInBackground(Object... params) {
        String username = (String) params[0];
        String password = (String) params[1];
        Log.e("mychart", "username " + username + " password " + password);
        WPLoginResultType type = WPOpen.doLogin(context, username, password);
        Log.e("mychart", "After dologin" + type);
        if (type != WPLoginResultType.Success) {
            return type;
        } else {
            int count = WPOpen.getPatients().size();
            for (int i = 0; i < count; i++) {
                WPOpen.setPatientIndex(i);
                if (WPOpen.isFeatureOn("ALERTS")) {
                    Map<String, Integer> badgeMap = WPOpen.downloadAlerts();
                    //didGetAlerts(i, badgeMap);
                } else {
//                                    didGetAlerts(i, new HashMap<String, Integer>(0));
                }
            }
            WPOpen.setPatientIndex(0);
            return WPLoginResultType.Success;
        }
    }

    @Override
    protected void onPostExecute(WPLoginResultType result) {
        /*if (result == WPLoginResultType.Success) {
                if (listener != null) {
            listener.didLogIn();
            }
                        *//*}
                        else {
                            Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
                        }*//*
           *//* _progressBar.setVisibility(View.GONE);
            _loginContainer.setVisibility(View.VISIBLE);*//*
        }else if(result == WPLoginResultType.NewTerms){
            Intent intent = WPOpen
                    .makeTermsAndConditionsIntent(context, result);
            ((Activity)context).startActivityForResult(intent, Constant.REQUEST_TERMSCONDITIONS);
        }else if(result == WPLoginResultType.Failure){
            Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
        }*/
        switch (result) {
            case Success:
                if (listener != null) {
                    listener.didLogIn(true);
                }
                break;
            case NewTerms:
            case ShowProxyDisclaimer:
            case ShowUpdatedTerms:
                if (listener != null) {
                    listener.showNewTems(result);
                }
                break;
            case Failure:
                if (listener != null) {
                    listener.didLogIn(false);
                }
                break;
            default:
                Toast.makeText(context, result
                        .toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
