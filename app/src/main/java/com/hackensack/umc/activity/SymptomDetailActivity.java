package com.hackensack.umc.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.SymptomDetail;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class SymptomDetailActivity extends BaseActivity implements HttpDownloadCompleteListener {

    private WebView mSymptomWebView;

    private ProgressBar mSymptomProgressBar;

    private SymptomDetail mSymptomDetailItem;

    private Button mFindDoctor;

    RelativeLayout mButtonRelativeLayout;

    HashMap<String, ArrayList<String>> mSymptomSpecialty = new HashMap<String, ArrayList<String>>();
    private String mSymtomTitle;
    private String mBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_symptom_detail);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        mSymtomTitle = getIntent().getStringExtra(Constant.SYMPTOM_TITLE);
        mBody = getIntent().getStringExtra(Constant.SYMPTOM_BODY_CLASSIFIED);
        bar.setTitle(mSymtomTitle);

        String url = getSymptomUrl(getIntent().getStringExtra(Constant.SYMPTOM_SELECTED));

        HttpDownloader http = null;
        if (Util.ifNetworkAvailableShowProgressDialog(SymptomDetailActivity.this, getString(R.string.loading_text), true)) {
            http = new HttpDownloader(this, url, Constant.SYMPTOM_ITEM_DATA, this);
            http.startDownloading();
        }


        mSymptomWebView = (WebView) findViewById(R.id.webview_symptom);
        mSymptomWebView.requestFocus(View.FOCUS_DOWN);

        mButtonRelativeLayout = (RelativeLayout) findViewById(R.id.relative_button);
        mFindDoctor = (Button) findViewById(R.id.register_button);

        mFindDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSymptomSpecialtyListActivity();

            }
        });

        WebSettings webSettings = mSymptomWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mSymptomWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {

                    if (url != null && (url.contains("www") || url.startsWith("http:") || url.startsWith("https:"))) {
                        view.loadUrl(url);
                    } else if (url != null && url.startsWith("tel:")) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        startActivity(callIntent);
                    }

                } catch (Exception e) {
                    Log.e("Exception", " : " + e.toString());
                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mButtonRelativeLayout.setVisibility(View.VISIBLE);

            }

        });

        mSymptomProgressBar = (ProgressBar) findViewById(R.id.horizontal_progress);
        //   mSymptomProgressBar.setVisibility(View.VISIBLE);

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("SymptomCheckerSpecialties.csv")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rawData = line.split(",");

                String key = rawData[0];
                ArrayList<String> specialty = new ArrayList<String>();

                for (int count = 1; count < rawData.length; count++) {

                    String item = rawData[count];
                    if (item != null && item.length() > 0) {
                        specialty.add(rawData[count]);
                    }
                }

                mSymptomSpecialty.put(key, specialty);

            }

        } catch (Exception e) {

        }
    }

    public String getSymptomUrl(String itemSelected) {

        Date d = new Date();
        String seconds = "" + (d.getTime() / 1000);
        String verSvc = "json/1.0/en/";
        String item = "topic/" + itemSelected;

        String hash = "";
        String createHash = Constant.SYMPTOM_PUBLIC_KEY + "|" + verSvc + item + "|" + seconds;


        try {
            String macData = createHash;
            Mac mac = Mac.getInstance("HmacSHA256");

            byte[] secretByte = Constant.SYMPTOM_PRIVATE_KEY.getBytes("UTF-8");
            byte[] dataBytes = macData.getBytes("UTF-8");

            SecretKey secret = new SecretKeySpec(secretByte, "HmacSHA256");
            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);

            hash = URLEncoder.encode(new String(Base64.encode(doFinal, Base64.NO_WRAP)));

        } catch (Exception x) {
            Log.e("ContentProvider", "requestUpdate(): " + x.getMessage());
        }

        String url = "https://api.selfcare.info/Services.svc/" + verSvc + item + "?token=" + hash + "&key=" + Constant.SYMPTOM_PUBLIC_KEY + "&timestamp=" + seconds;

        return url;
    }

    public void startSymptomSpecialtyListActivity() {

        Intent intent = new Intent(SymptomDetailActivity.this, SymptomSpecialtyListActivity.class);
        ArrayList<String> item = mSymptomSpecialty.get(mSymtomTitle);
        if (item == null) {
            item = new ArrayList<String>();
            if (mSymptomDetailItem.getPediatric() || mBody != null) {
//                specialtyArray = [NSArray arrayWithObjects:@"Family Medicine", @"Pediatric General", nil];
                item.add("Family Medicine");
                item.add("Pediatric General");
            } else {
//                specialtyArray = [NSArray arrayWithObjects:@"Family Medicine", nil];
                item.add("Family Medicine");
            }
        } else if (item.size() == 0) {
            //Code here for no data
            if (mSymptomDetailItem.getPediatric() || mBody != null) {
//                specialtyArray = [NSArray arrayWithObjects:@"Family Medicine", @"Pediatric General", nil];
                item.add("Family Medicine");
                item.add("Pediatric General");
            } else {
//                specialtyArray = [NSArray arrayWithObjects:@"Family Medicine", nil];
                item.add("Family Medicine");
            }
        } else {
            Collections.sort(item);
        }
        intent.putStringArrayListExtra(Constant.DOCTOR_SPECIALTY, item);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

//        MenuInflater inflater = getMenuInflater();
//        getMenuInflater().inflate(R.menu.menu_doctor_search, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;

            case R.id.action_search:

//                Intent intent = new Intent(SymptomDetailActivity.this, SymptomSpecialtyListActivity.class);
//                intent.putStringArrayListExtra(Constant.DOCTOR_SPECIALTY, mSymptomSpecialty.get(mSymptomDetailItem.getTitle()));
//                startActivity(intent);

//                startSymptomSpecialtyListActivity();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        Object obj = data.getDataObject();

        if (obj != null && !isFinishing()) {

            mSymptomDetailItem = (SymptomDetail) data.getDataObject();

            mSymptomWebView.loadData(mSymptomDetailItem.getAdviceText().toString(), "text/html", null);
            // mSymptomProgressBar.setVisibility(View.GONE)
            stopProgress();


//            Log.e("Symptom Detail Activity", "" + mSymptomSpecialty.get(mSymptomDetailItem.getTitle()));


        } else {

            Util.showNetworkOfflineDialog(SymptomDetailActivity.this, "Error", "Error at server side. Please try again later.");
//            showDialogBox();

        }
    }

//    public void showDialogBox() {
//
//        if (!isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Error");
//            builder.setMessage("Server connectivity problem");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
//                }
//            });
//            builder.show();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isActivityFinished || isUserLogout) {
            finish();
        }
    }
}
