package com.hackensack.umc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.SpecialtyListAdapter;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.util.Arrays;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class DoctorSearchActivity extends BaseActivity implements HttpDownloadCompleteListener {

    private EditText mSpecialtyEditText, mZipCodeEditText, mGenderEditText, mFirstNameEditText, mLastNameEditText;

    private String mDoctorSearchQuery;
    private boolean isSpecialtyShown = false, isGenderDialogShown = false;

//    private String mSelectedSpecialty;
//    private String mSelectedGender;
//    private String mSelectedZipcode;
//    private String mSelectedFirstName;
//    private String mSelectedLastName;

//    String[] speciality = {"Podiatry","Pedodontics","Infectious Diseases","Dentistry - General","Orthopaedic Pediatric","Dentistry - Endodontics","Pediatric Primary Care","Dentistry - Oral Surgery","Gynecologic & Breast Pathology",
//            "Pediatric Emergency Medicine","Laparascopic Surgery", "Thoracic Surgery", "Obstetrics & Gynecology", "Pain & Palliative Medicine", "Vascular Surgery", "Pediatric Urology", "Pediatric Hematology & Oncology", "Surgery General",
//            "Orthopaedic Spine", "Anesthesiology", "Reproductive Endocrinology & Infertility", "Pediatric Intensive Care", "Child & Adolescent Psychiatry", "Orthopaedic General", "Maternal Fetal Medicine", "Family Medicine", "Neuro - Oncology Oncology",
//            "Orthopaedic Foot & Ankle", "Oculoplastics - Ophthalmology", "Wound Center", "Oncology", "Dentistry - Oral & Maxillofacial Surgery", "Pathology", "Pediatric Cardiology", "Podiatry Foot", "Pediatric Dermatology", "Clinical Cardiac Electrophysiology",
//            "Neuro - Ophthalmology", "Hand Surgery", "Family Practice", "Pediatric Endocrinology & Diabetes", "Pediatrics Ophthalmology", "Pediatric Gastroenterology & Nutrition", "Orthopaedic Hand", "Pediatric Otolaryngology", "Pediatric Nephrology", "Urology",
//            "Radiology", "Gynecology", "Pediatric Developmental & Behavioral", "Bariatric Surgery", "Hematology & Oncology", "Genetics", "Pediatric Critical Care", "Ophthalmology", "Internal Medicine - General", "Rheumatology & Arthritis", "Orthopaedic Trauma",
//            "Neurosurgery", "Dermatology", "Hip & Knee Replacement", "Critical Care", "Orthopaedic Oncology", "Radiation Oncology", "Emergency Medicine", "Breast Surgery", "Otolaryngology - General", "Pediatric Neurosurgery", "Orthopaedic Knee-Shoulder-Sports",
//            "Surgery Transplant", "Trauma / Critical Care / Injury Prevention Surgery", "Cardiac Surgery", "Psychology", "Neurology", "Dentistry - Prosthodontics", "Pediatric Neurology", "Pulmonary Disease", "Gastroenterology", "Pediatric Ophthalmology", "Ophthalmology - General",
//            "Geriatric Medicine", "Pediatric General", "Gynecology & Oncology", "Pediatric Surgery", "Neonatology", "Surgery Oncology", "Organ Transplantation Services - Surgery", "Interventional Cardiology", "Pediatric Pulmonology", "Colon & Rectal Surgery", "Plastic Surgery",
//            "Cardiology", "Retinal Surgery", "Nephrology", "Psychiatry", "Dentistry - Orthodontics", "Allergy & Immunology", "Physical Medicine & Rehabilitation", "Genitourinary & Gynecologic Pathology", "Pediatric Rheumatology& Arthritis", "Endocrinology", "Pediatric Infectious Disease", "Pediatric Blood & Marrow Transplantation"};

    String[] speciality = {"Specialty"};
    String[] gender = {"Male", "Female"};

    private CardView mMyDoctorRelativeLayout;

    private SpecialtyListAdapter mSpecialityAdapter, mGenderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        HttpDownloader http = null;
        if (Util.ifNetworkAvailableShowProgressDialog(DoctorSearchActivity.this, getString(R.string.loading_text), false)) {
            isSpecialtyShown =true;
            http = new HttpDownloader(this, ServerConstants.SPECIALITY_URL, Constant.DOCTOR_SPECIALTY_DATA, this);
            http.startDownloading();
        }

        setContentView(R.layout.activity_doctor_search_new);
//        setContentView(R.layout.activity_doctor_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mZipCodeEditText = (EditText) findViewById(R.id.edittext_zipcode);
        mZipCodeEditText.addTextChangedListener(mTextWatcher);
        mFirstNameEditText = (EditText) findViewById(R.id.edittext_first_name);
        mFirstNameEditText.addTextChangedListener(mTextWatcher);
        mLastNameEditText = (EditText) findViewById(R.id.edittext_last_name);
        mLastNameEditText.addTextChangedListener(mTextWatcher);

        mGenderAdapter = new SpecialtyListAdapter(this, gender, true);
        mGenderEditText = (EditText) findViewById(R.id.edittext_gender);
        mGenderEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP && !isSpecialtyShown)
                    showGenderDialog();

                return false;
            }
        });

        mSpecialtyEditText = (EditText) findViewById(R.id.edittext_specialty);
        mSpecialtyEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP && !isGenderDialogShown) {
                    if (Util.ifNetworkAvailableShowProgressDialog(DoctorSearchActivity.this, getString(R.string.loading_text), false)) {
                        showSpecialtyDialog();
                    }
                }
                return false;
            }
        });

        mMyDoctorRelativeLayout = (CardView) findViewById(R.id.relative_my_doctor);
        mMyDoctorRelativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorSearchActivity.this, FavoriteDoctorList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }

        });

