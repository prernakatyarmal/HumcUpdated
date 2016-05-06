package com.hackensack.umc.patient_data;

import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Subscriber
{

    private String reference;
    private ArrayList<Extension> extension;

    public Subscriber(String referance, ArrayList<Extension> extension) {
        this.reference = referance;
        this.extension = extension;
    }

    public Subscriber(ArrayList<Extension> extensions) {
        this.extension=extensions;
    }

    public Subscriber(String reference) {
        this.reference=reference;
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

