package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Type implements Serializable
{
    private ArrayList<Coding> coding;

    public Type(ArrayList<Coding> coding) {
        this.coding = coding;
    }

    public ArrayList<Coding> getCoding ()
    {
        return coding;
    }

    public void setCoding (ArrayList<Coding> coding)
    {
        this.coding = coding;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [coding = "+coding+"]";
    }
}