//        mDateSpinner = (Spinner) findViewById(R.id.date_spinner);
//
//        View.OnTouchListener Spinner_OnTouch = new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
////                    showDatePickerDialog(v);
//                    ArrayAdapter<CharSequence> mSpecialityAdapter = ArrayAdapter.createFromResource(DoctorSearchActivity.this,
//                            R.array.planets_array, android.R.layout.simple_spinner_item);
//                    // Specify the layout to use when the list of choices appears
//                    mSpecialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    // Apply the adapter to the spinner
//                    mSpecialitySpinner.setAdapter(mSpecialityAdapter);
//                }
//                return true;
//            }
//        };
//        mSpecialitySpinner.setOnTouchListener(Spinner_OnTouch);

//        View.OnKeyListener Spinner_OnKey = new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//                    showDatePickerDialog(v);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        };
//
//        mDateSpinner.setOnTouchListener(Spinner_OnTouch);
//        mDateSpinner.setOnKeyListener(Spinner_OnKey);
    }

//    public boolean showDatePickerDialog() {
//
//        DatePickerDialog dDialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//
////                        Toast.makeText(DoctorSearchActivity.this, "Something",
////                                Toast.LENGTH_SHORT).show();
//
//                        mDateTextView.setText(" Year : "+year+" Month : "+monthOfYear+" Day : "+dayOfMonth);
//
////                        ((Spinner)v).setPrompt(" Year : "+year+" Month : "+monthOfYear+" Day : "+dayOfMonth);
//
//
//                    }
//
//                }, 2012, 3, 3);
//        dDialog.show();
//
////        final Calendar c = Calendar.getInstance();
////        int year = c.get(Calendar.YEAR);
////        int month = c.get(Calendar.MONTH);
////        int day = c.get(Calendar.DAY_OF_MONTH);
////
////        // Create a new instance of DatePickerDialog and return it
////        return new DatePickerDialog(getActivity(), this, year, month, day);
//
//        return false;
//    }


    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (mResetMenu != null)
                mResetMenu.setVisible(true);

        }

        @Override
        public void afterTextChanged(Editable editable) {

//            if (!(editable.toString()).matches("[a-zA-]Z*")) {
//                Toast.makeText(DoctorSearchActivity.this, "Error ", Toast.LENGTH_SHORT).show();
//            }

        }
    };

//    TextWatcher mFirstNameWatcher = new TextWatcher() {
//
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            if(mResetMenu != null)
//                mResetMenu.setVisible(true);
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
////            if (!(editable.toString()).matches("[a-zA-]Z*")) {
////                Toast.makeText(DoctorSearchActivity.this, "Error ", Toast.LENGTH_SHORT).show();
////            }
//
//        }
//    };

    MenuItem mResetMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        getMenuInflater().inflate(R.menu.menu_doctor_search, menu);

        mResetMenu = menu.findItem(R.id.action_reset);
        mResetMenu.setVisible(false);

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

