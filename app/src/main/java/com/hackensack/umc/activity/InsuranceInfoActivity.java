package com.hackensack.umc.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.InsuranceInfo;
import com.hackensack.umc.util.CameraFunctionality;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InsuranceInfoActivity extends BaseActivity {
    public static final String INSURANCE_STR = "insurance";
    private InsuranceInfo mInsuranceInfo;
    private String insuranceName = "INC1234";
    private String memberNumber = "123456";//member number value from app
    private String group = "Grup123";
    private String subscriberId = "Sub1234";
    private String reference = "Patient/123";//MRN no of patient.
    private String subscriberName = "Test";
    private String subscriberDateOfBirth = "1980-05-30";
    private EditText edtPlanProvider;
    private EditText edtGroupNumber, edtSubscriberId, edtSubscriberName,  edtMemberNumber;
    private EditText edtSubscriberDateOfBirth;
    private String mMRNnumber;
    private String mPlanProvider;
    private String mMemberNumber;
    private String mGropupNumber;
    private String mSubscribeId;
    private String mSubscriberName;
    private String mSubscriberBirthdate;
    private int mMonth;
    private int mDay;
    private int mYear;
    private ImageView imgIsuranceCard;
    private LinearLayout insurance_img_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_insurance_info);
        edtPlanProvider = (EditText) findViewById(R.id.plan_prov_edt);
        edtMemberNumber = (EditText) findViewById(R.id.edtMemberNumber);//
        edtGroupNumber = (EditText) findViewById(R.id.grp_no_edt);
        edtSubscriberId = (EditText) findViewById(R.id.subscriberId_edt_edt);
        edtSubscriberName = (EditText) findViewById(R.id.edtSubscriberName);
        edtSubscriberDateOfBirth = (EditText) findViewById(R.id.date_tv);
        imgIsuranceCard= (ImageView) findViewById(R.id.insurance_img);
        insurance_img_ll= (LinearLayout) findViewById(R.id.insurance_img_ll);
        /*edtSubscriberDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(InsuranceInfoActivity.this,edtSubscriberDateOfBirth);
            }
        });*/

        //  mCoverage = (EditText) findViewById(R.id.cov_edt);
        // mPlantype = (EditText) findViewById(R.id.plan_type_edt);


        // edtSubscriberName = (EditText) findViewById(R.id.insuranceName_edt);
        //  edtSubscriberDateOfBirth = (EditText) findViewById(R.id.su);


        Bundle bundle = getIntent().getExtras();
        mMRNnumber = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE).getString(Constant.MRN, "");
        if (bundle != null) {
            mInsuranceInfo = (InsuranceInfo) bundle.getSerializable(INSURANCE_STR);
            if (mInsuranceInfo != null) {
                edtPlanProvider.setText(mInsuranceInfo.getPlanProvider());
                edtMemberNumber.setText(mInsuranceInfo.getMemberNumber());
                edtGroupNumber.setText(mInsuranceInfo.getGrpNumber());
                edtSubscriberId.setText(mInsuranceInfo.getSubscriberId());
                if(!TextUtils.isEmpty(mInsuranceInfo.getSubscriberName())) {
                    edtSubscriberName.setText(mInsuranceInfo.getSubscriberName());
                }
                if(!TextUtils.isEmpty(mInsuranceInfo.getSubscriberDateOfBirth())) {
                    edtSubscriberDateOfBirth.setText(mInsuranceInfo.getSubscriberDateOfBirth());
                }
                if(mInsuranceInfo.getImageUrl()!=null) {
                    insurance_img_ll.setVisibility(View.VISIBLE);
                   try {
                       CameraFunctionality
                               .setPhotoToImageViewWithoutOrientation(mInsuranceInfo.getImageUrl()
                                       , imgIsuranceCard, InsuranceInfoActivity.this);
                   }catch (Exception e){

                   }
                }else{
                    insurance_img_ll.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insurance_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;
            case R.id.ic_action_done:
                if(validteInput()) {
                    //InsuranceInfo(String planProvider, String groupNumber, String memberNumber, String reference, String subscriberDateOfBirth, String subscriberName) {
                    mInsuranceInfo = new InsuranceInfo(edtPlanProvider.getText().toString(), edtGroupNumber.getText().toString(), edtMemberNumber.getText().toString(),
                            edtSubscriberId.getText().toString(), edtSubscriberDateOfBirth.getText().toString(), edtSubscriberName.getText().toString());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(INSURANCE_STR, mInsuranceInfo);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
                return true;

        }

       /* //noinspection SimplifiableIfStatement
        if(id==android.R.id.home){
            finish();
            return true;
        }*/
       /* if (id == R.id.ic_action_done) {
            mInsuranceInfo = new InsuranceInfo(mPlanProvider.getText().toString(), mMemberId.getText().toString(), mGrpName.getText().toString(), mGrpNum.getText().toString(), mCoverage.getText().toString(), mPlantype.getText().toString());
            Intent returnIntent = new Intent();
            returnIntent.putExtra(INSURANCE_STR, mInsuranceInfo);
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private boolean validteInput() {
        mPlanProvider = edtPlanProvider.getText().toString();
        mMemberNumber = edtMemberNumber.getText().toString();//
        mGropupNumber = edtGroupNumber.getText().toString();
        mSubscribeId = edtSubscriberId.getText().toString();
        mSubscriberName = edtSubscriberName.getText().toString();
        mSubscriberBirthdate = edtSubscriberDateOfBirth.getText().toString();
        if (TextUtils.isEmpty(mPlanProvider)) {
            Util.showAlert(InsuranceInfoActivity.this,getString(R.string.msg_enter_plan_provider),getString(R.string.app_name));
            return false;
        }/*else if(TextUtils.isEmpty(mGropupNumber)){
            Util.showAlert(InsuranceInfoActivity.this,getString(R.string.msg_enter_group_number),getString(R.string.app_name));
            return false;
        }
        else if(TextUtils.isEmpty(mSubscribeId)){
            Util.showAlert(InsuranceInfoActivity.this,getString(R.string.msg_enter_subscriber_id),getString(R.string.app_name));
            return false;
        }
        else if(TextUtils.isEmpty(mSubscriberName)){
            Util.showAlert(InsuranceInfoActivity.this,getString(R.string.msg_enter_subscriber_name),getString(R.string.app_name));
            return false;
        }
        else if(TextUtils.isEmpty(mSubscriberBirthdate)){
            Util.showAlert(InsuranceInfoActivity.this,getString(R.string.msg_enter_subscriber_birthdate),getString(R.string.app_name));
            return false;
        }else if(TextUtils.isEmpty(mMemberNumber)){
            Util.showAlert(InsuranceInfoActivity.this,getString(R.string.msg_enter_member_number),getString(R.string.app_name));
            return false;
        }*/


        return true;
    }
    public void showDatePickerDialog(Context context, final TextView tv) {

        DatePickerDialog dDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        edtSubscriberDateOfBirth.setText(monthOfYear + "-" + dayOfMonth + "-" + year);

                        mMonth = monthOfYear;

                        mDay = dayOfMonth;
                        mYear = year;

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);


                        mSubscriberBirthdate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

                        // birthDate = format.(calendar.getTime());



                    }

                }, 2012, 3, 3);
        dDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isUserLogout) {
            finish();
        }
    }

}
