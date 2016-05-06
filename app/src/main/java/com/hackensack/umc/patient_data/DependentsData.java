package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 2/22/2016.
 */
public class DependentsData implements Serializable{
    private String dateOfBirth;

    private String loginId;

    private String sex;

    private String state;

    private String houseNumber;

    private String password;

    private String homePhone;

    private String hashedPassword;

    private String city;

    private String postalCode;

    private String webUserIDType;

    private String workPhone;

    private String firstName;

    private String district;

    private String middleName;

    private String webUserID;

    private String lastName;

    private String nationalIdentifier;

    private String emailAddress;

    private String passwordResetQuestion;

    private String country;

    private String mobilePhone;

    private String address;

    private String county;

    public DependentsData(String emailAddress, String loginId, String passwordResetAnswer, String password, String passwordResetQuestion, String nationalIdentifier, String receiveEmailNotifications, String lastName, String firstName, String dateOfBirth) {
        this.emailAddress = emailAddress;
        this.loginId = loginId;
        this.passwordResetAnswer = passwordResetAnswer;
        this.password = password;
        this.passwordResetQuestion = passwordResetQuestion;
        this.nationalIdentifier = nationalIdentifier;
        this.receiveEmailNotifications = receiveEmailNotifications;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
    }

    private String receiveEmailNotifications;

    private String passwordResetAnswer;

//alueForKey:@"Email"], @"loginId", _passwordTextField.text, @"password", @"Favorite game?",
// @"passwordResetQuestion", @"cricket", @"passwordResetAnswer",
// [appDelegate.personalInfoDictionary valueForKey:@"Email"],
// @"emailAddress",@"true", @"receiveEmailNotifications",
// [appDelegate.personalInfoDictionary valueForKey:@"LastName"],'
// '@"lastName", [appDelegate.personalInfoDictionary valueForKey:@"FirstName"],
// @"firstName",birthdate, @"dateOfBirth",@"", @"nationalIdentifier",nil];
    public DependentsData(String dateOfBirth, String loginId, String state, String sex, String houseNumber, String password, String homePhone, String city, String hashedPassword, String postalCode, String webUserIDType, String firstName, String workPhone, String district, String middleName, String webUserID, String lastName, String nationalIdentifier, String emailAddress, String passwordResetQuestion, String country, String mobilePhone, String address, String county, String receiveEmailNotifications, String passwordResetAnswer) {
        this.dateOfBirth = dateOfBirth;
        this.loginId = loginId;
        this.state = state;
        this.sex = sex;
        this.houseNumber = houseNumber;
        this.password = password;
        this.homePhone = homePhone;
        this.city = city;
        this.hashedPassword = hashedPassword;
        this.postalCode = postalCode;
        this.webUserIDType = webUserIDType;
        this.firstName = firstName;
        this.workPhone = workPhone;
        this.district = district;
        this.middleName = middleName;
        this.webUserID = webUserID;
        this.lastName = lastName;
        this.nationalIdentifier = nationalIdentifier;
        this.emailAddress = emailAddress;
        this.passwordResetQuestion = passwordResetQuestion;
        this.country = country;

        this.mobilePhone = mobilePhone;
        this.address = address;
        this.county = county;
        this.receiveEmailNotifications = receiveEmailNotifications;
        this.passwordResetAnswer = passwordResetAnswer;
    }
//ionaryWithObjectsAndKeys:[appDelegate.personalInfoDictionary v
//All information of parents

    public String getDateOfBirth ()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth (String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLoginId ()
    {
        return loginId;
    }

    public void setLoginId (String loginId)
    {
        this.loginId = loginId;
    }

    public String getSex ()
    {
        return sex;
    }

    public void setSex (String sex)
    {
        this.sex = sex;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getHouseNumber ()
    {
        return houseNumber;
    }

    public void setHouseNumber (String houseNumber)
    {
        this.houseNumber = houseNumber;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getHomePhone ()
    {
        return homePhone;
    }

    public void setHomePhone (String homePhone)
    {
        this.homePhone = homePhone;
    }

    public String getHashedPassword ()
    {
        return hashedPassword;
    }

    public void setHashedPassword (String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getPostalCode ()
    {
        return postalCode;
    }

    public void setPostalCode (String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getWebUserIDType ()
    {
        return webUserIDType;
    }

    public void setWebUserIDType (String webUserIDType)
    {
        this.webUserIDType = webUserIDType;
    }

    public String getWorkPhone ()
    {
        return workPhone;
    }

    public void setWorkPhone (String workPhone)
    {
        this.workPhone = workPhone;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getDistrict ()
    {
        return district;
    }

    public void setDistrict (String district)
    {
        this.district = district;
    }

    public String getMiddleName ()
    {
        return middleName;
    }

    public void setMiddleName (String middleName)
    {
        this.middleName = middleName;
    }

    public String getWebUserID ()
    {
        return webUserID;
    }

    public void setWebUserID (String webUserID)
    {
        this.webUserID = webUserID;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getNationalIdentifier ()
    {
        return nationalIdentifier;
    }

    public void setNationalIdentifier (String nationalIdentifier)
    {
        this.nationalIdentifier = nationalIdentifier;
    }

    public String getEmailAddress ()
    {
        return emailAddress;
    }

    public void setEmailAddress (String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getPasswordResetQuestion ()
    {
        return passwordResetQuestion;
    }

    public void setPasswordResetQuestion (String passwordResetQuestion)
    {
        this.passwordResetQuestion = passwordResetQuestion;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getMobilePhone ()
    {
        return mobilePhone;
    }

    public void setMobilePhone (String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getCounty ()
    {
        return county;
    }

    public void setCounty (String county)
    {
        this.county = county;
    }

    public String getReceiveEmailNotifications ()
    {
        return receiveEmailNotifications;
    }

    public void setReceiveEmailNotifications (String receiveEmailNotifications)
    {
        this.receiveEmailNotifications = receiveEmailNotifications;
    }

    public String getPasswordResetAnswer ()
    {
        return passwordResetAnswer;
    }

    public void setPasswordResetAnswer (String passwordResetAnswer)
    {
        this.passwordResetAnswer = passwordResetAnswer;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [dateOfBirth = "+dateOfBirth+", loginId = "+loginId+", sex = "+sex+", state = "+state+", houseNumber = "+houseNumber+", password = "+password+", homePhone = "+homePhone+", hashedPassword = "+hashedPassword+", city = "+city+", postalCode = "+postalCode+", webUserIDType = "+webUserIDType+", workPhone = "+workPhone+", firstName = "+firstName+", district = "+district+", middleName = "+middleName+", webUserID = "+webUserID+", lastName = "+lastName+", nationalIdentifier = "+nationalIdentifier+", emailAddress = "+emailAddress+", passwordResetQuestion = "+passwordResetQuestion+", country = "+country+", mobilePhone = "+mobilePhone+", address = "+address+", county = "+county+", receiveEmailNotifications = "+receiveEmailNotifications+", passwordResetAnswer = "+passwordResetAnswer+"]";
    }
}

