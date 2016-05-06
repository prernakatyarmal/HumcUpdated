package com.hackensack.umc.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackensack.umc.R;
import com.hackensack.umc.coverage_data.CoverageDataNew;
import com.hackensack.umc.datastructure.Address;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.LoginUserData;
import com.hackensack.umc.http.CommonAPICalls;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.patient_data.Entry;
import com.hackensack.umc.patient_data.InsuranceDataNew;
import com.hackensack.umc.patient_data.Telecom;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ViewProfileActivity extends BaseActivity implements HttpDownloadCompleteListener {
    private LoginUserData mPatient;
    private TextView insuranceInfoTv;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
        insuranceInfoTv = (TextView) findViewById(R.id.prof_insurance_tv);
        if (Util.ifNetworkAvailableShowProgressDialog(ViewProfileActivity.this, "Getting Info...", true)) {
            HttpDownloader http = new HttpDownloader(ViewProfileActivity.this, ServerConstants.READ_PATIENT_URL + Util.getPatientMRNID(this) + ServerConstants.READ_PATIENT_URL_PART, Constant.READ_PATIENT_DATA, ViewProfileActivity.this);
            http.startDownloading();
        }

       /* if (Util.isUserLogin(this) && Util.isPatientIdValid(this)) {

            try {
                mPatient = new LoginUserData(new JSONObject(Util.getPatientJSON(this)));
                ((TextView) findViewById(R.id.profile_fname)).setText(mPatient.getFirstName());
                ((TextView) findViewById(R.id.prof_lname)).setText(mPatient.getLastName());
                ((TextView) findViewById(R.id.prof_license)).setText(TextUtils.isEmpty(mPatient.getDrivingLicense()) ? "-" : mPatient.getDrivingLicense());
                DateFormat formatterDate = new SimpleDateFormat("MM-dd-yyyy");

                Date d = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    d = sdf.parse(mPatient.getBirthDate());
                } catch (Exception e) {
                    Log.e("Date", " " + e);
                }

                //Bug is there : Null Pointer : Appointment Date
                ((TextView) findViewById(R.id.prof_date)).setText(formatterDate.format(d.getTime()));
                ((TextView) findViewById(R.id.prof_gender_tv)).setText(mPatient.getGender());
                //Code for getting and displaying phone numbers
                ArrayList<Telecom> telecom = mPatient.getTelephone();
                Log.v("Telecom", telecom.toString());
                String phonestr = null;
                for (int i = 0; i < telecom.size(); i++) {
                    if (( telecom.get(i)).getSystem().equalsIgnoreCase(Telecom.TELECOM_EMAIL)) {
                        ((TextView) findViewById(R.id.prof_email)).setText(( telecom.get(i)).getValue());
                    } else if (( telecom.get(i)).getSystem().equalsIgnoreCase(Telecom.TELECOM_PHONE)) {
                        phonestr = telecom.get(i).getValue();
                        final Editable doctorPhoneNum = new SpannableStringBuilder(phonestr);
                        PhoneNumberUtils.formatNumber(doctorPhoneNum, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                        if (( telecom.get(i)).getUse().equalsIgnoreCase(Telecom.TELECOM_MOBILE_PHONE)) {
                            findViewById(R.id.mobile_ll).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.prof_mob_num)).setText(doctorPhoneNum);

                        } else if (( telecom.get(i)).getUse().equalsIgnoreCase(Telecom.TELECOM_HOME_PHONE)) {
                            findViewById(R.id.home_ll).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.prof_home_num)).setText(doctorPhoneNum);

                        } else if (( telecom.get(i)).getUse().equalsIgnoreCase(Telecom.TELECOM_WORK_PHONE)) {
                            findViewById(R.id.work_ll).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.prof_work_num)).setText(doctorPhoneNum);
                        }
                    }
                }
                //Code to get and display address
                if (mPatient.getAddress() != null && mPatient.getAddress().size() > 0) {
                    Address address = ( mPatient.getAddress()).get(0);
                    ((TextView) findViewById(R.id.prof_addr_tv)).setText(address.getStreet1() + "," +
                            (TextUtils.isEmpty(address.getStreet2()) ? "" : address.getStreet2() + ",") +
                            address.getCity() + "," + address.getState() + "," + address.getZip() + "," + address.getCountry());
                }

            } catch (Exception e) {
                Log.e("isUserLogin", "", e);

            }
            //Code to get and display insurance info

            new GetInsuranceInfo().execute(mPatient.getMRNId());
        }*/

       /* mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_view_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        Object obj = data.getDataObject();

        if (obj != null)
            Log.v("ViewProfileActivity", obj.toString());
        if (data.getRequestType() == Constant.READ_PATIENT_DATA) {
            if (Util.isUserLogin(this) && Util.isPatientIdValid(this)) {

                try {
                    mPatient = new LoginUserData(new JSONObject(Util.getPatientJSON(this)));
                    //Get Insurance Data
                    new GetInsuranceInfo().execute(mPatient.getMRNId());
                    ((TextView) findViewById(R.id.profile_fname)).setText(mPatient.getFirstName());
                    ((TextView) findViewById(R.id.prof_lname)).setText(mPatient.getLastName());
                    ((TextView) findViewById(R.id.prof_license)).setText(TextUtils.isEmpty(mPatient.getDrivingLicense()) ? "-" : mPatient.getDrivingLicense());
                    DateFormat formatterDate = new SimpleDateFormat("MM-dd-yyyy");

                    Date d = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        d = sdf.parse(mPatient.getBirthDate());
                    } catch (Exception e) {
                        Log.e("Date", " " + e);
                    }

                    //Bug is there : Null Pointer : Appointment Date
                    ((TextView) findViewById(R.id.prof_date)).setText(formatterDate.format(d.getTime()));
                    ((TextView) findViewById(R.id.prof_gender_tv)).setText(mPatient.getGender());
                    //Code for getting and displaying phone numbers
                    ArrayList<Telecom> telecom = mPatient.getTelephone();
                    Log.v("Telecom", telecom.toString());
                    String phonestr = null;
                    for (int i = 0; i < telecom.size(); i++) {
                        if ((telecom.get(i)).getSystem().equalsIgnoreCase(Telecom.TELECOM_EMAIL)) {
                            if(TextUtils.isEmpty(((TextView) findViewById(R.id.prof_email)).getText()))
                            ((TextView) findViewById(R.id.prof_email)).setText((telecom.get(i)).getValue());
                        } else if ((telecom.get(i)).getSystem().equalsIgnoreCase(Telecom.TELECOM_PHONE)) {
                            phonestr = telecom.get(i).getValue();
                            final Editable doctorPhoneNum = new SpannableStringBuilder(phonestr);
                            PhoneNumberUtils.formatNumber(doctorPhoneNum, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                            if ((telecom.get(i)).getUse().equalsIgnoreCase(Telecom.TELECOM_MOBILE_PHONE)) {
                                findViewById(R.id.mobile_ll).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.prof_mob_num)).setText(doctorPhoneNum);

                            } else if ((telecom.get(i)).getUse().equalsIgnoreCase(Telecom.TELECOM_HOME_PHONE)) {
                                findViewById(R.id.home_ll).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.prof_home_num)).setText(doctorPhoneNum);

                            } else if ((telecom.get(i)).getUse().equalsIgnoreCase(Telecom.TELECOM_WORK_PHONE)) {
                                findViewById(R.id.work_ll).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.prof_work_num)).setText(doctorPhoneNum);
                            }
                        }
                    }
                    //Code to get and display address
                    if (mPatient.getAddress() != null && mPatient.getAddress().size() > 0) {
                        Address address = (mPatient.getAddress()).get(0);
                        ((TextView) findViewById(R.id.prof_addr_tv)).setText(address.getStreet1() + ", " +
                                /*(TextUtils.isEmpty(address.getStreet2()) ? "" : address.getStreet2() + ", ") +*/
                                address.getCity() + ", " + address.getState() + ", " + address.getZip() + ", " + (TextUtils.isEmpty(address.getCountry()) ? "US" : address.getCountry()));
                    }

                } catch (Exception e) {
                    Log.e("isUserLogin", "", e);

                }
                //Code to get and display insurance info


            }
        }
    }

    private class GetInsuranceInfo extends AsyncTask<String, Void, InsuranceDataNew> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //startProgress(ViewProfileActivity.this, "Getting Info...");
        }

        @Override
        protected InsuranceDataNew doInBackground(String... params) {
            String response = HttpUtils.getHttpGetResponse(ViewProfileActivity.this, ServerConstants.URL_GET_COVERAGE_DETAILS + params[0], getHeadersForInsuranceCall());
            Gson gson = new Gson();
            InsuranceDataNew insuranceData = gson.fromJson(response, InsuranceDataNew.class);
            return insuranceData;
        }

        @Override
        protected void onPostExecute(InsuranceDataNew insuranceData) {
            super.onPostExecute(insuranceData);
            stopProgress();
            if (insuranceData != null && insuranceInfoTv != null) {
                ArrayList<Entry> entry = insuranceData.getEntry();
                if (entry != null && entry.size() > 0) {
                    CoverageDataNew coverageData = entry.get(0).getResource();
                    if (coverageData != null) {
                        String insuranceInfoStr = "";
                        try {
                            insuranceInfoStr = "Provider=" + coverageData.getExtension().get(0).getValue().get(0).getInsuranceName() + ", Group#=" + coverageData.getGroup() + ", Subscriber#=" + coverageData.getSubscriberId().getValue() + ", Member#=" + coverageData.getExtension().get(0).getValue().get(0).getMemberNumber();
                                /*((coverageData.getSubscriber()!=null && coverageData.getSubscriber().getExtension()!=null &&coverageData.getSubscriber().getExtension().size()>=1)?", SubscriberName= "+coverageData.getSubscriber().getExtension().get(0).getValueString():"")+*/
                                /*((coverageData.getSubscriber()!=null && coverageData.getSubscriber().getExtension()!=null &&coverageData.getSubscriber().getExtension().size()>=0)?", Subscriber date of birth= "+coverageData.getSubscriber().getExtension().get(1).getValueString():"");*/
                        } catch (Exception e) {
                            Log.e("", "Exception Occured", e);
                        }
                        insuranceInfoTv.setText(TextUtils.isEmpty(insuranceInfoStr.toString()) ? "-" : insuranceInfoStr.toString());
                    }
                } else {
                    insuranceInfoTv.setText("-");
                }
            } else {
                insuranceInfoTv.setText("-");
            }
        }
    }

    private List<NameValuePair> getHeadersForInsuranceCall() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
//        headers.add(new BasicNameValuePair("Authorization", "Bearer " + new CommonAPICalls(ViewProfileActivity.this).getEpicAccessToken()));
        headers.add(new BasicNameValuePair("Authorization", "Bearer " + Util.getToken(this)));
        return headers;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isUserLogout) {
            finish();
        }
    }

   /* private ProgressDialog mProgressDialog;
    public void startProgress(String message) {
//        mProgressDialog.setTitle("Logging in...");
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void stopProgress() {
        mProgressDialog.dismiss();
    }*/

}
