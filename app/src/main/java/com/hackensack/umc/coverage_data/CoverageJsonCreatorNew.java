package com.hackensack.umc.coverage_data;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hackensack.umc.patient_data.Issuer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 1/18/2016.
 */
public class CoverageJsonCreatorNew implements Serializable {
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
    private String subscriberDateOfBirth = "";

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

    public CoverageJsonCreatorNew(String insuranceName, String dependent, String group, String subscriberId, String reference, String subscriberName, String subscriberDateOfBirth) {
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


        CoverageDataNew resource = new CoverageDataNew(resourceType);

        if (!TextUtils.isEmpty(Group)) {
            resource.setGroup(Group);
        }

        if (!TextUtils.isEmpty(subscriberId)) {
            resource.setSubscriberId(new SubscriberId("office", subscriberId));
        }

        resource.setExtension(ceateExentionList());
        //  CoverageData(resourceType, issuer, dependent, Group, subscriberId, subscriber);

        Gson gson = new Gson();
        covarageData = gson.toJson(resource);
        JSONObject coverageObject = null;
        try {
            coverageObject = new JSONObject(covarageData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("Coverage Object", coverageObject.toString());
        return coverageObject;
    }

    private ArrayList<Extension> ceateExentionList() {
        ArrayList<Extension> extension = new ArrayList<>();
        String url = "SearchTag";
        ArrayList<Value> value = new ArrayList<>();
        Value value1 = new Value();

      /*  InsuranceName": "<<insurance name>>",
        "SubscriberName": "<<Name>>",
                "SubscriberDateOfBirth": "<<yyyy-mm-dd>>",
                "MemberNumber": "<<membernumber>>",

                "PatientId": "<<MRN>>"*/
        if (!TextUtils.isEmpty(insuranceName)) {
            value1.setInsuranceName(insuranceName);
        }
        if (!TextUtils.isEmpty(subscriberName)) {
            value1.setSubscriberName(subscriberName);
        }
        if (!TextUtils.isEmpty(subscriberDateOfBirth)) {
            value1.setSubscriberDateOfBirth(subscriberDateOfBirth);
        }
        if (!TextUtils.isEmpty(dependent)) {
            value1.setMemberNumber(dependent);
        }
        if (!TextUtils.isEmpty(reference)) {
            value1.setPatientId(reference);
        }
        value.add(value1);

        extension.add(new Extension(url, value));
        return extension;
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

}

/*{
    "resourceType": "Coverage",
	"group": "<<group number>>",
	"subscriberId": {
		"use": "office",
		"value": "<<SubscriberID>>"
	},
	"extension": [{
		"url": "SearchTag",
		"value": [{
			"InsuranceName": "<<insurance name>>",
			"SubscriberName": "<<Name>>",
			"SubscriberDateOfBirth": "<<yyyy-mm-dd>>",
			"MemberNumber": "<<membernumber>>",
			"PatientId": "<<MRN>>"

		}]
	}]
}*/
///My call for new covrage:
//{
        /*"extension": [{
        "value": [{
        "InsuranceName": "test",
        "PatientId": "Patient\/900703172",
        "SubscriberName": "CHARLES  CHESKIEWICZ ",
        "SubscriberDateOfBirth": "1953-12-19"
        }],
        "url": "SearchTag"
        }],
        "subscriberId": {
        "value": "56y7",
        "use": "office"
        },
        "resourceType": "Coverage"
        }*/

//Response::
/*
Insurance response::{"subscriberPatient":null,"subPlan":null,"dependent":null,"class":"com.mphrx.consus.resources.Coverage","type":null,"period":null,"network":null,"resourceType":"Coverage","extension":[{"value":[{"SubscriberDateOfBirth":"1953-12-19","InsuranceName":"test","PatientId":"Patient/900703172","SubscriberName":"CHARLES  CHESKIEWICZ "}],"url":"SearchTag"}],"id":53,"issuer":null,"plan":null,"sequence":null,"subscriberId":{"extension":null,"id":null,"system":null,"value":"56y7",
        "use":"office","label":null,"useCode":"office","period":null,"type":null},"bin":null,"group":null,"identifier":null}*/
