package com.hackensack.umc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;

import com.hackensack.umc.R;
import com.hackensack.umc.util.Constant;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        CountDownTimer myCountDown = new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                finish();
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, HackensackUMCActivity.class);
                intent.putExtra(Constant.ACTIVITY_FLOW, Constant.SHOW_EMERGENCY_POP_UP);
                startActivity(intent);

            }
        };

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
        }else {
            myCountDown.start();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
