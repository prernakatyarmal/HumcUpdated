package com.hackensack.umc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.DirectionList;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

public class DirectionMapsActivity extends BaseActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DirectionList directionListDTO;
    private Location currentLocation;
    private LocationManager locationManager;
    static AlertDialog alert;
    private LinearLayout addressEditLL;
    private EditText addressEdit;
    private RadioGroup radioGroup;
    private Uri gmmIntentUri = null;
    private FrameLayout frameLayout;
    private boolean isOverlayRemoved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout = new FrameLayout(this);
        // creating LayoutParams
        FrameLayout.LayoutParams frameLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(frameLayout, frameLayoutParam);
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        View mainView = layoutInflater.inflate(R.layout.activity_direction_maps, null, false);
        frameLayout.addView(mainView);
        //setContentView(R.layout.activity_direction_maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //show help overlay
        showHelpView(layoutInflater);

        //default maptype to be normal
        ((RadioButton) mainView.findViewById(R.id.normal)).setChecked(true);

        radioGroup = (RadioGroup) findViewById(R.id.radio_grp);
        //default maptype to be normal
        ((RadioButton) findViewById(R.id.normal)).setChecked(true);
        addressEditLL = (LinearLayout) findViewById(R.id.address_editll);
        addressEdit = (EditText) findViewById(R.id.address_edit);
InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(addressEdit, InputMethodManager.SHOW_IMPLICIT);
        //get current location.
        currentLocation = getLocation();
        Intent intent = getIntent();
        intent = getIntent();
        directionListDTO = (DirectionList) intent.getSerializableExtra(DirectionsListActivity.EXTRA_DIRECTION_OBJ);
        if (directionListDTO != null) {
            setUpMapIfNeeded();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isUserLogout) {
            finish();
        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(directionListDTO);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setPadding(0, 30, 0, 0);
            } else {
                radioGroup.setVisibility(View.GONE);
            }


        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(DirectionList directionListDTO) {
        LatLng location = new LatLng(directionListDTO.getLat(), directionListDTO.getLon());
        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(directionListDTO.getName()).snippet(directionListDTO.getAddressStr()));
        marker.showInfoWindow();
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(location)
                .zoom(15)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                10, null);

    }


    public void onMaptypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.normal:
                if (checked) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.hybrid:
                if (checked)
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.satelite:
                if (checked)
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (!isOverlayRemoved) {
            isOverlayRemoved = Util.removeHelpViewFromParent(this, R.id.apt_sum_help_rl);
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                isBackPressed = true;
                super.onBackPressed();
                return true;

            case R.id.curr_loc:
                if (currentLocation != null) {
                    gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "(Current Location)" +
                            "&daddr=" + directionListDTO.getAddressStr() /*directionListDTO.getLat() + "," + directionListDTO.getLon()*/);
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            gmmIntentUri);
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
//                    finish();
                } else {
                    createLocationServiceError(this);
                }
                return true;

            case R.id.home_addr:
                addressEditLL.setVisibility(View.VISIBLE);
String homeAddress = Util.getAddressinSharePreferances(DirectionMapsActivity.this, Constant.PREF_HOME_ADDRESS);
                if (mMap != null)
                    mMap.setPadding(0, 90, 0, 0);
                if (TextUtils.isEmpty(homeAddress)) {
                    addressEdit.setHint("Enter Home Address ");
                } else {
                    addressEdit.setText(homeAddress);
                    addressEdit.requestFocus();
                }
                addressEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            final String homeAddr = addressEdit.getText().toString();
                            if (!TextUtils.isEmpty(homeAddr)) {
Util.saveAddressinSharePreferances(DirectionMapsActivity.this, Constant.PREF_HOME_ADDRESS, homeAddr);
                                gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + homeAddr + "&daddr=" + directionListDTO.getAddressStr()/*directionListDTO.getLat() + "," + directionListDTO.getLon()*/);
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        gmmIntentUri);
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
//                                finish();
                            } else {
                                Toast.makeText(DirectionMapsActivity.this, "Please enter valid address ", Toast.LENGTH_LONG).show();
                            }
                        }
                        return false;
                    }
                });
                return true;

            case R.id.work_addr:
                addressEditLL.setVisibility(View.VISIBLE);
