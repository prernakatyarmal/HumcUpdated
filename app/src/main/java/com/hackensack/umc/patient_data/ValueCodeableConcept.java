package com.hackensack.umc.patient_data;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class ValueCodeableConcept
{
    private Coding coding;

    public ValueCodeableConcept(Coding coding) {
        this.coding = coding;
    }

    public Coding getCoding ()
    {
        return coding;
    }

    public void setCoding (Coding coding)
    {
        this.coding = coding;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [coding = "+coding+"]";
    }
}

