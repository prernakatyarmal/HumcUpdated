package com.hackensack.umc.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.SpecialtyListAdapter;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.PatientData;
import com.hackensack.umc.datastructure.UserToken;
import com.hackensack.umc.http.CommonAPICalls;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.listener.ILoginInteface;
import com.hackensack.umc.patient_data.PatientMRN;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.MychartLogin;
import com.hackensack.umc.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import epic.mychart.android.library.open.WPLoginResultType;
import epic.mychart.android.library.open.WPOpen;

public class CreatePasswordActivity extends BaseActivity implements HttpDownloadCompleteListener, ILoginInteface {
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
    private String mUserEmailId;
    private String lastName;
    private String firstName;
    private String birthDate;
    private String gender;
    // private Button btnGetMRN;
    // Extra fields included
    private EditText mFname, mLname, mGender;
    private TextView mDob;
    private android.support.v7.widget.CardView mAddInfo;
    private int mMonth, mDay, mYear;
    private String[] mGenderArray = {"Male", "Female"};
    private SpecialtyListAdapter mGenderAdapter;
    private ListView mDialogListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_password);

        iflateXml();
        getDataFromIntent();
        initialiseVariables();
        getcurrentDate();

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
                .makeTermsAndConditionsIntent(CreatePasswordActivity.this, result);
        startActivityForResult(intent, Constant.REQUEST_TERMSCONDITIONS);
    }

    @Override
    public void didLogIn(boolean isLoggedIn) {
        stopProgress();
        if (isLoggedIn) {
            isMyChartLogin = true;
            Log.e("mychart", "CreatePassword didlogin " + isLoggedIn);
            Util.startMychartTokenSync(CreatePasswordActivity.this, WPOpen.millisToTokenExpiration());
            Util.startDeviceSync(CreatePasswordActivity.this, WPOpen.millisToDeviceTimeOut());
            if (Util.ifNetworkAvailableShowProgressDialog(CreatePasswordActivity.this, getString(R.string.login_text), true)) {
                HttpDownloader http = new HttpDownloader(CreatePasswordActivity.this, ServerConstants.LOGIN_URL, Constant.LOGIN_DATA, CreatePasswordActivity.this);
                http.startDownloading();
            }
        } else {
            Log.e("mychart", "CreatePassword didlogin " + isLoggedIn);
            AlertDialog.Builder builder = new AlertDialog.Builder(CreatePasswordActivity.this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);
            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getString(R.string.error_str));
            if (isFromLogin) {
                ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.mychart_decline_str_from_login));
            } else {
                ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.mychart_decline_str));
            }
            Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    isLoginDone = true;
                    isMyChartLogin = false;
                    Util.isRegistrationFlowFinished = true;
                    Util.setPatientMRNID(CreatePasswordActivity.this, mMRN);
                    if (isFromLogin) {
                        edtPassword.setText("");
                        edtVerifyPassword.setText("");
                    } else {
                        CreatePasswordActivity.this.finish();
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
                if (!TextUtils.isEmpty(txtUserName.getText().toString()) && !TextUtils.isEmpty(txtUserName.getText().toString())) {
                    if (isFromLogin && validateExtraFields()) {
                        firstName = mFname.getText().toString();
                        lastName = mLname.getText().toString();
                        gender = mGender.getText().toString();
                        if (validatePasswords()) {
                            Log.v(TAG, "create_password_action_done");
                            if (Util.ifNetworkAvailableShowProgressDialog(CreatePasswordActivity.this, (getString(R.string.creating_mychart)), true)) {
                                new getEpicAccessToken().execute();
                            }
                        }
                    } else if (validatePasswords()) {
                        Log.v(TAG, "create_password_action_done");
                        if (Util.ifNetworkAvailableShowProgressDialog(CreatePasswordActivity.this, (getString(R.string.creating_mychart)), true)) {
                            new getEpicAccessToken().execute();
                        }

                    }
                } else {
                    Util.showAlert(CreatePasswordActivity.this, getString(R.string.msg_enter_valid_acc_info), getString(R.string.app_name));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     * initialise the class variables
     */
    private void initialiseVariables() {
        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
    }

    /**
     * get data from the intent send from previous activity
     */
    private void getDataFromIntent() {
        Intent intent = getIntent();
        int from = intent.getIntExtra(Constant.FROM_ACTIVTY, Constant.FROM_REGISTRATION_ACTIVITY);
        if (from == Constant.FROM_REGISTRATION_ACTIVITY) {
            isFromLogin = false;
            txtUserName.setEnabled(false);
            txtMRN.setEnabled(false);
            mUserName = intent.getStringExtra(Constant.USER_NAME);
            mUserEmailId = intent.getStringExtra(Constant.USER_NAME);
            mMRN = intent.getStringExtra(Constant.MRN);

            lastName = intent.getStringExtra(Constant.PARENTS_LAST_NAME);
            firstName = intent.getStringExtra(Constant.PARENTS_FIRST_NAME);
            birthDate = intent.getStringExtra(Constant.PARENTS_BIRTHDATE);
            gender = intent.getStringExtra(Constant.PARENTS_GENDER);

            txtUserName.setText(mUserName);
            txtMRN.setText(mMRN);
            mAddInfo.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.personal_info_tv)).setVisibility(View.GONE);
            setTitle(getString(R.string.title_activity_create_password));


        } else if (from == Constant.FROM_LOGIN_ACTIVITY) {
            isFromLogin = true;
            //btnGetMRN.setVisibility(View.VISIBLE);
            txtUserName.setEnabled(true);
            txtMRN.setEnabled(true);
            setTitle(getString(R.string.title_activity_create_creadentials));
            mAddInfo.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.personal_info_tv)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.create_cred_date_ll)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDatePickerDialog(CreatePasswordActivity.this, ((TextView) findViewById(R.id.dob_tv)));

                }
            });
            mGenderAdapter = new SpecialtyListAdapter(CreatePasswordActivity.this, mGenderArray, true);
            mGender = (EditText) findViewById(R.id.create_cred_gender_edt);
            mGender.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

