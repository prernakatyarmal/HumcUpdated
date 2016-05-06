package com.hackensack.umc.patient_data;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class ManagingOrganization
{
    public ManagingOrganization(String display, String reference) {
        this.display = display;
        this.reference = reference;
    }

    private String display;

    private String reference;

    public String getDisplay ()
    {
        return display;
    }

    public void setDisplay (String display)
    {
        this.display = display;
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
        return "ClassPojo [display = "+display+", reference = "+reference+"]";
    }
}


