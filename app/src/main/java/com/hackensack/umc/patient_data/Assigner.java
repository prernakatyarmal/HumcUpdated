package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Assigner implements Serializable
{
    private String display;

    public Assigner(String display) {
        this.display=display;
    }

    public String getDisplay ()
    {
        return display;
    }

    public void setDisplay (String display)
    {
        this.display = display;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [display = "+display+"]";
    }
}