//                if(mDoctorSearchQuery == null) {
//
//                    try {
//
//                        mSelectedSpecialty = mSpecialtyEditText.getText().toString();
//                        if(mSelectedSpecialty != null && mSelectedSpecialty.length() > 1) {
//                            mDoctorSearchQuery = "?specialty=" + URLEncoder.encode(mSelectedSpecialty.trim().replaceAll("&amp;", "&").trim(), "UTF-8");
//                        }
//
//                    }catch(Exception e) {
//
//                    }
//                }
//
//                mSelectedGender = mGenderEditText.getText().toString();
//                if(mDoctorSearchQuery == null) {
//                    if(mSelectedGender != null && mSelectedGender.length() > 1) {
//                        mDoctorSearchQuery = "?gender=" + mSelectedGender.toString().trim();
//                    }
//                }else {
//                    if(mSelectedGender != null && mSelectedGender.length() > 1)
//                        mDoctorSearchQuery = mDoctorSearchQuery + "&gender="+ mSelectedGender.trim();
//                }
//
//                mSelectedZipcode = mZipCodeEditText.getText().toString();
//                if(mSelectedZipcode != null && mSelectedZipcode.length() > 0) {
//                    try {
//                        if (mDoctorSearchQuery == null) {
//                            mDoctorSearchQuery = "?zipcode=" + URLEncoder.encode(mSelectedZipcode, "UTF-8");
//                        } else {
//                            mDoctorSearchQuery = mDoctorSearchQuery + "&zipcode=" + URLEncoder.encode(mSelectedZipcode, "UTF-8");
//                        }
//                    }catch(Exception e) {
//
//                    }
//                }
//
//                mSelectedFirstName = mFirstNameEditText.getText().toString();
//                if(mSelectedFirstName != null && mSelectedFirstName.length() > 0) {
//                    try {
//                        if(mDoctorSearchQuery == null) {
//                            mDoctorSearchQuery = "?firstName=" + URLEncoder.encode(mSelectedFirstName, "UTF-8");
//                        }else {
//                            mDoctorSearchQuery = mDoctorSearchQuery + "&firstName=" + URLEncoder.encode(mSelectedFirstName, "UTF-8");
//                        }
//                    }catch(Exception e) {
//
//                    }
//                }
//
//                mSelectedLastName = mLastNameEditText.getText().toString();
//                if(mSelectedLastName != null && mSelectedLastName.length() > 0) {
//                    try {
//                        if (mDoctorSearchQuery == null) {
//                            mDoctorSearchQuery = "?lastName=" + URLEncoder.encode(mSelectedLastName, "UTF-8");
//                        } else {
//                            mDoctorSearchQuery = mDoctorSearchQuery + "&lastName=" + URLEncoder.encode(mSelectedLastName, "UTF-8");
//                        }
//                    }catch(Exception e) {
//
//                    }
//                }

//                Intent intent = new Intent(DoctorSearchActivity.this, DoctorResultActivity.class);
//                if(mDoctorSearchQuery == null) {
//                    intent.putExtra(Constant.DOCTOR_SEARCH_QUERY, "");
//                }else {
//                    intent.putExtra(Constant.DOCTOR_SEARCH_QUERY, mDoctorSearchQuery);
//                }
//                startActivity(intent);

