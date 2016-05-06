package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Text implements Serializable
{
    private String status;

    private String div;
    private String text;

    public Text(String status, String div) {
        this.status = status;
        this.div = div;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getDiv ()
    {
        return div;
    }

    public void setDiv (String div)
    {
        this.div = div;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", div = "+div+"]";
    }
}

