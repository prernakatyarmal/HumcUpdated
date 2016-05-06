package com.hackensack.umc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.UserToken;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.listener.ILoginInteface;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.MychartLogin;
import com.hackensack.umc.util.Util;

import java.util.Set;

import epic.mychart.android.library.open.WPBaseFeatureType;
import epic.mychart.android.library.open.WPLoginResultType;
import epic.mychart.android.library.open.WPOpen;

public class LoginActivity extends BaseActivity implements HttpDownloadCompleteListener, ILoginInteface {

    Button mLoginButton, mGuestButton, mDependentButton;
    private Button btnCreateCredential;
    private TextView welcomeTv;

    EditText mUserName, mPassword;

    private boolean isUserManualLogOut = false;

    private String mCalledClass;
    private String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //To initialize mychart Library
        WPOpen.initialize(LoginActivity.this);

        getSupportActionBar().hide();

        mDependentButton = (Button) findViewById(R.id.dependent_button);
        mGuestButton = (Button) findViewById(R.id.proceed_guest_button);
        (findViewById(R.id.back_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackPressed = true;
                finish();
            }
        });
        Intent intent = getIntent();
        mCalledClass = intent.getStringExtra(Constant.ACTIVITY_FLOW);
        btnCreateCredential = (Button) findViewById(R.id.create_cred_button);
        welcomeTv = (TextView) findViewById(R.id.welcome_text);
        showCredentialButton = false;

       if (intent != null && mCalledClass != null && mCalledClass.equals(Constant.APPOINTMENT_SUMMARY_FLOW)) {
            
            welcomeTv.setText(getString(R.string.appt_login_guide_text));
            ( findViewById(R.id.action_bar_ll)).setVisibility(View.GONE);
            ( findViewById(R.id.copy_right_text)).setVisibility(View.GONE);
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ( findViewById(R.id.create_cred_info_txt)).setVisibility(View.INVISIBLE);

           btnCreateCredential.setVisibility(View.GONE);
//           btnCreateCredential.setText(getResources().getString(R.string.register_patient_button_text));
//           btnCreateCredential.setBackgroundColor(getResources().getColor(R.color.profile_proceed_green));
//           btnCreateCredential.setOnClickListener(new View.OnClickListener() {
//
//               @Override
//               public void onClick(View view) {
//                   showButton = true;
//                   //For parent Registration
//                   Util.setRegistrationIsFor(Constant.REGISTRATION_FOR_PARENTS,LoginActivity.this);
//                   Intent intent = new Intent(LoginActivity.this, ProfileSelfieActivity.class);
//                   startActivity(intent);
//
//
//               }
//
//           });

//            mGuestButton.setText(getResources().getString(R.string.register_new_dependant_btn_text));
           mGuestButton.setText(getResources().getString(R.string.register_patient_button_text));
            mGuestButton.setBackgroundColor(getResources().getColor(R.color.profile_proceed_green));
            mGuestButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    showButton = true


                    //For parent Registration
                    Util.setRegistrationIsFor(Constant.REGISTRATION_FOR_PARENTS, LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, ProfileSelfieActivity.class);
                    startActivity(intent);

                }

            });
           mDependentButton.setVisibility(View.VISIBLE);
           mDependentButton.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View view) {
//                   showButton = true;

                   //For dependent registration
                    Util.setRegistrationIsFor(Constant.REGISTRATION_FOR_DEPENDENT,LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, DependentDetailsActivity.class);
                    startActivity(intent);

               }

           });

        } else {
            (findViewById(R.id.create_cred_info_txt)).setVisibility(View.VISIBLE);
            (findViewById(R.id.copy_right_text)).setVisibility(View.VISIBLE);
            welcomeTv.setText(getString(R.string.guide_text));

           mGuestButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    finish();
                }

            });
           btnCreateCredential.setVisibility(View.VISIBLE);
           btnCreateCredential.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent1 = new Intent(LoginActivity.this, CreatePasswordActivity.class);
                   intent1.putExtra(Constant.FROM_ACTIVTY, Constant.FROM_LOGIN_ACTIVITY);
                   startActivity(intent1);
               }
           });
        }

        if (intent != null && mCalledClass != null && mCalledClass.equals(Constant.SHOW_LOGIN_SCREEN)) {
            isUserManualLogOut = true;
        }

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String userName = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if ((userName == null || userName.length() < 1) && (password == null || password.length() < 1)) {
                    Util.showNetworkOfflineDialog(LoginActivity.this, getString(R.string.error_text), getString(R.string.login_validation));
                    return;
                } else if (userName == null || userName.length() < 1) {
                    Util.showNetworkOfflineDialog(LoginActivity.this, getString(R.string.error_text), getString(R.string.login_validation_username));
                    return;
                } else if (password == null || password.length() < 1) {
                    Util.showNetworkOfflineDialog(LoginActivity.this, getString(R.string.error_text), getString(R.string.login_validation_password));
                    return;
                }


                if (Util.ifNetworkAvailableShowProgressDialog(LoginActivity.this, getString(R.string.login_text), true)) {
                    MychartLogin mychartLoginAsync = new MychartLogin(LoginActivity.this, LoginActivity.this);
                    mychartLoginAsync.execute(userName, password);
                }

            }

        });

        mUserName = (EditText) findViewById(R.id.username_edittext);
        mPassword = (EditText) findViewById(R.id.password_edittext);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_TERMSCONDITIONS:
                if (resultCode == RESULT_OK)
                    didLogIn(true);
                else
                didLogIn(false);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    public String getUserData() {

        String str = "{\"username\": \"USERID\", \"password\": \"PASS\"}";

        str = str.replace("USERID", mUserName.getText().toString().trim());
        str = str.replace("PASS", mPassword.getText().toString().trim());

        return str;
    }

    public String getUserName() {
        return mUserName.getText().toString().trim();
    }

    public String getPassword() {
        return mPassword.getText().toString().trim();
    }

    @Override
    public void showNewTems(WPLoginResultType result) {
        Intent intent = WPOpen
                .makeTermsAndConditionsIntent(LoginActivity.this, result);
        startActivityForResult(intent, Constant.REQUEST_TERMSCONDITIONS);
    }

    @Override
    public void didLogIn(boolean isloggedIn) {
        if (isloggedIn) {
            isMyChartLogin = true;
            Log.e("mychart","CreatePassword didlogin "+isloggedIn);
            Util.startMychartTokenSync(LoginActivity.this,WPOpen.millisToTokenExpiration());
            Util.startDeviceSync(LoginActivity.this,WPOpen.millisToDeviceTimeOut());
            Set<WPBaseFeatureType> basefeatures = WPBaseFeatureType.getFeatureSet();
            if (Util.ifNetworkAvailableShowProgressDialog(LoginActivity.this, getString(R.string.login_text), false)) {
                HttpDownloader http = new HttpDownloader(LoginActivity.this, ServerConstants.LOGIN_URL, Constant.LOGIN_DATA, LoginActivity.this);
                http.startDownloading();
            }
            Util.setRegistrationIsFor(Constant.PARENTS_LOGIN, LoginActivity.this);
        } else {
            Util.showNetworkOfflineDialog(LoginActivity.this, getString(R.string.error_text), getString(R.string.login_error_validation));
            mPassword.setText("");
            stopProgress();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void HttpDownloadCompleted(HttpResponse data) {
        Log.v(TAG, String.valueOf(data.getResponseCode()));
        Log.v(TAG, "in HttpDownloadCompleted");

        Object obj = data.getDataObject();

        if (obj != null)
            Log.v(TAG, obj.toString());
        if (data.getRequestType() == Constant.LOGIN_DATA) {
            if ((data.getResponseCode() >= Constant.HTTP_OK && data.getResponseCode() < Constant.HTTP_REDIRECT)) {
                if (obj != null && !isFinishing()) {

                    if (obj instanceof UserToken) {

                        String token = ((UserToken) obj).getAccessToken();

                        if (token != null && token.length() > 0) {

                            HttpDownloader http = new HttpDownloader(LoginActivity.this, ServerConstants.READ_PATIENT_URL + Util.getPatientMRNID(this) + ServerConstants.READ_PATIENT_URL_PART, Constant.READ_PATIENT_DATA, LoginActivity.this);
                            http.startDownloading();

                            Util.startUserSession(this);

                            return;
                        }
                    }

                }
            } else {
                Util.showNetworkOfflineDialog(LoginActivity.this, getString(R.string.error_text), getString(R.string.login_error_validation));
                mPassword.setText("");
            }

        } else if (data.getRequestType() == Constant.READ_PATIENT_DATA && !isFinishing()) {

            isLoginDone = true;
            finish();
            Util.storehelpboolean(this, Constant.HELP_PREF_HACKENSACK, true);

        }
        stopProgress();
    }




    @Override
    protected void onResume() {
        super.onResume();

        if(btnCreateCredential != null && showCredentialButton) {
            btnCreateCredential.setVisibility(View.VISIBLE);
            btnCreateCredential.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(LoginActivity.this, CreatePasswordActivity.class);
                    intent1.putExtra(Constant.FROM_ACTIVTY, Constant.FROM_LOGIN_ACTIVITY);
                    startActivity(intent1);
                }
            });
        }

        if (!isUserManualLogOut && (isUserLogout || isLoginDone)) {
            finish();
        }
    }

}
