package com.hackensack.umc.datastructure;

import android.text.TextUtils;
import android.util.Log;

import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bhagyashree_kumawat on 10/14/2015.
 */
public class Address implements Serializable, Comparable {
    private String street1;
    private String street2;
    private String state;
    private String city;
    private String stateAbbreviation;
    private String country="US";
    //private String county;

    private String mUse;

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    @Override
    public String toString() {
        return street1 +
                ", " + ((TextUtils.isEmpty(street2)) ? "" : street2 + ",") +
                "\n" + city + ", " + state +
                ",\n" + zip + ", " + country;
    }

    public String getAppointmentAddress() {
        return street1 + ((TextUtils.isEmpty(street2)) ? "" : ", " + street2) +
                "\n" + state + ", " + zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.stateAbbreviation = state;
    }

    /*public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }*/

    private String zip;


    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }


    public Address(JSONObject json) {

        try {

            mUse = Util.getJsonData(json, Constant.TELECOM_USE);

            JSONArray addressLine = json.getJSONArray(Constant.ADDRESS_LINE);
            for (int count = 0; count < addressLine.length(); count++) {
                if (count == 0)
                    street1 = (addressLine.getString(count).replace("[", "")).replace("[", "");
                else
                    street2 = street2 + (addressLine.getString(count).replace("[", "")).replace("[", "");
            }

            this.state = Util.getJsonData(json, Constant.ADDRESS_STATE);
            this.city = Util.getJsonData(json, Constant.ADDRESS_CITY);
            this.zip = Util.getJsonData(json, Constant.ADDRESS_POSTAL_CODE);
            this.country = Util.getJsonData(json, Constant.ADDRESS_COUNTRY);

        } catch (Exception e) {

        }
    }

    public Address(String street1, String street2, String city, String state, String zip, String country) {
        this.street1 = street1;
        this.street2 = street2;
        this.state = state;
        this.city = city;
        this.zip = zip;
        this.country = country;
        this.stateAbbreviation = state;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        Log.v("Address", "equesls");
        Address address = (Address) o;
        if (this.getZip().equalsIgnoreCase(address.getZip()) && (this.getStateAbbreviation().equalsIgnoreCase(address.getStateAbbreviation())) && (this.getCity().equalsIgnoreCase(address.getCity()))) {
            Log.v("Address", "obj are equesls");
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Object another) {
        Address address = (Address) another;
        if (this.getZip().equalsIgnoreCase(address.getZip()) && (this.getState().equalsIgnoreCase(address.getState())) && (this.getCity().equalsIgnoreCase(address.getCity()))) {
            Log.v("Address", "obj are equesls");
            return 1;
        }
        return 0;
    }

    public static <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        for (E e : list) {
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
}
