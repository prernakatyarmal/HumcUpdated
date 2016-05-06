package com.hackensack.umc.response;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/28/2015.
 */
public class CardResponse implements Serializable {

    private String TemplateType;

    private String Text1;

    private String Text2;

    private String RXID;

    private String Text3;

    private String EffectiveDate;

    private String Deductible;

    private String RXBin;

    private String LastName;

    private String PlanType;

    private String HairColor;

    private String ExpirationDate;

    private String Coverage;

    private String Weight;

    private String CSC;

    private String Address3;

    private String Address2;

    private String ID;

    private String CopayUC;

    private String Sex;

    private String GroupName;

    private String Suffix;

    private String PlanAdmin;

    private String FatherName;

    private String Address4;

    private String Other;

    private String Address5;

    private String Height;

    private String Address6;

    private String Prefix;

    private String Country;

    private String MotherName;

    private String Nationality;

    private String DateOfBirth;

    private String CopayOV;


    private String RXPCN;

    private String RXGRoup;

    private String PlanProvider;

    private String MemberName;

    private String PayerID;

    private String IssueDate;

    private String Type;

    private String CopaySP;

    private String PassportNumber;

    private String EyeColor;

    private String Endorsements;

    private String FirstName;

    private String Zip;

    private String PlaceOfIssue;

    private String PersonalNumber;

    private String CopayER;

    private String RawText;

    private String PlaceOfBirth;

    private String MemberID;

    private String City;

    private String ContractCode;

    private String GroupNumber;

    private String IssuerNumber;

    private String MiddleName;

    private String State;

    private String License;

    private String Restrictions;

    private String Address;

    private String Employer;


   ////
   /* [{
        "Address": "1 FOX HOLLOW OP",
                "Address2": null,
                "Address3": null,
                "Address4": null,
                "Address5": null,
                "Address6": null,
                "City": "DALLAS",
                "Class": "",
                "ContractCode": null,
                "CopayER": null,
                "CopayOV": null,
                "CopaySP": null,
                "CopayUC": null,
                "Country": "United States",
                "Coverage": null,
                "CSC": null,
                "DateOfBirth": "12-05-1957",
                "Deductible": null,
                "EffectiveDate": null,
                "Employer": null,
                "Endorsements": "",
                "ExpirationDate": "",
                "EyeColor": "",
                "FatherName": null,
                "FirstName": "CHARLES",
                "GroupName": null,
                "GroupNumber": null,
                "HairColor": "",
                "Height": "",
                "ID": "0384",
                "IssueDate": "-02-2000",
                "IssuerNumber": null,
                "LastName": "CHEEKS",
                "License": "0384",
                "MemberID": null,
                "MemberName": null,
                "MiddleName": "J",
                "MotherName": null,
                "Nationality": null,
                "Other": null,
                "PassportNumber": null,
                "PayerID": null,
                "PersonalNumber": null,
                "PlaceOfBirth": null,
                "PlaceOfIssue": null,
                "PlanAdmin": null,
                "PlanProvider": null,
                "PlanType": null,
                "Prefix": null,
                "RawText": null,
                "Restrictions": "",
                "RXBin": null,
                "RXGRoup": null,
                "RXID": null,
                "RXPCN": null,
                "Sex": "C",
                "State": "PA",
                "Suffix": "",
                "TemplateType": null,
                "Text1": null,
                "Text2": null,
                "Text3": null,
                "Type": null,
                "Weight": "",
                "Zip": "18612"
    }, {
        "Address": null,
                "Address2": null,
                "Address3": null,
                "Address4": null,
                "Address5": null,
                "Address6": null,
                "City": null,
                "Class": null,
                "ContractCode": "",
                "CopayER": "",
                "CopayOV": "41",
                "CopaySP": "",
                "CopayUC": "",
                "Country": null,
                "Coverage": "",
                "CSC": null,
                "DateOfBirth": "",
                "Deductible": "",
                "EffectiveDate": "",
                "Employer": "",
                "Endorsements": null,
                "ExpirationDate": "",
                "EyeColor": null,
                "FatherName": null,
                "FirstName": "N",
                "GroupName": "",
                "GroupNumber": "GH-S90",
                "HairColor": null,
                "Height": null,
                "ID": null,
                "IssueDate": null,
                "IssuerNumber": "",
                "LastName": "Cadbefly Avenue",
                "License": null,
                "MemberID": "MA171223220",
                "MemberName": null,
                "MiddleName": null,
                "MotherName": null,
                "Nationality": null,
                "Other": "",
                "PassportNumber": null,
                "PayerID": "",
                "PersonalNumber": null,
                "PlaceOfBirth": null,
                "PlaceOfIssue": null,
                "PlanAdmin": "",
                "PlanProvider": "",
                "PlanType": "",
                "Prefix": null,
                "RawText": null,
                "Restrictions": null,
                "RXBin": "",
                "RXGRoup": "",
                "RXID": "",
                "RXPCN": "",
                "Sex": null,
                "State": null,
                "Suffix": null,
                "TemplateType": null,
                "Text1": null,
                "Text2": null,
                "Text3": null,
                "Type": null,
                "Weight": null,
                "Zip": null
    }]
*/

