package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Name implements Serializable
{
    private String given;

    private String family;

    private String use;

    public Name(String given, String family, String use) {
        this.given = given;
        this.family = family;
        this.use = use;
    }

    public String getGiven ()
    {
        return given;
    }

    public void setGiven (String given)
    {
        this.given = given;
    }

    public String getFamily ()
    {
        return family;
    }

    public void setFamily (String family)
    {
        this.family = family;
    }

    public String getUse ()
    {
        return use;
    }

    public void setUse (String use)
    {
        this.use = use;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [given = "+given+", family = "+family+", use = "+use+"]";
    }
}


