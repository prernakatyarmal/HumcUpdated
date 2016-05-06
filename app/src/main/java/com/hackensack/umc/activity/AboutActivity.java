package com.hackensack.umc.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.hackensack.umc.R;

import java.io.IOException;
import java.io.InputStream;

public class AboutActivity extends BaseActivity {

    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.about);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	        AssetManager assetManager = getAssets();
        	InputStream input;
        	String text = "";

        	    try {
        	        input = assetManager.open("AboutHackensackUMC.html");

        	        int size = input.available();
        	        byte[] buffer = new byte[size];
        	        input.read(buffer);
        	        input.close();

        	        // byte buffer into a string
        	        text = new String(buffer);
        	        
        	        String mime = "text/html; charset=ISO-8859-1";
        	        String encoding = "utf-8";

        	        WebView myWebView = (WebView)this.findViewById(R.id.web);
        	        myWebView.getSettings().setJavaScriptEnabled(true);
        	        myWebView.loadDataWithBaseURL(null, text, mime, encoding, null);


        	    } catch (IOException e) {
        	        // TODO Auto-generated catch block
        	        e.printStackTrace();
        	    }

		//overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

	     }
	  
//	    @Override
//	    public void onBackPressed() {
//	    		finish();
//	    }

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

			case android.R.id.home:
				finish();
				isBackPressed = true;
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

	@Override
	protected void onPause() {
		super.onPause();
		//overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
	}
}

	    