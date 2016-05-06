package com.hackensack.umc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.SpecialtyListAdapter;
import com.hackensack.umc.datastructure.Address;
import com.hackensack.umc.http.CommonAPICalls;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.patient_data.Text;
import com.hackensack.umc.response.AccessTokenResponse;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends BaseActivity implements View.OnFocusChangeListener {
    public static final String ADRESS_STR = "address";
    private static final String TAG = "AddressActivity";
    private  boolean calledFromDilog=true;
    //private String[] mStateArr = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

    private String[] mStateArr = {"AL", "AK", "AS", "AZ", "AR", "CA", "CO", "CT", "DC", "DE", "FL", "FM", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MH", "MD", "MA", "MI", "MN", "MP", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "VI", "WA", "WV", "WI", "WY"};
    EditText street1Edit, street2Edit, zipEdit, countryEdit, cityEdit, stateEdit;
    private ArrayAdapter<String> stateAdapter;
    private Address mAddress;
    private AlertDialog alert;
    private ListView mDialogListView;
    private SharedPreferences sharedPreferences;
    private String zipCode;
    private AlertDialog dialog;
    private ArrayList addressLists;
    private String error = "";
    private EditText edtzipCode;
    private AlertDialog.Builder builder ;
    private boolean isInvalidDialogShown=true;
    private AlertDialog finalAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_address);

        street1Edit = (EditText) findViewById(R.id.strt1_edt);
        street2Edit = (EditText) findViewById(R.id.strt2_edt);
        cityEdit = (EditText) findViewById(R.id.city_edt);
        zipEdit = (EditText) findViewById(R.id.zip_edt);
        createZipCodeDiloag();

        zipEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // zipcodeshowPopup();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() == 5) {
                   /* if (zipCode == null) {
                        //   if (!zipCode.equalsIgnoreCase(s.toString())) {
                        // zipcodeshowPopup();*/
                    String error = validateZipCode(zipEdit.getText().toString());
                    if (TextUtils.isEmpty(error)) {
                        if (Util.ifNetworkAvailableShowProgressDialog(AddressActivity.this, (getString(R.string.loading_text)), true)) {

                            //if (edtzipCode != null) {

                           if (!calledFromDilog&&!isInvalidDialogShown) {
                                new getEpicAccessToken_2().execute();
                           }
                        }
                    } else {
                        Util.showAlert(AddressActivity.this, error, getString(R.string.alert_title));
                    }
                        /* }
                    }*/
                }


            }
        });

        countryEdit = (EditText) findViewById(R.id.country_edt);
        /*mStateSpinner = ((Spinner) findViewById(R.id.state_spinner));
         stateSpinnerAdapter = new SpinnerCustomAdapter(AddressActivity.this, android.R.layout.simple_spinner_dropdown_item, mStateArr);
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStateSpinner.setAdapter(stateSpinnerAdapter);*/

        stateAdapter = new SpecialtyListAdapter(this, mStateArr, true);
        stateEdit = (EditText) findViewById(R.id.state_edt);
        stateEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                if((alert == null || !alert.isShowing()) && count == 0)
                if (event.getAction() == MotionEvent.ACTION_UP)

                    showStateDialog();

                return false;
            }
        });
       /* zipEdit.setOnFocusChangeListener(this);
        street1Edit.setOnFocusChangeListener(this);
        cityEdit.setOnFocusChangeListener(this);
        countryEdit.setOnFocusChangeListener(this);
*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAddress = (Address) bundle.get(ADRESS_STR);
            if (mAddress != null) {
                isInvalidDialogShown=false;
                calledFromDilog=false;
                street1Edit.setText(mAddress.getStreet1());
                street2Edit.setText(mAddress.getStreet2());
                cityEdit.setText(mAddress.getCity());
                stateEdit.setText(mAddress.getState());
                zipCode = mAddress.getZip();
                zipEdit.setText(zipCode);
                countryEdit.setText(mAddress.getCountry());

            }
        }


        sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
        if (mAddress == null) {
            zipcodeshowPopup();

        } else {
            setFieldsDisable(mAddress);
            if (mAddress.getStateAbbreviation() == null || TextUtils.isEmpty(mAddress.getStateAbbreviation())) {
                zipcodeshowPopup();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address, menu);
        return true;
    }

    private String validateZipCode(String inputStr) {
        String errorStr = "";
        if (TextUtils.isEmpty(inputStr)) {
            errorStr = "Zip code is Required";
        } else {
// To support all US zip code Regex :"^\\d{5}(-\\d{4})?$\n")
            if (!(inputStr.matches("\\d{5}"))) {
                errorStr = getString(R.string.enter_validzip);
            }
        }
        return errorStr;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            isBackPressed = true;
            return true;
        }
        if (id == R.id.action_done) {
            checkValidationsOnDoneButton();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkValidationsOnDoneButton() {
        if (checkInputValidations()) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(ADRESS_STR, mAddress);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else {
            //Show error dialog
            if (!error.equalsIgnoreCase("AddressListNull")) {

//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddressActivity.this);
//                builder.setMessage(
//                        error)
//                        .setTitle("Alert")
//                        .setCancelable(false)
//                        .setPositiveButton(getString(R.string.button_ok),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                    }
//                                });
//                builder.create().show();

                Util.showNetworkOfflineDialog(this, getString(R.string.alert_title), error);
            }
            //return true;
        }
    }

    private boolean checkInputValidations() {
        if (validateInputs()) {
            mAddress = new Address(street1Edit.getText().toString(), street2Edit.getText().toString(), cityEdit.getText().toString(), stateEdit.getText().toString(), zipEdit.getText().toString(), countryEdit.getText().toString());
            mAddress.setCountry(countryEdit.getText().toString());
            if (validateZipCodeValues(mAddress)) {
                return true;
            } else {

                return false;
            }

        } else {
            return false;
        }
    }

    private boolean validateInputs() {

        if (TextUtils.isEmpty(street1Edit.getText()) || !TextUtils.isEmpty(street1Edit.getError())) {
            error = "Please enter street address";
            return false;
        } else if (TextUtils.isEmpty(cityEdit.getText())) {
            error = "Invalid city ";
            return false;
        } else if (TextUtils.isEmpty(stateEdit.getText()) || !TextUtils.isEmpty(Util.validateTextInput(stateEdit.getText().toString()))) {
            error = "State is Required";
            return false;
        } else if (TextUtils.isEmpty(zipEdit.getText()) || !TextUtils.isEmpty(validateZipCode(zipEdit.getText().toString()))) {
            error = "Invalid zip code";
            return false;
        } else if (TextUtils.isEmpty(countryEdit.getText().toString())) {
            error = "Invalid country ";
            return false;
        }
        return true;
    }

    private boolean validateZipCodeValues(Address address) {
        if (addressLists != null) {
            if (addressLists.contains(address)) {
                return true;
            } else {
                error = "Invalid Address";
                return false;

            }
        } else {
            /*if (Util.ifNetworkAvailableShowProgressDialog(AddressActivity.this, (getString(R.string.loading_text)), true)) {
                new getEpicAccessToken().execute();
            }*/
            error = "Invalid Address";
            return false;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String error = "";
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.zip_edt:
                    error = validateZipCode(zipEdit.getText().toString());
                    if (!TextUtils.isEmpty(error)) {
                        zipEdit.setError(error);
                    }
                    break;
                case R.id.city_edt:
                    error = Util.validateTextInput(cityEdit.getText().toString());
                    if (!TextUtils.isEmpty(error)) {
                        cityEdit.setError(error);
                    }
                    break;
                case R.id.state_edt:
                    if (TextUtils.isEmpty(stateEdit.getText())) {
                        stateEdit.setError("Please select valid state");
                    }
                    break;
                case R.id.country_edt:
                    error = Util.validateTextInput(countryEdit.getText().toString());
                    if (!TextUtils.isEmpty(error)) {
                        countryEdit.setError(error);
                    }
                    break;
                case R.id.strt1_edt:
                    error = "Invalid Address";
                    if (TextUtils.isEmpty(street1Edit.getText())) {
                        street1Edit.setError(error);
                    }

            }
        }
    }

    private void inFlateStateDialog() {

    }

    private void showStateDialog() {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_list, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("State");

            dialogView.findViewById(R.id.progress_bar).setVisibility(View.GONE);

            (dialogView.findViewById(R.id.relative_dialog_button)).setVisibility(View.GONE);
            mDialogListView = (ListView) dialogView.findViewById(R.id.list_specialty);
            mDialogListView.setAdapter(stateAdapter);

            ((ListView) dialogView.findViewById(R.id.list_specialty)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    stateEdit.setText(mStateArr[i]);
                    alert.dismiss();
                    mDialogListView = null;

                }
            });

            alert = builder.show();
        }
    }

    private class GetCityDetailsFromZipCode extends AsyncTask<String, Void, ArrayList<Address>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addressLists = null;
            // startProgress();
        }

        @Override
        protected ArrayList<Address> doInBackground(String... params) {
            String response = HttpUtils.getHttpGetResponse(AddressActivity.this, ServerConstants.URL_GET_CITY_DETAILS + params[0], getHeadersForCityCall());
            addressLists = null;
            try {
                Gson gson = new Gson();
                try {
                    Type listType = new TypeToken<List<Address>>() {
                    }.getType();
                    List<Address> addressList = gson.fromJson(response, listType);
                    addressLists = (ArrayList) addressList;
                    for (Address add : addressList) {
                        add.setZip(params[0]);
                    }
                    Log.v(TAG, addressLists.toString());
                    return addressLists;
                } catch (Exception e) {

                    return null;

                }
            } catch (Exception e) {

            }
            return addressLists;
        }

        @Override
        protected void onPostExecute(ArrayList<Address> addressList) {
            super.onPostExecute(addressList);
            if (addressList != null) {
                isInvalidDialogShown=true;
                if (addressList.size() > 0) {
                    Address address = addressList.get(0);

                    setFieldsDisable(address);
                    isInvalidDialogShown=false;

                } else {
                    cityEdit.setText("");
                    cityEdit.setEnabled(true);
                    stateEdit.setText("");
                    stateEdit.setEnabled(true);
                    edtzipCode.setText("");
                    //if(!isInvalidDialogShown) {
                        showinValidZipDialog(AddressActivity.this,  "Alert",getString(R.string.enter_validzip),true);
                    //}
                }
                if (error.equalsIgnoreCase("AddressListNull")) {
                    checkValidationsOnDoneButton();
                }
            }
            calledFromDilog=false;

            stopProgress();
        }
    }
    public void showinValidZipDialog(Activity activity, String title, String message, final boolean fromDialog) {
        if (!activity.isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_zipcode_invalid, null);
            builder.setView(dialogView);

            if (title.length() > 0)
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            else
                (dialogView.findViewById(R.id.dialog_title)).setVisibility(View.GONE);

            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_ok);

            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    isInvalidDialogShown=false;
                    calledFromDilog=true;
                    isInvalidDialogShown=true;
                    finalAlert.dismiss();
                    if(fromDialog){
                        createZipCodeDiloag();
                     zipcodeshowPopup();
                    }
                }
            });

            finalAlert = builder.show();
        }

    }

    private void setFieldsDisable(Address address) {
        if (address != null) {


            String city = address.getCity();
            String state = address.getStateAbbreviation();
            String country = address.getCountry();
            if (!TextUtils.isEmpty(city)) {
                cityEdit.setText(address.getCity());
                // cityEdit.setEnabled(false);
            } else {
                cityEdit.setText("");
                //  cityEdit.setEnabled(true);
            }
            if (!TextUtils.isEmpty(state)) {
                stateEdit.setText(address.getStateAbbreviation());
                // stateEdit.setEnabled(false);
            } else {
                stateEdit.setText("");
                stateEdit.setEnabled(true);
            }
            countryEdit.setText("US");

        }
    }

    private List<NameValuePair> getHeadersForCityCall() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Authorization",
                "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "")));
        return headers;
    }


    private class getEpicAccessToken extends AsyncTask<Void, Void, String> {
        public getEpicAccessToken(boolean fromDilog) {
            calledFromDilog=fromDilog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            String accessToken = new CommonAPICalls(AddressActivity.this).getEpicAccessToken();
            return accessToken;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
               /* String accessToken = parseTokenResponse(result);
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
                sharedPreferences.edit().putString(ServerConstants.ACCESS_TOKEN_EPIC, accessToken).commit();*/
                //  if (Util.ifNetworkAvailableShowProgressDialog(AddressActivity.this, (getString(R.string.loading_text)), true)) {
                new GetCityDetailsFromZipCode().execute(zipEdit.getText().toString());
                //}
            }
            // new readPatient().execute(accessToken);//Login call after userCreatyion

        }
    }

    private class getEpicAccessToken_2 extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            String accessToken = new CommonAPICalls(AddressActivity.this).getEpicAccessToken();
            return accessToken;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
               /* String accessToken = parseTokenResponse(result);
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
                sharedPreferences.edit().putString(ServerConstants.ACCESS_TOKEN_EPIC, accessToken).commit();*/
                //  if (Util.ifNetworkAvailableShowProgressDialog(AddressActivity.this, (getString(R.string.loading_text)), true)) {
                new GetCityDetailsFromZipCode().execute(zipEdit.getText().toString());
                //}
            }
            // new readPatient().execute(accessToken);//Login call after userCreatyion

        }
    }


    private List<NameValuePair> createHeaderEpicToken() {

        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Authorization",
                "Basic MEtRWmJ2aU5iWXJnbUJteUFIVGhHN2pGdEdlYTBwNjQ6RW1pall1dmRBWHZDd25Rdg===="));
        return headers;

    }

    private String parseTokenResponse(String response) {
        Log.v("response in parse", response);
        /*
         * //// 10-22 21:14:38.670: V/Epic Token is::(455): { 10-22
		 * 21:14:38.670: V/Epic Token is::(455): "access_token":
		 * "xM8IhwAWkbe8ZiPCZt7GcsGKoJvW", 10-22 21:14:38.670: V/Epic Token
		 * is::(455): "expires_in": 3599, 10-22 21:14:38.670: V/Epic Token
		 * is::(455): "token_type": "BearerToken" 10-22 21:14:38.670: V/Epic
		 * Token is::(455): }
		 *
		 * /////
		 */
        Gson gson = new Gson();
        AccessTokenResponse accessTokenResponse = gson.fromJson(response,
                AccessTokenResponse.class);
        return accessTokenResponse.getAccess_token();

    }
