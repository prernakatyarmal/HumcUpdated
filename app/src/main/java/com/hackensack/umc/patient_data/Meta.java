package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Meta implements Serializable
{
    private String lastUpdated;

    public Meta(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdated ()
    {

        return lastUpdated;
    }

    public void setLastUpdated (String lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lastUpdated = "+lastUpdated+"]";
    }
}
