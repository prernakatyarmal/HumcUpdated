package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Coding implements Serializable
{
    private String system;

    private String display;

    private String code;

    public Coding(String system, String code) {
        this.system = system;
        this.code = code;
    }

    public Coding(String system, String code, String display) {
        this.system = system;
        this.code = code;
        this.display = display;

    }

    public String getSystem ()
    {
        return system;
    }

    public void setSystem (String system)
    {
        this.system = system;
    }

    public String getDisplay ()
    {
        return display;
    }

    public void setDisplay (String display)
    {
        this.display = display;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [system = "+system+", display = "+display+", code = "+code+"]";
    }
}

