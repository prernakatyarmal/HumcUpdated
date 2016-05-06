package com.hackensack.umc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.SpecialtyListAdapter;
import com.hackensack.umc.util.Constant;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class DoctorFilterActivity extends BaseActivity {

    private EditText mGenderEditText, mSearchWithInEditText, mLocationEditText;
    private SpecialtyListAdapter mGenderAdapter, mSearchWithInAdapter, mLocationAdapter;

    private String[] gender = {"Male", "Female"};
    private String[] searchWithIn = {"5 Miles", "10 Miles", "15 Miles", "20 Miles"};
    private String[] location = {"Patient Home", "Current Location"};

    //private MenuItem mResetMenu;
    private boolean isfromFilter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_doctor_filter);
        mGenderEditText = (EditText) findViewById(R.id.edittext_gender);
        String mSelectedGender = getIntent().getStringExtra(Constant.DOCTOR_SEARCH_GENDER_QUERY);
        boolean isFromFilter = getIntent().getBooleanExtra(Constant.DOCTOR_IS_FROM_FILTER, false);
        if (!isFromFilter) {
            if (TextUtils.isEmpty(mSelectedGender)) {
                mGenderAdapter = new SpecialtyListAdapter(this, gender, true);
                mGenderEditText.setEnabled(true);
                mGenderEditText.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP)
                            showGenderDialog();

                        return false;
                    }
                });
            } else {
                mGenderEditText.setText(mSelectedGender);
                mGenderEditText.setEnabled(false);
            }
        } else {
            mGenderAdapter = new SpecialtyListAdapter(this, gender, true);
            mGenderEditText.setText(mSelectedGender);
            mGenderEditText.setEnabled(true);
            mGenderEditText.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_UP)
                        showGenderDialog();

                    return false;
                }
            });
        }
        mSearchWithInAdapter = new SpecialtyListAdapter(this, searchWithIn, true);
        mSearchWithInEditText = (EditText) findViewById(R.id.edittext_search_within);
        mSearchWithInEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP)
                    showSearchWithInDialog();

                return false;
            }
        });

        mLocationAdapter = new SpecialtyListAdapter(this, location, true);
        mLocationEditText = (EditText) findViewById(R.id.edittext_location);
        mLocationEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP)
                    showLocationDialog();

                return false;
            }
        });

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           // mResetMenu.setVisible(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

//        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_doctor_filter, menu);
       /* mResetMenu = menu.findItem(R.id.action_reset);
        mResetMenu.setVisible(true);*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // mResetMenu.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                isBackPressed = true;
                finish();
                return true;

            case R.id.action_done:

                isBackPressed = true;
                finish();
                Intent intent = new Intent(DoctorFilterActivity.this, DoctorResultActivity.class);

                intent.putExtra(Constant.DOCTOR_SEARCH_GENDER_QUERY, mGenderEditText.getText().toString().trim());
                intent.putExtra(Constant.DOCTOR_SEARCH_SEARCH_WITHIN, mSearchWithInEditText.getText().toString().trim());
                intent.putExtra(Constant.DOCTOR_SEARCH_LOCATION, mLocationEditText.getText().toString().trim());
                intent.putExtra(Constant.DOCTOR_IS_FROM_FILTER, true);

                startActivity(intent);


                return true;

            case R.id.action_reset:

                if (mGenderEditText.isEnabled()) {
                    mGenderEditText.setText("");
                    mGenderEditText.setHint(getResources().getString(R.string.gender));
                }
                mSearchWithInEditText.setText("");
                mLocationEditText.setText("");

                mSearchWithInEditText.setHint(getResources().getString(R.string.search_with_in_text));
                mLocationEditText.setHint(getResources().getString(R.string.location_text));

                //mResetMenu.setVisible(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    AlertDialog alert;
    private ListView mDialogListView;
    private ProgressBar mDialogProgressBar;

    private void showGenderDialog() {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorFilterActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_list, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.gender));

            mDialogProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
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
                   // mResetMenu.setVisible(true);

                }
            });

            alert = builder.show();
        }
    }

    private void showSearchWithInDialog() {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorFilterActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_list, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.distance_text));

            mDialogProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
            mDialogProgressBar.setVisibility(View.GONE);

            (dialogView.findViewById(R.id.relative_dialog_button)).setVisibility(View.GONE);
            mDialogListView = (ListView) dialogView.findViewById(R.id.list_specialty);
            mDialogListView.setAdapter(mSearchWithInAdapter);

            ((ListView) dialogView.findViewById(R.id.list_specialty)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mSearchWithInEditText.setText(searchWithIn[i]);
                    alert.dismiss();
                    mDialogProgressBar = null;
                    mDialogListView = null;
                   // mResetMenu.setVisible(true);

                }
            });

            alert = builder.show();
        }
    }

    private void showLocationDialog() {

        if (!isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorFilterActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_list, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.from_text));

            mDialogProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
            mDialogProgressBar.setVisibility(View.GONE);

            (dialogView.findViewById(R.id.relative_dialog_button)).setVisibility(View.GONE);
            mDialogListView = (ListView) dialogView.findViewById(R.id.list_specialty);
            mDialogListView.setAdapter(mLocationAdapter);

            ((ListView) dialogView.findViewById(R.id.list_specialty)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mLocationEditText.setText(location[i]);
                    alert.dismiss();
                    mDialogProgressBar = null;
                    mDialogListView = null;
                   // mResetMenu.setVisible(true);

                }
            });

            alert = builder.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isUserLogout) {
            finish();
        }
    }

}
