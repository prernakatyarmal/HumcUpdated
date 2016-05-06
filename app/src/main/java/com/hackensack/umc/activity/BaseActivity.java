package com.hackensack.umc.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

public class BaseActivity extends AppCompatActivity {

    //    private ProgressDialog mProgressDialog;
    Dialog mProgressDialog;
    View mDialogView;

    public static boolean showSessionTimeOutPopUp = false;
    public static boolean userLoggingOutManually = false;

    public static boolean isUserLogout = false;

    static boolean isLoginDone = false;
    static boolean isMyChartLogin = false;
    static boolean isAppointmentDone = false;
    public static boolean isAppWentToBg = false;
    public static boolean isWindowFocused = false;
    public static boolean isMenuOpened = false;
    public static boolean isBackPressed = false;
    static boolean isConsentDone = false;
    public static boolean isDeviceSleep = false;
    public static boolean showCredentialButton = false;

    public static boolean isPopUpShown = false;
    boolean isHelpViewRemoved;
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mProgressDialog = new ProgressDialog(BaseActivity.this);

//        mProgressDialog = new Dialog(this, R.style.TransparentProgressDialog);
        mProgressDialog = new Dialog(this, R.style.AppTheme_PopupOverlay);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.semi_tranparent_white)));
        mDialogView = getLayoutInflater().inflate(R.layout.humc_loading, null);
        mProgressDialog.setContentView(mDialogView);


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                //inform yourself that the activity unexpectedly stopped
Log.e("bhagya","Exception occured",ex);
                BaseActivity.isUserLogout = true;
                Util.stopUserSession(BaseActivity.this);
                Util.setToken(BaseActivity.this, "", "", 0);
                Util.setToken(BaseActivity.this, "", "", 0, Constant.UNKNOWN_LOGIN);
                Util.setPatientMRNID(BaseActivity.this, "");
                Util.setPatientJSON(BaseActivity.this, "");

//                if (BaseActivity.isAppWentToBg || BaseActivity.isDeviceSleep || Util.getLoggedInType(BaseActivity.this) == Constant.GUEST_LOGIN) {
                    //Code for Session Management
                    android.os.Process.killProcess(android.os.Process.myPid());
//                }

            }
        });

        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onStart() {
        Log.d("onStart", "onStart isAppWentToBg " + isAppWentToBg);

        appEnterForeground();

        super.onStart();
    }

    private void appEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
//            Toast.makeText(getApplicationContext(), "App is in foreground", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("onStop", "onStop ");
        appEnterBackground();
    }

    public void appEnterBackground() {
        if (!isWindowFocused && !isPopUpShown) {
            isAppWentToBg = true;
//            Toast.makeText(getApplicationContext(), "App is Going to Background", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (this instanceof HackensackUMCActivity) {

        } else {
            isBackPressed = true;
        }
        if (BaseActivity.this instanceof DirectionsListActivity) {
            if (Util.removeHelpViewFromParent(this, R.id.help_rl)) {
            } else {
                super.onBackPressed();
            }

        } else if (BaseActivity.this instanceof DirectionMapsActivity) {
            if (Util.removeHelpViewFromParent(this, R.id.apt_sum_help_rl)) {
            } else {
                super.onBackPressed();
            }
        } else if (BaseActivity.this instanceof HackensackUMCActivity) {
            if (Util.removeHelpViewFromParent(this, R.id.bef_login_help_rl)) {

            } else {
                super.onBackPressed();
            }
        } else if (BaseActivity.this instanceof DoctorResultActivity) {
            if (Util.removeHelpViewFromParent(this, R.id.doc_result_help_rl)) {

            } else {
                super.onBackPressed();
            }
        } else if (BaseActivity.this instanceof SymptomCheckerActivity) {
            if (Util.removeHelpViewFromParent(this, R.id.symtom_help_rl)) {

            } else {
                super.onBackPressed();
            }
        } else if (BaseActivity.this instanceof DoctorAppointmentSummaryActivity) {
            if (Util.removeHelpViewFromParent(this, R.id.apt_sum_help_rl)) {

            } else {
                super.onBackPressed();
            }
        } else if (BaseActivity.this instanceof ProfileSelfieActivity) {
            if (Util.removeHelpViewFromParent(this, R.id.profile_help_rl)) {

            } else {
                super.onBackPressed();
            }
        }  else if (BaseActivity.this instanceof FavoriteDoctorList) {
            if (Util.removeHelpViewFromParent(this, R.id.doc_result_help_rl)) {

            } else {
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }

//        Log.d("onBackPressed", "onBackPressed " + isBackPressed + "" + this.getLocalClassName());
        // super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        isWindowFocused = hasFocus;

        if (isBackPressed && !hasFocus) {
            isBackPressed = false;
            isWindowFocused = true;
        }

        super.onWindowFocusChanged(hasFocus);
    }


    public void startProgress(Context context, String message) {

//        isWindowFocused = true;
        if (mProgressDialog != null) {
            ((TextView) mDialogView.findViewById(R.id.message_text)).setText(message);
            mProgressDialog.setCancelable(false);
            if (!mProgressDialog.isShowing() && !isPopUpShown) {
                isPopUpShown = true;
                mProgressDialog.show();
            }
        }
    }

    public void stopProgress() {
        if (mProgressDialog != null) {
            isPopUpShown = false;
            mProgressDialog.dismiss();
            // mProgressDialog = null;
        }
//        isWindowFocused = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
