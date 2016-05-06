package com.hackensack.umc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackensack.umc.R;
import com.hackensack.umc.coverage_data.CoverageJsonCreatorNew;
import com.hackensack.umc.http.CommonAPICalls;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.patient_data.ErrorForEpicCreatePatient;
import com.hackensack.umc.patient_data.PatientJsonCreater;
import com.hackensack.umc.response.OuterQuetions;
import com.hackensack.umc.response.Questions;
import com.hackensack.umc.response.SubmitKBAResult;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuetionsActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "QuetionsActivity";
    private String responseSubmitKBA;
    private SubmitKBAResult submitKBAResult;
    private LinearLayout mLinearLayout;
    private OuterQuetions outerQuetions;
    private HashMap<String, Questions> questionAns = new HashMap<>();
    private PatientJsonCreater patientJsonCreater;
    //  private String accessTokenForEpic;
    private ArrayList<Questions> qtnList = new ArrayList<>();
    private String emailId;
    private SharedPreferences sharedPreferences;
    private CoverageJsonCreatorNew coverageJsonCreator;
    private String patientId;
    private AlertDialog alert;
    private int regFor;
    public static boolean backFromCreatePassword = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.qutions_layout);

        mLinearLayout = (LinearLayout) findViewById(R.id.linear1);

        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
        // accessTokenForEpic = sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "");

        getDataFromIntent();


    }

    /**
     * get data from the intent send from previous activity
     */
    private void getDataFromIntent() {
        Intent intent = getIntent();
        //Bundle bundle = getIntent().getBundleExtra(Constant.BUNDLE);EMAIL_ID
        regFor = Util.getRegistrationIsForFromSharePref(QuetionsActivity.this);
        outerQuetions = (OuterQuetions) intent.getSerializableExtra(Constant.QUETIONS_DATA);
        patientJsonCreater = (PatientJsonCreater) intent.getSerializableExtra(Constant.PATIENT_FOR_EPIC_CALL);
        Log.v(TAG, patientJsonCreater.toString());
        emailId = intent.getStringExtra(Constant.EMAIL_ID);
        coverageJsonCreator = (CoverageJsonCreatorNew) intent.getSerializableExtra(Constant.INSURANCE_DATA_TO_SEND);

        Log.v(TAG, outerQuetions.toString());
        setValuesToLayout(outerQuetions);
    }

    /**
     * display questions and answers on the screen
     *
     * @param outerQuetions questions got in Registration details activity after successful data submission
     */
    private void setValuesToLayout(final OuterQuetions outerQuetions) {
        ArrayList<Questions> questions = null;
        ArrayList<String> answers = null;
        try {

            questions = outerQuetions.getQuestions();

            for (int i = 0; i < questions.size(); i++) {

                Questions jb = questions.get(i);

                //String prompt = jb.getString("prompt");
                String prompt = jb.getPrompt();
                TextView title = new TextView(this);
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                title.setText("Q" + (i + 1) + ". " + prompt);
                title.setTextColor(Color.BLACK);
                title.setPadding(10, 10, 0, 20);
                CardView cardView = new CardView(new ContextThemeWrapper(QuetionsActivity.this, R.style.CardView_Light), null, 0);

                //  mLinearLayout.addView(cardView);

                LinearLayout layout2 = new LinearLayout(QuetionsActivity.this);
                layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.VERTICAL);

                layout2.addView(title);


                // create radio button
                final RadioButton[] rb = new RadioButton[5];

                RadioGroup radioGroupAnswer = new RadioGroup(this);
                radioGroupAnswer.setOrientation(RadioGroup.VERTICAL);
//                rg.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                //radioGroupAnswer.setPadding(20, 5, 10, 5);
                float x = Util.convertDpToPixel((float) 20, QuetionsActivity.this);
                radioGroupAnswer.setPadding((int) x, (int) Util.convertDpToPixel((float) 5, QuetionsActivity.this), (int) Util.convertDpToPixel((float) 10, QuetionsActivity.this), (int) Util.convertDpToPixel((float) 5, QuetionsActivity.this));

                answers = jb.getAnswer();
                for (int j = 0; j < answers.size(); j++) {
                    String ans = answers.get(j);

                    rb[j] = new RadioButton(this);

                    StateListDrawable stateListDrawable = new StateListDrawable();
                    stateListDrawable.addState(new int[]{android.R.attr.state_checked}, getResources().getDrawable(R.drawable.radio_btn_active));
                    stateListDrawable.addState(new int[]{android.R.attr.state_focused}, getResources().getDrawable(R.drawable.radio_btn_active));
                    stateListDrawable.addState(new int[]{}, getResources().getDrawable(R.drawable.radio_btn_normal));
                    rb[j].setButtonDrawable(stateListDrawable);
//                    rb[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    rb[j].setTag(jb.getType());
                    rb[j].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    rb[j].setText(ans);
                    if ((Util.getDeviceWidth(this) - 100) <= Constant.SCREEN_MIN_WIDTH && (Util.getDeviceHeight(this) - 100) <= Constant.SCREEN_MIN_HEIGHT
                            || ((Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN))) {
                        rb[j].setPadding(50, 10, 10, 10);
                    } else {
                        rb[j].setPadding(10, 10, 10, 10);
                    }
//                    rb[j].setButtonDrawable(getResources().getDrawable(R.drawable.radio_selection));

                    radioGroupAnswer.addView(rb[j]);

                }

                if (radioGroupAnswer != null) {
                    ViewGroup parent = (ViewGroup) radioGroupAnswer.getParent();
                    if (parent != null) {
                        parent.removeView(radioGroupAnswer);
                    }
                    layout2.addView(radioGroupAnswer);

                }

                View v = new View(this);
                v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 15));
