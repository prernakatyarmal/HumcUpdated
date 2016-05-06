package com.hackensack.umc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.Address;
import com.hackensack.umc.datastructure.ConsentDoctorDetails;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.LoginUserData;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class ConsentResultActivity extends BaseActivity implements HttpDownloadCompleteListener {

    private Button mAllowButton, mCancelButton;
    private String mDoctorEmail;
    private ConsentDoctorDetails mConsentDoctorDetail;
    private String mStartDate, mEndDate;
    private TextView mMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consent_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent != null) {

            mConsentDoctorDetail = (ConsentDoctorDetails) intent.getSerializableExtra(Constant.DOCTOR_CONSENT);
            mDoctorEmail = intent.getStringExtra(Constant.DOCTOR_EMAIL_ID);

//            if (Util.ifNetworkAvailableShowProgressDialog(ConsentResultActivity.this, getString(R.string.loading_text), true)) {
//                mDoctorEmail = intent.getStringExtra(Constant.DOCTOR_EMAIL_ID);
//                HttpDownloader http = new HttpDownloader(this, ServerConstants.DOCTOR_EMAIL_URL, Constant.DOCTOR_EMAIL_SEARCH, this);
//                http.startDownloading();
//            }

            mConsentDoctorDetail.getDoctorImage().setDoctorImg(this, ((ImageView) findViewById(R.id.doctor_img)), null);
            ((TextView) findViewById(R.id.name_text)).setText(mConsentDoctorDetail.getDoctorSecondName() + " " + mConsentDoctorDetail.getDoctorFirstName());

            String mDoctorFullAddress = "";

            Address address = mConsentDoctorDetail.getAddress();
            String street1 = address.getStreet1();
            String street2 = address.getStreet2();
            String city = address.getCity();
            String state = address.getState();
            String zipcode = address.getZip();

            if (street1 != null && street1.length() > 0) {
                mDoctorFullAddress = street1;
            }

            if (street2 != null && street2.length() > 0) {
                mDoctorFullAddress = mDoctorFullAddress + ", " + street2;
            }

//                if (city != null && city.length() > 0) {
//                    mDoctorFullAddress = mDoctorFullAddress + "\n" + city;
//                }

            if (state != null && state.length() > 0) {
                mDoctorFullAddress = mDoctorFullAddress + "\n" + state;
            }

            if (zipcode != null && zipcode.length() > 0) {
                mDoctorFullAddress = mDoctorFullAddress + ", " + zipcode;
            }

            ((TextView) findViewById(R.id.address_one_text)).setText(mDoctorFullAddress);
            ((TextView) findViewById(R.id.email_text)).setText(mDoctorEmail);
            TextView textview = (TextView) findViewById(R.id.input_hint_text);
            textview.setText(mConsentDoctorDetail.getDoctorSecondName() + " " + mConsentDoctorDetail.getDoctorFirstName() + " " + getResources().getString(R.string.consent_result_text));
            textview.setVisibility(View.VISIBLE);

        }


        mAllowButton = (Button) findViewById(R.id.allow_button);
        mAllowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.ifNetworkAvailableShowProgressDialog(ConsentResultActivity.this, getString(R.string.loading_text), true)) {
                    HttpDownloader http = new HttpDownloader(ConsentResultActivity.this, ServerConstants.DOCTOR_ALLOW_URL, Constant.DOCTOR_ALLOW, ConsentResultActivity.this);
                    http.startDownloading();
                }

            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm a");
        Date date = new Date();

        long currentTime = date.getTime();
        mStartDate = sdf.format(currentTime);
        ((TextView) findViewById(R.id.from_text_display)).setText(""+dateFormat.format(currentTime));

        currentTime += 86400000;
        mEndDate = sdf.format(currentTime);
        ((TextView) findViewById(R.id.to_text_display)).setText(""+dateFormat.format(currentTime));

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
    protected void onResume() {
        super.onResume();

        if (Util.isActivityFinished || isUserLogout) {
            finish();
        }

    }

