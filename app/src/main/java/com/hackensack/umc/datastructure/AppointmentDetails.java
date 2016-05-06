package com.hackensack.umc.datastructure;

import java.io.Serializable;

/**
 * Created by gaurav_salunkhe on 9/29/2015.
 */
public class AppointmentDetails implements Serializable {

    private String mID;
    private String mStartDate;

    private String mPatientID;
    private String mLocationID;
    private String mPractitioner;

    private LocationDetails mLocation;

    private DoctorDetails mDoctorDetail;

    public LocationDetails getLocation() {
        return mLocation;
    }

    public void setLocation(LocationDetails location) {
        this.mLocation = location;
    }

    public String getPatientID() {
        return mPatientID;
    }

    public DoctorDetails getDoctorDetail() {
        return mDoctorDetail;
    }

    public void setDoctorDetail(DoctorDetails doctor) {
         mDoctorDetail = doctor;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getLocationID() {
        return mLocationID;
    }

    public void setLocationID(String mScheduleReference) {
        this.mLocationID = mScheduleReference;
    }

    public String getPractitioner() {
        return mPractitioner;
    }

    public void setPractitioner(String mFreeBusyType) {
        this.mPractitioner = mFreeBusyType;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String mStart) {
        this.mStartDate = mStart;
    }
}
