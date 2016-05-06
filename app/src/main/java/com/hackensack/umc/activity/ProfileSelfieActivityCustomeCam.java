package com.hackensack.umc.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.DataForAutoRegistration;
import com.hackensack.umc.datastructure.DocumentPojo;
import com.hackensack.umc.datastructure.Documents;
import com.hackensack.umc.fragment.ProfileIdInfoFragment;
import com.hackensack.umc.fragment.ProfileInsuranceInfoFragment;
import com.hackensack.umc.http.CommonAPICalls;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.response.CardResponse;
import com.hackensack.umc.response.DatamotionTokenCredential;
import com.hackensack.umc.response.MessageRespponse;
import com.hackensack.umc.util.Base64Converter;
import com.hackensack.umc.util.CameraFunctionality;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileSelfieActivityCustomeCam extends BaseActivity implements View.OnClickListener, ProfileInsuranceInfoFragment.OnFragmentInteractionListener, ProfileIdInfoFragment.OnFragmentInteractionListener {

    private static final String TAG = "ProfileSelfieActivity";
    private ImageView imgSelfie;
    private Button btnProceed;
    private Button btnManul;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri uriDlFront = null;
    private Uri uriDlback = null;
    private Uri uriIcFront = null;
    private Uri uriIcBack = null;
    private Uri uriSelfie = null;
    private String pathDlFront = null;
    private String pathDlBack = null;
    private String pathIcFront = null;
    private String pathIcBack = null;
    private String pathSelfie = null;
    OnAllImagesSelectedListner allImagesSelectedListner;
    private SharedPreferences sharedPreferences;
    private TextView txtSefileSet;
    private View helpView;
    private RelativeLayout helpLl;
    private int right = 0, left = 0, bottom = 0, screenWidth = 0;
    private FrameLayout frameLayout;
    private TextView txtGrayInstructions;
    private LayoutInflater layoutInflater;
    private LinearLayout layoutStepsButtons;
    private ImageView imgStepsLast;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_selfie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frameLayout = new FrameLayout(this);
        // creating LayoutParams
        FrameLayout.LayoutParams frameLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(frameLayout, frameLayoutParam);
        layoutInflater = LayoutInflater.from(this);
        View mainView = layoutInflater.inflate(R.layout.activity_profile_selfie, null, false);

        infalteXml(mainView);
        frameLayout.addView(mainView);


        //setContentView(R.layout.activity_list);


    }

    private void infalteXml(View mainView) {
        imgSelfie = (ImageView) mainView.findViewById(R.id.selfie_img);
        btnProceed = ((Button) mainView.findViewById(R.id.proceed_btn));
        btnProceed.setOnClickListener(this);
        btnManul = ((Button) mainView.findViewById(R.id.manual_btn));
        txtSefileSet = (TextView) mainView.findViewById(R.id.txtSelfieSet);
        txtGrayInstructions = (TextView) mainView.findViewById(R.id.ic_tv);
        layoutStepsButtons = (LinearLayout) mainView.findViewById(R.id.layoutStepsButtons);
        imgStepsLast = (ImageView) mainView.findViewById(R.id.imgStepLast);
        btnManul.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        imgSelfie.setOnClickListener(this);
        initialisevariables();
        fiindCoOrdinates();

    }

    private void fiindCoOrdinates() {
        txtGrayInstructions.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure you call it only once :
                if (bottom == 0) {
                    txtGrayInstructions.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    bottom = txtGrayInstructions.getBottom();
                }
                //showHelpView(layoutInflater);

            }
        });
        imgStepsLast.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure you call it only once :
                if (right == 0) {
                    imgStepsLast.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    right = imgStepsLast.getRight();
                }
                showHelpView(layoutInflater);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed_btn:
                ProfileInsuranceInfoFragment profileInsuranceInfoFragment = new ProfileInsuranceInfoFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmentIcContainer, profileInsuranceInfoFragment, "IsuranceFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.manual_btn:
                Intent intent = new Intent(ProfileSelfieActivityCustomeCam.this, RegistrationDetailsActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constant.REGISTRATION_MODE, Constant.MANUAL);
                intent.putExtra(Constant.BUNDLE, b);
                startActivity(intent);
                break;
            case R.id.selfie_img:
                imageUri= CameraFunctionality.dispatchTakePictureIntent(ProfileSelfieActivityCustomeCam.this, 1, 5);
                break;

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgSelfie, ProfileSelfieActivityCustomeCam.this);
                }
                break;
            case Constant.CROP_IMAGE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            imageUri = data.getData();
                            CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgSelfie, ProfileSelfieActivityCustomeCam.this);
                            getImageUris(imageUri, 5);
                            txtSefileSet.setText(R.string.done);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onInsuranceFragmentInteraction(Uri uri, int selectedImageView) {
        getImageUris(uri, selectedImageView);

    }

    @Override
    public void onIdFragmentInteraction(Uri uri, int selectedImageView) {
        getImageUris(uri, selectedImageView);
    }

    @Override
    public void onProceedButtonClicked() {
        if (checkValidationsBeforeCall()) {
            if (Util.ifNetworkAvailableShowProgressDialog(ProfileSelfieActivityCustomeCam.this, getString(R.string.scanning_images), true)) {

                new GetAccessTokenCallDataMotion().execute();
            }
        }


    }

    private void getImageUris(Uri imageUri, int selectedImageView) {
        switch (selectedImageView) {
            case 1:
                Log.v(TAG,"getImageUris_1:"+imageUri);
                pathDlFront = Base64Converter.createBase64StringFroImage(imageUri.toString(), ProfileSelfieActivityCustomeCam.this);
                uriDlFront = imageUri;
                Log.v(TAG,"getImageUri_2:"+imageUri);
                //allImagesSelectedListner.setUris(uriDlFront,pathDlFront);
                //CameraFunctionality.storeImagesInFolder(uriDlFront.toString(), ProfileSelfieActivity.this);
                //  PhotoList.DlFront=pathDlFront;
                Log.v("uriDlFront", uriDlFront.toString());
                break;
            case 2:
                pathDlBack = Base64Converter.createBase64StringFroImage(imageUri.toString(), ProfileSelfieActivityCustomeCam.this);
                uriDlback = imageUri;
                break;
            case 3:
                pathIcFront = Base64Converter.createBase64StringFroImage(imageUri.toString(), ProfileSelfieActivityCustomeCam.this);
                uriIcFront = imageUri;
                break;
            case 4:
                pathIcBack = Base64Converter.createBase64StringFroImage(imageUri.toString(), ProfileSelfieActivityCustomeCam.this);
                uriIcBack = imageUri;
                break;
            case 5:
                pathSelfie = Base64Converter.createBase64StringFroImage(imageUri.toString(), ProfileSelfieActivityCustomeCam.this);
                uriSelfie = imageUri;
                break;
        }


    }

    public interface OnAllImagesSelectedListner {
        public void setUris(Uri uri, String base64Image);
    }


    private class GetAccessTokenCallDataMotion extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v(TAG, "GetAccessTokenCallDataMotion::");
            //  startProgress(getString(R.string.scanning_images));
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = HttpUtils.getHttpGetResponse(ProfileSelfieActivityCustomeCam.this, ServerConstants.GET_CREDENTIALS_FOR_ACCESS_TOKEN, new CommonAPICalls(ProfileSelfieActivityCustomeCam.this).createTokenForDatamotionCall());
            Log.v("DatamotionCallResponse:", response);

            DatamotionTokenCredential datamotionTokenCredential = null;
            Gson gson = new Gson();
            try {
                datamotionTokenCredential = gson.fromJson(response, DatamotionTokenCredential.class);
            } catch (Exception e) {

            }
            if(datamotionTokenCredential!=null){
                return new CommonAPICalls(ProfileSelfieActivityCustomeCam.this).sendTokencallForDataMotion(datamotionTokenCredential);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            //  dismissProgressDialog();
            if (aVoid != null) {
                //if (checkValidationsBeforeCall()) {
                if (Util.ifNetworkAvailableShowProgressDialog(ProfileSelfieActivityCustomeCam.this, (getString(R.string.scanning_images)), true)) {
                    new sendCardImages().execute();
                    //}

                }
            } else {
                stopProgress();
                Util.showAlert(ProfileSelfieActivityCustomeCam.this, "Server error.Please try again.", "Alert");
            }


        }

    }

    private boolean checkValidationsBeforeCall() {
        if (pathIcFront == null && pathIcBack == null && pathDlFront == null && pathDlBack == null && pathSelfie == null) {
            Util.showAlert(ProfileSelfieActivityCustomeCam.this, "Please attach at least Id proof to proceed.", "Alert");
            return false;
        } else if ((pathDlFront != null && pathDlBack == null) || (pathDlBack != null && pathDlFront == null)) {

            Util.showAlert(ProfileSelfieActivityCustomeCam.this, "Please attach Id proof for front and back.", "Alert");
            return false;

        } else if ((pathIcFront != null && pathIcBack == null) || (pathIcBack != null && pathIcFront == null)) {
            Util.showAlert(ProfileSelfieActivityCustomeCam.this, "Please attach insurance proof for front and back.", "Alert");
            return false;
        } else if (pathSelfie != null) {
            if ((pathDlFront == null || pathDlBack == null)) {

                Util.showAlert(ProfileSelfieActivityCustomeCam.this, "Please attach Id proof for front and back.", "Alert");
                return false;
            }
        }
        return true;
    }

    private class sendCardImages extends AsyncTask<Void, Void, DataForAutoRegistration> {
        MessageRespponse messageRespponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected DataForAutoRegistration doInBackground(Void... params) {
            String cardResponse = HttpUtils.sendHttpPostForUsingJsonObj(ServerConstants.URL_SEND_CARD_IMAGES, createCardJson(), createHeaders());
            ArrayList<CardResponse> cardResponseList = parseCarddata(cardResponse);
            if (cardResponseList != null) {

                DataForAutoRegistration dataForAutoRegistration = getRequiredFileds(cardResponseList);
                Log.v(TAG, "dataForAutoRegistration" + dataForAutoRegistration);
                return dataForAutoRegistration;
            } else {
                try {
                    messageRespponse = new Gson().fromJson(cardResponse, MessageRespponse.class);
                } catch (Exception e) {

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(DataForAutoRegistration response) {
            super.onPostExecute(response);
            stopProgress();
            if (response != null) {
                startRegistrationActivity(response);
            } else {
                if (messageRespponse != null) {
                    showDilaog(messageRespponse.getMessage());
                }
            }


        }

    }

    private void showDilaog(String message) {
        Util.showAlert(ProfileSelfieActivityCustomeCam.this, getString(R.string.scan_fail_error) + "\n" + message, "Alert");
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        if (Util.ifNetworkAvailableShowProgressDialog(ProfileSelfieActivity.this, getString(R.string.loading_text), true)) {
//                            new sendCardImages().execute();
//                        }
//
//                        dialog.dismiss();
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        };

//        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSelfieActivity.this);
//        builder.setTitle("Alert");
//        builder.setMessage(message + " .Try again.").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

//        if (!isFinishing()) {

         /*   AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getString(R.string.scan_fail_error));
            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnOk.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    if (Util.ifNetworkAvailableShowProgressDialog(ProfileSelfieActivity.this, getString(R.string.loading_text), true)) {
                        new sendCardImages().execute();
                    }
                }
            });

            Button btnCancel = (Button) dialogView.findViewById(R.id.button_dialog_cancel);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            alert = builder.show();
        }
*/
    }

    private AlertDialog alert;

    private JSONObject createCardJson() {
        JSONObject cardJson = null;
        Gson gson = new Gson();

        ArrayList<Documents> documentList = new ArrayList<>();
        if (pathDlFront != null && pathDlBack != null) {
            documentList.add(new Documents(0, pathDlFront, pathDlBack));
        }
        if (pathIcFront != null && pathIcBack != null) {
            documentList.add(new Documents(1, pathIcFront, pathIcBack));
        }

        //documentList.add(new Documents(0, pathSelfie, pathSelfie));

        DocumentPojo documentPojo = new DocumentPojo(documentList);

        String jsonCard = gson.toJson(documentPojo);
        try {
            cardJson = new JSONObject(jsonCard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("JSON IS::", cardJson.toString());
        return cardJson;

    }

    private List<NameValuePair> createHeaders() {
        ArrayList<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        Log.v("Token is::", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_DATA_MOTION, ""));
        headers.add(new BasicNameValuePair("Authorization", getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE).getString(ServerConstants.ACCESS_TOKEN_DATA_MOTION, "")));
        return headers;

    }

    private DataForAutoRegistration getRequiredFileds(ArrayList<CardResponse> cardResponseList) {
        DataForAutoRegistration dataForAutoRegistration = new DataForAutoRegistration();

        /*private String fName;
        private String lastName;
        private String email;//not in response
        private String birthDate;
        private String gender;
        private String licennse;
        private String cellNumber;//no
        private String workPhoneNumber;//no
        private String homePhoneNumber;//no
        private String planProvider;
        private String memberId;
        private String grpName;
        private String grpNumber;
        private String coverage;
        private String planType;
        private String street1;*/
        if (cardResponseList != null) {
            for (int i = 0; i < cardResponseList.size(); i++) {
                String firstName = cardResponseList.get(i).getFirstName();
                String lastName = cardResponseList.get(i).getLastName();
                String birthDate = cardResponseList.get(i).getDateOfBirth();
                String licennse = cardResponseList.get(i).getLicense();
                String planProvider = cardResponseList.get(i).getPlanProvider();
                String gender = cardResponseList.get(i).getSex();
                String memberId = cardResponseList.get(i).getMemberID();
                String grpName = cardResponseList.get(i).getGroupName();
                String grpNumber = cardResponseList.get(i).getGroupNumber();
                String coverage = cardResponseList.get(i).getCoverage();
                String planType = cardResponseList.get(i).getPlanType();
                String city = cardResponseList.get(i).getCity();
                String state = cardResponseList.get(i).getState();


                if (checkValidationForField(firstName)) {
                    dataForAutoRegistration.setfName(firstName);
                }
                if (checkValidationForField(lastName)) {
                    dataForAutoRegistration.setLastName(lastName);

                }
                if (checkValidationForField(birthDate)) {
                    dataForAutoRegistration.setBirthDate(birthDate);
                }
                if (checkValidationForField(licennse)) {
                    dataForAutoRegistration.setLicennse(licennse);

                }
                if (checkValidationForField(planProvider)) {
                    dataForAutoRegistration.setPlanProvider(planProvider);
                }
                if (checkValidationForField(gender)) {
                    dataForAutoRegistration.setGender(gender);

                }
                if (checkValidationForField(memberId)) {
                    dataForAutoRegistration.setMemberId(memberId);
                }
                if (checkValidationForField(grpName)) {
                    dataForAutoRegistration.setGrpName(grpName);

                }
                if (checkValidationForField(grpNumber)) {
                    dataForAutoRegistration.setGrpNumber(grpNumber);
                }
                if (checkValidationForField(coverage)) {
                    dataForAutoRegistration.setCoverage(coverage);

                }
                if (checkValidationForField(planType)) {
                    dataForAutoRegistration.setPlanType(planType);
                }
                if (checkValidationForField(city)) {
                    dataForAutoRegistration.setCity(city);

                }
                if (checkValidationForField(state)) {
                    dataForAutoRegistration.setState(state);

                }

            }
        }
        return dataForAutoRegistration;
    }

    private boolean checkValidationForField(String data) {
        if (!data.equalsIgnoreCase("null")|| TextUtils.isEmpty(data)) {
            return false;
        }
        return true;
    }

    private void startRegistrationActivity(DataForAutoRegistration dataTosend) {
        Intent intent = new Intent(ProfileSelfieActivityCustomeCam.this, RegistrationDetailsActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(Constant.REG_REQUIRED_DATA, dataTosend);
        b.putInt(Constant.REGISTRATION_MODE, Constant.AUTO);
        b = putUrlValuesInBundle(b);
        intent.putExtra(Constant.BUNDLE, b);
        startActivity(intent);
    }

    private Bundle putUrlValuesInBundle(Bundle b) {
        if (uriSelfie != null) {
            b.putString(Constant.KEY_SLFIE, uriSelfie.toString());
        }
        if (uriDlFront != null) {
            b.putString(Constant.KEY_ID_FRONT, uriDlFront.toString());
        }
        if (uriDlback != null) {
            b.putString(Constant.KEY_ID_BACK, uriDlback.toString());
        }
        if (uriIcFront != null) {
            b.putString(Constant.KEY_INSURANCE_FRONT, uriIcFront.toString());
        }
        if (uriIcBack != null) {
            b.putString(Constant.KEY_INSURANCE_BACK, uriIcBack.toString());
        }
        return b;
    }

    private void initialisevariables() {
        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
    }

    private ArrayList<CardResponse> parseCarddata(String cardResponse) {
        Gson gson = new Gson();
        try {
            Type listType = new TypeToken<List<CardResponse>>() {
            }.getType();
            List<CardResponse> cardResponses = (List<CardResponse>) gson.fromJson(cardResponse, listType);
            ArrayList<CardResponse> cardResponseList = (ArrayList) cardResponses;
            Log.v(TAG, cardResponseList.toString());
            CardResponse cardRes = cardResponseList.get(0);
            Log.v(TAG, "CardResponse Data:as obj:" + cardRes.toString());
            return cardResponseList;
        } catch (Exception e) {

            return null;

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Util.isRegistrationFlowFinished || isUserLogout) {
            Util.isRegistrationFlowFinished = false;
            finish();
        }
    }

    public void showHelpOverlay(LayoutInflater inflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_PROFILE)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_PROFILE, false);
        }
    }

    private void showHelpView(LayoutInflater layoutInflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_PROFILE)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_PROFILE, false);
            helpView = layoutInflater.inflate(R.layout.profile_help, null, false);
            helpLl = (RelativeLayout) helpView.findViewById(R.id.profile_help_ll);
            helpLl.setY(bottom);
            TextView arrowTv = (TextView) helpView.findViewById(R.id.profile_help_arrow);
            arrowTv.setTypeface(Util.getArrowFont(this));
            int x = getWindowManager().getDefaultDisplay().getWidth() / 3 - right + 30;
            LinearLayout linearLayout = (LinearLayout) helpView.findViewById(R.id.layoutStepInstruction);
            linearLayout.setX(x);


            TextView arrowSteps = (TextView) helpView.findViewById(R.id.txtStepsArrow);
            arrowSteps.setTypeface(Util.getArrowFont(this));

            TextView txtCoatch = ((TextView) helpView.findViewById(R.id.txtCoachMarks));
            txtCoatch.setTypeface(Util.getFontSegoe(this));

           /* if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_LARGE|| Configuration.SCREENLAYOUT_LAYOUTDIR_MASK==Configuration.SCREENLAYOUT_SIZE_NORMAL||Configuration.SCREENLAYOUT_LAYOUTDIR_MASK==Configuration.SCREENLAYOUT_SIZE_XLARGE||Configuration.SCREENLAYOUT_LAYOUTDIR_MASK==Configuration.SCREENLAYOUT_SIZE_UNDEFINED) {
                txtCoatch.setTypeface(Util.getFontSegoe(this));
                txtCoatch.setVisibility(View.VISIBLE);

            }else {
                txtCoatch.setVisibility(View.GONE);
            }*/
            int h = getWindowManager().getDefaultDisplay().getHeight();
            if (getWindowManager().getDefaultDisplay().getHeight() <= 800) {
                txtCoatch.setVisibility(View.GONE);
            }


            ((TextView) helpView.findViewById(R.id.profile_tap_tv)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.profile_tap_tv_1)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.profile_tap_tv_2)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.profile_tap_tv_3)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.tap_tv_dismiss)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.txtCoachMarks)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.txtSteps)).setTypeface(Util.getFontSegoe(this));


            // arrowTv.setX((left+right) / 2);

            TextView txtHelp = ((TextView) helpView.findViewById(R.id.profile_help_tv));
            txtHelp.setTypeface(Util.getFontSegoe(this));
            //txtHelp.setY(bottom);;

            //((TextView) helpView.findViewById(R.id.help_tv)).setX((left+right) / 4);
            RelativeLayout helpLayout = ((RelativeLayout) helpView.findViewById(R.id.profile_help_rl));
            helpLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                }
            });
            frameLayout.addView(helpView);
        }
    }

}