private void createZipCodeDiloag(){
    builder = new AlertDialog.Builder(this);

    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
    builder.setView(dialogView);
    builder.setCancelable(false);

    ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getString(R.string.enter_zip_code));
    ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.only_five_digits));
    edtzipCode = (EditText) dialogView.findViewById(R.id.edtZipCode);
    edtzipCode.setVisibility(View.VISIBLE);

    Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_cancel);
    btnDismiss.setText(getString(R.string.close));
    btnDismiss.setVisibility(View.VISIBLE);
    btnDismiss.setOnClickListener(new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            alert.dismiss();
            calledFromDilog=false;
            isInvalidDialogShown=false;
        }
    });

    Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
    btnOk.setText(getString(R.string.done));
    btnOk.setOnClickListener(new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

            zipCode = edtzipCode.getText().toString();
            if (zipCode.length() == 5) {

                Util.hide_keyboard_from(AddressActivity.this, v);
                zipEdit.setText(zipCode);
                if (Util.isNetworkAvailable(AddressActivity.this)) {
                    new getEpicAccessToken(true).execute();

                }

            } else {
                showinValidZipDialog(AddressActivity.this,  "Alert",getString(R.string.enter_validzip),true);
                calledFromDilog=false;
                isInvalidDialogShown=false;

               // zipcodeshowPopup();
            }
            alert.dismiss();
        }
    });
}
    private void zipcodeshowPopup() {


        alert = builder.show();



    }

    public static <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        for (E e : list) {
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isUserLogout) {
            finish();
        }
    }

}
