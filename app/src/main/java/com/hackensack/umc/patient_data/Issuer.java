package com.hackensack.umc.patient_data;

import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Issuer
{
    private ArrayList<Extension> extension;

    public Issuer(ArrayList<Extension> extension) {
        this.extension = extension;
    }

    public ArrayList<Extension> getExtension ()
    {
        return extension;
    }

    public void setExtension (ArrayList<Extension> extension)
    {
        this.extension = extension;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [extension = "+extension+"]";
    }
}

