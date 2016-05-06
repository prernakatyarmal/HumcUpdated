package com.hackensack.umc.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hackensack.umc.R;
import com.hackensack.umc.util.Constant;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class WebViewActivity extends BaseActivity {

    private WebView mSymptomWebView;

    private ProgressBar mSymptomProgressBar;

    RelativeLayout mButtonRelativeLayout;

    private boolean isAboutRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_symptom_detail);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        isAboutRequested = getIntent().getBooleanExtra(Constant.IS_ABOUT, false);

        mSymptomWebView = (WebView) findViewById(R.id.webview_symptom);
        mSymptomWebView.requestFocus(View.FOCUS_DOWN);

        if(isAboutRequested) {
            bar.setTitle(getString(R.string.title_activity_about));
            mSymptomWebView.loadUrl("file:///android_asset/AboutHackensackUMC.html");
        }else {
            if(getIntent().getBooleanExtra(Constant.IS_FORM,false)){
                bar.setTitle("Detail");
                mSymptomWebView.loadUrl(getIntent().getStringExtra(Constant.HTML_URL));
                mSymptomWebView.getSettings().setLoadWithOverviewMode(true);
                mSymptomWebView.getSettings().setUseWideViewPort(true);
                mSymptomWebView.getSettings().setBuiltInZoomControls(true);
                mSymptomWebView.getSettings().setSupportZoom(true);
                //mSymptomWebView.setInitialScale(100);
            }else{
            bar.setTitle(getString(R.string.title_activity_news));
            mSymptomWebView.loadUrl(getIntent().getStringExtra(Constant.HTML_URL));}
        }


        ( findViewById(R.id.relative_button)).setVisibility(View.GONE);

        WebSettings webSettings = mSymptomWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mSymptomWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {

                    if (url != null && (url.contains("www") || url.startsWith("http:") || url.startsWith("https:"))) {
                        view.loadUrl(url);
                    }else if (url != null && url.startsWith("tel:")) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        startActivity(callIntent);
                    }

                }catch(Exception e) {
                    Log.e("Exception", " : "+e.toString());
                }

                mSymptomProgressBar.setVisibility(View.VISIBLE);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mSymptomProgressBar.setVisibility(View.GONE);
            }

        });

        mSymptomProgressBar = (ProgressBar) findViewById(R.id.horizontal_progress);

        if(isAboutRequested) {
            mSymptomProgressBar.setVisibility(View.GONE);
        }else {
            mSymptomProgressBar.setVisibility(View.VISIBLE);
        }

    }
  /*  private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
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

        if(isUserLogout) {
            finish();
        }
    }
}
