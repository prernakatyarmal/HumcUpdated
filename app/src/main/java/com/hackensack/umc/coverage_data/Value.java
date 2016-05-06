package com.hackensack.umc.coverage_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 1/18/2016.
 */
public class Value implements Serializable{
    public Value() {
    }
    /*"InsuranceName": "<<insurance name>>",
            "SubscriberName": "<<Name>>",
            "SubscriberDateOfBirth": "<<yyyy-mm-dd>>",
            "MemberNumber": "<<membernumber>>",

            "PatientId": "<<MRN>>"*/

    String InsuranceName;

    public Value(String insuranceName, String subscriberName, String subscriberDateOfBirth, String memberNumber, String patientId) {
        InsuranceName = insuranceName;
        SubscriberName = subscriberName;
        SubscriberDateOfBirth = subscriberDateOfBirth;
        MemberNumber = memberNumber;
        PatientId = patientId;
    }

    String SubscriberName;
    String SubscriberDateOfBirth;
    String MemberNumber;
    String PatientId;

    public String getInsuranceName() {
        return InsuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        InsuranceName = insuranceName;
    }

    public String getSubscriberName() {
        return SubscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        SubscriberName = subscriberName;
    }

    public String getSubscriberDateOfBirth() {
        return SubscriberDateOfBirth;
    }

    public void setSubscriberDateOfBirth(String subscriberDateOfBirth) {
        SubscriberDateOfBirth = subscriberDateOfBirth;
    }

    public String getMemberNumber() {
        return MemberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        MemberNumber = memberNumber;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }
}