//                if ((mFirstNameEditText.toString()).matches("[a-zA-Z]"))

                if (Util.ifNetworkAvailableShowProgressDialog(DoctorSearchActivity.this, "", false)) {

                    String firstName = mFirstNameEditText.getText().toString().trim();

//                    if (firstName != null && firstName.length() > 0 && !firstName.matches(Constant.STRING_REGULAR_EXPRESSION)) {
//                        showSpecialCharacterErrorDialog("first name");
//                        return true;
//                    }
//
                    String lastName = mLastNameEditText.getText().toString().trim();
//                    if (lastName != null && lastName.length() > 0 && !lastName.matches(Constant.STRING_REGULAR_EXPRESSION)) {
//                        showSpecialCharacterErrorDialog("last name");
//                        return true;
//                    }
//
                    String zipcode = mZipCodeEditText.getText().toString().trim();
//                    if (zipcode != null && zipcode.length() > 0 && !zipcode.matches(Constant.US_ZIP_CODE_REGULAR_EXPRESSION)) {
//                        showSpecialCharacterErrorDialog("zip code");
//                        return true;
//                    }


                    Intent intent = new Intent(DoctorSearchActivity.this, DoctorResultActivity.class);

                    intent.putExtra(Constant.DOCTOR_SEARCH_SPECIALTY_QUERY, mSpecialtyEditText.getText().toString().replaceAll("&amp;", "&").trim());
                    intent.putExtra(Constant.DOCTOR_SEARCH_GENDER_QUERY, mGenderEditText.getText().toString().trim());
                    intent.putExtra(Constant.DOCTOR_SEARCH_ZIPCODE_QUERY, zipcode);
                    intent.putExtra(Constant.DOCTOR_SEARCH_FIRST_NAME_QUERY, firstName);
                    intent.putExtra(Constant.DOCTOR_SEARCH_LAST_NAME_QUERY, lastName);

                    startActivity(intent);

                }
                return true;

            case R.id.action_reset:


                mSpecialtyEditText.setText("");
                mZipCodeEditText.setText("");
                mGenderEditText.setText("");
                mFirstNameEditText.setText("");
                mLastNameEditText.setText("");

                mSpecialtyEditText.setHint(getResources().getString(R.string.select_str));
                mZipCodeEditText.setHint(getResources().getString(R.string.enter_str));
                mGenderEditText.setHint(getResources().getString(R.string.select_str));
                mFirstNameEditText.setHint(getResources().getString(R.string.first_name_hint));
                mLastNameEditText.setHint(getResources().getString(R.string.last_name_hint));

                mResetMenu.setVisible(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

        Object obj = data.getDataObject();

        if (obj != null) {

            speciality = (String[]) data.getDataObject();
            //Sort here
            Arrays.sort(speciality);
            mSpecialityAdapter = new SpecialtyListAdapter(this, speciality, true);

            if (speciality.length > 1 && mDialogProgressBar != null) {
                mDialogProgressBar.setVisibility(View.GONE);
                stopProgress();
            }

            if (mDialogListView != null && !isGenderDialogShown) {
                mDialogListView.setAdapter(mSpecialityAdapter);
            }else {
                isSpecialtyShown = false;
            }
//            mSpecialityAdapter = new SpinnerAdapter(this,
//                    android.R.layout.simple_spinner_item,
//                    speciality);
//            mSpecialitySpinner.setAdapter(mSpecialityAdapter);

        } else {
            isSpecialtyShown = false;
        }

    }

    @Override
    protected void onResume() {

        mDoctorSearchQuery = null;
        super.onResume();

        if (Util.isActivityFinished || isUserLogout) {
            finish();
        }


    }

    AlertDialog alert;
    private ListView mDialogListView;
    private ProgressBar mDialogProgressBar;

    private void showGenderDialog() {

        if (!isFinishing() && !isSpecialtyShown) {
            isGenderDialogShown = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorSearchActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_list, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Gender");

            mDialogProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
            stopProgress();
            mDialogProgressBar.setVisibility(View.GONE);

            (dialogView.findViewById(R.id.relative_dialog_button)).setVisibility(View.GONE);
            mDialogListView = (ListView) dialogView.findViewById(R.id.list_specialty);
            mDialogListView.setAdapter(mGenderAdapter);

            ((ListView) dialogView.findViewById(R.id.list_specialty)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mGenderEditText.setText(gender[i]);
                    alert.dismiss();
                    mDialogProgressBar = null;
                    mDialogListView = null;
                    mResetMenu.setVisible(true);

                }
            });

            alert = builder.show();
            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isGenderDialogShown = false;
                }
            });

        }
    }

    private void showSpecialtyDialog() {

        if (!isFinishing() && !isGenderDialogShown) {
            isSpecialtyShown = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorSearchActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_list, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Specialty");

            mDialogProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);

            if (speciality.length > 1) {
                stopProgress();
                mDialogProgressBar.setVisibility(View.GONE);
            } else {
                isSpecialtyShown = true;
                (new HttpDownloader(this, ServerConstants.SPECIALITY_URL, Constant.DOCTOR_SPECIALTY_DATA, this)).startDownloading();
            }

            (dialogView.findViewById(R.id.relative_dialog_button)).setVisibility(View.GONE);
            mDialogListView = (ListView) dialogView.findViewById(R.id.list_specialty);
            mDialogListView.setAdapter(mSpecialityAdapter);

            ((ListView) dialogView.findViewById(R.id.list_specialty)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mSpecialtyEditText.setText(speciality[i]);
                    alert.dismiss();
                    isSpecialtyShown = false;
                    mDialogProgressBar = null;
                    mDialogListView = null;
                    mResetMenu.setVisible(true);

                }
            });

            alert = builder.show();
            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isSpecialtyShown = false;
                }
            });

        }

    }

    private void showSpecialCharacterErrorDialog(String str) {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Error");

            ((TextView) dialogView.findViewById(R.id.text_message)).setText("Please enter valid " + str);

            TextView btnDismiss = (TextView) dialogView.findViewById(R.id.button_dialog_ok);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            alert = builder.show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            isSpecialtyShown = false;
            isGenderDialogShown = false;
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
//    }
}
