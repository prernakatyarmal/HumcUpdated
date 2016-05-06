package com.hackensack.umc.datastructure;

import com.hackensack.umc.patient_data.Telecom;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gaurav_salunkhe on 9/29/2015.
 */
public class LocationDetails implements Serializable {

    private String mID;
    private String mName;

    ArrayList<Telecom> mTelephone = new ArrayList<Telecom>();

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        this.mAddress = address;
    }

    Address mAddress;


    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setTelephone(ArrayList<Telecom> arrayList) {

        mTelephone = arrayList;

    }

    public ArrayList<Telecom> getTelephone() {

        return mTelephone;

    }
}
