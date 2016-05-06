package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 11/26/2015.
 */

public class Issue implements  Serializable
{
    private Details details;

    private String severity;

    public Details getDetails ()
    {
        return details;
    }

    public void setDetails (Details details)
    {
        this.details = details;
    }

    public String getSeverity ()
    {
        return severity;
    }

    public void setSeverity (String severity)
    {
        this.severity = severity;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [details = "+details+", severity = "+severity+"]";
    }
}

