package com.hackensack.umc.datastructure;

/**
 * Created by prerana_katyarmal on 10/24/2015.
 */
public class PatientData {
    private String patientId;
    private String patientIdType;
    private String loginId;
    private String password;
    private String passwordResetQuestion;
    private String passwordResetAnswer;
    private String emailAddress;
    private String gender;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    private String firstName;
    private String lastName;
    private String dateOfBirth;

    public PatientData(String patientId, String patientIdType, String loginId, String passwordResetQuestion, String password, String passwordResetAnswer, String emailAddress, String firstName, String lastName, String dateOfBirth, String receiveEmailNotifications) {
        this.patientId = patientId;
        this.patientIdType = patientIdType;
        this.loginId = loginId;
        this.passwordResetQuestion = passwordResetQuestion;
        this.password = password;
        this.passwordResetAnswer = passwordResetAnswer;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.receiveEmailNotifications = receiveEmailNotifications;
    }

    public PatientData() {

    }

    public PatientData(String patientId, String patientIdType, String loginId, String passwordResetQuestion, String passwordResetAnswer, String emailAddress, String receiveEmailNotifications, String password) {
        this.patientId = patientId;
        this.patientIdType = patientIdType;
        this.loginId = loginId;
        this.passwordResetQuestion = passwordResetQuestion;
        this.passwordResetAnswer = passwordResetAnswer;
        this.emailAddress = emailAddress;
        this.receiveEmailNotifications = receiveEmailNotifications;
        this.password = password;
    }

    private String receiveEmailNotifications;

    public void setGender(String gender) {
        this.gender = gender;
    }
}