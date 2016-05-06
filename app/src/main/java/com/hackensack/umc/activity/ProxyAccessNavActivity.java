package com.hackensack.umc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hackensack.umc.R;
import com.hackensack.umc.util.Constant;

public class ProxyAccessNavActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_proxy_access_nav);
        (findViewById(R.id.goto_form_button)).setOnClickListener(this);
        (findViewById(R.id.add_info_btn)).setOnClickListener(this);
        (findViewById(R.id.done_info_btn)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_proxy_access_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goto_form_button:
                Intent formIntent = new Intent(ProxyAccessNavActivity.this, WebViewActivity.class);
                formIntent.putExtra(Constant.IS_FORM, true);
                formIntent.putExtra(Constant.HTML_URL, Constant.MYCHART_FORM);
                startActivity(formIntent);
                break;
            case R.id.add_info_btn:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mychartsupport@hackensackUMC.org"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Proxy Form Inquiry");
                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));
                break;
            case R.id.done_info_btn:
                ProxyAccessNavActivity.this.finish();
                break;
        }

    }
}
