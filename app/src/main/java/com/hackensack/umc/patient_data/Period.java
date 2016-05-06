package com.hackensack.umc.patient_data;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */

public class Period
{
    private String start;
    private String end;

    public Period(String start) {
        this.start = start;
    }

    public Period(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart ()
    {
        return start;
    }

    public void setStart (String start)
    {
        this.start = start;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [start = "+start+"]";
    }
}
