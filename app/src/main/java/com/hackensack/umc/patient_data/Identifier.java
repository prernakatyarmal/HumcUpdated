package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Identifier implements Serializable{
    private String system;

    private String value;

    String use;
    Type type;
    Assigner aassAssigner;

    public Identifier(String system, String value, String use, Type type, Assigner aassAssigner) {
        this.system = system;
        this.value = value;
        this.use = use;
        this.type = type;
        this.aassAssigner = aassAssigner;
    }



    /*identifier":[

    {
        "use":"usual",
            "type":{
        "coding":[
        {
            "system":"http://hl7.org/fhir/v2/0203",
                "code":"DL"
        }
        ]
    },
        "system":"urn:oid:1.2.36.146.595.217.0.1",
            "value":"12345",
            "period":{
        "start":"2001-05-06"
    },
        "assigner":{
        "display":"Government"
    }
    }
    ],*/

    public Identifier(String system, String value) {
        this.system = system;
        this.value = value;
    }

    public Identifier(String use, Type type, String drivingLicence) {
        this.use=use;
        this.type=type;
        this.value=drivingLicence;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ClassPojo [system = " + system + ", value = " + value + "]";
    }
}
