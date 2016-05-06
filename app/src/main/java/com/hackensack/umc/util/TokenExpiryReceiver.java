package com.hackensack.umc.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hackensack.umc.listener.ILogoutInterface;

import epic.mychart.android.library.open.IWPOnAuthorized;
import epic.mychart.android.library.open.WPOpen;

/**
 * Created by bhagyashree_kumawat on 2/8/2016.
 */
public class TokenExpiryReceiver extends BroadcastReceiver {
    ILogoutInterface logoutListner;

    public TokenExpiryReceiver() {
    }

    public TokenExpiryReceiver(ILogoutInterface logoutListner) {
        this.logoutListner = logoutListner;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        IWPOnAuthorized listner = new IWPOnAuthorized() {
            @Override
            public void didGetNewTicket(boolean success) {
                Log.e("mychart", "onreceive TokenExpiryReceiver " + success);
                if (success) {
                    Log.e("mychart", "TokenExpiryReceiver success");
                    // Util.startMychartTokenSync(context,WPOpen.millisToTokenExpiration());
                } else {
                    Log.e("mychart", "TokenExpiryReceiver success");
                    Util.stopMychartTokenSync(context);
                    Util.stopDeviceSync(context);
                    if(logoutListner!=null)
                    logoutListner.didLogout();
                    //Unable to refresh the token
                    //callback to update logoutUI
                }
            }
        };
        WPOpen.initialize(context);
        WPOpen.refreshAuthorizationToken(listner);
    }
}
