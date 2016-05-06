package com.hackensack.umc.patient_data;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Link
{
    private Other other;

    public Link(Other other, String type) {
        this.other = other;
        this.type = type;
    }

    private String type;

    public Other getOther ()
    {
        return other;
    }

    public void setOther (Other other)
    {
        this.other = other;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [other = "+other+", type = "+type+"]";
    }
}

