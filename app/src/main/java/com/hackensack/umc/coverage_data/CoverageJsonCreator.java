package com.hackensack.umc.coverage_data;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hackensack.umc.patient_data.Extension;
import com.hackensack.umc.patient_data.Identifier;
import com.hackensack.umc.patient_data.Issuer;
import com.hackensack.umc.patient_data.Subscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creat
 * ed by prerana_katyarmal on 10/27/2015.
 */
public class CoverageJsonCreator implements Serializable{
 /*   private String insuranceName = "INC1234";
    private String dependent = "123456";//member number value from app
    private String Group = "Grup123";
    private String subscriberId = "Sub1234";
    private String reference = "Patient/123";//MRN no of patient.
    private String subscriberName = "Test";
    private String subscriberDateOfBirth="1980-05-30";
    private String resourceType = "Coverage";
    private Issuer issuer;*/
    private String insuranceName = "";
    private String dependent = "";//member number value from app
    private String Group = "";
    private String subscriberId = "";
    private String reference = "";//MRN no of patient.
    private String subscriberName = "";
    private String subscriberDateOfBirth="";

    public String getInsuranceName() {
        return insuranceName;
    }

    private String resourceType = "Coverage";
    private Issuer issuer;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public CoverageJsonCreator(String insuranceName, String dependent, String group, String subscriberId, String reference, String subscriberName, String subscriberDateOfBirth) {
        this.insuranceName = insuranceName;
        this.dependent = dependent;
        this.Group = group;
        this.subscriberId = subscriberId;

        this.reference = reference;
        this.subscriberName = subscriberName;
        this.subscriberDateOfBirth = subscriberDateOfBirth;
    }

    public JSONObject createCoverageData() {
        String covarageData;
        issuer = new Issuer(createExtentionForIssuer(insuranceName));
        Subscriber subscriber=null;
        ArrayList<Extension> extensions = createExtensionListForSubScriber();
        if(extensions.size()>1&&!TextUtils.isEmpty(reference)){
            subscriber = new Subscriber(reference, extensions);
        }else{
            if(extensions.size()>1) {
                subscriber = new Subscriber(extensions);
            }else if(!TextUtils.isEmpty(reference)){
              subscriber = new Subscriber(reference);
            }
        }


        CoverageData resource = new CoverageData(resourceType, issuer);
        if(subscriber!=null){
            resource.setSubscriber(subscriber);
        }
        if(!TextUtils.isEmpty(dependent)) {
            resource.setDependent(dependent);
        }
        if(!TextUtils.isEmpty(Group)){
            resource.setGroup(Group);
        }

        if(!TextUtils.isEmpty(subscriberId)){
            resource.setSubscriberId(subscriberId);
        }

      //  CoverageData(resourceType, issuer, dependent, Group, subscriberId, subscriber);

        Gson gson = new Gson();
        covarageData = gson.toJson(resource);
        JSONObject coverageObject = null;
        try {
            coverageObject = new JSONObject(covarageData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("Coverage Object",coverageObject.toString());
        return coverageObject;
    }

    /*{
        "Group": "Grup123",
            "dependent": 123456,
            "issuer": {
        "extension": [
        {
            "url": "InsuranceName",
                "valueString": "INC1234"
        }
        ]
    },
        "resourceType": "Coverage",
            "subscriber": {
        "extension": [
        {
            "url": "SubscriberName",
                "valueString": "Test"
        },
        {
            "url": "SubscriberDateOfBirth",
                "valueString": "1980-05-30"
        }
        ],
        "reference": "patient/900702715"
    },
        "subscriberId": "Sub1234"
    }*/

    /*{ "resourceType": "Coverage",
            "issuer": {
        "extension": [
        {
            "url": "InsuranceName",
                "valueString": "INC1234"
        }
        ]
    },
        "dependent":123456,
            "Group": "Grup123",
            "subscriberId": "Sub1234",
            "subscriber": {
        "reference":"Patient/900702721",
                "extension": [
        {
            "url": "SubscriberName",
                "valueString": "Test"
        },
        {
            "url": "SubscriberDateOfBirth",
                "valueString": "1980-05-30"
        }
        ]
    }
    }*/
    /* {
        "resourceType": "Coverage",
            "issuer": {
        "extension": [
        {
            "url": "InsuranceName",
                "valueString": "INC1234" // Coverage Through value from app
        }
        ]
    },
        "dependent":123456,//member number value from app
            "Group": "Grup123",//group number value from app
            "subscriberId": "Sub1234",//subscriber ID from app
            "subscriber": {
        "reference":"Patient/123",// put MRN number after Patient/
                "extension": [
        {
            "url": "SubscriberName",
                "valueString": "Test" // Subscriber Name from app
        },
        {
            "url": "SubscriberDateOfBirth",
                "valueString": "1980-05-30" //Subscriber Date Of Birth from app
        }
        ]
    }
    }*/

    /*   public CoverageData coverageJsonObject() {
           JSONObject jsonCoverage = null;
           String subPlan = "123";
           String dependent = "123456";
           String subscriberId = "Sub1234";

           Period period = new Period("2011-05-23");
           String group = "Grup123";
           Type type = new Type("http: //hl7.org/fhir/v3/ActCode", "EHCPOL", "extendedhealthcare");
           String resourceType = "Coverage";
           Issuer issuer = new Issuer(createExtentionForIssuer());
           String sequence = "1";
           ArrayList<Identifier> identifiers = creatIdentifires();

           Request request = new Request("POST", "Coverage");

           // CoverageData resource=new CoverageData(text,subPlan,dependent,period,group,type,resourceType,issuer,sequence,subscribeId,subscriber,identifiers);

           String plan = "CBI35";
           Text text = new Text("generated", "<div>Ahuman-readablerenderingofthecoverage</div>");
           com.hackensack.umc.coverage_data.CoverageData resource = new CoverageData(resourceType, issuer, dependent, group, subscriberId, subscriber);
           //CoverageData(Text text, String subPlan, String dependent, Period period, String group, Type type, String resourceType, Issuer issuer,
           // String plan, String sequence, String subscriberId, Subscriber subscriber, ArrayList<Identifier> identifier)
           Gson gson = new Gson();
           String coverageResource = gson.toJson(resource);
           try {
               jsonCoverage = new JSONObject(coverageResource);
           } catch (JSONException e) {
               e.printStackTrace();
           }
           return resource;
       }
   */
    private ArrayList<Extension> createExtentionForIssuer(String insuranceName) {
        ArrayList<Extension> extensions = new ArrayList<>();
        extensions.add(new Extension(insuranceName, "InsuranceName"));

        return extensions;


    }

    private ArrayList<Extension> createExtensionListForSubScriber() {
        ArrayList<Extension> extensions = new ArrayList<>();
        if(!TextUtils.isEmpty(subscriberName)){
            extensions.add(new Extension(subscriberName, "SubscriberName"));
        }
       if(!TextUtils.isEmpty(subscriberDateOfBirth)) {
           extensions.add(new Extension(subscriberDateOfBirth, "SubscriberDateOfBirth"));
       }




        /*"extension":[
        {
            "url":"MemberName",
                "valueString":"Shilpa"
        },
        {
            "url":"SubscriberName ",
                "valueString":"shaha"
        },
        {
            "url":"SubscriberDOB",
                "valueString":"2011-05-23"
        }
*/

        return extensions;
    }

    private ArrayList<Identifier> creatIdentifires() {
        ArrayList<Identifier> identifiers = new ArrayList<>();


        Identifier idFir = new Identifier("http: //benefitsinc.com/certificate", "12345");


        return identifiers;
    }


}