//                if((alert == null || !alert.isShowing()) && count == 0)
                    if (event.getAction() == MotionEvent.ACTION_UP)
                        showGenderDialog();

                    return false;
                }
            });
        }

       /* mMRN = "900702417";
        mUserName = "gautam_zodape@persistent.com";*/

    }

    /**
     *Inflate all views from xml
     */
    private void iflateXml() {
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtVerifyPassword = (EditText) findViewById(R.id.edtVerifyPassword);
        txtUserName = (EditText) findViewById(R.id.txt_userName);
        txtMRN = (EditText) findViewById(R.id.txt_MRN);

        mAddInfo = (CardView) findViewById(R.id.create_cred_extra_info);
        mFname = (EditText) findViewById(R.id.create_cred_fname);
        mLname = (EditText) findViewById(R.id.create_cred_lname);
        mGender = (EditText) findViewById(R.id.create_cred_gender_edt);
        mDob = (TextView) findViewById(R.id.dob_tv);

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

                            HttpDownloader http = new HttpDownloader(CreatePasswordActivity.this, ServerConstants.READ_PATIENT_URL + Util.getPatientMRNID(this) + ServerConstants.READ_PATIENT_URL_PART, Constant.READ_PATIENT_DATA, CreatePasswordActivity.this);
                            http.startDownloading();

                            Util.startUserSession(this);

                            return;
                        }
                    }

                }
            } else {
                Util.showNetworkOfflineDialog(CreatePasswordActivity.this, "Fail", "Log in fail, please try again with valid username and password");
            }

        } else if (data.getRequestType() == Constant.READ_PATIENT_DATA && !isFinishing()) {

            isLoginDone = true;
            Util.isRegistrationFlowFinished = true;
            finish();

        }
        stopProgress();
    }

    /**
     * Asyntak to create password after entering details by user
     */
    private class CreatePassword extends AsyncTask<String, Void, String> {
        String createPasswordSuccess;
        String responseCode;
        String message = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {


            String response = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_CREATE_ACCOUNT, createJsonForCreatePassword(params[0]), createHeadersForCreatePassword());
            try {
                JSONObject jsonObject = new JSONObject(response);

                //11-04 15:23:26.683: V/CreatePasswordActivity(14731): response to createPassword::{"success":true}

                createPasswordSuccess = jsonObject.getString("success");
            } catch (JSONException e) {
                e.printStackTrace();
                //11-05 19:41:12.174: V/CreatePasswordActivity(27479): response to createPassword::{"message":"The login ID passed in already belongs to somebody else's MyChart account.","code":"422"}
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    message = jsonObject.getString("message");
                    responseCode = jsonObject.getString("code");
                    return message;
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            Log.v(TAG, "response to createPassword::" + response);
            return createPasswordSuccess;
        }

        @Override
        protected void onPostExecute(String response) {

            super.onPostExecute(response);

            // stopProgress();

            if (response != null) {
                if (response.equalsIgnoreCase("true")) {
//
                    if (Util.ifNetworkAvailableShowProgressDialog(CreatePasswordActivity.this, getString(R.string.login_text), false)) {
                        MychartLogin mychartLoginAsync = new MychartLogin(CreatePasswordActivity.this, CreatePasswordActivity.this);
                        mychartLoginAsync.execute(mUserEmailId, mPassword);
                    }

                } else {
                    if (responseCode.equalsIgnoreCase("500")) {
                        if (message.equalsIgnoreCase(getString(R.string.msg_from_epic_user_alredy_registered))) {
                            message = (getString(R.string.msg_from_app_user_already_registered));
                            showDaialogForAlreadyRegisterUser(CreatePasswordActivity.this, getString(R.string.error_str), message);
                        } else if (message.equalsIgnoreCase(getString(R.string.msg_from_epic_user_miss_match))) {
                            message = (getString(R.string.msg_from_app_user_miss_match));
                            showDaialogForCredentialMissMatched(CreatePasswordActivity.this, getString(R.string.error_str), message);
                        } else {
                            Util.showAlert(CreatePasswordActivity.this, response, getString(R.string.error_str));
                        }
                        stopProgress();
                    } else {
                        Util.showAlert(CreatePasswordActivity.this, message, getString(R.string.error_str));
                        stopProgress();
                    }

                }
            } else {
                stopProgress();
            }
            // stopProgress();
        }

        private void showDaialogForCredentialMissMatched(Activity activity, String title, String message) {

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
                        alert.dismiss();
                        isMyChartLogin = false;
                        isLoginDone = true;
                        Util.isRegistrationFlowFinished = true;
                        Util.setPatientMRNID(CreatePasswordActivity.this, mMRN);
                        CreatePasswordActivity.this.finish();
                    }
                });

                alert = builder.show();
            }
        }
    }

    private void showDaialogForAlreadyRegisterUser(Activity activity, String title, String message) {

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
                    alert.dismiss();
                    txtUserName.setEnabled(true);
                }
            });

            alert = builder.show();
        }

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


    /**
     *
     * @param patientId
     * @return -Json object to send for create password formed with the fields enter by user.
     */
    private JSONObject createJsonForCreatePassword(String patientId) {
        Gson gson = new Gson();
        JSONObject jsonForCreatePassword = null;
        mMRN = txtMRN.getText().toString().trim();
        mUserName = txtUserName.getText().toString().trim();
        patientId = mMRN;
        if (TextUtils.isEmpty(mUserEmailId)) {
            mUserEmailId = mUserName;
        }
        //New change to be uncomment after deployment from CSL team.
        PatientData patientData = new PatientData(patientId, "HUMC", mUserName, "Favorite game?", mPassword, "cricket", mUserEmailId, firstName, lastName, birthDate, "true");
        //patientData.setGender(gender);

        //  PatientData patientData = new PatientData(patientId, "HUMC", mUserName, "Favorite game?", "cricket", mUserEmailId, "true", mPassword);


        String dataForPasswordCreation = gson.toJson(patientData);
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

    private boolean validateExtraFields() {
        if (TextUtils.isEmpty(mFname.getText()) || !Util.validateTextInput1(mFname.getText().toString())) {
            Util.showAlert(CreatePasswordActivity.this, getString(R.string.invalid_fname), getString(R.string.app_name));
        } else if (TextUtils.isEmpty(mLname.getText()) || !Util.validateTextInput1(mLname.getText().toString())) {
            Util.showAlert(CreatePasswordActivity.this, getString(R.string.invalid_lname), getString(R.string.app_name));
        } else if (TextUtils.isEmpty(mGender.getText())) {
            Util.showAlert(CreatePasswordActivity.this, getString(R.string.gender_error), getString(R.string.app_name));
        } else if (TextUtils.isEmpty(((TextView) findViewById(R.id.dob_tv)).getText())) {
            Util.showAlert(CreatePasswordActivity.this, getString(R.string.dob_error), getString(R.string.app_name));
        }
        return true;
    }

    private boolean validatePasswords() {
        mPassword = edtPassword.getText().toString();
        mVerifyPassword = edtVerifyPassword.getText().toString();
        if (TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mVerifyPassword)) {
            Util.showAlert(CreatePasswordActivity.this, getString(R.string.msg_enter_valid_password), getString(R.string.app_name));
            edtPassword.setText("");
            edtVerifyPassword.setText("");
            return false;
        } else if (!mPassword.matches(Constant.PASSWOSRD_REGULAR_EXPRESSION)) {
            Util.showAlert(CreatePasswordActivity.this, getString(R.string.msg_enter_valid_password), getString(R.string.app_name));
            edtPassword.setText("");
            edtVerifyPassword.setText("");
            return false;
        } else if (!mPassword.equalsIgnoreCase(mVerifyPassword)) {
            Util.showAlert(CreatePasswordActivity.this, getString(R.string.msg_enter_password_not_matching), getString(R.string.app_name));
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
            String accessToken = new CommonAPICalls(CreatePasswordActivity.this).getEpicAccessToken();
            Log.v(TAG, "accessToken:" + accessToken);
            return accessToken;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (Util.ifNetworkAvailableShowProgressDialog(CreatePasswordActivity.this, (getString(R.string.creating_mychart)), true)) {
                new CreatePassword().execute(mMRN);
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        QuetionsActivity.backFromCreatePassword = true;
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

    public void showDatePickerDialog(Context context, final TextView tv) {

        DatePickerDialog dDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        mYear = year;

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);


                        birthDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                        String birthDateToshow = new SimpleDateFormat("MM-dd-yyyy").format(calendar.getTime());

                        // birthDate = format.(calendar.getTime());
                        Log.v(TAG, "strDate is::" + birthDateToshow);
                        tv.setText(birthDateToshow);


                    }

                }, mYear, mMonth, mDay);
        dDialog.getDatePicker().setMaxDate(new Date().getTime());
        dDialog.show();

    }

    private void getcurrentDate() {
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void showGenderDialog() {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CreatePasswordActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_list, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Gender");

            dialogView.findViewById(R.id.progress_bar).setVisibility(View.GONE);

            ((RelativeLayout) dialogView.findViewById(R.id.relative_dialog_button)).setVisibility(View.GONE);
            mDialogListView = (ListView) dialogView.findViewById(R.id.list_specialty);
            mDialogListView.setAdapter(mGenderAdapter);

            ((ListView) dialogView.findViewById(R.id.list_specialty)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mGender.setText(mGenderArray[i]);
                    alert.dismiss();
                    mDialogListView = null;

                }
            });

            alert = builder.show();
        }
    }

}
