package com.hackensack.umc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.SymptomListAdapter;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.SymptomList;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SymptomListActivity extends BaseActivity implements AdapterView.OnItemClickListener, HttpDownloadCompleteListener {

    private ListView listView;
    private List<SymptomList> mSymptomListResult = new ArrayList<SymptomList>();
    private SymptomListAdapter mAdapter;
    private boolean mHttpCallSuccess;

//    private ProgressBar mProgressBar;
    private SearchView mSearchView;
    private TextView mEmptyView;

    private String mBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String bodyPart = "?BodyPart=Foot" ;

        String bodyPart = getIntent().getStringExtra(Constant.SYMPTOM_BODY_PART);
        mBody = getIntent().getStringExtra(Constant.SYMPTOM_BODY_CLASSIFIED);

        String url = getSymptomUrl(bodyPart, mBody);

        Log.e("SYMPTOM URL", "url : " + url);

        if (mBody != null) {
            if (Util.ifNetworkAvailableShowProgressDialog(SymptomListActivity.this, getString(R.string.loading_text), true)) {
                HttpDownloader http = null;
                if (mBody.equals(Constant.MALE_BODY)) {
                    http = new HttpDownloader(this, url, Constant.SYMPTOM_LIST_DATA_MALE, this);
                } else if (mBody.equals(Constant.FEMALE_BODY)) {
                    http = new HttpDownloader(this, url, Constant.SYMPTOM_LIST_DATA_FEMALE, this);
                } else {
                    http = new HttpDownloader(this, url, Constant.SYMPTOM_LIST_DATA_CHILD, this);
                }
                mHttpCallSuccess = http.startDownloading();
            }
        }

        setContentView(R.layout.activity_list);
        mEmptyView = (TextView) findViewById(android.R.id.empty);
        mEmptyView.setGravity(Gravity.CENTER);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(getResources().getString(R.string.complaints));

        ( findViewById(R.id.find_doc_ll)).setVisibility(View.GONE);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setFocusable(false);
        mSearchView.setFocusableInTouchMode(false);

//        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setIconified(false);
        mSearchView.setBackgroundColor(getResources().getColor(R.color.white));
        mSearchView.clearFocus();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchView.setFocusable(false);
                mAdapter.getFilter().filter(newText.toString());
                return true;
            }
        });

        listView = (ListView) findViewById(R.id.directionList);
        listView.setEmptyView(mEmptyView);
        listView.setOnItemClickListener(this);

        mAdapter = new SymptomListAdapter(this, mSymptomListResult);

        if (mAdapter != null)
            listView.setAdapter(mAdapter);

        hideSoftKeyboard(mSearchView);

    }

    public String getSymptomUrl(String bodyPart, String body) {

//        String timeInSecond = (System.currentTimeMillis()/1000l) + "" ;
//
//        String message = Constant.SYMPTOM_PUBLIC_KEY + Constant.SYMPTOM_ADULT_URL + "|" + timeInSecond ;
//        String token = Util.getHASHCode(message);
//
//        return Constant.SYMPTOM_BASE_ADULT_URL + bodyPart + "&token=" + token + "&key=HAK&timestamp=" + timeInSecond ;//+ "&callback=?" ;

        Date d = new Date();
        String seconds = "" + (d.getTime() / 1000);
        String verSvc = "json/1.0/en/";
        String item = "topictable/adult";
        if (body != null && body.equals(Constant.CHILD_BODY)) {
            item = "topictable/peds";
        }
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

        String url = "https://api.selfcare.info/Services.svc/" + verSvc + item + bodyPart + "&token=" + hash + "&key=" + Constant.SYMPTOM_PUBLIC_KEY + "&timestamp=" + seconds;

        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_directions_list, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, SymptomDetailActivity.class);
        String selectedItem = ( mAdapter.getItem(position)).getId();
        String selectedTitle = ( mAdapter.getItem(position)).getTitle();
        intent.putExtra(Constant.SYMPTOM_SELECTED, selectedItem);
        intent.putExtra(Constant.SYMPTOM_TITLE, selectedTitle);

        if(mBody.equals(Constant.CHILD_BODY))
            intent.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.CHILD_BODY);

        startActivity(intent);

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

            case R.id.action_search:
//                    Toast.makeText(DoctorSearchActivity.this, "click on search : ", Toast.LENGTH_LONG).show();

//                startActivity(new Intent(DoctorFilterActivity.this, DoctorResultActivity.class));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        Object obj = data.getDataObject();

        if (obj != null && !isFinishing()) {

            if (((ArrayList<SymptomList>) obj).size() > 0) {

                try {

                    mSymptomListResult.addAll((ArrayList<SymptomList>) obj);
//            ArrayList<SymptomList> mDoctorsResult = (ArrayList<SymptomList>) obj;
//            Log.i("SymptomList", "SymptomList Data -> " + mDoctorsResult.toString());
                    mAdapter.notifyDataSetChanged();

                    Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.down_from_top);
                    animation.setStartOffset(0);
                    mSearchView.startAnimation(animation);
                    mSearchView.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    //Show popup
                    Util.showNetworkOfflineDialog(SymptomListActivity.this, getString(R.string.error_text), getString(R.string.error_description));
                }
            }else {
                Util.showNetworkOfflineDialog(SymptomListActivity.this, getString(R.string.no_matching_title), getString(R.string.no_matching_text));
            }

        } else {
            //Show popup
            Util.showNetworkOfflineDialog(SymptomListActivity.this, getString(R.string.error_text), getString(R.string.error_description));
        }

        mEmptyView.setText(getString(R.string.no_matching_text));
//        mProgressBar.setVisibility(View.GONE);


        stopProgress();
    }


    public void showDialogBox(String title, String message) {

        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isActivityFinished || isUserLogout) {
            finish();
        }
    }

    public void hideSoftKeyboard(View view) {
        if(getCurrentFocus()!= null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        }
    }

}
