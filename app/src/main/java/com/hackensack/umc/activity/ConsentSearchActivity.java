package com.hackensack.umc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.ConsentDoctorDetails;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class ConsentSearchActivity extends BaseActivity implements HttpDownloadCompleteListener {

    private EditText mEmailEditText;
    private Button mSubmitButton;
    private AlertDialog mAlert;
    private String mDoctorEmail;
    private ConsentDoctorDetails mConsentDoctorDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consent_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmailEditText = (EditText) findViewById(R.id.edittext_email);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDoctorEmail = mEmailEditText.getText().toString().trim();

                if (mDoctorEmail == null || mDoctorEmail.length() < 1) {
                    Util.showNetworkOfflineDialog(ConsentSearchActivity.this, getString(R.string.error_text), getString(R.string.valid_email));
                    return;
                }

//                Intent intent = new Intent(ConsentSearchActivity.this, ConsentResultActivity.class);
//                intent.putExtra(Constant.DOCTOR_EMAIL_ID, emailId);
//                startActivity(intent);

                if (Util.ifNetworkAvailableShowProgressDialog(ConsentSearchActivity.this, getString(R.string.loading_text), true)) {
                    HttpDownloader http = new HttpDownloader(ConsentSearchActivity.this, ServerConstants.DOCTOR_EMAIL_URL, Constant.DOCTOR_EMAIL_SEARCH, ConsentSearchActivity.this);
                    http.startDownloading();
                }


            }
        });

    }

    public String getData() {

        return "email="+mDoctorEmail;
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

        if (Util.isActivityFinished || isUserLogout || isConsentDone) {
            finish();
        }

    }

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        if (data != null && !isFinishing()) {

            int httpsServerResponse = data.getResponseCode();
            int requestType = data.getRequestType();
            if(requestType == Constant.DOCTOR_EMAIL_SEARCH) {

                if (httpsServerResponse >= Constant.HTTP_OK && httpsServerResponse < Constant.HTTP_REDIRECT) {

                    mConsentDoctorDetail = (ConsentDoctorDetails) data.getDataObject();

                    Intent intent = new Intent(ConsentSearchActivity.this, ConsentResultActivity.class);
                    intent.putExtra(Constant.DOCTOR_EMAIL_ID, mDoctorEmail);
                    intent.putExtra(Constant.DOCTOR_CONSENT, mConsentDoctorDetail);
                    startActivity(intent);

                }else {
                    consentPopup(getResources().getString(R.string.error_str), getResources().getString(R.string.no_doctor_found) + " " + getResources().getString(R.string.valid_email), false);
                }
            }

        }
        stopProgress();

    }

    AlertDialog alert;
    private void consentPopup(String title, String description, final boolean flag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ConsentSearchActivity.this);

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
