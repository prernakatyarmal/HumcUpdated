package com.hackensack.umc.util;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hackensack.umc.R;

/**
 * Created by gaurav_salunkhe on 11/19/2015.
 */
//AsyncTask to calculate the Distance between current position and doctors address
public class DistanceLoader extends AsyncTask<Object, String, String> {

    private View view;
    private String distance = null,addressStr;
    private Context mContext;
    private Location mCurrentLocation;

    public DistanceLoader(Context context, TextView mileText, Location currentLocation,String address) {
        mContext = context;
        view = mileText;
        mCurrentLocation = currentLocation;
        addressStr = address;
    }

    @Override
    protected void onPreExecute() {

        ((TextView) view).setText("( ... " + mContext.getString(R.string.mile_str) + " )");

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... parameters) {
        LatLng latLng = Util.getLocationFromAddress(addressStr, mContext);
        if (mCurrentLocation != null) {
            double distanceInMile = Util.getDistanceInMiles(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), latLng.latitude, latLng.longitude);
            distance = String.format("%.1f", distanceInMile);

        }

        return distance;
    }

    @Override
    protected void onPostExecute(String distance) {
        if (distance != null && view != null) {
            String dist = "( " + distance + " " + mContext.getString(R.string.mile_str) + " )";
            ((TextView) view).setText(dist);
//            if (mItems != null && mItems.size() >= position) {
//                DoctorDetails doctorDetails = mItems.get(position);
//                doctorDetails.setmDocDistance(dist);
//                mItems.set(position, doctorDetails);
//            }


        }
    }
}
