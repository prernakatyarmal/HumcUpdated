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

public class RegistrationDetailsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener/*, View.OnFocusChangeListener*/, HttpDownloadCompleteListener {

    private String[] mGenderArray = {"Male", "Female"};
    private static final int addressCode = 12, insuranceCode = 23;
    private int numberOfCellPhonell = 1;
    private Address mAddress;
    private InsuranceInfo mInsuranceInfo;
    private EditText mFname, mLname, mEmail, mPhone, mGender;
    private TableLayout phoneTable;
    private List<TableRow> phoneFields = new ArrayList<TableRow>();
    private SpecialtyListAdapter mGenderAdapter;
    private AlertDialog alert;
    private ListView mDialogListView;
    private String token;
    private SubmitKBAResult submitKBAResult;
    private String responseSubmitKBA;
    private String TAG = "RegistrationDetailsActivity";
    private SharedPreferences sharedPreferences;

    private TableRow tableRow;

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
    private String insuranceName = "INC1234";
    private String dependent = "123456";//member number value from app
    private String group = "Grup123";
    private String subscriberId = "Sub1234";
    private String reference = "Patient/123";//MRN no of patient.
    private String subscriberName = "Test";
    private String subscriberDateOfBirth = "1980-05-30";
    private String pathIdBack;
    private String pathIcFront;
    private String pathIcBack;
    private LinearLayout layoutMobileNumber;
    private LinearLayout layoutHomeNumber;
    private LinearLayout layoutWorkNumber;
    private EditText edtMobileNumber;
    private EditText edtHomeNumber;
    private EditText edtWorkNumber;
    private ImageView btnRemoveHome;
    private ImageView btnRemoveMobile;
    private ImageView btnRemoveWork;
    private TextView btnAddPhone;
    private HttpDownloader httpCalls;
    private String location = "1110101211";//hardcoded for now..will come from previous activity//gaurav
    private int regMode;
    private PatientJsonCreater patientJsonCreater;
    private CoverageJsonCreatorNew coverageJsonCreator;
    private int registationFor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_registration_details);

        initialisevariables();
        inflateXml();
        getIntentData(getIntent());
        getcurrentDate();

    }

    /**
     * initialise the class variables
     */
    private void initialisevariables() {
        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
    }

    /**
     * inflate all views from xml
     */
    private void inflateXml() {
        mGenderAdapter = new SpecialtyListAdapter(RegistrationDetailsActivity.this, mGenderArray, true);
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
        phoneTable = (TableLayout) findViewById(R.id.phone_tl);
        inflatePhoneNumbersLayout();
    }

    /**
     * inflate view in phone filed for..Cell no,Work no,Mobile no.
     */
    private void inflatePhoneNumbersLayout() {
        layoutMobileNumber = (LinearLayout) findViewById(R.id.mobileNoLayout);
        layoutHomeNumber = (LinearLayout) findViewById(R.id.homeNoLayout);
        layoutWorkNumber = (LinearLayout) findViewById(R.id.workNoLayout);

        edtMobileNumber = (EditText) layoutMobileNumber.findViewById(R.id.cell_edt);
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
                showErrorAlertDialog("Cell number is required.");
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

    /**
     * get data from the intent send from previous activity
     * @param intent
     */
    private void getIntentData(Intent intent) {
        Bundle bundle = intent.getBundleExtra(Constant.BUNDLE);
        regMode = bundle.getInt(Constant.REGISTRATION_MODE, 0);
        registationFor = Util.getRegistrationIsForFromSharePref(RegistrationDetailsActivity.this);
        if (regMode == Constant.AUTO) {
            DataForAutoRegistration dataForAutoRegistration = (DataForAutoRegistration) bundle.getSerializable(Constant.REG_REQUIRED_DATA);
            // Log.v(TAG, dataForAutoRegistration.toString());
            pathSelfie = bundle.getString(Constant.KEY_SLFIE);
            pathIdFront = bundle.getString(Constant.KEY_ID_FRONT);

            try {
                pathIdBack = bundle.getString(Constant.KEY_ID_BACK);
                pathIcFront = bundle.getString(Constant.KEY_INSURANCE_FRONT);
                pathIcBack = bundle.getString(Constant.KEY_INSURANCE_BACK);
            } catch (Exception e) {

            }
            setDataToField(dataForAutoRegistration);
        }
        if (registationFor == Constant.REGISTRATION_FOR_DEPENDENT) {
            //Bundle bundle = getIntent().getBundleExtra(Constant.BUNDLE);
            patientJsonCreater = (PatientJsonCreater) intent.getSerializableExtra(Constant.PATIENT_FOR_EPIC_CALL);
            Log.v(TAG, patientJsonCreater.toString());
            coverageJsonCreator = (CoverageJsonCreatorNew) intent.getSerializableExtra(Constant.INSURANCE_DATA_TO_SEND);
        }


        //proceedAccordingToActivityMode(regMode);
    }

    /**
     * if Scanning card is used to enter the user details,intent will return that data in
     * object of DataForAutoRegistration. Set the data to views
     *
     * @param dataForAutoRegistration
     */
    private void setDataToField(DataForAutoRegistration dataForAutoRegistration) {
        mFname.setText(dataForAutoRegistration.getfName());
        mLname.setText(dataForAutoRegistration.getLastName());

        if (dataForAutoRegistration.getSex().equalsIgnoreCase("F")) {
            mGender.setText("Female");
        }
        if (dataForAutoRegistration.getSex().equalsIgnoreCase("M")) {
            mGender.setText("Male");
        }
        txtBirthDate.setText(dataForAutoRegistration.getDateOfBirth());
        edtLicense.setText(dataForAutoRegistration.getLicennse());

        mAddress = new Address(dataForAutoRegistration.getAddress(), dataForAutoRegistration.getStreet2(), dataForAutoRegistration.getCity(), dataForAutoRegistration.getState(), dataForAutoRegistration.getZip(), dataForAutoRegistration.getCountry());
        mAddress.setStreet1(dataForAutoRegistration.getAddress());
        mAddress.setCountry(dataForAutoRegistration.getCountry());
        //nsuranceInfo(String planProvider, String groupNumber, String memberNumber, String subscriberId, String subscriberDateOfBirth, String subscriberName)
        ((TextView) findViewById(R.id.addr_tv)).setText(mAddress.toString());
        mInsuranceInfo = new InsuranceInfo(dataForAutoRegistration.getPlanProvider(), dataForAutoRegistration.getGrpNumber(), dataForAutoRegistration.getMemberId(),
                "", dataForAutoRegistration.getDateOfBirth(), "");
        mInsuranceInfo.setImageUrl(pathIcFront);
        ((TextView) findViewById(R.id.insurance_tv)).setText(mInsuranceInfo.toString());

        Util.showAlert(RegistrationDetailsActivity.this, "Please check details for correctness and edit if required, e.g. First name, Last name, Address etc.", "Attention!");
    }

    /**
     * get current date to show in birthDate filed.
     */
    private void getcurrentDate() {
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * get the data into class variables from views
     */
    private void getData() {
        fName = mFname.getText().toString();
        lastName = mLname.getText().toString();
        email = mEmail.getText().toString();
        gender = mGender.getText().toString();
        licennse = edtLicense.getText().toString();
        getPhoneNumberdata();
    }

    private void getPhoneNumberdata() {
        cellNumber = edtMobileNumber.getText().toString();
        homePhoneNumber = edtHomeNumber.getText().toString();
        workPhoneNumber = edtWorkNumber.getText().toString();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    private boolean validateEmailInput(String inputStr) {
        String errorStr = "";

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(inputStr).matches()) {
            //(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
            return true;
        }
        return false;
    }

    private boolean validatePhoneInput(String inputStr) {
        if ((inputStr.matches("\\d{3}-\\d{7}") || inputStr.matches("\\d{3}-\\d{3}-\\d{4}") || inputStr.matches("\\d{10}"))) {
            return true;
        }
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
                String err = validateAllFields();
                if (TextUtils.isEmpty(err)) {
                    if (Util.ifNetworkAvailableShowProgressDialog(RegistrationDetailsActivity.this, getString(R.string.validating), true)) {
                        httpCalls = new HttpDownloader(RegistrationDetailsActivity.this, ServerConstants.URL_TOKEN, Constant.GET_DATAMOTION_ACCESS_TOKEN, RegistrationDetailsActivity.this, getString(R.string.validating));
                        httpCalls.startDownloading();
                    }
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
        }*/ else if (TextUtils.isEmpty(mEmail.getText()) || !validateEmailInput(mEmail.getText().toString())) {
            error = "Invalid Email Address";
        } else if (TextUtils.isEmpty(((EditText) findViewById(R.id.cell_edt)).getText()) || !validatePhoneInput(((EditText) findViewById(R.id.cell_edt)).getText().toString())) {
            error = "Invalid Phone Number";
        } else if (TextUtils.isEmpty(((TextView) findViewById(R.id.addr_tv)).getText()) || !TextUtils.isEmpty(((TextView) findViewById(R.id.addr_tv)).getError())) {
            error = "Invalid Address";
        }
        if (regMode == Constant.AUTO) {
            if (TextUtils.isEmpty(((TextView) findViewById(R.id.reg_license)).getText()) || !TextUtils.isEmpty(((TextView) findViewById(R.id.reg_license)).getError())) {
                error = "Invalid License number";
            }
        }
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
                showDatePickerDialog(RegistrationDetailsActivity.this, ((TextView) findViewById(R.id.date_tv)));
                break;
            case R.id.addr_rl:
                Intent intent = new Intent(RegistrationDetailsActivity.this, AddressActivity.class);
                if (mAddress != null) {
                    intent.putExtra(AddressActivity.ADRESS_STR, mAddress);
                }
                startActivityForResult(intent, addressCode);
                break;
            case R.id.insurance_rl:
                Intent intent1 = new Intent(RegistrationDetailsActivity.this, InsuranceInfoActivity.class);
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


                if (numberOfCellPhonell == 1) {
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


                        birthDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());//birth date format to send to server
                        String birthDateToshow = new SimpleDateFormat("MM-dd-yyyy").format(calendar.getTime());//birth date to show on date filed

                        Log.v(TAG, "strDate is::" + birthDateToshow);
                        tv.setText(birthDateToshow);


                    }

                }, mYear, mMonth, mDay);
        dDialog.getDatePicker().setMaxDate(new Date().getTime());
        dDialog.show();

    }

    private void showGenderDialog() {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationDetailsActivity.this);

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

    /**
     * Create Json to send to epic from Questions Activity after successful submission of data to datamotion.
     * Json is created by using data entered by user.
     *
     * @return
     */
    private PatientJsonCreater createPationToSendForEpicCall() {
        getData();

        ArrayList<Telecom> teleComList = new ArrayList<>();
        teleComList.add(new Telecom("phone", cellNumber, "mobile"));//need this format//hardcoded for now
        teleComList.add(new Telecom("email", email, "home"));
        teleComList.add(new Telecom("phone", homePhoneNumber, "home"));
        teleComList.add(new Telecom("phone", workPhoneNumber, "work"));//co.in not allowed only .com allowed


        ArrayList<String> lineList = new ArrayList<>();
        lineList.add(mAddress.getStreet1());
        lineList.add(mAddress.getStreet2());


        ArrayList<com.hackensack.umc.patient_data.Address> addressList = new ArrayList<com.hackensack.umc.patient_data.Address>();

        String url = "http: //hl7.org/fhir/StructureDefinition/us-core-county";
        String valueString = "Orange County";
        ArrayList<Extension> extension = new ArrayList<Extension>();
        extension.add(new Extension(url, valueString));
        addressList.add(new com.hackensack.umc.patient_data.Address("home", lineList, mAddress.getStateAbbreviation(), mAddress.getCity(), mAddress.getZip(), mAddress.getCountry()));
        PatientJsonCreater patientJsonCreater = new PatientJsonCreater(fName, lastName, birthDate, gender, teleComList, addressList,
                pathIdFront, pathIdBack, pathIcFront, pathIcBack, pathSelfie, licennse, Util.getLoactionIdFromSharePref(RegistrationDetailsActivity.this));
        Log.v(TAG, "birthDate:" + birthDate);
        Log.v(TAG, patientJsonCreater.toString());//check birthDate
        return patientJsonCreater;
    }

    /**
     * Parse the response got from
     *
     * @param response: response string from server
     */
    private void parseResultFromPatientDataVarification(String response) {
        boolean isErrorShown = false;
        OuterQuetions outerQuetions = parseResponsse(response);
        if (outerQuetions != null) {
            //Log.v(TAG,outerQuetions.getQuestions().size()==0||outerQuetions.getQuestions()==null);
            if (outerQuetions.getQuestions() == null || outerQuetions.getQuestions().size() == 0) {
                // if (outerQuetions != null) {
                try {
                    String error = outerQuetions.getErrors().get(0);
                    //    Util.showAlert(RegistrationDetailsActivity.this, error, "Error!");
                    if (TextUtils.isEmpty(error) || TextUtils.isEmpty(null)) {
                        isErrorShown = true;
                        Util.showAlert(RegistrationDetailsActivity.this, "Information can not be submitted Please check your data", "Error!");
                    }
                } catch (Exception e) {
                    if (!isErrorShown) {
                        Util.showAlert(RegistrationDetailsActivity.this, "Information can not be submitted Please check your data", "Error!");
                    }
                }


            } else {
                showAlertDataSubmissionSuccessDilaog(RegistrationDetailsActivity.this, "Your credentials has been accepted and sent for verification.", "Accepted", outerQuetions);

            }
        }
        stopProgress();
    }

    public void showAlertDataSubmissionSuccessDilaog(Context context, String message, String title, final OuterQuetions outerQuetions) {

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

                    Intent intent = new Intent(RegistrationDetailsActivity.this, QuetionsActivity.class);
                    intent.putExtra(Constant.QUETIONS_DATA, outerQuetions);
                    if (registationFor == Constant.REGISTRATION_FOR_DEPENDENT) {
                        if (coverageJsonCreator == null) {
                            coverageJsonCreator = new CoverageJsonCreatorNew(mInsuranceInfo.getPlanProvider(), mInsuranceInfo.getMemberNumber(), mInsuranceInfo.getGroupNumber(), mInsuranceInfo.getSubscriberId(), mInsuranceInfo.getReference(), mInsuranceInfo.getSubscriberName(), mInsuranceInfo.getSubscriberDateOfBirth());
                        }

                        if (patientJsonCreater.getAddress().size() == 0) {
                            ArrayList<String> lineList = new ArrayList<>();
                            lineList.add(mAddress.getStreet1());
                            lineList.add(mAddress.getStreet2());
                            ArrayList<com.hackensack.umc.patient_data.Address> addressList = new ArrayList<com.hackensack.umc.patient_data.Address>();
                            addressList.add(new com.hackensack.umc.patient_data.Address("home", lineList, mAddress.getStateAbbreviation(), mAddress.getCity(), mAddress.getZip(), mAddress.getCountry()));
                            patientJsonCreater.setAddress(addressList);
                        }
                        if (TextUtils.isEmpty(patientJsonCreater.getTelecom().get(1).getValue())) {
                            patientJsonCreater.getTelecom().get(1).setValue(mEmail.getText().toString());
                        }
                        if (TextUtils.isEmpty(patientJsonCreater.getTelecom().get(0).getValue())) {
                            patientJsonCreater.getTelecom().get(0).setValue(cellNumber);
                        }
                        intent.putExtra(Constant.REGISTRATION_FOR, Constant.REGISTRATION_FOR_DEPENDENT);
                        intent.putExtra(Constant.INSURANCE_DATA_TO_SEND, coverageJsonCreator);
                        intent.putExtra(Constant.EMAIL_ID, mEmail.getText().toString());
                        intent.putExtra(Constant.PARENTS_BIRTHDATE, birthDate);
                        intent.putExtra(Constant.PARENTS_FIRST_NAME, fName);
                        intent.putExtra(Constant.PARENTS_LAST_NAME, lastName);
                        intent.putExtra(Constant.PATIENT_FOR_EPIC_CALL, patientJsonCreater);
                    } else {
                        if (mInsuranceInfo == null) {
                            mInsuranceInfo = new InsuranceInfo();
                        }
                        //with nwe json creator.
                        intent.putExtra(Constant.INSURANCE_DATA_TO_SEND, new CoverageJsonCreatorNew(mInsuranceInfo.getPlanProvider(), mInsuranceInfo.getMemberNumber(), mInsuranceInfo.getGroupNumber(), mInsuranceInfo.getSubscriberId(), mInsuranceInfo.getReference(), mInsuranceInfo.getSubscriberName(), mInsuranceInfo.getSubscriberDateOfBirth()));
                        intent.putExtra(Constant.PATIENT_FOR_EPIC_CALL, createPationToSendForEpicCall());
                        intent.putExtra(Constant.EMAIL_ID, mEmail.getText().toString());
                        intent.putExtra(Constant.PARENTS_BIRTHDATE, birthDate);
                        intent.putExtra(Constant.PARENTS_FIRST_NAME, fName);
                        intent.putExtra(Constant.PARENTS_LAST_NAME, lastName);
                        intent.putExtra(Constant.PARENTS_GENDER, gender);
                    }
                    // intent.putExtra("bundle", b);
                    startActivity(intent);

                }
            });

            alert = builder.show();
        }

    }

    /**
     * create json from the user entered data to submit to data motion
     *
     * @return json from data entered in fields
     */
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
                jsonObject.put("FaceImage", Base64Converter.createBase64StringFroImage(pathSelfie, RegistrationDetailsActivity.this));
                jsonObject.put("IdImage", Base64Converter.createBase64StringFroImage(pathIdFront, RegistrationDetailsActivity.this));
            }

            Log.v(TAG, "jsonObject" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;


    }

    /**
     * @return response after sending user data to data motion
     */
    public String sendHttpPostForRegistration() {

        String submitResponse = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_SUBMITINFO, createJson(), createTokenSubmitInfoHeaders());

        return submitResponse;
    }

    /**
     * @param submitResponse: string data submission response
     * @return object containing questions list
     */
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

    private void showErrorAlertDialog(String msg) {

        Util.showAlert(this, msg, "Alert");

    }

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
                    if (Util.ifNetworkAvailableShowProgressDialog(RegistrationDetailsActivity.this, getString(R.string.validating), true)) {
                        httpCalls = new HttpDownloader(RegistrationDetailsActivity.this, ServerConstants.URL_SUBMITINFO, Constant.SEND_PATIENT_DATA_FOR_VARIFICATION, RegistrationDetailsActivity.this, getString(R.string.validating));
                        httpCalls.startDownloading();
                    }
                } else {
                    Util.showAlert(RegistrationDetailsActivity.this, "Server error.Please try again.", "Alert");
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
