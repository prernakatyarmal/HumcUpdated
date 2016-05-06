package com.hackensack.umc.patient_data;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Other
{
    private String reference;

    public Other(String reference) {
        this.reference = reference;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [reference = "+reference+"]";
    }
}
