package com.hackensack.umc.datastructure;

import com.hackensack.umc.patient_data.Telecom;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gaurav_salunkhe on 9/29/2015.
 */
public class DoctorOfficeDetail  implements Serializable {

    String mID;
    Address mAddress;
    ArrayList<Telecom> mTelephone = new ArrayList<Telecom>();
    ArrayList<ScheduleDetails> mSchedules = new ArrayList<ScheduleDetails>();
    ArrayList<String> visitTypeFilter = new ArrayList<String>();

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address mLocation) {
        this.mAddress = mLocation;
    }

    public void setTelecom(ArrayList<Telecom> telecom) {
        mTelephone = telecom;
    }

    public ArrayList<Telecom> getTelephone() {
        return mTelephone;
    }

    public void setTelephone(ArrayList<Telecom> mTelephone) {
        this.mTelephone = mTelephone;
    }

    public ArrayList<ScheduleDetails> getSchedules() {
        return mSchedules;
    }

    public void setSchedules(ArrayList<ScheduleDetails> mSchedules) {
        this.mSchedules = mSchedules;
    }

    public void addSchedule(ScheduleDetails schedules) {
        mSchedules.add(schedules);
    }


    public ArrayList<String> getVisitTypeFilter() {
        return visitTypeFilter;
    }

    public void setVisitTypeFilter(ArrayList<String> visitType) {
        this.visitTypeFilter = visitType;
    }

    public void addVisitType(String visitType) {
        visitTypeFilter.add(visitType);
    }



}
