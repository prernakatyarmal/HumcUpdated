package com.hackensack.umc.datastructure;

import java.io.Serializable;

/**
 * Created by gaurav_salunkhe on 9/29/2015.
 */
public class TimeSlotDetails  implements Serializable {

    private String mID;
    private String mScheduleReference;
    private String mFreeBusyType;
    private String mStart;

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getScheduleReference() {
        return mScheduleReference;
    }

    public void setScheduleReference(String mScheduleReference) {
        this.mScheduleReference = mScheduleReference;
    }

    public String getFreeBusyType() {
        return mFreeBusyType;
    }

    public void setFreeBusyType(String mFreeBusyType) {
        this.mFreeBusyType = mFreeBusyType;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String mStart) {
        this.mStart = mStart;
    }
}
