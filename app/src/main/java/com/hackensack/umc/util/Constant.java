package com.hackensack.umc.util;

/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public class Constant {

    public static final int DOCTOR_RESULT_DATA = 0;
    public static final int DOCTOR_SPECIALTY_DATA = 1;
    public static final int DOCTOR_IMAGE_DATA = 2;
    public static final int DOCTOR_OFFICE_DATA = 3;
    public static final int SYMPTOM_LIST_DATA_MALE = 4;
    public static final int SYMPTOM_LIST_DATA_FEMALE = 5;
    public static final int SYMPTOM_LIST_DATA_CHILD = 6;
    public static final int SYMPTOM_ITEM_DATA = 7;
    public static final int LOGIN_DATA = 8;
    public static final int SCHEDULE_APPOINTMENT = 9;
    public static final int READ_PATIENT_DATA = 10;
    public static final int LOGOUT_DATA = 11;
    public static final int LOGIN_REFRESH = 12;
    public static final int GET_EPIC_ACCESS_TOKEN = 13;
    public static final int SEND_PATIENT_DATA_FOR_VARIFICATION = 14;
    public static final int VIEW_APPOINTMENT = 15;
    public static final int CANCEL_APPOINTMENT = 16;
    public static final int RESCHEDULE_APPOINTMENT = 17;
    public static final int APPOINTMENT_DOCTOR = 18;
    public static final String FROM_ACTIVTY = "fromACtivity";
    public static final int FROM_REGISTRATION_ACTIVITY = 111;
    public static final int FROM_LOGIN_ACTIVITY = 222;
    public static final String DATAMOTION_TOKEN_CURRENT_TIME_STRING = "dataMotionCurrentTime";
    public static final String _DATAMOTIONTOKE_EXPIRE_TIME_STRING = "dataMotionTokenExpireTiem";
    public static final String EPIC_CURRENT_TIME_STRING = "epicCurrentTime";
    public static final String EPIC_EXPIRE_TIME_STRING = "epicTimeExpire";
    public static final int TURN_ON_HELP = 4;
    public static final int CROP_IMAGE_ACTIVITY =11 ;
    public static final String TRANSACTION_ID ="transcationID";
    public static final String IMAGE_FILE_PATH = "imagePath";
    public static final String CROPPED_IMAGE = "croppedImage";
    public static final String SELECTED_IMAGE_VIEW = "selectedImageView";
    public static final String BITMAP_IMAGE ="bitmapImage" ;
    public static final String BASE64_FILE_PATH = "base64File";
    public static final String CAPTURE_IMAGE_URI ="captureImageUri" ;
    public static final int GET_DATAMOTION_ACCESS_TOKEN = 20;
    public static final int FRAGMENT_CODE =111 ;
    public static final int REGISTRATION_FOR_DEPENDENT = 333;
    public static final String REGISTRATION_FOR = "registrationFor";
    public static final int REGISTRATION_FOR_PARENTS = 444;
    public static final int PARENTS_LOGIN = 555;
    public static final String PARENTS_BIRTHDATE ="parentsBirthDate" ;
    public static final String PARENTS_FIRST_NAME ="firstName" ;
    public static final String PARENTS_LAST_NAME = "lastName";
    public static final int DOCTOR_EMAIL_SEARCH = 21;
    public static final int DOCTOR_ALLOW = 22;
    public static final String PARENTS_GENDER = "gender";
    public static final int FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE_CUSTOM = 100;

    public static int RESULT_ERROR = 0;
    public static int RESULT_SUCCESS = 1;


    public static int DEFAULT_HTTP_TIMEOUT_INTERVAL = 10000;
    public static final int HTTP_OK = 200;
    public static final int HTTP_REDIRECT = 300;
    public static final int HTTP_EXCEPTION = 0;

   // public static String BASE_TOKEN = "MEtRWmJ2aU5iWXJnbUJteUFIVGhHN2pGdEdlYTBwNjQ6RW1pall1dmRBWHZDd25Rdg====";
//    public static String BASE_TOKEN_URL = "https://hackensack-prod.apigee.net/v1/oauth/token?grant_type=client_credentials";

//    public static String BASE_LOGIN_TOKEN = "MEtRWmJ2aU5iWXJnbUJteUFIVGhHN2pGdEdlYTBwNjQ6RW1pall1dmRBWHZDd25Rdg==";

  /*  public static final String PRACTITIONER_URL = "https://hackensack-prod.apigee.net/v1/api/Practitioner";
    public static final String SPECIALITY_URL = "https://hackensack-prod.apigee.net/v1/api/Specialty";*/

    public static String TOKEN_KEY_ACCESS = "access_token";
    public static String TOKEN_KEY_EXPIRE_IN = "expires_in";
    public static String TOKEN_KEY_REFRESH = "refresh_token";
    public static String TOKEN_KEY_MRN_ID = "mrn_id";
    public static String TOKEN_KEY_TYPE = "token_type";

    public static String KEY_LOGIN_SUCCESSFUL = "KEY_LOGIN_SUCCESSFUL";

    public static String TOKEN_SHARED_PREFERENCE = "TOKEN_SHARED_PREFERENCE";
    public static String TOKEN_SHARED_PREFERENCE_TOKEN_STRING = "TOKEN_SHARED_PREFERENCE_TOKEN_STRING";
    public static String TOKEN_SHARED_PREFERENCE_REFRESH_TOKEN_STRING = "TOKEN_SHARED_PREFERENCE_REFRESH_TOKEN_STRING";
    public static String TOKEN_CURRENT_TIME_STRING = "TOKEN_CURRENT_TIME_STRING";
    public static String TOKEN_CURRENT_EXPIRE_TIME_STRING = "TOKEN_CURRENT_EXPIRE_TIME_STRING";

    public static String LOGIN_TYPE_STRING = "LOGIN_TYPE_STRING";
    public static int UNKNOWN_LOGIN = 0;
    public static int GUEST_LOGIN = 1;
    public static int USER_LOGIN = 2;
    public static final int REQUEST_TERMSCONDITIONS = 4;

    public static String PATIENT_SHARED_PREFERENCE = "PATIENT_SHARED_PREFERENCE";
    public static String PATIENT_MRN_ID_SHARED_PREFERENCE_STRING = "PATIENT_MRN_ID_SHARED_PREFERENCE_STRING";
    public static String PATIENT_JSON_SHARED_PREFERENCE_STRING = "PATIENT_JSON_SHARED_PREFERENCE_STRING";
    public static String PREF_HOME_ADDRESS = "PREF_HOME_ADDRESS";
    public static String PREF_WORK_ADDRESS = "PREF_WORK_ADDRESS";

//    public static String PATIENT_MRN_ID_CURRENT_TIME_STRING     = "PATIENT_MRN_ID_CURRENT_TIME_STRING";
//    public static String PATIENT_MRN_ID_CURRENT_EXPIRE_TIME     = "PATIENT_MRN_ID_CURRENT_EXPIRE_TIME";


    public static String KEY_USER_LOGIN = "KEY_USER_LOGIN";
    public static String KEY_NAME = "name";
    public static String KEY_FIRST_NAME = "given";
    public static String KEY_LAST_NAME = "family";
    public static String KEY_BIRTH_DAY = "birthDate";

    /*public static final String PRACTITIONER_BASE_SERVER = "https://hackensack-prod.apigee.net/v1/api";*/


    //    public static final String PRACTITIONER_TIME_SLOT_URL	= "https://hackensack-prod.apigee.net/v2/fhir/Slot?schedule.actor=1770544504&_include:recurse=Slot:schedule";
//    public static final String PRACTITIONER_TIME_SLOT_URL	= "https://hackensack-prod.apigee.net/v2/fhir/Slot?schedule.actor=1730341322&_include:recurse=Slot:schedule";
   /* public static final String PRACTITIONER_TIME_SLOT_URL = "https://hackensack-prod.apigee.net/v2/fhir/Slot?schedule.actor=";
    public static final String PRACTITIONER_TIME_SLOT_URL_PART = "&_include:recurse=Slot:schedule";

    public static final String LOGIN_URL = "https://hackensack-prod.apigee.net/v1/oauth/token?grant_type=password";
    public static final String LOGOUT_URL = "https://hackensack-prod.apigee.net/v1/oauth/token";
    public static final String REFRESH_LOGIN_URL = "https://hackensack-prod.apigee.net/v1/oauth/token?grant_type=refresh_token";

    public static final String SCHEDULE_APPOINTMENT_URL = "https://hackensack-prod.apigee.net/v2/fhir/Appointment";


    public static final String READ_PATIENT_URL = "https://hackensack-prod.apigee.net/v2/fhir/Patient/"; //900702012
    public static final String READ_PATIENT_URL_PART = "?_format=json";

    public static final String VIEW_APPOINTMENT_URL = "https://hackensack-prod.apigee.net/v2/fhir/Appointment?patient=";

    public static final String CANCEL_APPOINTMENT_URL = "https://hackensack-prod.apigee.net/v2/fhir/Appointment/";*/


    public static final String ACTIVITY_FLOW = "ACTIVITY_FLOW";
    public static final String APPOINTMENT_SUMMARY_FLOW = "APPOINTMENT_SUMMARY_FLOW";
    public static final String SHOW_LOGIN_SCREEN = "SHOW_LOGIN_SCREEN";

    public static final String SHOW_EMERGENCY_POP_UP = "SHOW_EMERGENCY_POP_UP";

    /***
     *
     */
    public static final String SYMPTOM_ADULT_URL = "json/1.0/en/topictable/adult";
    public static final String SYMPTOM_CHILD_URL = "json/1.0/en/topictable/peds";

    //    public static final String SYMPTOM_BASE_ADULT_URL   = "https://demo.selfcare.info/Services.svc/json/1.0/en/topictable/adult" ;
    public static final String SYMPTOM_BASE_ADULT_URL = "https://api.selfcare.info/Services.svc/json/1.0/en/topictable/adult";
    public static final String SYMPTOM_BASE_CHILD_URL = "https://api.selfcare.info/Services.svc/json/1.0/en/topictable/ped";


    public static final String SYMPTOM_PRIVATE_KEY = "/22ndbLnQFGo80ouijSPihs5iQ/bvpGroXcVi4jZrCMQNo9GGWyfBQ1POhAvu7wDcuM9JaiPUE1goz050El/Yw==";
    public static final String SYMPTOM_PUBLIC_KEY = "HAK";

//    public static final String PRACTITIONER_URL	= "http://humcdev1.cloudapp.net:8080/apiserver/Practitioner";
//    public static final String PRACTITIONER_URL	= "http://humcdev1.cloudapp.net:8080/humc-server/api/Practitioner";
//public static final String SPECIALITY_URL	= "http://humcdev1.cloudapp.net:8080/humc-server/api/Speciality";
//    public static final String PRACTITIONER_URL	= "https://fhir.hackensackumc.net:8080/humc-server/api/Practitioner";
//    public static final String SPECIALITY_URL	= "https://fhir.hackensackumc.net:8080/humc-server/api/Speciality";

//    public static final String PRACTITIONER_SERVER = "http://humcdev1.cloudapp.net:8080/";
//    public static final String PRACTITIONER_SERVER = "https://fhir.hackensackumc.net:8080/humc-server/api";

    public static String DOCTOR_SELECTED = "DOCTOR_SELECTED";
    public static String DOCTOR_SPECIALTY = "DOCTOR_SPECIALTY";
    public static String DOCTOR_SEARCH_QUERY = "DOCTOR_SEARCH_QUERY";
    public static String DOCTOR_ADDRESS_SELECTED = "DOCTOR_ADDRESS_SELECTED";
    public static String DOCTOR_ADDRESS = "DOCTOR_ADDRESS";
    public static String DOCTOR_TELEPHONE = "DOCTOR_TELEPHONE";
    public static String DOCTOR_APPOINTMENT_TIME = "DOCTOR_APPOINTMENT_TIME";
    public static String DOCTOR_APPOINTMENT_TYPE = "DOCTOR_APPOINTMENT_TYPE";
    public static String DOCTOR_APPOINTMENT_ID = "DOCTOR_APPOINTMENT_ID";
    public static String DOCTOR_DISTANCE_MILE = "DOCTOR_DISTANCE_TIME";
    public static String DOCTOR_APPOINTMENT = "DOCTOR_APPOINTMENT";
    public static String RESCHEDULE_APPOINTMENT_ID = "RESCHEDULE_APPOINTMENT_ID";
	public static String APPOINTMENT_FILTER = "APPOINTMENT_FILTER";

    public static String DOCTOR_SEARCH_SPECIALTY_QUERY = "DOCTOR_SEARCH_SPECIALTY_QUERY";
    public static String DOCTOR_SEARCH_GENDER_QUERY = "DOCTOR_SEARCH_GENDER_QUERY";
    public static String DOCTOR_IS_FROM_FILTER = "DOCTOR_IS_FROM_FILTER";
    public static String DOCTOR_SEARCH_ZIPCODE_QUERY = "DOCTOR_SEARCH_ZIPCODE_QUERY";
    public static String DOCTOR_SEARCH_FIRST_NAME_QUERY = "DOCTOR_SEARCH_FIRST_NAME_QUERY";
    public static String DOCTOR_SEARCH_LAST_NAME_QUERY = "DOCTOR_SEARCH_LAST_NAME_QUERY";
    public static String DOCTOR_SEARCH_SEARCH_WITHIN = "DOCTOR_SEARCH_SEARCH_WITHIN";
    public static String DOCTOR_SEARCH_LOCATION = "DOCTOR_SEARCH_LOCATION";

    public static String APPOINTMENT_TYPE_SCREEN = "APPOINTMENT_TYPE_SCREEN";

    public static String SYMPTOM_BODY_PART = "SYMPTOM_BODY_PART";
    public static String SYMPTOM_BODY_CLASSIFIED = "SYMPTOM_BODY_CLASSIFIED";
    public static String SYMPTOM_SELECTED = "SYMPTOM_SELECTED";
    public static String SYMPTOM_TITLE = "SYMPTOM_TITLE";

    public static final String MALE_BODY = "M";
    public static final String FEMALE_BODY = "F";
    public static final String CHILD_BODY = "C";
    public static final String COMMON_BODY = "B";

    public static String KEY_DEGREE = "degree";
    public static String KEY_FULL_NAME = "fullname";
    public static String KEY_SPECIALTY = "speciality";
    public static String KEY_HUMC_ID = "humcId";
    public static String KEY_IMAGE_URL = "imageUrl";
    public static String KEY_NPI_ID = "npi";
    public static String KEY_SLOT = "slot";
    public static String KEY_ADDRESS = "address";
    public static String KEY_ADDRESS_SUITE = "suite";
    public static String KEY_ADDRESS_STREET = "street";
    public static String KEY_ADDRESS_STATE = "state";
    public static String KEY_ADDRESS_PHONE = "phone";
    public static String KEY_ADDRESS_ZIP = "zip";
    public static String KEY_DOCTOR_LIST = "practitionerList";
    public static String KEY_DOCTOR_SEARCH_NEXT_URL = "nextPageLink";

    public static String KEY_ADDRESS_BUILDING = "building";
    public static String KEY_ADDRESS_CITY = "city";
    public static String KEY_ADDRESS_FAX = "fax";
    public static String KEY_ADDRESS_GROUP_NAME = "groupname";
    public static String KEY_ADDRESS_HA = "ha";

    public static String KEY_DOCTOR_LOCATION_ARRAY = "entry";
    public static String KEY_DOCTOR_LOCATION_RESOURCE = "resource";
    public static String KEY_DOCTOR_LOCATION_RESOURCE_TYPE = "resourceType";
    public static String KEY_DOCTOR_ID = "id";
    public static String KEY_DOCTOR_ADDRESS = "address";

    public static String RESOURCE_TYPE_LOCATION = "Location";
    public static String TELECOM_ARRAY = "telecom";
    public static String TELECOM_SYSTEM = "system";
    public static String TELECOM_VALUE = "value";
    public static String TELECOM_USE = "use";
    public static String IDENTIFIER_USE = "identifier";
    public static String KEY_LABEL = "label";
    public static String DRIVING_VALUE = "DrivingLisence";

    public static String ADDRESS_ARRAY = "address";
    public static String ADDRESS_LINE = "line";
    public static String ADDRESS_CITY = "city";
    public static String ADDRESS_STATE = "state";
    public static String ADDRESS_POSTAL_CODE = "postalCode";
    public static String ADDRESS_COUNTRY = "country";

    public static String PHOTO_ARRAY = "photo";
    public static String PHOTO_DATA = "data";

    public static String SCHEDULE_EXTENSION = "extension";
    public static String SCHEDULE_VALUE_REFERENCE = "valueReference";
    public static String SCHEDULE_ACTOR = "actor";
    public static String SCHEDULE_ACTOR_DISPLAY = "display";
    public static String RESOURCE_TYPE_SCHEDULE = "Schedule";
    public static String SCHEDULE_TYPE = "type";
    public static String SCHEDULE_CODING = "coding";
    public static String SCHEDULE_CODE = "code";

    public static String SCHEDULE_PLANNING_HORIZON = "planningHorizon";
    public static String SCHEDULE_DATE = "start";

    public static String RESOURCE_TYPE_SLOT = "Slot";
    public static String SLOT_SCHEDULE = "schedule";
    public static String SLOT_REFERENCE = "reference";
    public static String SLOT_FREE_BUSY_TYPE = "freeBusyType";

    public static String RESOURCE_TYPE_APPOINTMENT = "Appointment";
    public static String PARTICIPANT_TYPE = "participant";
    public static String KEY_PRACTITIONER = "Practitioner";

    public static String KEY_TITLES = "titles";
    public static String KEY_ID = "id";
    public static String KEY_TITLE = "title";
    public static String KEY_SPECIAL_TOPIC = "specialtopic";
    public static String KEY_PEDIATRIC = "pediatric";
    public static String KEY_GENDER = "gender";
    public static String KEY_BODY_AREAS = "bodyareas";
    public static String KEY_KEYWORDS = "keywords";
    public static String KEY_BACKGROUND = "background";
    public static String KEY_ADVICE = "advice";
    public static String KEY_TEXT = "text";
    public static String KEY_FOOTER = "footer";
    public static String KEY_TOPIC = "topic";

//    public static String KEY_FIRST_NAME = "family";
//    public static String KEY_SECOND_NAME = "given";

    public static String KEY_FREE = "free";

    public static int SCREEN_MIN_WIDTH = 400;
    public static int SCREEN_MIN_HEIGHT = 800;

    public static String newUrl = "http://www.hackensackumc.org/rss/news.aspx";
    public static String erUrl = "http://mdnetap1.humcmd.net/resources/rssfeeds/edwt/humcedwt.xmleFragment";
    public static String url = "";


    public static final String ABOUT_URL = "file:///android_asset/AboutHackensackUMC.html";
    public static final String HTML_URL = "HTML_URL";
    public static final String IS_ABOUT = "IS_ABOUT";
public static final String IS_FORM = "IS_FORM";
    public static final String MYCHART_FORM="https://mychart.humed.com/MyChart/default.asp?mode=stdfile&option=CustomForm2";


    /**
     * Registration Constant
     */
    public static final String SHAREPREF_TAG = "SharePreferances";
    public static final String CARD_RESPONSE_FILE_PATH = "CardResponse.bin";
    public static final String REGISTRATION_MODE = "registrationMode";
    public static final int AUTO = 0;
    public static final String KEY_BASE64_IAMGES = "obBase64Images";
    public static final String BUNDLE = "bundle";
    public static final String REG_REQUIRED_DATA = "regFiled";
    public static final String QUETIONS_DATA = "qutionsData";
    public static int MANUAL = 1;
    public static final String KEY_ID_FRONT = "idFront";
    public static final String KEY_ID_BACK = "idBack";
    public static final String KEY_INSURANCE_FRONT = "insuFront";
    public static final String KEY_INSURANCE_BACK = "insuBack";
    public static final String KEY_SLFIE = "selfie";


    public static final String US_ZIP_CODE_REGULAR_EXPRESSION = "^\\d{5}(-\\d{4})?$";
    // public static final String PASSWOSRD_REGULAR_EXPRESSION = "^([a-zA-Z+]+[0-9+]+[&@!#+]+)$";//^(?=.*\\d)(?=.*[a-zA-Z])(?!\\w*$).{8,}
    public static final String PASSWOSRD_REGULAR_EXPRESSION = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,20}";

    public static final String STRING_REGULAR_EXPRESSION = "[A-Za-z]*";


    /////
    public static final String PATIENT_FOR_EPIC_CALL = "PatientForEpicCall";
    public static final String EMAIL_ID = "emailId";
    public static final String USER_NAME = "userName";
    public static final String MRN = "MRN";


    public static String EPIC_ACCESS_TOKEN_TIME = "epicTokenTimeCreated";
    public static final String INSURANCE_DATA_TO_SEND = "Insurance Data to send";


    public static boolean backFromCreatePassword = false;

    public static final String LOCATION_ID = "locationId";
    public static final String PROFILE_DIALOG = "profilelDialog";

    public static final int FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE = 1;

    public static int ID_PROOF_FRONT = 1;
    public static int ID_PROOF_BACK = 2;
    public static int INSU_PROOF_FRONT = 3;
    public static int INSU_PROOF_BACK = 4;
    public static int SELFIE = 5;
    public static String HELP_PREF_DIRECTION = "DIRECTION_HELP_PREFERENCE";
    public static String HELP_PREF_HACKENSACK = "HELP_PREF_HACKENSACK";
    public static final String HELP_PREF_SYMPTOM_CHECKER = "HELP_PREF_SYMPTOM_CHECKER";
    public static final String HELP_PREF_PROFILE ="HELP_PREF_SYMPTOM_PROFILE";
    public static final String HELP_PREF_DOCTOR_RESULT = "HELP_PREF_DOCTOR_RESULT";
    public static final String HELP_PREF_APPIONTMENT_SUMMARY ="HELP_PREF_DOCTOR_RESULT_APPIONTMENT_SUMMARY";
    public static final String HELP_PREF_FAVORITE_DOCTOR ="HELP_PREF_FAVORITE_DOCTOR" ;
    public static final String HELP_PREF_MAP_DIRECTION ="HELP_PREF_MAP_DIRECTION";

    public static final String DOCTOR_EMAIL_ID = "DOCTOR_EMAIL_ID";
    public static final String DOCTOR_CONSENT = "DOCTOR_CONSENT";


}

