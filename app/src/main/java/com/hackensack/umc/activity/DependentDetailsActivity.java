package com.hackensack.umc.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.SpecialtyListAdapter;
import com.hackensack.umc.coverage_data.CoverageJsonCreatorNew;
import com.hackensack.umc.datastructure.Address;
import com.hackensack.umc.datastructure.DataForAutoRegistration;
import com.hackensack.umc.datastructure.InsuranceInfo;
import com.hackensack.umc.http.CommonAPICalls;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.patient_data.Extension;
import com.hackensack.umc.patient_data.PatientJsonCreater;
import com.hackensack.umc.patient_data.Telecom;
import com.hackensack.umc.response.AccessTokenResponse;
import com.hackensack.umc.response.CardResponse;
import com.hackensack.umc.response.DatamotionTokenCredential;
import com.hackensack.umc.response.OuterQuetions;
import com.hackensack.umc.response.PatientResponse;
import com.hackensack.umc.response.SubmitKBAResult;
import com.hackensack.umc.util.Base64Converter;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DependentDetailsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, HttpDownloadCompleteListener {

    private String[] mGenderArray = {"Male", "Female"};
    private static final int addressCode = 12, insuranceCode = 23;
    private int numberOfCellPhonell = 1;
    private Address mAddress;
    private InsuranceInfo mInsuranceInfo;
    private EditText mFname, mLname, mEmail, mGender;
    private SpecialtyListAdapter mGenderAdapter;
    private AlertDialog alert;
    private ListView mDialogListView;
    private String TAG = "DependentDetailsActivity";
    private SharedPreferences sharedPreferences;
    private String fName;
    private String lastName;
    private String email;
    private String birthDate;
    private String gender;
    private String licennse;
    private String cellNumber;
    private String workPhoneNumber;
    private String homePhoneNumber;
    private TextView txtBirthDate;
    private EditText edtLicense;
    private LinearLayout bDateLayout;
    private String pathSelfie = null;
    private int mMonth;
    private int mDay;
    private int mYear;
    private String pathIdFront = null;
    private String pathIdBack;
    private String pathIcFront;
    private String pathIcBack;
    private LinearLayout layoutMobileNumber;
    private LinearLayout layoutHomeNumber;
    private LinearLayout layoutWorkNumber;
    private EditText edtMobileNumber;
    private EditText edtHomeNumber;
    private EditText edtWorkNumber;
    private TextView btnAddPhone;
    private HttpDownloader httpCalls;
    private int regMode;
    private Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_dependents_details);

        initialisevariables();
        inflateXml();
        //   getIntentData(getIntent());
        getcurrentDate();
    }

    private void inflateXml() {
        mGenderAdapter = new SpecialtyListAdapter(DependentDetailsActivity.this, mGenderArray, true);
        mGender = (EditText) findViewById(R.id.gender_edt);
        mGender.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                if((alert == null || !alert.isShowing()) && count == 0)
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showGenderDialog();

                return false;
            }
        });
        bDateLayout = (LinearLayout) findViewById(R.id.date_ll);
        bDateLayout.setOnClickListener(this);
        findViewById(R.id.addr_rl).setOnClickListener(this);
        findViewById(R.id.insurance_rl).setOnClickListener(this);
        btnAddPhone = (TextView) findViewById(R.id.add_phone);
        btnAddPhone.setOnClickListener(this);


        mFname = (EditText) findViewById(R.id.reg_fname);
        mLname = (EditText) findViewById(R.id.reg_lname);
        mEmail = (EditText) findViewById(R.id.reg_email);
        txtBirthDate = ((TextView) bDateLayout.findViewById(R.id.date_tv));
        edtLicense = ((EditText) findViewById(R.id.reg_license));
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);


        inflatePhoneNumbersLayout();
    }

    private void inflatePhoneNumbersLayout() {
        layoutMobileNumber = (LinearLayout) findViewById(R.id.mobileNoLayout);
        layoutHomeNumber = (LinearLayout) findViewById(R.id.homeNoLayout);
        layoutWorkNumber = (LinearLayout) findViewById(R.id.workNoLayout);

        edtMobileNumber = (EditText) layoutMobileNumber.findViewById(R.id.cell_edt);
        edtMobileNumber.setHint(getString(R.string.optional_str));
        edtHomeNumber = (EditText) layoutHomeNumber.findViewById(R.id.cell_edt);
        edtHomeNumber.setHint(getString(R.string.optional_str));
        edtWorkNumber = (EditText) layoutWorkNumber.findViewById(R.id.cell_edt);
        edtWorkNumber.setHint(getString(R.string.optional_str));

        edtMobileNumber.addTextChangedListener(new PhoneNumberTextWatcher(edtMobileNumber));
        edtHomeNumber.addTextChangedListener(new PhoneNumberTextWatcher(edtHomeNumber));
        edtWorkNumber.addTextChangedListener(new PhoneNumberTextWatcher(edtWorkNumber));


        TextView txtMobileNumber = (TextView) layoutMobileNumber.findViewById(R.id.cell_tv);
        txtMobileNumber.setText("Cell");
        TextView txtHomeNumber = (TextView) layoutHomeNumber.findViewById(R.id.cell_tv);
        txtHomeNumber.setText("Home");
        TextView txtWorkNumber = (TextView) layoutWorkNumber.findViewById(R.id.cell_tv);
        txtWorkNumber.setText("Work");


        layoutMobileNumber.findViewById(R.id.btnRemoveField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showErrorAlertDialog("Cell number is required.");
                edtMobileNumber.setText("");
                numberOfCellPhonell--;
                layoutMobileNumber.setVisibility(View.GONE);
            }
        });
        layoutHomeNumber.findViewById(R.id.btnRemoveField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfCellPhonell--;
                edtHomeNumber.setText("");
                layoutHomeNumber.setVisibility(View.GONE);
                Log.v(TAG, "numberOfCellPhonell::" + String.valueOf(numberOfCellPhonell));

                Log.v(TAG, "numberOfCellPhonell::" + String.valueOf(numberOfCellPhonell));
                btnAddPhone.setVisibility(View.VISIBLE);
            }
        });
        layoutWorkNumber.findViewById(R.id.btnRemoveField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfCellPhonell--;
                edtWorkNumber.setText("");
                layoutWorkNumber.setVisibility(View.GONE);
                Log.v(TAG, "numberOfCellPhonell::" + String.valueOf(numberOfCellPhonell));

                Log.v(TAG, "numberOfCellPhonell::" + String.valueOf(numberOfCellPhonell));
                btnAddPhone.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getcurrentDate() {
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void getData() {

        fName = mFname.getText().toString();
        lastName = mLname.getText().toString();
        email = mEmail.getText().toString();
        gender = mGender.getText().toString();
        // birthDate = txtBirthDate.getText().toString();
        licennse = edtLicense.getText().toString();
        // cellNumber= mPhone.getText().toString();
        getPhoneNumberdata();


        //TableRow t = tableRow;

    }

    private void getPhoneNumberdata() {//not getting phone data.
        cellNumber = edtMobileNumber.getText().toString();
        homePhoneNumber = edtHomeNumber.getText().toString();
        workPhoneNumber = edtWorkNumber.getText().toString();
    }

    private void initialisevariables() {
        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    private boolean validateEmailInput(String inputStr) {
        String errorStr = "";
        /*if (TextUtils.isEmpty(inputStr)) {
            errorStr = "Invalid Email Address.";
        } else {*/
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(inputStr).matches()) {
            //(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
            return true;
        }
        //}
        return false;
    }

    private boolean validatePhoneInput(String inputStr) {
       /* String errorStr = "";
        if (TextUtils.isEmpty(inputStr)) {
            errorStr = "Cell number is required";
        } else {*/
        if ((inputStr.matches("\\d{3}-\\d{7}") || inputStr.matches("\\d{3}-\\d{3}-\\d{4}") || inputStr.matches("\\d{10}"))) {
            return true;
            //errorStr = "Please enter a valid phone number in the xxx-xxx-xxxx format";
        }
        /*}*/
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;
            case R.id.reg_action_done:
                //Here Server call
                //new GetAccessTokenCallDataMotion().execute();
              /*  String err = validateAllFields();
                if (TextUtils.isEmpty(err)) {
                    //  new SubmitInfoCall().execute();
                    //new GetAccessTokenCallDataMotion().execute();
                    if (Util.ifNetworkAvailableShowProgressDialog(DependentDetailsActivity.this, getString(R.string.validating), true)) {
                        httpCalls = new HttpDownloader(DependentDetailsActivity.this, ServerConstants.URL_TOKEN, Constant.GET_DATAMOTION_ACCESS_TOKEN, DependentDetailsActivity.this, getString(R.string.validating));
                        httpCalls.startDownloading();
                    }
                } else {
                    //show error alert
                    // stopProgress();
                    showErrorAlertDialog(err);
                }*/
                String err = validateAllFields();
                if (TextUtils.isEmpty(err)) {
                    Intent intentParentRegistration = new Intent(DependentDetailsActivity.this, ProfileSelfieActivity.class);
                    if (mInsuranceInfo == null) {
                        mInsuranceInfo = new InsuranceInfo();
                    }
                    //with nwe json creator.
                    intentParentRegistration.putExtra(Constant.INSURANCE_DATA_TO_SEND, new CoverageJsonCreatorNew(mInsuranceInfo.getPlanProvider(), mInsuranceInfo.getMemberNumber(), mInsuranceInfo.getGroupNumber(), mInsuranceInfo.getSubscriberId(), mInsuranceInfo.getReference(), mInsuranceInfo.getSubscriberName(), mInsuranceInfo.getSubscriberDateOfBirth()));
                    intentParentRegistration.putExtra(Constant.PATIENT_FOR_EPIC_CALL, createPationToSendForEpicCall());
                    // intent.putExtra("bundle", b);
                    startActivity(intentParentRegistration);
                } else {
                    //show error alert
                    // stopProgress();
                    showErrorAlertDialog(err);
                }


                //  new GetPatientsId().execute(cretepationToSend().createPatientJson());


                //   getData();
                // new getEpicAccessToken().execute();

                return true;

        }


        return super.onOptionsItemSelected(item);
    }

  /*  private String validateAllFields() {
        String error = "";
        if (TextUtils.isEmpty(mFname.getText()) || !TextUtils.isEmpty(mFname.getError())) {
            error = "Invalid First Name";
        } else if (TextUtils.isEmpty(mLname.getText()) || !TextUtils.isEmpty(mLname.getError())) {
            error = "Invalid Last Name";
        } else if (TextUtils.isEmpty(mGender.getText())) {
            error = "Invalid Gender";
        } else if (TextUtils.isEmpty(((TextView) findViewById(R.id.date_tv)).getText())) {
            error = "Please enter valid Date of Birth in the MM-DD-YYYY format";
        } else if (TextUtils.isEmpty(mEmail.getText()) || !TextUtils.isEmpty(validateEmailInput(mEmail.getText().toString()))) {
            error = "Invalid Email Address";
        } else if (TextUtils.isEmpty(((EditText) findViewById(R.id.cell_edt)).getText()) || !TextUtils.isEmpty(validatePhoneInput(((EditText) findViewById(R.id.cell_edt)).getText().toString()))) {
            error = "Please enter phone number in the xxx-xxx-xxxx format.";
        } else if (TextUtils.isEmpty(((TextView) findViewById(R.id.addr_tv)).getText()) || !TextUtils.isEmpty(((TextView) findViewById(R.id.addr_tv)).getError())) {
            error = "Invalid valid Address";
        }
        return error;
    }
*/

    private String validateAllFields() {
        String error = "";
       /* if (TextUtils.isEmpty(mFname.getText())){
            error = "Please enter mandatory information";
        }else*/
        if (TextUtils.isEmpty(mFname.getText()) || !Util.validateTextInput1(mFname.getText().toString())) {
            error = "Invalid First Name";
        }/* else if (TextUtils.isEmpty(mLname.getText())){
            error = "Please enter mandatory information";
        }*/ else if (TextUtils.isEmpty(mLname.getText()) || !Util.validateTextInput1(mLname.getText().toString())) {
            error = "Invalid Last Name";
        } else if (TextUtils.isEmpty(mGender.getText())) {
            error = "Please enter mandatory information";
        } else if (TextUtils.isEmpty(((TextView) findViewById(R.id.date_tv)).getText())) {
            error = "Please enter valid Date of Birth in the MM-DD-YYYY format";//"Please enter mandatory information";
        } /*else if (TextUtils.isEmpty(mEmail.getText())){
            error = "Please enter mandatory information";
        } else if (TextUtils.isEmpty(mEmail.getText()) || !validateEmailInput(mEmail.getText().toString())) {
            error = "Invalid Email Address";
        }*/ else if (!TextUtils.isEmpty(((EditText) findViewById(R.id.cell_edt)).getText())) {
            if (!validatePhoneInput(((EditText) findViewById(R.id.cell_edt)).getText().toString())) {
                error = "Invalid Phone Number";
            }
        }
       /* else if (TextUtils.isEmpty(((TextView) findViewById(R.id.addr_tv)).getText()) || !TextUtils.isEmpty(((TextView) findViewById(R.id.addr_tv)).getError())) {
            error = "Invalid Address";
        }*/
       /* if(regMode== Constant.AUTO) {
            if (TextUtils.isEmpty(((TextView) findViewById(R.id.reg_license)).getText()) || !TextUtils.isEmpty(((TextView) findViewById(R.id.reg_license)).getError())) {
                error = "Invalid License number";
            }
        }*/
        return error;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case addressCode:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    mAddress = (Address) bundle.getSerializable(AddressActivity.ADRESS_STR);
                    ((TextView) findViewById(R.id.addr_tv)).setText(mAddress.toString());
                }
                break;
            case insuranceCode:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    mInsuranceInfo = (InsuranceInfo) bundle.getSerializable(InsuranceInfoActivity.INSURANCE_STR);
                    ((TextView) findViewById(R.id.insurance_tv)).setText(mInsuranceInfo.toString());
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_ll:
                showDatePickerDialog(DependentDetailsActivity.this, ((TextView) findViewById(R.id.date_tv)));
                break;
            case R.id.addr_rl:
                Intent intent = new Intent(DependentDetailsActivity.this, AddressActivity.class);
                if (mAddress != null) {
                    intent.putExtra(AddressActivity.ADRESS_STR, mAddress);
                }
                startActivityForResult(intent, addressCode);
                break;
            case R.id.insurance_rl:
                Intent intent1 = new Intent(DependentDetailsActivity.this, InsuranceInfoActivity.class);
                if (mInsuranceInfo == null) {
                    mInsuranceInfo = new InsuranceInfo();
                }
                mInsuranceInfo.setSubscriberName(mFname.getText().toString()
                        + " " + mLname.getText().toString());
                mInsuranceInfo.setSubscriberDateOfBirth(birthDate);
                intent1.putExtra(InsuranceInfoActivity.INSURANCE_STR, mInsuranceInfo);

                startActivityForResult(intent1, insuranceCode);
                break;
            case R.id.add_phone:
                Log.v(TAG, "numberOfCellPhonell::" + String.valueOf(numberOfCellPhonell));
                if (numberOfCellPhonell == 0) {
                    layoutMobileNumber.setVisibility(View.VISIBLE);
                    numberOfCellPhonell++;

                } else if (numberOfCellPhonell == 1) {
                    layoutWorkNumber.setVisibility(View.VISIBLE);
                    numberOfCellPhonell++;

                    Log.v(TAG, "numberOfCellPhonell::" + String.valueOf(numberOfCellPhonell));
                    if (numberOfCellPhonell == 3) {
                        btnAddPhone.setVisibility(View.GONE);
                    }
                } else if (numberOfCellPhonell == 2) {
                    layoutWorkNumber.setVisibility(View.VISIBLE);
                    layoutHomeNumber.setVisibility(View.VISIBLE);
                    numberOfCellPhonell++;
                    Log.v(TAG, "numberOfCellPhonell::" + String.valueOf(numberOfCellPhonell));
                    if (numberOfCellPhonell == 3) {
                        btnAddPhone.setVisibility(View.GONE);
                    }

                }

                break;
            case R.id.btnProceed:
                Intent intentParentRegistration = new Intent(DependentDetailsActivity.this, RegistrationDetailsActivity.class);
                if (mInsuranceInfo == null) {
                    mInsuranceInfo = new InsuranceInfo();
                }
                //with nwe json creator.
                intentParentRegistration.putExtra(Constant.INSURANCE_DATA_TO_SEND, new CoverageJsonCreatorNew(mInsuranceInfo.getPlanProvider(), mInsuranceInfo.getMemberNumber(), mInsuranceInfo.getGroupNumber(), mInsuranceInfo.getSubscriberId(), mInsuranceInfo.getReference(), mInsuranceInfo.getSubscriberName(), mInsuranceInfo.getSubscriberDateOfBirth()));
                intentParentRegistration.putExtra(Constant.PATIENT_FOR_EPIC_CALL, createPationToSendForEpicCall());
                // intent.putExtra("bundle", b);
                startActivity(intentParentRegistration);
           /* case R.id.btnRemoveField:
                if (numberOfCellPhonell > 2) {
                   *//* TableLayout linearRow = (TableLayout) v*//**//*.getParent()*//**//*.getParent().getParent().getParent();
                    if (((TableRow) v*//**//*.getParent()*//**//*.getParent().getParent()).getTag() != null) {
                        linearRow.removeView(linearRow.getChildAt(Integer.parseInt(((TableRow) v*//**//*.getParent()*//**//*.getParent().getParent()).getTag().toString())));
                    }
                    for (int i = Integer.parseInt(((TableRow) v*//**//*.getParent()*//**//*.getParent().getParent()).getTag().toString()); i != linearRow.getChildCount(); i++) {
                        final View viewRow = linearRow.getChildAt(i);
                        viewRow.setTag(i);

                    }*//*
                    numberOfCellPhonell--;
                    findViewById(R.id.add_phone).setVisibility(View.VISIBLE);
                } else if (numberOfCellPhonell == 1) {
                    //show error Dialog
                    showErrorAlertDialog("Phone field is mandatory");
                }*/
                // break;

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


    private void showGenderDialog() {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(DependentDetailsActivity.this);

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





   /* @Override
    public void onResponseSuccessListner(Object object, int requestCode) {
        switch (requestCode) {

        }

    }*/


    private class GetAccessTokenCallDataMotion extends AsyncTask<Void, Void, String> {
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgress(DependentDetailsActivity.this, getString(R.string.validating));

            Log.v(TAG, "GetAccessTokenCallDataMotion::");
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = HttpUtils.getHttpGetResponse(DependentDetailsActivity.this, ServerConstants.GET_CREDENTIALS_FOR_ACCESS_TOKEN, new CommonAPICalls(DependentDetailsActivity.this).createTokenForDatamotionCall());
            Log.v("DatamotionCallResponse:", response);

            DatamotionTokenCredential datamotionTokenCredential = null;
            Gson gson = new Gson();
            try {
                datamotionTokenCredential = gson.fromJson(response, DatamotionTokenCredential.class);
            } catch (Exception e) {

            }
            if (datamotionTokenCredential != null) {
                return new CommonAPICalls(DependentDetailsActivity.this).sendTokencallForDataMotion(datamotionTokenCredential);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);

            if (token != null) {
                if (Util.isNetworkAvailable(DependentDetailsActivity.this)) {
                    new SubmitInfoCall().execute();
                }
            } else {
                Util.showAlert(DependentDetailsActivity.this, "Server error.Please try again.", "Alert");
                // stopProgress();

            }
            //  dismissProgressDialog();
            // new sendCardImages().execute();


        }
    }


    // //// ///////////////////////getAccessToken////////////////////////
    private class getEpicAccessToken extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String accessToken = HttpUtils.httpPost(DependentDetailsActivity.this,
                    ServerConstants.URL_TOKEN_FOR_EPIC, null,
                    createHeaderEpicToken());
            Log.v("Epic Token is::", accessToken);
            return accessToken;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String accessToken = parseTokenResponse(result);
            sharedPreferences.edit().putString(ServerConstants.ACCESS_TOKEN_EPIC, accessToken).commit();

            // new readPatient().execute(accessToken);//Login call after userCreatyion

        }
    }

    private PatientJsonCreater createPationToSendForEpicCall() {
        getData();

        ArrayList<Telecom> teleComList = new ArrayList<>();
        teleComList.add(new Telecom("phone", cellNumber, "mobile"));//need this format//hardcoded for now
        teleComList.add(new Telecom("email", email, "home"));
        teleComList.add(new Telecom("phone", homePhoneNumber, "home"));
        teleComList.add(new Telecom("phone", workPhoneNumber, "work"));//co.in not allowed only .com allowed


        ArrayList<String> lineList = new ArrayList<>();
        if (mAddress != null) {
            lineList.add(mAddress.getStreet1());
            lineList.add(mAddress.getStreet2());
        }


        ArrayList<com.hackensack.umc.patient_data.Address> addressList = new ArrayList<com.hackensack.umc.patient_data.Address>();

        String url = "http: //hl7.org/fhir/StructureDefinition/us-core-county";
        String valueString = "Orange County";
        ArrayList<Extension> extension = new ArrayList<Extension>();
        extension.add(new Extension(url, valueString));
        if (mAddress != null) {
            addressList.add(new com.hackensack.umc.patient_data.Address("home", lineList, mAddress.getStateAbbreviation(), mAddress.getCity(), mAddress.getZip(), mAddress.getCountry()));
        }
        PatientJsonCreater patientJsonCreater = new PatientJsonCreater(fName, lastName, birthDate, gender, teleComList, addressList,
                pathIdFront, pathIdBack, pathIcFront, pathIcBack, pathSelfie, licennse, Util.getLoactionIdFromSharePref(DependentDetailsActivity.this));
        Log.v(TAG, "birthDate:" + birthDate);
        Log.v(TAG, patientJsonCreater.toString());//check birthDate
        return patientJsonCreater;
    }

    private List<NameValuePair> createHeaderEpicToken() {

        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Authorization",
                "Basic MEtRWmJ2aU5iWXJnbUJteUFIVGhHN2pGdEdlYTBwNjQ6RW1pall1dmRBWHZDd25Rdg===="));
        return headers;

    }

    private String parseTokenResponse(String response) {
        Log.v("response in parse", response);
        /*
         * //// 10-22 21:14:38.670: V/Epic Token is::(455): { 10-22
		 * 21:14:38.670: V/Epic Token is::(455): "access_token":
		 * "xM8IhwAWkbe8ZiPCZt7GcsGKoJvW", 10-22 21:14:38.670: V/Epic Token
		 * is::(455): "expires_in": 3599, 10-22 21:14:38.670: V/Epic Token
		 * is::(455): "token_type": "BearerToken" 10-22 21:14:38.670: V/Epic
		 * Token is::(455): }
		 *
		 * /////
		 */
        Gson gson = new Gson();
        AccessTokenResponse accessTokenResponse = gson.fromJson(response,
                AccessTokenResponse.class);
        return accessTokenResponse.getAccess_token();

    }

    // //////////////////////////////////////////////////

    // ////Read patioent////////

    private class readPatient extends AsyncTask<String, Void, Void> {///Login call

        @Override
        protected Void doInBackground(String... paramArrayOfParams) {
            String response = HttpUtils.getHttpGetResponse(DependentDetailsActivity.this,
                    ServerConstants.URL_READ_PATIEONT,
                    createHeaderForReadPatioent(paramArrayOfParams[0]));
            Log.v("Patient is", response);
            parsePatient(response);
            return null;
        }

    }

    private List<NameValuePair> createHeaderForReadPatioent(String accessToken) {
        List<NameValuePair> heades = new ArrayList<NameValuePair>();
        heades.add(new BasicNameValuePair("Authorization", "Bearer "
                + accessToken));
        return heades;
    }

    public void parsePatient(String response) {
        Gson gson = new Gson();
        PatientResponse rePatientResponse = gson.fromJson(response,
                PatientResponse.class);
        Log.v("Patient is::", rePatientResponse.toString());


    }

    // //////////////////////////////////////////////////////////////////////
    // /////SEND PATIONT////

    private class GetPatientsId extends AsyncTask<JSONObject, Void, HttpResponse> {//get patient Id by creatiig pationet with data.


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HttpResponse doInBackground(JSONObject... params) {
            HttpResponse response = HttpUtils.sendHttpPostForUsingJsonObj_1(ServerConstants.URL_SEND_PATIENT, params[0], createHeadersForPatioentId());


            Log.v(TAG, "Httpresponse: " + String.valueOf(response.getStatusLine().getStatusCode()));
            //response contains patient Id as last parameter of header.
            return response;


        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            super.onPostExecute(response);
            getPationtIdFromResponse(response);

        }

        private List<NameValuePair> createHeadersForPatioentId() {
            ArrayList<NameValuePair> headers = new ArrayList<>();
            headers.add(new BasicNameValuePair("Content-Type", "application/json"));
            Log.v("Token is::", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, ""));
            headers.add(new BasicNameValuePair("Authorization", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "")));
            return headers;

        }
    }

    private void getPationtIdFromResponse(HttpResponse response) {
        switch (response.getStatusLine().getStatusCode()) {
            case 200://alredy
                // Util.showAlert(DependentDetailsActivity.this,getString(R.string.user_creates),"Alert");
                // Header header = response.getFirstHeader("loacation");
                //Log.v(TAG, "headers:" + header.toString());
                //  new CreateAccount().execute();
                break;
            case 201://created
                Header header = response.getFirstHeader("Location");
                Log.v(TAG, "headers:" + header.toString());
                String headerStrining = header.toString();
                String patientId = getPationId(headerStrining);
                sharedPreferences.edit().putString(Constant.MRN, patientId).commit();
                Log.v(TAG, "patientId:" + patientId);
                //   new CreateAccount().execute(patientId);
                //  Util.showAlert(DependentDetailsActivity.this,getString(R.string.user_creates),"All ready exist");
                break;
            case 422:
                //  Util.showAlert(DependentDetailsActivity.this,getString(R.string.user_creates),"Conflict");
                break;
            case 500:
                // Util.showAlert(DependentDetailsActivity.this,getString(R.string.user_creates),"Server Error");
                break;

        }
    }

    private String getPationId(String headerStrining) {
        String patientId = headerStrining.substring(headerStrining.lastIndexOf("/") + 1, headerStrining.length());
        Log.v(TAG, "patientId::" + patientId);
        Log.v(TAG, "patientId::" + patientId);
        return patientId;
    }
    /*private class CreateAccount extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String response = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_CREATE_ACCOUNT, createJsonForCreatePassword(params[0]), createHeadersForCreatePassword());
            Log.v(TAG, "response to createPassword::"+response);
            return null;
        }

    }

    private List<NameValuePair> createHeadersForCreatePassword() {
        ArrayList<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        Log.v("Token is::", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, ""));
        headers.add(new BasicNameValuePair("Authorization", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "")));
        return headers;
    }

    private JSONObject createJsonForCreatePassword(String patientId) {
        Gson gson=new Gson();
        JSONObject jsonForCreatePassword = null;
        String dataForPasswordCreation=gson.toJson(new PatientData(patientId,"HUMC",email,"","",email,"true","test123"));
        //String patientId, String patientIdType, String loginId, String passwordResetQuestion, String passwordResetAnswer,
        // String emailAddress, String receiveEmailNotifications, String password) {
        try {
             jsonForCreatePassword=new JSONObject(dataForPasswordCreation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonForCreatePassword;
    }
*/
    ///////////////////////////create JsonToSend/////
  /*  private JSONObject createCardJson() {
        JSONObject cardJson = null;
        Gson gson = new Gson();

        ArrayList<Documents> documentList = new ArrayList<>();
        documentList.add(new Documents(0, pathDlFront, pathDlBack));
        documentList.add(new Documents(1, pathIcFront, pathIcBack));
        documentList.add(new Documents(0,pathSelfie,pathSelfie));

        DocumentPojo documentPojo = new DocumentPojo(documentList);

        String jsonCard = gson.toJson(documentPojo);
        try {
            cardJson = new JSONObject(jsonCard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("JSON IS::", cardJson.toString());
        return cardJson;

    }



    private class sendCardImages extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v(TAG, "pathDlFront" + pathDlFront);
        }

        @Override
        protected String doInBackground(Void... params) {
            String cardResponse = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_SEND_CARD_IMAGES, createCardJson(), createHeaders());
            ArrayList<CardResponse>cardResponseList=parseCarddata(cardResponse);
            Log.v(TAG, "cardResponse" + cardResponseList);
            return cardResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);


        }

    }
    private ArrayList<CardResponse> parseCarddata(String cardResponse) {
        Gson gson = new Gson();

        Type listType = new TypeToken<List<CardResponse>>(){}.getType();
        List<CardResponse> cardResponses = (List<CardResponse>) gson.fromJson(cardResponse, listType);
        ArrayList<CardResponse>cardResponseList=(ArrayList)cardResponses;

        return cardResponseList;

    }
*/

    ////Get Saved CardResponse Object obtained from server//
    public Object loadClassFile(File f) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            return o;
        } catch (Exception ex) {
            Log.v("Address Book", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private CardResponse getSavedCardResponseObject() {
        CardResponse cardResponse = (CardResponse) loadClassFile(new File(Constant.CARD_RESPONSE_FILE_PATH));
        Log.v(TAG, "cardResponse Saved:" + cardResponse);
        return cardResponse;

    }


    private class SubmitInfoCall extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {


            return sendHttpPostForRegistration();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            boolean isErrorShown = false;
        }
    }

    private void parseResultFromPatientDataVarification(String response) {
        boolean isErrorShown = false;
        OuterQuetions outerQuetions = parseResponsse(response);
        if (outerQuetions != null) {
            //Log.v(TAG,outerQuetions.getQuestions().size()==0||outerQuetions.getQuestions()==null);
            if (outerQuetions.getQuestions() == null || outerQuetions.getQuestions().size() == 0) {
                // if (outerQuetions != null) {
                try {
                    String error = outerQuetions.getErrors().get(0);
                    //    Util.showAlert(DependentDetailsActivity.this, error, "Error!");
                    if (TextUtils.isEmpty(error) || TextUtils.isEmpty(null)) {
                        isErrorShown = true;
                        Util.showAlert(DependentDetailsActivity.this, "Information can not be submitted Please check your data", "Error!");
                    }
                } catch (Exception e) {
                    if (!isErrorShown) {
                        Util.showAlert(DependentDetailsActivity.this, "Information can not be submitted Please check your data", "Error!");
                    }
                }


            } else {
                showAlert(DependentDetailsActivity.this, "Your credentials has been accepted and sent for verification.", "Accepted", outerQuetions);

            }
        }
        stopProgress();
    }

    public void showAlert(Context context, String message, String title, final OuterQuetions outerQuetions) {

//        new android.app.AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        /*Bundle b = new Bundle();
//                        b.putParcelable(Constant.QUETIONS_DATA, outerQuetions);
//                        b.putString(Constant.EMAIL_ID, mEmail.getText().toString());
//                        b.putParcelable(Constant.PATIENT_FOR_EPIC_CALL, cretepationToSendForEpicCall());*/
//
//                        Intent intent = new Intent(DependentDetailsActivity.this, QuetionsActivity.class);
//                        intent.putExtra(Constant.QUETIONS_DATA, outerQuetions);
//                        if (mInsuranceInfo == null) {
//                            mInsuranceInfo = new InsuranceInfo();
//                        }
//                        intent.putExtra(Constant.INSURANCE_DATA_TO_SEND, new CoverageJsonCreator(mInsuranceInfo.getPlanProvider(), mInsuranceInfo.getMemberNumber(), mInsuranceInfo.getGroupNumber(), mInsuranceInfo.getSubscriberId(), mInsuranceInfo.getReference(), mInsuranceInfo.getSubscriberName(), mInsuranceInfo.getSubscriberDateOfBirth()));
//                        intent.putExtra(Constant.EMAIL_ID, mEmail.getText().toString());
//                        intent.putExtra(Constant.PATIENT_FOR_EPIC_CALL, createPationToSendForEpicCall());
//                        // intent.putExtra("bundle", b);
//                        startActivity(intent);
//                    }
//                })
//                .setCancelable(false)
//                .show();


        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();

                    Intent intent = new Intent(DependentDetailsActivity.this, QuetionsActivity.class);
                    intent.putExtra(Constant.QUETIONS_DATA, outerQuetions);
                    if (mInsuranceInfo == null) {
                        mInsuranceInfo = new InsuranceInfo();
                    }
                    //with nwe json creator.
                    intent.putExtra(Constant.INSURANCE_DATA_TO_SEND, new CoverageJsonCreatorNew(mInsuranceInfo.getPlanProvider(), mInsuranceInfo.getMemberNumber(), mInsuranceInfo.getGroupNumber(), mInsuranceInfo.getSubscriberId(), mInsuranceInfo.getReference(), mInsuranceInfo.getSubscriberName(), mInsuranceInfo.getSubscriberDateOfBirth()));
                    intent.putExtra(Constant.EMAIL_ID, mEmail.getText().toString());
                    intent.putExtra(Constant.PATIENT_FOR_EPIC_CALL, createPationToSendForEpicCall());
                    // intent.putExtra("bundle", b);
                    startActivity(intent);

                }
            });

            alert = builder.show();
        }

    }

    private JSONObject createJson() {//dynamic binding
        JSONObject jsonObject = new JSONObject();
        Log.v(TAG, "mAddress.getStreet1(" + mAddress.getStreet1());
        Log.v(TAG, "mAddress" + mAddress.toString());
        Log.v(TAG, "DateForDatamotion(" + "mDay:" + mDay + "mMonth:" + mMonth + "mYear:" + mYear);
        getData();
        try {
            jsonObject.put("Address", mAddress.getStreet1().trim());
            jsonObject.put("City", mAddress.getCity().trim());
            jsonObject.put("DateOfBirth_Day", String.valueOf(mDay).trim());
            jsonObject.put("DateOfBirth_Month", String.valueOf(mMonth + 1).trim());
            jsonObject.put("DateOfBirth_Year", String.valueOf(mYear).trim());
            jsonObject.put("EmailAddress", email.trim());
            jsonObject.put("FirstName", fName.trim());
            jsonObject.put("GCMToken", "XYZ");
            jsonObject.put("KBA", "true");
            jsonObject.put("LastName", lastName.trim());
            jsonObject.put("NotifyUser", "true");
            jsonObject.put("PhoneNumber", cellNumber.trim());
            jsonObject.put("State", mAddress.getStateAbbreviation().trim());
            jsonObject.put("Zip", mAddress.getZip().trim());
            if (pathSelfie != null) {
                jsonObject.put("FaceImage", Base64Converter.createBase64StringFroImage(pathSelfie, DependentDetailsActivity.this));
                jsonObject.put("IdImage", Base64Converter.createBase64StringFroImage(pathIdFront, DependentDetailsActivity.this));
            }

            Log.v(TAG, "jsonObject" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;


    }

    /*  private JSONObject createJson() {//hardcoded
          JSONObject jsonObject = new JSONObject();
          try {
              jsonObject.put("Address", "71 FOX HOLLOW DR");
              jsonObject.put("City", "DALLAS");
              jsonObject.put("DateOfBirth_Day", String.valueOf(mDay));
              jsonObject.put("DateOfBirth_Month", String.valueOf(mMonth));
              jsonObject.put("DateOfBirth_Year", String.valueOf(mYear));
              jsonObject.put("EmailAddress", "gautam_zodape@persistent.co.in");
              jsonObject.put("FirstName", "CHARLES");
              jsonObject.put("GCMToken", "XYZ");
              jsonObject.put("KBA", "true");
              jsonObject.put("LastName", "CHESKIEWICZ");
              jsonObject.put("NotifyUser", "true");
              jsonObject.put("PhoneNumber", "570-371-9646");
              jsonObject.put("State", "PA");
              jsonObject.put("Zip", "18612-8902");
              if (pathSelfie != null) {
                  jsonObject.put("FaceImage", pathSelfie);
                  jsonObject.put("IdImage", pathIdFront);
              }
              Log.v(TAG, "jsonObject:" + jsonObject);

          } catch (JSONException e) {
              e.printStackTrace();
          }
          return jsonObject;


      }*/
    /*private JSONObject createJson() {//hardcoded
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Address", "71 FOX HOLLOW DR");
            jsonObject.put("City", "DALLAS");
            jsonObject.put("DateOfBirth_Day", String.valueOf(mDay));
            jsonObject.put("DateOfBirth_Month", String.valueOf(mMonth));
            jsonObject.put("DateOfBirth_Year", String.valueOf(mYear));
            jsonObject.put("EmailAddress", "gautam_zodape@persistent.co.in");
            jsonObject.put("FirstName", "CHARLES");
            jsonObject.put("GCMToken", "XYZ");
            jsonObject.put("KBA", "true");
            jsonObject.put("LastName", "CHESKIEWICZ");
            jsonObject.put("NotifyUser", "true");
            jsonObject.put("PhoneNumber", "570-371-9646");
            jsonObject.put("State", "PA");
            jsonObject.put("Zip", "18612-8902");
            if (pathSelfie != null) {
                jsonObject.put("FaceImage", pathSelfie);
                jsonObject.put("IdImage", pathIdFront);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;


    }
*/
    public String sendHttpPostForRegistration() {

        String submitResponse = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_SUBMITINFO, createJson(), createTokenSubmitInfoHeaders());

        return submitResponse;
    }

    private OuterQuetions parseResponsse(String submitResponse) {
        String message = null;
        OuterQuetions responseSubmitInfo;
        try {

            Gson gson = new Gson();
            responseSubmitInfo = gson.fromJson(submitResponse, OuterQuetions.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return responseSubmitInfo;
    }

    private List<NameValuePair> createTokenSubmitInfoHeaders() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        String token = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE).getString(ServerConstants.ACCESS_TOKEN_DATA_MOTION, "");
        Log.v(TAG, "***************************************************** Token to send to server::" + token);
        headers.add(new BasicNameValuePair("Authorization", token));
        return headers;
    }

    /*data:{"Address": "71 FOX HOLLOW DR",
            "City": "DALLAS",
            "DateOfBirth_Day": "05",
            "DateOfBirth_Month": "12",
            "DateOfBirth_Year": "1957",
            "EmailAddress": "gautam_zodape@persistent.co.in",
            "FirstName": "CHARLES",
            "GCMToken": "XYZ",
            "KBA": true,
            "LastName": "CHESKIEWICZ",
            "NotifyUser": true,
            "PhoneNumber": "570-371-9646",
            "State": "PA",
            "Zip": "18612-8902"
            }*/
    private void showDilaog(String message) {

//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        new SubmitInfoCall().execute();
//                        dialog.dismiss();
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        };
//
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DependentDetailsActivity.this);
//        builder.setTitle("Alert");
//        builder.setMessage(message + " .Try again.").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder((getSupportActionBar().getThemedContext()));

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Alert");
            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnCancel = (Button) dialogView.findViewById(R.id.button_dialog_cancel);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnOk.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    new SubmitInfoCall().execute();
                    alert.dismiss();

                }
            });

            alert = builder.show();
        }

    }

    private void showErrorAlertDialog(String msg) {

//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DependentDetailsActivity.this);
//        builder.setMessage(
//                msg)
//                .setTitle("Alert")
//                .setCancelable(false)
//                .setPositiveButton(getString(R.string.button_ok),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        });
//
//        builder.create().show();

        Util.showAlert(this, msg, "Alert");

    }

    /* private class SendCoverageData extends AsyncTask<Void,Void,String>{

         @Override
         protected String doInBackground(Void... params) {
             CoverageJsonCreator coverageJsonCreator=new CoverageJsonCreator(insuranceName,dependent,group,subscriberId,reference,subscriberName,subscriberDateOfBirth);
             String coverageResponse=HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_SEND_COVERAGE,coverageJsonCreator.createCoverageData(),createHeadersForCreatePassword());

             return coverageResponse;
         }
     }*/
    private class PhoneNumberTextWatcher implements TextWatcher {


        private EditText edTxt;
        private boolean isDelete;
        private int keyDel;
        private String a;

        public PhoneNumberTextWatcher(EditText edTxtPhone) {
            this.edTxt = edTxtPhone;
        /*edTxt.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    isDelete = true;
                }else {
                    isDelete = false;
                }
                return false;
            }
        });*/
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean flag = true;
            String eachBlock[] = edTxt.getText().toString().split("-");
            for (int i = 0; i < eachBlock.length; i++) {
                if (eachBlock[i].length() > 3) {
                    Log.v("11111111111111111111", "cc" + flag + eachBlock[i].length());
                }
            }
            if (flag) {
                edTxt.setOnKeyListener(new View.OnKeyListener() {

                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            keyDel = 1;
                        return false;
                    }
                });

                if (keyDel == 0) {

                    if (((edTxt.getText().length() + 1) % 4) == 0) {
                        if (edTxt.getText().toString().split("-").length <= 2) {
                            edTxt.setText(edTxt.getText() + "-");
                            edTxt.setSelection(edTxt.getText().length());
                        }
                    }
                    a = edTxt.getText().toString();
                } else {
                    a = edTxt.getText().toString();
                    keyDel = 0;
                }

            } else {
                edTxt.setText(a);
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void afterTextChanged(Editable s) {

/*        if (isDelete) {
            //isDelete = false;
            String str = s.toString();
            edTxt.removeTextChangedListener(this);
            if (str.length() > 0 && str.charAt(str.length()-1)=='x') {
                str = str.substring(0, str.length()-1);
            }
            edTxt.setText(str);
            return;
        }else {
            String val = s.toString();
            String a = "";
            String b = "";
            String c = "";
            if (val != null && val.length() > 0) {
                val = val.replace("-", "");
                if (val.length() >= 3) {
                    a = val.substring(0, 3);
                } else if (val.length() < 3) {
                    a = val.substring(0, val.length());
                }
                if (val.length() >= 6) {
                    b = val.substring(3, 6);
                    c = val.substring(6, val.length());
                } else if (val.length() > 3 && val.length() < 6) {
                    b = val.substring(3, val.length());
                }
                StringBuffer stringBuffer = new StringBuffer();
                if (a != null && a.length() > 0) {
                    stringBuffer.append(a);
                    if (a.length() == 3) {
                       stringBuffer.append("-");
                    }
                }
                if (b != null && b.length() > 0) {
                    stringBuffer.append(b);
                    if (b.length() == 3) {
                        stringBuffer.append("-");
                    }
                }
                if (c != null && c.length() > 0) {
                    stringBuffer.append(c);
                }
                edTxt.removeTextChangedListener(this);
                edTxt.setText(stringBuffer.toString());
                edTxt.setSelection(edTxt.getText().toString().length());
                edTxt.addTextChangedListener(this);
            } else {
                edTxt.removeTextChangedListener(this);
                edTxt.setText("");
                edTxt.addTextChangedListener(this);
            }
        }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isRegistrationFlowFinished || isUserLogout) {
            finish();
        }
    }


    @Override
    public void HttpDownloadCompleted(com.hackensack.umc.datastructure.HttpResponse data) {
        switch (data.getRequestType()) {
            case Constant.GET_DATAMOTION_ACCESS_TOKEN: {

                if ((String) data.getDataObject() != null) {
                    if (Util.ifNetworkAvailableShowProgressDialog(DependentDetailsActivity.this, getString(R.string.validating), true)) {
                        httpCalls = new HttpDownloader(DependentDetailsActivity.this, ServerConstants.URL_SUBMITINFO, Constant.SEND_PATIENT_DATA_FOR_VARIFICATION, DependentDetailsActivity.this, getString(R.string.validating));
                        httpCalls.startDownloading();
                    }
                } else {
                    Util.showAlert(DependentDetailsActivity.this, "Server error.Please try again.", "Alert");
                    stopProgress();

                }

            }
            break;
            case Constant.SEND_PATIENT_DATA_FOR_VARIFICATION:
                Log.v(TAG, "SEND_PATIENT_DATA_FOR_VARIFICATION");
                parseResultFromPatientDataVarification((String) data.getDataObject());
                //stopProgress();
                break;
        }

    }

}
