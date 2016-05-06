package com.hackensack.umc.coverage_data;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Type
{
    private String system;

    private String display;

    private String code;

    public Type(String system, String display, String code) {
        this.system = system;
        this.display = display;
        this.code = code;
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


