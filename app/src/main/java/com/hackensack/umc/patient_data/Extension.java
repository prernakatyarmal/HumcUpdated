package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Extension implements Serializable
{
    private String valueString;
    private ArrayList<ValueCodeableConcept>valueCodeableConcept;
   // private String reference;

    public Extension(ValueReference valueReference, String url) {
        this.valueReference = valueReference;
        this.url = url;
    }

    private ValueReference valueReference;

    public Extension(String valueString, String url, ArrayList<ValueCodeableConcept> valueCodeableConcept) {
        this.valueString = valueString;
        this.url = url;
        this.valueCodeableConcept = valueCodeableConcept;

    }

    public Extension(String url,ArrayList<ValueCodeableConcept> valueCodeableConcept) {
        this.valueCodeableConcept = valueCodeableConcept;
        this.url = url;
    }

    public Extension(String valueString, String url) {
        this.valueString = valueString;
        this.url = url;

    }

    private String url;


    public String getValueString ()
    {
        return valueString;
    }

    public void setValueString (String valueString)
    {
        this.valueString = valueString;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [valueString = "+valueString+", url = "+url+"]";
    }

}


