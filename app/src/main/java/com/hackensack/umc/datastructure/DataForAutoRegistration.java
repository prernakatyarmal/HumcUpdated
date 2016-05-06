package com.hackensack.umc.datastructure;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/28/2015.
 */
public class DataForAutoRegistration implements Serializable {


    private String Text2;

    private String LastName;

    private String PlanType;

    private String Sex;

    private String GroupName;

    private String Other;


    private String Height;


    private String Country;


    private String DateOfBirth;


    private String PlanProvider;


    private String Type;


    private String FirstName;



    private String MemberID;

    private String City;


    private String GroupNumber;



    private String State;

    private String License;


    private String Address;

    private String cellNumber;
    private String workPhoneNumber;
    private String homePhoneNumber;
    private String planProvider;
    private String street1;
    private String street2;
    private String email;
    private String MemberName;
    private String Zip;


    @Override
    public String toString() {
        return "DataForAutoRegistration{" +
                "fName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", birthDate='" + getDateOfBirth() + '\'' +
                ", gender='" + getSex() + '\'' +
                ", licennse='" + getLicense() + '\'' +
                ", cellNumber='" + cellNumber + '\'' +
                ", workPhoneNumber='" + workPhoneNumber + '\'' +
                ", homePhoneNumber='" + homePhoneNumber + '\'' +
                ", planProvider='" + planProvider + '\'' +
                ", memberId='" + getMemberId() + '\'' +
                ", grpName='" + getGroupName() + '\'' +
                ", grpNumber='" + getGrpNumber() + '\'' +
                ", coverage='" + getPlanProvider() + '\'' +
                ", planType='" + getPlanType() + '\'' +
                ", street1='" + getAddress() + '\'' +
                ", street2='" + street2 + '\'' +
                ", state='" + getState() + '\'' +
                ", city='" + getCity() + '\'' +
                '}';
    }


    public DataForAutoRegistration() {
        super();
    }


 /*   protected DataForAutoRegistration(Parcel in) {
        fName = in.readString();
        lastName = in.readString();
        email = in.readString();
        birthDate = in.readString();
        gender = in.readString();
        licennse = in.readString();
        planProvider = in.readString();
        memberId = in.readString();
        grpName = in.readString();
        grpNumber = in.readString();
        coverage = in.readString();
        planType = in.readString();
        state = in.readString();
        city = in.readString();
    }*/

    public String getfName() {
        return FirstName;
    }

    public void setfName(String fName) {
        this.FirstName = fName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return DateOfBirth;
    }

    public void setBirthDate(String birthDate) {
        this.DateOfBirth = birthDate;
    }

    public String getGender() {
        return Sex;
    }

    public void setGender(String gender) {
        this.Sex = gender;
    }



    public String getLicennse() {
        return License;
    }

    public void setLicennse(String licennse) {
        this.License = licennse;
    }


    
    
    public String getPlanProvider() {
        return planProvider;
    }

    public void setPlanProvider(String planProvider) {
        this.planProvider = planProvider;
    }

    public String getMemberId() {
        return MemberID;
    }

    public void setMemberId(String memberId) {
        this.MemberID = memberId;
    }


    public void setGrpName(String grpName) {
        this.GroupName = grpName;
    }

    public String getGrpNumber() {
        return GroupNumber;
    }

    public void setGrpNumber(String grpNumber) {
        this.GroupNumber = grpNumber;
    }

    public String getCoverage() {
        return PlanProvider;
    }

    public void setCoverage(String coverage) {
        this.PlanProvider = coverage;
    }



    public String getPlanType() {
        return PlanType;
    }

    public void setPlanType(String planType) {
        this.PlanType = planType;
    }

    public String getStreet2() {
        return street2;
    }


    public String getState() {
        return State;
    }

    public void setState(String state) {
        this.State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

   

    public String getText2() {
        return Text2;
    }

    public void setText2(String text2) {
        Text2 = text2;
    }

   

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

 

    public String getOther() {
        return Other;
    }

    public void setOther(String other) {
        Other = other;
    }

    

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

   


    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

  

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

   

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

   

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

   

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }

  

    public String getLicense() {
        return License;
    }

    public void setLicense(String license) {
        License = license;
    }

    

    public String getAddress() {
        return Address;
    }

   

    public void setAddress(String address) {
        this.Address=address;
    }

    public void setGroupNumber(String groupNumber) {
        this.GroupNumber = groupNumber;
    }
}
