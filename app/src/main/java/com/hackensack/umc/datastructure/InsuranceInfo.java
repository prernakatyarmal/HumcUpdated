package com.hackensack.umc.datastructure;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by bhagyashree_kumawat on 10/14/2015.
 */
public class InsuranceInfo implements Serializable {
    private String planProvider;
    private String memberNumber;
    private String groupNumber;
    private String subscriberId;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public InsuranceInfo(String planProvider, String groupNumber, String memberNumber, String subscriberId, String subscriberDateOfBirth, String subscriberName) {
        this.planProvider = planProvider;
        this.groupNumber = groupNumber;
        this.memberNumber = memberNumber;
        this.subscriberId = subscriberId;
        this.subscriberDateOfBirth = subscriberDateOfBirth;
        this.subscriberName = subscriberName;

    }

    public InsuranceInfo() {
    }

    private String coverage;
    private String planType;

    // private String insuranceName = "INC1234";

    // private String group = "Grup123";
    //  private String subscriberId = "Sub1234";
    private String reference = "Patient/123";//MRN no of patient.
    private String subscriberName = "Test";
    private String subscriberDateOfBirth = "1980-05-30";

    /* public InsuranceData(String insuranceName, String dependent, String group, String subscriberId, String reference, String subscriberName, String subscriberDateOfBirth) {
         this.planProvider = insuranceName;
         this.dependent = dependent;
         this.grpNumber = group;
         this.memberId = subscriberId;
         this.reference = reference;
         this.subscriberName = subscriberName;
         this.subscriberDateOfBirth = subscriberDateOfBirth;
     }
 */
    @Override
    public String toString() {
        String returnStr = "";

               returnStr = TextUtils.isEmpty(planProvider)?"":("Provider=" + planProvider ) +(TextUtils.isEmpty(groupNumber)?"":(", Group#=" + groupNumber)) +
                        (TextUtils.isEmpty(subscriberId)?"":(", Subscriber#=" + subscriberId))+
                        (TextUtils.isEmpty(memberNumber)?"":( ", Member#=" + memberNumber ));
                        /* (TextUtils.isEmpty(coverage)?"":(", Coverage=" + coverage)) +
                        (TextUtils.isEmpty(planType)?"":(", PlanType=" + planType))*/
        return returnStr;
    }

   /* public InsuranceData(String planProvider, String memberId, String grpName, String grpNumber, String coverage, String planType) {
        this.planProvider = planProvider;
        this.memberId = memberId;
        this.grpName = grpName;
        this.grpNumber = grpNumber;
        this.coverage = coverage;
        this.planType = planType;
    }*/

    public String getPlanProvider() {
        return planProvider;
    }

    public void setPlanProvider(String planProvider) {
        this.planProvider = planProvider;
    }


    public String getGrpNumber() {
        return groupNumber;
    }

    public void setGrpNumber(String grpNumber) {
        this.groupNumber = grpNumber;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getSubscriberDateOfBirth() {
        return subscriberDateOfBirth;
    }

    public void setSubscriberDateOfBirth(String subscriberDateOfBirth) {
        this.subscriberDateOfBirth = subscriberDateOfBirth;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }
}