//                v.setBackgroundColor(Color.rgb(211, 211, 211));
                v.setBackgroundColor(Color.TRANSPARENT);

            /*    layout2.addView(cardView);
                mLinearLayout.addView(cardView);*/

                cardView.addView(layout2);
                mLinearLayout.addView(cardView);
                mLinearLayout.addView(v);
                radioGroupAnswer.setOnCheckedChangeListener(this);


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questions, menu);
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
            case R.id.ques_action_done:
                Log.v(TAG, "action_done");
                if (questionAns.size() < 3) {
                    Util.showAlert(this, "Please Answer all the questions", "Alert");
                } else {
                    if (Util.ifNetworkAvailableShowProgressDialog(QuetionsActivity.this, getString(R.string.sending_ans), true)) {
                        new GetResult().execute(outerQuetions);
                    }

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroupAnswer, int checkedId) {
        int selectedId = radioGroupAnswer.getCheckedRadioButtonId();
        RadioButton radioAnswerButton = (RadioButton) findViewById(selectedId);
        questionAns.put((String) radioAnswerButton.getTag(), (new Questions((String) radioAnswerButton.getTag(), radioAnswerButton.getText().toString())));
    }

    /**
     * Asyntask to get result of the submitted answers
     */
    private class GetResult extends AsyncTask<OuterQuetions, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  startProgress(getString(R.string.sending_ans));
        }

        @Override
        protected String doInBackground(OuterQuetions... params) {
            return sendHttpPostForSubmitKBA(params[0]);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            try {
                String result = parseResponseForSubmitKBA(aVoid);
                if (result != null) {
                    if (result.equalsIgnoreCase("PASS")) {
                        //  new GetPatientsId().execute(patientJsonCreater.createPatientJson());
                        showAlertPass(QuetionsActivity.this, getString(R.string.ans_correct), getString(R.string.pass));


                    } else {
                        if (result.equalsIgnoreCase("fail")) {
                            showAlertForfailResult(QuetionsActivity.this, getString(R.string.validation_result), getString(R.string.fail));
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(result)) {
                        Util.showAlert(QuetionsActivity.this, result, "Error!");
                    } else {
                        Util.showAlert(QuetionsActivity.this, "Server Error.Please try again.", "Error!");
                    }
                }
            } catch (Exception e) {

            }
            stopProgress();
        }
    }

    /**
     * Show dialog if user got result as pass
     *
     * @param context -Activity context
     * @param message -message to display
     * @param title   -Dialog title
     */
    private void showAlertPass(Context context, String message, String title) {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_questions_activity, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    alert.dismiss();
                    if (Util.ifNetworkAvailableShowProgressDialog(QuetionsActivity.this, getString(R.string.registration_progress), true)) {
                        new getEpicAccessToken().execute();
                    }

                }
            });

            alert = builder.show();
        }

    }

    /**
     * Show dialog if user got result as Fail
     *
     * @param context -Activity context
     * @param message -message to display
     * @param title   -Dialog title
     */
    public void showAlertForfailResult(Context context, String message, String title) {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    alert.dismiss();
                    finish();

                }
            });

            alert = builder.show();
        }

    }

    /**
     * @param questionAns -Questions with all answer options
     * @return reponse for server after submitting answers of questions
     */
    private String sendHttpPostForSubmitKBA(OuterQuetions questionAns) {

        responseSubmitKBA = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_SUBMIT_KBA, createSubmitAnsJson(questionAns), createTokenSubmitInfoHeaders());
        return responseSubmitKBA;

    }

    /**
     * Create json to send to serve
     *
     * @param request1 Questions with all answer options
     * @return Json object with question and its correct answer
     */
    private JSONObject createSubmitAnsJson(OuterQuetions request1) {

        JSONObject jsonObject = null;
        Gson gson = new Gson();

        qtnList = new ArrayList<Questions>(questionAns.values());

        String qutionJson = gson.toJson(new OuterQuetions(request1.getIdNumber(), qtnList));
        try {
            jsonObject = new JSONObject(qutionJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Json to Send" + jsonObject.toString());

        return jsonObject;

    }

    /**
     * Parse the Pass or Fail result from response of datamotion
     *
     * @param responseSubmitKBA -String Response after sending questions and correct answer to data motion
     * @return -String Exact result of question submission
     */
    private String parseResponseForSubmitKBA(String responseSubmitKBA) {
        try {
            Gson gson = new Gson();
            submitKBAResult = gson.fromJson(responseSubmitKBA, SubmitKBAResult.class);
            Util.setTransactionId(submitKBAResult.getIdNumber(), QuetionsActivity.this);
            //  Log.v(TAG, submitKBAResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (submitKBAResult.getKBAResult() != null) {
            return submitKBAResult.getKBAResult();
        } else {
            try {
                return submitKBAResult.getErrors().get(0);
            } catch (Exception e) {

            }
        }
        return null;

    }

    private List<NameValuePair> createTokenSubmitInfoHeaders() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        String token = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE).getString(ServerConstants.ACCESS_TOKEN_DATA_MOTION, "");
        Log.v(TAG, "****************************************************** Token to send to server::" + token);
        headers.add(new BasicNameValuePair("Authorization", token));
        return headers;
    }


    /**
     * Asytask to get patient id after getting result as "PASS" after answer submission
     */
    private class GetPatientsId extends AsyncTask<JSONObject, Void, HttpResponse> {//get patient Id by creatiig pationet with data.

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HttpResponse doInBackground(JSONObject... params) {

            HttpResponse response = HttpUtils.sendHttpPostForUsingJsonObj_1(ServerConstants.URL_SEND_PATIENT, params[0], createHeadersForPatioentId());


            Log.v(TAG, "Httpresponse: " + String.valueOf(response.getStatusLine().getStatusCode()));
            //response contains patient Id as last parameter of header.
            return response;


        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            super.onPostExecute(response);

            getPationtIdFromResponse(response);
            stopProgress();


        }

        private List<NameValuePair> createHeadersForPatioentId() {
            ArrayList<NameValuePair> headers = new ArrayList<>();
            headers.add(new BasicNameValuePair("Content-Type", "application/json"));
            //  Log.v("Token is::", "Bearer " + accessTokenForEpic);
            headers.add(new BasicNameValuePair("Authorization", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "")));
            return headers;

        }
    }

    /**
     * get Id from response of GetPatientsId
     *
     * @param response- HttpResponse from GetPatientsId asyntask
     */
    private void getPationtIdFromResponse(HttpResponse response) {
        switch (response.getStatusLine().getStatusCode()) {
            case 200://alredy
                //showAlertDialogForExistinguserNew(getString(R.string.msg_user_alredy_registered_new), getString(R.string.alert_title));
                //showAlertDialogForExistinguser(getString(R.string.msg_user_alredy_registered), getString(R.string.alert_title));
/*                Header header = response.getFirstHeader("loacation");
                Log.v(TAG,"headers:"+header.toString());
                new CreateAccount().execute();
                break*/
                QuetionsActivity.backFromCreatePassword = false;
                ;
                //  break;
            case 201://created
                Header header = response.getFirstHeader("Location");
                Log.v(TAG, "headers:" + header.toString());
                String headerStrining = header.toString();
                patientId = getPationId(headerStrining);
                Log.v(TAG, "patientId is::" + patientId);
                //  Log.v(TAG, "coverageJsonCreator is::" + coverageJsonCreator.getInsuranceName());
                coverageJsonCreator.setReference(patientId);
                if (coverageJsonCreator != null) {
                    if (!TextUtils.isEmpty(coverageJsonCreator.getInsuranceName())) {
                        new SendCoverageData().execute(coverageJsonCreator);
                    } else {
                        if (regFor == Constant.REGISTRATION_FOR_DEPENDENT) {
                            Intent intent = new Intent(QuetionsActivity.this, DependentCreatePasswordActivity.class);
                            intent.putExtra(Constant.USER_NAME, emailId);
                            intent.putExtra(Constant.PARENTS_BIRTHDATE, getIntent().getStringExtra(Constant.PARENTS_BIRTHDATE));
                            intent.putExtra(Constant.PARENTS_FIRST_NAME, getIntent().getStringExtra(Constant.PARENTS_FIRST_NAME));
                            intent.putExtra(Constant.PARENTS_LAST_NAME, getIntent().getStringExtra(Constant.PARENTS_LAST_NAME));
                            intent.putExtra(Constant.MRN, patientId);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(QuetionsActivity.this, CreatePasswordActivity.class);
                            intent.putExtra(Constant.FROM_ACTIVTY, Constant.FROM_REGISTRATION_ACTIVITY);
                            intent = sendDataWithParentsIntent(intent);
                            startActivity(intent);
                        }
                    }
                }
               /* Toast.makeText(QuetionsActivity.this,"patientId is::" + patientId,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(QuetionsActivity.this, CreatePasswordActivity.class);
                intent.putExtra(Constant.USER_NAME,emailId);
                intent.putExtra(Constant.MRN,patientId);
                startActivity(intent);*/
                //new CreatePassword().execute(patientId);
                //  Util.showAlert(RegistrationDetailsActivity.this,getString(R.string.user_creates),"All ready exist");
                break;
            case 422:
                String errorFromServer = "";

                try {
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.v(TAG, "422" + responseStr);
                    Gson gson = new Gson();
                    ErrorForEpicCreatePatient error = gson.fromJson(responseStr, ErrorForEpicCreatePatient.class);

                    if (error != null) {
                        errorFromServer = error.getIssue().get(0).getDetails().getText();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Util.showAlert(QuetionsActivity.this, "Server conflict.Please check your data for registration and try again ", getString(R.string.alert_title));
                //  Util.showAlert(QuetionsActivity.this, errorFromServer,getString(R.string.alert_title) );
                break;
            case 500:
                Util.showAlert(QuetionsActivity.this, "Internal server error.Please try again after sometime.", getString(R.string.alert_title));
                break;
            default:
                Util.showAlert(QuetionsActivity.this, "Server error.Please try again after sometime.", getString(R.string.alert_title));
                break;

        }
    }

    /**
     * @param intent -Intent to start Activity
     * @return -Intent after adding extra fields.
     */
    private Intent sendDataWithParentsIntent(Intent intent) {
        intent.putExtra(Constant.USER_NAME, emailId);
        intent.putExtra(Constant.MRN, patientId);
        intent.putExtra(Constant.PARENTS_BIRTHDATE, getIntent().getStringExtra(Constant.PARENTS_BIRTHDATE));
        intent.putExtra(Constant.PARENTS_FIRST_NAME, getIntent().getStringExtra(Constant.PARENTS_FIRST_NAME));
        intent.putExtra(Constant.PARENTS_LAST_NAME, getIntent().getStringExtra(Constant.PARENTS_LAST_NAME));
        intent.putExtra(Constant.PARENTS_GENDER, getIntent().getStringExtra(Constant.PARENTS_GENDER));
        return intent;
    }

    private void showAlertDialogForExistinguser(String message, String title) {

//        new AlertDialog.Builder(QuetionsActivity.this)
//                .setTitle(title)
//                .setMessage(message).setCancelable(false).setCancelable(false)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Util.isRegistrationFlowFinished = true;
//                        Constant.backFromCreatePassword = false;
//                        dialog.dismiss();
//                        finish();
//                    }
//                }).show();

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    alert.dismiss();

                    Util.isRegistrationFlowFinished = true;
                    QuetionsActivity.backFromCreatePassword = false;
                    finish();

                }
            });

            alert = builder.show();
        }

    }

    private void showAlertDialogForExistinguserNew(String message, String title) {

//        new AlertDialog.Builder(QuetionsActivity.this)
//                .setTitle(title)
//                .setMessage(message).setCancelable(false).setCancelable(false)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Util.isRegistrationFlowFinished = true;
//                        Constant.backFromCreatePassword = false;
//                        dialog.dismiss();
//                        finish();
//                    }
//                }).show();

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Util.isRegistrationFlowFinished = false;
                    alert.dismiss();
                    finish();

                }
            });

            alert = builder.show();
        }

    }

    private String getPationId(String headerStrining) {
        String patientId = headerStrining.substring(headerStrining.lastIndexOf("/") + 1, headerStrining.length());
        Log.v(TAG, "patientId::" + patientId);
        Log.v(TAG, "patientId::" + patientId);
        return patientId;
    }


    private class getEpicAccessToken extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   startProgress(getString(R.string.registration_progress));
        }

        @Override
        protected String doInBackground(Void... params) {
            String accessToken = new CommonAPICalls(QuetionsActivity.this).getEpicAccessToken();
            return accessToken;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                if (Util.ifNetworkAvailableShowProgressDialog(QuetionsActivity.this, getString(R.string.registration_progress), true)) {
                    new GetPatientsId().execute(patientJsonCreater.createPatientJson(QuetionsActivity.this));
                }
                // new readPatient().execute(accessToken);//Login call after userCreatyion

            } else {
                Util.showAlert(QuetionsActivity.this, "Server Error.Please try again.", "Error!");
            }
        }
    }

    /**
     * Asyntask to send coverage data of patient
     */
    private class SendCoverageData extends AsyncTask<CoverageJsonCreatorNew, Void, String> {

        @Override
        protected String doInBackground(CoverageJsonCreatorNew... params) {
            Log.v(TAG, "Sending coverage request..");
            // CoverageJsonCreator coverageJsonCreator = new CoverageJsonCreator(insuranceName, dependent, group, subscriberId, reference, subscriberName, subscriberDateOfBirth);
            String coverageResponse = null;
            try {
                coverageResponse = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_SEND_COVERAGE, params[0].createCoverageData(), createHeadersForInsurance());
                Log.v(TAG, "coverageResponse is::" + coverageResponse);
            } catch (Exception e) {
                return null;
            }
            return coverageResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.v(TAG, "Insurance response::" + response);
            if (regFor == Constant.REGISTRATION_FOR_DEPENDENT) {
                Intent intent = new Intent(QuetionsActivity.this, DependentCreatePasswordActivity.class);
                intent.putExtra(Constant.USER_NAME, emailId);
                intent.putExtra(Constant.PARENTS_BIRTHDATE, getIntent().getStringExtra(Constant.PARENTS_BIRTHDATE));
                intent.putExtra(Constant.PARENTS_FIRST_NAME, getIntent().getStringExtra(Constant.PARENTS_FIRST_NAME));
                intent.putExtra(Constant.PARENTS_LAST_NAME, getIntent().getStringExtra(Constant.PARENTS_LAST_NAME));
                intent.putExtra(Constant.MRN, patientId);

                startActivity(intent);
            } else {
                Intent intent = new Intent(QuetionsActivity.this, CreatePasswordActivity.class);
                intent.putExtra(Constant.FROM_ACTIVTY, Constant.FROM_REGISTRATION_ACTIVITY);
                intent = sendDataWithParentsIntent(intent);
                startActivity(intent);
            }


        }
    }

    private List<NameValuePair> createHeadersForInsurance() {

        ArrayList<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        headers.add(new BasicNameValuePair("Authorization", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "")));
        return headers;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isRegistrationFlowFinished || isUserLogout) {
            finish();
        }
        if (backFromCreatePassword) {
            //  showAlertDialogForExistinguser(getString(R.string.msg_user_alredy_registered), getString(R.string.alert_title));
        }
    }

}
