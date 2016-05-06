package com.hackensack.umc.datastructure;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gaurav_salunkhe on 9/29/2015.
 */
public class ScheduleDetails implements Serializable {

    String mID;
    String mLocationValueReference;
    String mPractitionerValueReference;
    String mDate;
    String mSystem;
    String mCode;
    String mDisplay;
    ArrayList<TimeSlotDetails> mSlots = new ArrayList<TimeSlotDetails>();


    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getLocationValueReference() {
        return mLocationValueReference;
    }

    public void setLocationValueReference(String mLocationValueReference) {
        this.mLocationValueReference = mLocationValueReference;
    }

    public String getPractitionerValueReference() {
        return mPractitionerValueReference;
    }

    public void setPractitionerValueReference(String mPractitionerValueReference) {
        this.mPractitionerValueReference = mPractitionerValueReference;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public ArrayList<TimeSlotDetails> getSlots() {
        return mSlots;
    }

    public void setSlots(ArrayList<TimeSlotDetails> mSlots) {
        this.mSlots = mSlots;
    }

    public void addSlot(TimeSlotDetails slot) {
        mSlots.add(slot);
    }

    public String getSystem() {
        return mSystem;
    }

    public void setSystem(String system) {
        this.mSystem = system;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        this.mCode = code;
    }

    public String getDisplay() {
        return mDisplay;
    }

    public void setDisplay(String display) {
        this.mDisplay = display;
    }
}
