package com.hackensack.umc.http;

/**
 * Created by prerana_katyarmal on 10/13/2015.
 */
public class ServerConstants {

    public static final String BASE_URL = "https://sandbox.datamotion.com:4334/idvs/";
    public static final String URL_TOKEN = BASE_URL + "token";


    public static final String URL_SUBMITINFO = BASE_URL
            + "api/IdValidation/SubmitIdInfo";
    public static final String SERVER_PARA_GRANT_TYPE = "grant_type";
    public static final String SERVER_PARA_USER_NAME = "username";
    public static final String SERVER_PARA_PASSWORD = "password";
    public static final String URL_SUBMITKBA = "api/IdValidation/SubmitKBA";
    public static final int REQ_CODE_TOKEN_REQUEST = 1;

    public static final String BASE_STARTS="https://hackensackumc-test.apigee.net/";
    public static final String BASE_STARTS_PROD="https://hackensackumc-prod.apigee.net/";
    public static final String BASE_URL_EPIC=BASE_STARTS+"v1/fhir/";

    public static final String URL_READ_PATIEONT = BASE_URL_EPIC+"Patient/900702012?_format=json";
    public static final String URL_TOKEN_FOR_EPIC = BASE_STARTS+"v1/oauth/token?grant_type=client_credentials";
    public static final String URL_SEND_PATIENT = BASE_URL_EPIC+"Patient?_format=json";
    public static final String ACCESS_TOKEN_EPIC ="access_token_manual" ;
    public static final String URL_CREATE_ACCOUNT=BASE_STARTS+"v1/api/Account";
    public static final String URL_CREATE_ACCOUNT_DEPENDENT=BASE_STARTS+"/v1/api/NonPatientAccount";
    public static final String URL_SUBMIT_KBA="https://sandbox.datamotion.com:4334/idvs/api/IdValidation/SubmitKBA";
    public static final String URL_SEND_CARD_IMAGES =BASE_URL+"api/IdValidation/ExtractIdInfo" ;
   // public static final String URL_GET_COVERAGE_DETAILS =BASE_URL_EPIC+"Coverage?patientId=" ;
    public static final String URL_GET_COVERAGE_DETAILS =BASE_URL_EPIC+"Coverage?patient=" ;
    public static final String URL_SEND_COVERAGE =BASE_URL_EPIC+"Coverage" ;
    public static final String URL_GET_CITY_DETAILS =BASE_STARTS+"v1/api/zip/" ;

    public static final String CREDENTIAL_FOR_EPIC_TOKEN ="Basic UUJHN0htdVgyOFpMQm80WnRQODNzRE5SS2ZyY3lBS006Y252cHJ6RlB6T2dieFhLQQ==";
    public static final String USER_NAME_FOR_DATAMOTION_ACCESSTOKEN = "charles_cheskiewicz@persistent.com";
    public static final String PASSWORD_FOR_DATAMOTION_ACCESSTOKEN = "oPxvapvsqO$f6QEuhY0DV";
    public static final String ACCESS_TOKEN_DATA_MOTION = "access_tokaen_datamotion";
    public static final String GET_CREDENTIALS_FOR_ACCESS_TOKEN =
            BASE_STARTS+"v1/api/credentials?client=datamotion";//https://hackensackumc-prod.apigee.net/v1/api/credentials?client=datamotion

    /*Other URLS*/
    public static final String PRACTITIONER_BASE_SERVER = BASE_STARTS+"v1/api";


    //    public static final String PRACTITIONER_TIME_SLOT_URL	= "https://hackensack-prod.apigee.net/v2/fhir/Slot?schedule.actor=1770544504&_include:recurse=Slot:schedule";
//    public static final String PRACTITIONER_TIME_SLOT_URL	= "https://hackensack-prod.apigee.net/v2/fhir/Slot?schedule.actor=1730341322&_include:recurse=Slot:schedule";
    public static final String PRACTITIONER_TIME_SLOT_URL = BASE_STARTS+"v1/fhir/Slot?schedule.actor=";
    public static final String PRACTITIONER_TIME_SLOT_URL_PART = "&_include:recurse=Slot:schedule";

    public static final String LOGIN_URL = BASE_STARTS+"v1/oauth/token?grant_type=password";
    public static final String LOGOUT_URL = BASE_STARTS+"v1/oauth/token";
    public static final String REFRESH_LOGIN_URL = BASE_STARTS+"v1/oauth/token?grant_type=refresh_token";

    public static final String SCHEDULE_APPOINTMENT_URL = BASE_STARTS+"v1/fhir/Appointment";


    public static final String READ_PATIENT_URL = BASE_STARTS+"v1/fhir/Patient/"; //900702012
    public static final String READ_PATIENT_URL_PART = "?_format=json";

    public static final String VIEW_APPOINTMENT_URL = BASE_STARTS+"v1/fhir/Appointment?patient=";

    public static final String CANCEL_APPOINTMENT_URL = BASE_STARTS+"v1/fhir/Appointment/";
    //public static String BASE_TOKEN = "MEtRWmJ2aU5iWXJnbUJteUFIVGhHN2pGdEdlYTBwNjQ6RW1pall1dmRBWHZDd25Rdg====";
    public static String BASE_TOKEN_URL = BASE_STARTS+"v1/oauth/token?grant_type=client_credentials";
    public static final String PRACTITIONER_URL = BASE_STARTS+"v1/api/Practitioner";
    public static final String SPECIALITY_URL = BASE_STARTS+"v1/api/Specialty";

    public static final String DOCTOR_EMAIL_URL = BASE_STARTS+"v1/fhir/Practitioner/_search";
    public static final String DOCTOR_ALLOW_URL = BASE_STARTS+"v1/fhir/Contract";
public static final String URL_GET_MRN_FOR_PROXY = BASE_STARTS + "v1/api/Identifier?";
}