String workAddress = Util.getAddressinSharePreferances(DirectionMapsActivity.this, Constant.PREF_WORK_ADDRESS);
                if (mMap != null)
                    mMap.setPadding(0, 90, 0, 0);
if (TextUtils.isEmpty(workAddress)) {
                    addressEdit.setHint("Enter Work Address ");
                } else {
                    addressEdit.setText(workAddress);
                    addressEdit.requestFocus();
                }
                addressEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            final String workAddr = addressEdit.getText().toString();
                            if (!TextUtils.isEmpty(workAddr)) {
Util.saveAddressinSharePreferances(DirectionMapsActivity.this, Constant.PREF_HOME_ADDRESS, workAddr);
                                gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + workAddr + "&daddr=" + directionListDTO.getAddressStr() /*directionListDTO.getLat() + "," + directionListDTO.getLon()*/);
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        gmmIntentUri);
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
//                                finish();
                            } else {
                                Toast.makeText(DirectionMapsActivity.this, "Please enter valid address ", Toast.LENGTH_LONG).show();
                            }
                        }
                        return false;
                    }
                });
                return true;

            case R.id.other_addr:
                addressEditLL.setVisibility(View.VISIBLE);
                if (mMap != null)
                    mMap.setPadding(0, 90, 0, 0);
                addressEdit.setHint("Enter Other Address ");
                addressEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            final String workAddr = addressEdit.getText().toString();
                            if (!TextUtils.isEmpty(workAddr)) {
                                gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + workAddr + "&daddr=" + directionListDTO.getAddressStr()/*directionListDTO.getLat() + "," + directionListDTO.getLon()*/);
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        gmmIntentUri);
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
//                                finish();
                            } else {
                                Toast.makeText(DirectionMapsActivity.this, "Please enter valid address ", Toast.LENGTH_LONG).show();
                            }
                        }
                        return false;
                    }
                });
                return true;
            case R.id.hackensack_addr:
                gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=20 Prospect Avenue, Hackensack NJ 07601&daddr=" + directionListDTO.getAddressStr() /*directionListDTO.getLat() + "," + directionListDTO.getLon()*/);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
//                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Method to show alert if the Location service is not ON
    public void createLocationServiceError(final Activity activityObj) {

        // show alert dialog if Internet is not connected
        AlertDialog.Builder builder = new AlertDialog.Builder(activityObj);

        builder.setMessage(
                "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                .setTitle("Alert")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activityObj.startActivity(intent);
                                alert.dismiss();
                                activityObj.finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.dismiss();

                            }
                        });
        alert = builder.create();
        alert.show();
    }

    public Location getLocation() {
        boolean isGPSEnabled, isNetworkEnabled;
        Location location = null;
        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
            } else {


                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    Log.d("GPS Enabled", "GPS Enabled");

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e("test",
                    "Unable to connect to LocationManager", e);
        }

        return location;
    }

    private void showHelpView(LayoutInflater layoutInflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_MAP_DIRECTION)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_MAP_DIRECTION, false);
            final View helpView = layoutInflater.inflate(R.layout.apt_summary_help, null, false);

            (helpView.findViewById(R.id.apt_sum_arrow)).setVisibility(View.GONE);
            ((TextView) helpView.findViewById(R.id.apt_sum_tv)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.tap_tv)).setTypeface(Util.getFontSegoe(this));
            ((TextView) helpView.findViewById(R.id.apt_sum_tv)).setText(R.string.map_help_text);

            (helpView.findViewById(R.id.apt_sum_help_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                    isOverlayRemoved = true;
                }
            });
            frameLayout.addView(helpView);
        }
    }

}
