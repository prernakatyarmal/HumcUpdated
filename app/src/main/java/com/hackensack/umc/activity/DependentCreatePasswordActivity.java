package com.hackensack.umc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.PatientData;
import com.hackensack.umc.datastructure.UserToken;
import com.hackensack.umc.http.CommonAPICalls;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.listener.ILoginInteface;
import com.hackensack.umc.patient_data.DependentsData;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.MychartLogin;
import com.hackensack.umc.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import epic.mychart.android.library.open.WPLoginResultType;
import epic.mychart.android.library.open.WPOpen;

public class DependentCreatePasswordActivity extends BaseActivity implements HttpDownloadCompleteListener, ILoginInteface {
    private EditText edtPassword;
    private EditText edtVerifyPassword;
    private EditText txtUserName;
    private EditText txtMRN;
    private String mUserName;
    private String mMRN;
    private String TAG = "CreatePasswordActivity";
    private SharedPreferences sharedPreferences;
    private String mPassword;
    private String mVerifyPassword;
    private boolean isFromLogin = false;
    private String lastName;
    private String firstName;
    private String birthDate;
    private boolean isPasswordCreated;
    // private Button btnGetMRN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_password_for_dependent);

        iflateXml();
        getDataFromIntent();
        initialiseVariables();
        showCredentialButton = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_password, menu);
        return true;
    }

    @Override
    public void showNewTems(WPLoginResultType result) {
        Intent intent = WPOpen
                .makeTermsAndConditionsIntent(DependentCreatePasswordActivity.this, result);
        startActivityForResult(intent, Constant.REQUEST_TERMSCONDITIONS);
    }

    @Override
    public void didLogIn(boolean isLoggedIn) {
        stopProgress();
        if (isLoggedIn) {
            isMyChartLogin = true;
            Log.e("mychart", "CreatePassword didlogin " + isLoggedIn);
            Util.startMychartTokenSync(DependentCreatePasswordActivity.this, WPOpen.millisToTokenExpiration());
            Util.startDeviceSync(DependentCreatePasswordActivity.this, WPOpen.millisToDeviceTimeOut());
            if (Util.ifNetworkAvailableShowProgressDialog(DependentCreatePasswordActivity.this, getString(R.string.login_text), true)) {
                HttpDownloader http = new HttpDownloader(DependentCreatePasswordActivity.this, ServerConstants.LOGIN_URL, Constant.LOGIN_DATA, DependentCreatePasswordActivity.this);
                http.startDownloading();
            }
        } else {
            Log.e("mychart", "CreatePassword didlogin " + isLoggedIn);
            AlertDialog.Builder builder = new AlertDialog.Builder(DependentCreatePasswordActivity.this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);
            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getString(R.string.error_str));
            if (isFromLogin) {
                ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.mychart_decline_str_from_login));
            } else {
                ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.login_fail_non_patient_parent));
            }
            Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    isLoginDone = true;
                    isMyChartLogin = false;
                    Util.isRegistrationFlowFinished = true;
                    Util.setPatientMRNID(DependentCreatePasswordActivity.this, mMRN);
                    if (isFromLogin) {
                        edtPassword.setText("");
                        edtVerifyPassword.setText("");
                    } else {
                        DependentCreatePasswordActivity.this.finish();
                    }
                }
            });

            alert = builder.show();
            //Util.showNetworkOfflineDialog(CreatePasswordActivity.this, getString(R.string.error_str), getString(R.string.mychart_decline_str));

            //stopProgress();
            //Show popup call helpdesk
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_TERMSCONDITIONS:
                if (resultCode == RESULT_OK) {
                    Log.e("mychart", "CreatePassword REQUEST_TERMSCONDITIONS " + resultCode);
                    didLogIn(true);
                } else {
                    Log.e("mychart", "CreatePassword REQUEST_TERMSCONDITIONS " + resultCode);
                    didLogIn(false);
                }

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
               QuetionsActivity.backFromCreatePassword = true;
                isBackPressed = true;
                finish();
                return true;
            case R.id.password_action_done:
                if (validatePasswords()) {
                    Log.v(TAG, "create_password_action_done");
                    if (Util.ifNetworkAvailableShowProgressDialog(DependentCreatePasswordActivity.this, (getString(R.string.creating_mychart)), true)) {
                        new getEpicAccessToken().execute();
                    }

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void initialiseVariables() {

        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        mUserName = intent.getStringExtra(Constant.USER_NAME);
        mMRN = intent.getStringExtra(Constant.MRN);
        txtUserName.setText(mUserName);
        txtUserName.setEnabled(false);
        lastName = intent.getStringExtra(Constant.PARENTS_LAST_NAME);
        firstName = intent.getStringExtra(Constant.PARENTS_FIRST_NAME);
        birthDate = intent.getStringExtra(Constant.PARENTS_BIRTHDATE);

    }

    private void iflateXml() {
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtVerifyPassword = (EditText) findViewById(R.id.edtVerifyPassword);
        txtUserName = (EditText) findViewById(R.id.txt_userName);

       /* btnGetMRN= (Button) findViewById(R.id.btnGetMRN);
        btnGetMRN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePasswordActivity.this, ProfileSelfieActivity.class);
                startActivity(intent);
                finish();
            }
        });*/


    }

    private AlertDialog alert;

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

                            HttpDownloader http = new HttpDownloader(DependentCreatePasswordActivity.this, ServerConstants.READ_PATIENT_URL + Util.getPatientMRNID(this) + ServerConstants.READ_PATIENT_URL_PART, Constant.READ_PATIENT_DATA, DependentCreatePasswordActivity.this);
                            http.startDownloading();

                            Util.startUserSession(this);

                            return;
                        }
                    }

                }
            } else {
                Util.showNetworkOfflineDialog(DependentCreatePasswordActivity.this, "Fail", "Log in fail, please try again with valid username and password");
            }

        } else if (data.getRequestType() == Constant.READ_PATIENT_DATA && !isFinishing()) {

            isLoginDone = true;
            Util.isRegistrationFlowFinished = true;
            finish();

        }
        stopProgress();
    }

    private class CreatePassword extends AsyncTask<String, Void, String> {
        String createPasswordSuccess;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String message = null;
            String response = null;
            org.apache.http.HttpResponse httpResponse = HttpUtils.sendHttpPostForUsingJsonObj_1(ServerConstants.URL_CREATE_ACCOUNT_DEPENDENT, createJsonForCreatePassword(params[0]), createHeadersForCreatePassword());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                isPasswordCreated = true;
            }

            try {
                response = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                Log.v(TAG, jsonObject.toString());
                return message;

                // createPasswordSuccess = jsonObject.getString("success");

            } catch (IOException eo) {

            } catch (JSONException e) {
                e.printStackTrace();
                // {"message":"Error occurred while handling SOAP response at HUMC EPIC Server.The national ID passed in does not have a valid format.","code":"500"}
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    message = jsonObject.getString("message");
                    Log.v(TAG, "response to createPassword::" + response);
                    return message;
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }


            return response;
        }

        @Override
        protected void onPostExecute(String response) {

            super.onPostExecute(response);

            stopProgress();

            if (response != null) {
                if (isPasswordCreated) {
//
                    if (Util.ifNetworkAvailableShowProgressDialog(DependentCreatePasswordActivity.this, getString(R.string.login_text), true)) {
                        /*HttpDownloader http = new HttpDownloader(CreatePasswordActivity.this, ServerConstants.LOGIN_URL, Constant.LOGIN_DATA, CreatePasswordActivity.this);
                        http.startDownloading();*/
                        MychartLogin mychartLoginAsync = new MychartLogin(DependentCreatePasswordActivity.this, DependentCreatePasswordActivity.this);
                        mychartLoginAsync.execute(mUserName, mPassword);
                    }
                    //Util.showAlert(CreatePasswordActivity.this, getString(R.string.msg_password_created_successfully), getString(R.string.success));
                } else {
                    showAlert(DependentCreatePasswordActivity.this,  getString(R.string.error_str),response);
                }
            }
            // stopProgress();
        }
    }

    public void showAlert(Activity activity, String title, String message) {

        if (!activity.isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            if (title.length() > 0)
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            else
                (dialogView.findViewById(R.id.dialog_title)).setVisibility(View.GONE);

            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    makeAppointmentForDependentOfAlreadyRegisteredParent();
                }
            });

            alert = builder.show();
        }

    }

    private void makeAppointmentForDependentOfAlreadyRegisteredParent() {
        isLoginDone = true;
        isMyChartLogin = false;

        Util.isRegistrationFlowFinished = true;
        Util.setPatientMRNID(DependentCreatePasswordActivity.this, mMRN);
        DependentCreatePasswordActivity.this.finish();

    }

    public String getUserData() {

        String str = "{\"username\": \"USERID\", \"password\": \"PASS\"}";

        str = str.replace("USERID", mUserName.trim());
        str = str.replace("PASS", edtVerifyPassword.getText().toString().trim());

        return str;
    }

    private List<NameValuePair> createHeadersForCreatePassword() {
        ArrayList<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        headers.add(new BasicNameValuePair("Authorization", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "")));
        return headers;
    }

    private JSONObject createJsonForCreatePassword(String patientId) {
        Gson gson = new Gson();
        JSONObject jsonForCreatePassword = null;
        mUserName = txtUserName.getText().toString().trim();
        //ionaryWithObjectsAndKeys:[appDelegate.personalInfoDictionary valueForKey:@"Email"], @"loginId", _passwordTextField.text, @"password", @"Favorite game?", @"passwordResetQuestion", @"cricket", @"passwordResetAnswer",[appDelegate.personalInfoDictionary valueForKey:@"Email"], @"emailAddress",@"true", @"receiveEmailNotifications", [appDelegate.personalInfoDictionary valueForKey:@"LastName"], @"lastName", [appDelegate.personalInfoDictionary valueForKey:@"FirstName"], @"firstName",birthdate, @"dateOfBirth",@"", @"nationalIdentifier",nil];
//All information of parents
        // patientId=mMRN;
        //String dataForPasswordCreation = gson.toJson(new DependentsData(patientId, "HUMC", mUserName, "Favorite game?", "cricket", mUserName, "true", mPassword));
        //DependentsData(String emailAddress, String loginId, String passwordResetAnswer, String password, String passwordResetQuestion, String nationalIdentifier, String receiveEmailNotifications, String lastName, String firstName, String dateOfBirth) {
        String dataForPasswordCreation = gson.toJson(new DependentsData(mUserName, mUserName, "cricket?", mPassword, "Favorite game?", "", "true", lastName, firstName, birthDate));
        //  String dataForPasswordCreation = gson.toJson(new PatientData(txtMRN.getText().toString().trim(), "HUMC", txtUserName.getText().toString().trim(), "", "", txtUserName.getText().toString().trim(), "true", mPassword));

        //String patientId, String patientIdType, String loginId, String passwordResetQuestion, String passwordResetAnswer,
        // String emailAddress, String receiveEmailNotifications, String password) {
        try {
            jsonForCreatePassword = new JSONObject(dataForPasswordCreation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "jsonForCreatePassword::" + jsonForCreatePassword.toString());
        return jsonForCreatePassword;
    }

    private boolean validatePasswords() {
        mPassword = edtPassword.getText().toString();
        mVerifyPassword = edtVerifyPassword.getText().toString();
        if (TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mVerifyPassword)) {
            Util.showAlert(DependentCreatePasswordActivity.this, getString(R.string.msg_enter_valid_password), getString(R.string.app_name));
            edtPassword.setText("");
            edtVerifyPassword.setText("");
            return false;
        } else if (!mPassword.matches(Constant.PASSWOSRD_REGULAR_EXPRESSION)) {
            Util.showAlert(DependentCreatePasswordActivity.this, getString(R.string.msg_enter_valid_password), getString(R.string.app_name));
            edtPassword.setText("");
            edtVerifyPassword.setText("");
            return false;
        } else if (!mPassword.equalsIgnoreCase(mVerifyPassword)) {
            Util.showAlert(DependentCreatePasswordActivity.this, getString(R.string.msg_enter_password_not_matching), getString(R.string.app_name));
            edtPassword.setText("");
            edtVerifyPassword.setText("");
            return false;
        }
        return true;
    }

    private class getEpicAccessToken extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // startProgress(getString(R.string.creating_mychart));
        }

        @Override
        protected String doInBackground(Void... params) {
            String accessToken = new CommonAPICalls(DependentCreatePasswordActivity.this).getEpicAccessToken();
            return accessToken;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (Util.ifNetworkAvailableShowProgressDialog(DependentCreatePasswordActivity.this, (getString(R.string.creating_mychart)), true)) {
                new CreatePassword().execute(mMRN);
            }


            // new readPatient().execute(accessToken);//Login call after userCreatyion

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        QuetionsActivity.backFromCreatePassword  = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            QuetionsActivity.backFromCreatePassword = true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isUserLogout) {
            finish();
        }
    }
}