//    public String getData() {
//
//        return "email="+mDoctorEmail;
//    }

    public String getJSONData() {

        String json = "{\"resourceType\": \"Contract\", \"text\": { \"status\": \"generated\" }, \"issued\": \"2016-02-19T11:00:00-05:00\", \"applies\": { \"start\": \"START_DATE\", \"end\": \"START_END\" }, \"subject\": [ { \"reference\": \"Patient/PATIENT_ID\", \"display\": \"PATIENT_NAME\" }], \"type\": { \"coding\": [ { \"system\": \"http://loinc.org\", \"code\": \"57016-8\" } ] }, \"subType\": [ { \"coding\": [ { \"system\": \"http://www.<authorizingauthortiy Datamotion or csl or EPIC).org/Consent-subtype-codes\", \"code\": \"Opt-In\", \"display\": \"Default Authorization with exceptions.\" } ] } ], \"actor\": [ { \"entity\": { \"reference\": \"Patient/PATIENT_ID\" } }, { \"entity\": { \"reference\": \"Organization/2.16.840.1.113883.X.X\", \"display\": \"Practitioner/PRACTITIONER_ID\" } } ], \"signer\": [ { \"type\": { \"coding\": [ { \"code\": \"1.2.840.10065.1.12.1.7\", \"system\": \"http://hl7.org/fhir/contractsignertypecodes\", \"display\": \"Consent\", \"definition\": \"the signature of an individual consenting to what is described in a health information document.\" } ] }, \"party\": { \"reference\": \"Patient/PATIENT_ID\" }, \"signature\": \"\" } ], \"term\": [ { \"type\": { \"coding\": [ { \"system\": \"http://hl7.org/fhir/consent-term-type-codes\", \"code\": \"withhold-object-type\" } ] }, \"subType\": { \"coding\": [ { \"system\": \"http://hl7.org/fhir/ValueSet/resource-types\", \"code\": \"Order\" } ] }, \"text\": \"Withhold orders from any provider.\" }, { \"type\": { \"coding\": [ { \"code\": \"withhold-object-type\" } ] }, \"subType\": { \"coding\": [ { \"system\": \"http://hl7.org/fhir/ValueSet/resource-types\", \"code\": \"OrderResponse\" } ] }, \"text\": \"Withhold order results from any provider.\" } ] }";

        try {

            LoginUserData mPatient = new LoginUserData(new JSONObject(Util.getPatientJSON(this)));

            json = json.replaceAll("PRACTITIONER_ID", mConsentDoctorDetail.getDoctorId());
            json = json.replaceAll("PATIENT_ID", Util.getPatientMRNID(this));
            json = json.replaceAll("PATIENT_NAME", mPatient.getFirstName() + " " + mPatient.getLastName());
            json = json.replaceAll("START_DATE", mStartDate);
            json = json.replaceAll("START_END", mEndDate);

            //START_DATE, START_END, PATIENT_ID, PATIENT_NAME, PRACTITIONER_ID

        }catch (Exception e) {

            Log.e("Error", "JSON Exception: "+e.toString());

        }

        return json;
    }

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        if (data != null && !isFinishing()) {

            int httpsServerResponse = data.getResponseCode();
            int requestType = data.getRequestType();
            if(requestType == Constant.DOCTOR_EMAIL_SEARCH) {

                if (httpsServerResponse >= Constant.HTTP_OK && httpsServerResponse < Constant.HTTP_REDIRECT) {

                    mConsentDoctorDetail = (ConsentDoctorDetails) data.getDataObject();

                    mConsentDoctorDetail.getDoctorImage().setDoctorImg(this, ((ImageView) findViewById(R.id.doctor_img)), null);
                    ((TextView) findViewById(R.id.name_text)).setText(mConsentDoctorDetail.getDoctorSecondName() + " " + mConsentDoctorDetail.getDoctorFirstName());

                    String mDoctorFullAddress = "";

                    Address address = mConsentDoctorDetail.getAddress();
                    String street1 = address.getStreet1();
                    String street2 = address.getStreet2();
                    String city = address.getCity();
                    String state = address.getState();
                    String zipcode = address.getZip();

                    if (street1 != null && street1.length() > 0) {
                        mDoctorFullAddress = street1;
                    }

                    if (street2 != null && street2.length() > 0) {
                        mDoctorFullAddress = mDoctorFullAddress + ", " + street2;
                    }

//                if (city != null && city.length() > 0) {
//                    mDoctorFullAddress = mDoctorFullAddress + "\n" + city;
//                }

                    if (state != null && state.length() > 0) {
                        mDoctorFullAddress = mDoctorFullAddress + "\n" + state;
                    }

                    if (zipcode != null && zipcode.length() > 0) {
                        mDoctorFullAddress = mDoctorFullAddress + ", " + zipcode;
                    }

                    ((TextView) findViewById(R.id.address_one_text)).setText(mDoctorFullAddress);
                    ((TextView) findViewById(R.id.email_text)).setText(mDoctorEmail);
                    TextView textview = (TextView) findViewById(R.id.input_hint_text);
                    textview.setText(mConsentDoctorDetail.getDoctorSecondName() + " " + mConsentDoctorDetail.getDoctorFirstName() + " " + getResources().getString(R.string.consent_result_text));
                    textview.setVisibility(View.VISIBLE);

                }else {
                    consentPopup(getResources().getString(R.string.error_str), getResources().getString(R.string.no_doctor_found) + " " + getResources().getString(R.string.valid_email), true);
                }
            }else {

                if(httpsServerResponse >= Constant.HTTP_OK && httpsServerResponse < Constant.HTTP_REDIRECT) {
                    isConsentDone = true;
                    consentPopup(getResources().getString(R.string.success), getResources().getString(R.string.consent_success), true);
                }else {
                    consentPopup(getResources().getString(R.string.error_str), getResources().getString(R.string.no_consent_send), false);
                }

            }

        }
        stopProgress();
    }

    AlertDialog alert;
    private void consentPopup(String title, String description, final boolean flag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ConsentResultActivity.this);

        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
        builder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
        ((TextView) dialogView.findViewById(R.id.text_message)).setText(description);

        TextView btnNo = (TextView) dialogView.findViewById(R.id.button_dialog_cancel);
        btnNo.setText(getString(R.string.button_cancel));
        btnNo.setVisibility(View.GONE);

        Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
        btnOk.setText(getString(R.string.button_ok));
        btnOk.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                alert.dismiss();
                if (flag) {
                    finish();
                }

            }
        });
        alert = builder.show();

    }


}