    /////







    public String getTemplateType() {
        return TemplateType;
    }

    public void setTemplateType(String TemplateType) {
        this.TemplateType = TemplateType;
    }

    public String getText1() {
        return Text1;
    }

    public void setText1(String Text1) {
        this.Text1 = Text1;
    }

    public String getText2() {
        return Text2;
    }

    public void setText2(String Text2) {
        this.Text2 = Text2;
    }

    public String getRXID() {
        return RXID;
    }

    public void setRXID(String RXID) {
        this.RXID = RXID;
    }

    public String getText3() {
        return Text3;
    }

    public void setText3(String Text3) {
        this.Text3 = Text3;
    }

    public String getEffectiveDate() {
        return EffectiveDate;
    }

    public void setEffectiveDate(String EffectiveDate) {
        this.EffectiveDate = EffectiveDate;
    }

    public String getDeductible() {
        return Deductible;
    }

    public void setDeductible(String Deductible) {
        this.Deductible = Deductible;
    }

    public String getRXBin() {
        return RXBin;
    }

    public void setRXBin(String RXBin) {
        this.RXBin = RXBin;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getPlanType() {
        return PlanType;
    }

    public void setPlanType(String PlanType) {
        this.PlanType = PlanType;
    }

    public String getHairColor() {
        return HairColor;
    }

    public void setHairColor(String HairColor) {
        this.HairColor = HairColor;
    }

    public String getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(String ExpirationDate) {
        this.ExpirationDate = ExpirationDate;
    }

    public String getCoverage() {
        return Coverage;
    }

    public void setCoverage(String Coverage) {
        this.Coverage = Coverage;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

    public String getCSC() {
        return CSC;
    }

    public void setCSC(String CSC) {
        this.CSC = CSC;
    }

    public String getAddress3() {
        return Address3;
    }

    public void setAddress3(String Address3) {
        this.Address3 = Address3;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCopayUC() {
        return CopayUC;
    }

    public void setCopayUC(String CopayUC) {
        this.CopayUC = CopayUC;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getSuffix() {
        return Suffix;
    }

    public void setSuffix(String Suffix) {
        this.Suffix = Suffix;
    }

    public String getPlanAdmin() {
        return PlanAdmin;
    }

    public void setPlanAdmin(String PlanAdmin) {
        this.PlanAdmin = PlanAdmin;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String FatherName) {
        this.FatherName = FatherName;
    }

    public String getAddress4() {
        return Address4;
    }

    public void setAddress4(String Address4) {
        this.Address4 = Address4;
    }

    public String getOther() {
        return Other;
    }

    public void setOther(String Other) {
        this.Other = Other;
    }

    public String getAddress5() {
        return Address5;
    }

    public void setAddress5(String Address5) {
        this.Address5 = Address5;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String Height) {
        this.Height = Height;
    }

    public String getAddress6() {
        return Address6;
    }

    public void setAddress6(String Address6) {
        this.Address6 = Address6;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String Prefix) {
        this.Prefix = Prefix;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String MotherName) {
        this.MotherName = MotherName;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String Nationality) {
        this.Nationality = Nationality;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    public String getCopayOV() {
        return CopayOV;
    }

    public void setCopayOV(String CopayOV) {
        this.CopayOV = CopayOV;
    }

    public String getRXPCN() {
        return RXPCN;
    }

    public void setRXPCN(String RXPCN) {
        this.RXPCN = RXPCN;
    }

    public String getRXGRoup() {
        return RXGRoup;
    }

    public void setRXGRoup(String RXGRoup) {
        this.RXGRoup = RXGRoup;
    }

    public String getPlanProvider() {
        return PlanProvider;
    }

    public void setPlanProvider(String PlanProvider) {
        this.PlanProvider = PlanProvider;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public String getPayerID() {
        return PayerID;
    }

    public void setPayerID(String PayerID) {
        this.PayerID = PayerID;
    }

    public String getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(String IssueDate) {
        this.IssueDate = IssueDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getCopaySP() {
        return CopaySP;
    }

    public void setCopaySP(String CopaySP) {
        this.CopaySP = CopaySP;
    }

    public String getPassportNumber() {
        return PassportNumber;
    }

    public void setPassportNumber(String PassportNumber) {
        this.PassportNumber = PassportNumber;
    }

    public String getEyeColor() {
        return EyeColor;
    }

    public void setEyeColor(String EyeColor) {
        this.EyeColor = EyeColor;
    }

    public String getEndorsements() {
        return Endorsements;
    }

    public void setEndorsements(String Endorsements) {
        this.Endorsements = Endorsements;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String Zip) {
        this.Zip = Zip;
    }

    public String getPlaceOfIssue() {
        return PlaceOfIssue;
    }

    public void setPlaceOfIssue(String PlaceOfIssue) {
        this.PlaceOfIssue = PlaceOfIssue;
    }

    public String getPersonalNumber() {
        return PersonalNumber;
    }

    public void setPersonalNumber(String PersonalNumber) {
        this.PersonalNumber = PersonalNumber;
    }

    public String getCopayER() {
        return CopayER;
    }

    public void setCopayER(String CopayER) {
        this.CopayER = CopayER;
    }

    public String getRawText() {
        return RawText;
    }

    public void setRawText(String RawText) {
        this.RawText = RawText;
    }

    public String getPlaceOfBirth() {
        return PlaceOfBirth;
    }

    public void setPlaceOfBirth(String PlaceOfBirth) {
        this.PlaceOfBirth = PlaceOfBirth;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getContractCode() {
        return ContractCode;
    }

    public void setContractCode(String ContractCode) {
        this.ContractCode = ContractCode;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String GroupNumber) {
        this.GroupNumber = GroupNumber;
    }

    public String getIssuerNumber() {
        return IssuerNumber;
    }

    public void setIssuerNumber(String IssuerNumber) {
        this.IssuerNumber = IssuerNumber;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String MiddleName) {
        this.MiddleName = MiddleName;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getLicense() {
        return License;
    }

    public void setLicense(String License) {
        this.License = License;
    }

    public String getRestrictions() {
        return Restrictions;
    }

    public void setRestrictions(String Restrictions) {
        this.Restrictions = Restrictions;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getEmployer() {
        return Employer;
    }

    public void setEmployer(String Employer) {
        this.Employer = Employer;
    }

    @Override
    public String toString() {
        return "CardResponse [TemplateType = " + TemplateType + ", Text1 = " + Text1 + ", Text2 = " + Text2 + ", RXID = " + RXID + ", Text3 = " + Text3 + ", EffectiveDate = " + EffectiveDate + ", Deductible = " + Deductible + ", RXBin = " + RXBin + ", LastName = " + LastName + ", PlanType = " + PlanType + ", HairColor = " + HairColor + ", ExpirationDate = " + ExpirationDate + ", Coverage = " + Coverage + ", Weight = " + Weight + ", CSC = " + CSC + ", Address3 = " + Address3 + ", Address2 = " + Address2 + ", ID = " + ID + ", CopayUC = " + CopayUC + ", Sex = " + Sex + ", GroupName = " + GroupName + ", Suffix = " + Suffix + ", PlanAdmin = " + PlanAdmin + ", FatherName = " + FatherName + ", Address4 = " + Address4 + ", Other = " + Other + ", Address5 = " + Address5 + ", Height = " + Height + ", Address6 = " + Address6 + ", Prefix = " + Prefix + ", Country = " + Country + ", MotherName = " + MotherName + ", Nationality = " + Nationality + ", DateOfBirth = " + DateOfBirth + ", CopayOV = " + CopayOV + ",  RXPCN = " + RXPCN + ", RXGRoup = " + RXGRoup + ", PlanProvider = " + PlanProvider + ", MemberName = " + MemberName + ", PayerID = " + PayerID + ", IssueDate = " + IssueDate + ", Type = " + Type + ", CopaySP = " + CopaySP + ", PassportNumber = " + PassportNumber + ", EyeColor = " + EyeColor + ", Endorsements = " + Endorsements + ", FirstName = " + FirstName + ", Zip = " + Zip + ", PlaceOfIssue = " + PlaceOfIssue + ", PersonalNumber = " + PersonalNumber + ", CopayER = " + CopayER + ", RawText = " + RawText + ", PlaceOfBirth = " + PlaceOfBirth + ", MemberID = " + MemberID + ", City = " + City + ", ContractCode = " + ContractCode + ", GroupNumber = " + GroupNumber + ", IssuerNumber = " + IssuerNumber + ", MiddleName = " + MiddleName + ", State = " + State + ", License = " + License + ", Restrictions = " + Restrictions + ", Address = " + Address + ", Employer = " + Employer + "]";
    }


}
