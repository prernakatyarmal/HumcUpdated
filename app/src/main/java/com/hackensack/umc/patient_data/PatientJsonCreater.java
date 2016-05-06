package com.hackensack.umc.patient_data;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hackensack.umc.util.Base64Converter;
import com.hackensack.umc.util.CameraFunctionality;
import com.hackensack.umc.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class PatientJsonCreater implements Serializable {
    private String mFirstName, mLastname, mBirthDate;
    private ArrayList<Telecom> telecom;
    private ArrayList<Address> address;
    private String gender;
    private String dlFront;
    private String dlBack;

    public ArrayList<Telecom> getTelecom() {
        return telecom;
    }

    public void setTelecom(ArrayList<Telecom> telecom) {
        this.telecom = telecom;
    }

    private String insuFront;
    private String insuBack;

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    private String selfie;
    private String drivingLicence;
    private String location = "1110101211";//hardcoded for now..will come from previous activity//gaurav


    /* public static final Creator<PatientJsonCreater> CREATOR = new Creator<PatientJsonCreater>() {
         @Override
         public PatientJsonCreater createFromParcel(Parcel in) {
             return new PatientJsonCreater(in);
         }

         @Override
         public PatientJsonCreater[] newArray(int size) {
             return new PatientJsonCreater[size];
         }
     };
 */
    public PatientJsonCreater(String mFirstName, String mLastname, String mBirthDate, String gender, ArrayList<Telecom> telecomList, ArrayList<Address> addresses, String dlFront,
                              String dlBack, String insuFront, String insBack, String selfie, String drivingLicence, String loacation) {
        this.mFirstName = mFirstName;
        this.mLastname = mLastname;
        this.mBirthDate = mBirthDate;
        this.telecom = telecomList;
        this.address = addresses;
        this.gender = gender;
        this.dlFront = dlFront;
        this.dlBack = dlBack;
        this.insuFront = insuFront;
        this.insuBack = insBack;
        this.selfie = selfie;
        this.drivingLicence = drivingLicence;
        this.location = loacation;


    }
/*  private String use = "home";
    private String state = "CA";
    private String city = "Milpitas";
    private  String postalCode = "95035";
    private String country = "en-US"
    String gender = "male";;*/

/*
    protected PatientJsonCreater(Parcel in) {
        mFirstName = in.readString();
        mLastname = in.readString();
        mBirthDate = in.readString();
        gender = in.readString();
    }
*/


    public JSONObject createPatientJson(Context context) {
     /*   {
    "gender": "male",
    "photo": [
        {
            "title": "DrivingLicenseFront",
            "contentType": "image/gif",
            "data": "R0lGODlhEwARAPcAAAAAAAAA/+9aAO+1AP/WAP/eAP/eCP/eEP/eGP/nAP/nCP/nEP/nIf/nKf/nUv/nWv/vAP/vCP/vEP/vGP/vIf/vKf/vMf/vOf/vWv/vY//va//vjP/3c//3lP/3nP//tf//vf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAEALAAAAAATABEAAAi+AAMIDDCgYMGBCBMSvMCQ4QCFCQcwDBGCA4cLDyEGECDxAoAQHjxwyKhQAMeGIUOSJJjRpIAGDS5wCDly4AALFlYOgHlBwwOSNydM0AmzwYGjBi8IHWoTgQYORg8QIGDAwAKhESI8HIDgwQaRDI1WXXAhK9MBBzZ8/XDxQoUFZC9IiCBh6wEHGz6IbNuwQoSpWxEgyLCXL8O/gAnylNlW6AUEBRIL7Og3KwQIiCXb9HsZQoIEUzUjNEiaNMKAAAA7"
        },
        {
            "title": "DrivingLicenseBack",
            "contentType": "image/gif",
            "data": "R0lGODlhEwARAPcAAAAAAAAA/+9aAO+1AP/WAP/eAP/eCP/eEP/eGP/nAP/nCP/nEP/nIf/nKf/nUv/nWv/vAP/vCP/vEP/vGP/vIf/vKf/vMf/vOf/vWv/vY//va//vjP/3c//3lP/3nP//tf//vf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAEALAAAAAATABEAAAi+AAMIDDCgYMGBCBMSvMCQ4QCFCQcwDBGCA4cLDyEGECDxAoAQHjxwyKhQAMeGIUOSJJjRpIAGDS5wCDly4AALFlYOgHlBwwOSNydM0AmzwYGjBi8IHWoTgQYORg8QIGDAwAKhESI8HIDgwQaRDI1WXXAhK9MBBzZ8/XDxQoUFZC9IiCBh6wEHGz6IbNuwQoSpWxEgyLCXL8O/gAnylNlW6AUEBRIL7Og3KwQIiCXb9HsZQoIEUzUjNEiaNMKAAAA7"
        },
        {
            "title": "InsuranceCardFront",
            "contentType": "image/gif",
            "data": "R0lGODlhEwARAPcAAAAAAAAA/+9aAO+1AP/WAP/eAP/eCP/eEP/eGP/nAP/nCP/nEP/nIf/nKf/nUv/nWv/vAP/vCP/vEP/vGP/vIf/vKf/vMf/vOf/vWv/vY//va//vjP/3c//3lP/3nP//tf//vf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAEALAAAAAATABEAAAi+AAMIDDCgYMGBCBMSvMCQ4QCFCQcwDBGCA4cLDyEGECDxAoAQHjxwyKhQAMeGIUOSJJjRpIAGDS5wCDly4AALFlYOgHlBwwOSNydM0AmzwYGjBi8IHWoTgQYORg8QIGDAwAKhESI8HIDgwQaRDI1WXXAhK9MBBzZ8/XDxQoUFZC9IiCBh6wEHGz6IbNuwQoSpWxEgyLCXL8O/gAnylNlW6AUEBRIL7Og3KwQIiCXb9HsZQoIEUzUjNEiaNMKAAAA7"
        },
        {
            "title": "InsuranceCardBack",
            "contentType": "image/gif",
            "data": "R0lGODlhEwARAPcAAAAAAAAA/+9aAO+1AP/WAP/eAP/eCP/eEP/eGP/nAP/nCP/nEP/nIf/nKf/nUv/nWv/vAP/vCP/vEP/vGP/vIf/vKf/vMf/vOf/vWv/vY//va//vjP/3c//3lP/3nP//tf//vf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAEALAAAAAATABEAAAi+AAMIDDCgYMGBCBMSvMCQ4QCFCQcwDBGCA4cLDyEGECDxAoAQHjxwyKhQAMeGIUOSJJjRpIAGDS5wCDly4AALFlYOgHlBwwOSNydM0AmzwYGjBi8IHWoTgQYORg8QIGDAwAKhESI8HIDgwQaRDI1WXXAhK9MBBzZ8/XDxQoUFZC9IiCBh6wEHGz6IbNuwQoSpWxEgyLCXL8O/gAnylNlW6AUEBRIL7Og3KwQIiCXb9HsZQoIEUzUjNEiaNMKAAAA7"
        },
        {
            "title": "Selfie",
            "contentType": "image/gif",
            "data": "R0lGODlhEwARAPcAAAAAAAAA/+9aAO+1AP/WAP/eAP/eCP/eEP/eGP/nAP/nCP/nEP/nIf/nKf/nUv/nWv/vAP/vCP/vEP/vGP/vIf/vKf/vMf/vOf/vWv/vY//va//vjP/3c//3lP/3nP//tf//vf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAEALAAAAAATABEAAAi+AAMIDDCgYMGBCBMSvMCQ4QCFCQcwDBGCA4cLDyEGECDxAoAQHjxwyKhQAMeGIUOSJJjRpIAGDS5wCDly4AALFlYOgHlBwwOSNydM0AmzwYGjBi8IHWoTgQYORg8QIGDAwAKhESI8HIDgwQaRDI1WXXAhK9MBBzZ8/XDxQoUFZC9IiCBh6wEHGz6IbNuwQoSpWxEgyLCXL8O/gAnylNlW6AUEBRIL7Og3KwQIiCXb9HsZQoIEUzUjNEiaNMKAAAA7"
        }
    ],
    "identifier": [
        {
            "use": "usual",
            "type": {
                "coding": [
                    {
                        "system": "http://hl7.org/fhir/v2/0203",
                        "code": "DL"
                    }
                ]
            },
            "value": "12345"
        }
    ],
    "address": [
        {
            "use": "home",
            "line": [
                "71 FOX HOLLOW DR",
                null
            ],
            "state": "PA",
            "city": "DALLAS",
            "country": "en-US",
            "postalCode": "18612-8902",
            "extension": [
                {
                    "url": "http://hl7.org/fhir/StructureDefinition/us-core-county",
                    "valueString": "Orange County"
                }
            ]
        }
    ],
    "active": "true",
    "telecom": [
        {
            "system": "phone",
            "value": "570-371-9646",
            "use": "mobile"
        },
        {
            "system": "phone",
            "value": "035-555-6473",
            "use": "home"
        },
        {
            "system": "email",
            "value": "chuckchesk@gmail.com",
            "use": "home"
        }
    ],
    "birthDate": "1959-12-06",
    "maritalStatus": {
        "coding": [
            {
                "system": "http://hl7.org/fhir/v3/MaritalStatus",
                "code": "M",
                "display": "Married"
            }
        ],
        "text": "Getrouwd"
    },
    "text": {
        "status": "generated",
        "div": "<div>!-- Snipped for Brevity --></div>"
    },
    "extension": [
        {
            "valueCodeableConcept": [
                {
                    "coding": {
                        "system": "http://hl7.org/fhir/v3/Race",
                        "code": "1096-7"
                    }
                }
            ],
            "url": "http://hl7.org/fhir/StructureDefinition/us-core-race"
        },
        {
            "url": "https://www.hl7.org/fhir/location.html",
            "valueReference": {
                "reference": "Location/1110101211"
            }
        },
        {
            "url": "http://hl7.org/fhir/StructureDefinition/us-core-race",
            "valueCodeableConcept": [
                {
                    "coding": {
                        "system": "http://hl7.org/fhir/v3/Ethnicity",
                        "code": "2162-6"
                    }
                }
            ]
        }
    ],
    "resourceType": "Patient",
    "name": {
        "use": "official",
        "given": "CHARLES",
        "family": "CHESKIEWICZ"
    }
}
*/
        JSONObject jsonObjectPatient = null;

        String resourceType = "Patient";
        String id = "bundle-transaction";//not prersent is json to be send

        Name name = creatName();
        //  ArrayList<Telecom> telecom = createTelecomeList();
        String birthDate = mBirthDate;
        // ArrayList<Address> address = createAddressJson();
        MaritalStatus maritalStatus = createMaterialStatus();

        String active = "true";
        ArrayList<Extension> extension = createExtensionList();


        String type = "transaction";

        String currentTime = "2014-08-18T01:43:30Z";//change dynamic;
        Meta meta = new Meta(currentTime);
        Text text = new Text("generated", "<div>!--SnippedforBrevity--></div>");

        //String gender = "male";
        PatientForEpic patientResponse = new com.hackensack.umc.patient_data.PatientForEpic(resourceType, text, name,
                telecom, birthDate, address, maritalStatus, gender, active, extension);
        ArrayList<Photo> usersPhoto = createPhotoListNew(context);
        if (usersPhoto.size() > 0) {
            patientResponse.setphoto(usersPhoto);
        }

        if (!TextUtils.isEmpty(drivingLicence)) {
            patientResponse.setIdentifiers(creatIdentifires(context));
        }

        Gson gson = new Gson();

        String patientJson = gson.toJson(patientResponse);
        // Text text = gson.fromJson(textjson, Text.class);
        Log.v("patientJson::", patientJson);

        try {
            jsonObjectPatient = new JSONObject(patientJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("New PatientJson", jsonObjectPatient.toString());

        return jsonObjectPatient;
        /*
         * JsonObject jsonObjectPatient = new JsonObject();
		 * jsonObjectPatient.add("resourceType", value);
		 * jsonObjectPatient.add("id", value);
		 * jsonObjectPatient.add("birthDate", value);
		 * jsonObjectPatient.add("text", value);
		 * jsonObjectPatient.add("maritalStatus", value);
		 *
		 * JsonArray jsonArray = new JsonArray(); JsonObject jsonObjectName =
		 * new JsonObject(); jsonObjectName.add("", value);
		 *
		 * return jsonObject;
		 */
    }

    /*  private ArrayList<Entry> creteEntryList(PatientForEpic patientResponse) {
          ArrayList<Entry> entryList = new ArrayList<>();
          String fullUrl = "urn:uuid:61ebe359-bfdc-4613-8bf2-c5e300945f0a";



          *//*String subPlan="123";
        String dependent="1";
        String subscribeId="90023";
        ArrayList<Extension>extensions=createExtensionList();
        Subscriber subscriber=new Subscriber(extensions);
        Period period=new Period("2011-05-23");
        String group="group123";
        Type type=new Type("http: //hl7.org/fhir/v3/ActCode","EHCPOL","extendedhealthcare");
        String resourceType="Patient";
        //Issuer issuer=new Issuer();
        String sequence="";*//*
        ArrayList<Identifier> identifiers = creatIdentifires();
        // CoverageData resource=new CoverageData(text,subPlan,dependent,period,group,type,resourceType,issuer,sequence,subscribeId,subscriber,identifier);
        Request request = new Request("POST", "Patient");


        ArrayList<Photo> photoList = new ArrayList<>();
        Photo photo = new Photo("DrivingLicenseFront", "image/gif", "testImage");
        photoList.add(photo);

        //PatientForEpic patientResponse, ManagingOrganization managingOrganization, ArrayList<com.hackensack.umc.patient_data.Photo> photo, Request request, ArrayList<Identifier> identifiers) {
        ManagingOrganization managingOrganization = new ManagingOrganization("Organization/22542", "ACME Healthcare, Inc");
        CoverageData resource = new CoverageData(patientResponse, managingOrganization, photoList, identifiers);
        Entry entry = new Entry(fullUrl, request, resource, new CoverageJsonConverter().coverageJsonObject());
        entryList.add(entry);

        return entryList;
    }*/
    private ArrayList<Photo> createPhotoList(Context context) {
        ArrayList<Photo> photoList = new ArrayList<>();
        if (!TextUtils.isEmpty(dlFront)) {
            Bitmap bitmapImage = CameraFunctionality.getBitmapFromFile(dlFront, context);
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, context);
            photoList.add(new Photo("DrivingLicenseFront", base64, "image/gif"));
            //CameraFunctionality.deleteImage(dlFront, context);
        }
        if (!TextUtils.isEmpty(dlBack)) {

            Bitmap bitmapImage = CameraFunctionality.getBitmapFromFile(dlBack, context);
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, context);
            photoList.add(new Photo("DrivingLicenseBack", base64, "image/gif"));

            //  CameraFunctionality.deleteImage(dlBack, context);
        }
        if (!TextUtils.isEmpty(insuFront)) {
            Bitmap bitmapImage = CameraFunctionality.getBitmapFromFile(insuFront, context);
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, context);
            photoList.add(new Photo("InsuranceCardFront", base64, "image/gif"));
            // CameraFunctionality.deleteImage(insuFront, context);
        }
        if (!TextUtils.isEmpty(insuBack)) {
            Bitmap bitmapImage = CameraFunctionality.getBitmapFromFile(insuBack, context);
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, context);
            photoList.add(new Photo("InsuranceCardBack", base64, "image/gif"));
            // CameraFunctionality.deleteImage(insuBack, context);
        }
        if (!TextUtils.isEmpty(selfie)) {
            Bitmap bitmapImage = CameraFunctionality.getBitmapFromFile(selfie, context);
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, context);
            photoList.add(new Photo("Selfie", base64, "image/gif"));
            //  CameraFunctionality.deleteImage(selfie, context);
        }
        return photoList;

    }

    private ArrayList<Photo> createPhotoListNew(Context context) {
        ArrayList<Photo> photoList = new ArrayList<>();
        if (!TextUtils.isEmpty(dlFront) && !TextUtils.isEmpty(dlBack)) {
            Bitmap bitmapImageFront = CameraFunctionality.getBitmapFromFile(dlFront, context);
            Bitmap bitmapImageBack = CameraFunctionality.getBitmapFromFile(dlBack, context);
            String base64 = Base64Converter.combineImageAndGetBase64(bitmapImageFront, bitmapImageBack, context);
            Log.v("base64 dl:", base64);

            photoList.add(new Photo("DrivingLicense", base64, "image/gif"));
            // CameraFunctionality.deleteImage(dlFront, context);
            CameraFunctionality.deleteImageAfterUploading(dlFront.toString(), context);
            CameraFunctionality.deleteImageAfterUploading(dlBack.toString(), context);
        }

        if (!TextUtils.isEmpty(insuFront) && !TextUtils.isEmpty(insuBack)) {
            Bitmap bitmapFront = CameraFunctionality.getBitmapFromFile(insuFront, context);
            Bitmap bitmapBack = CameraFunctionality.getBitmapFromFile(insuBack, context);
            String base64 = Base64Converter.combineImageAndGetBase64(bitmapFront, bitmapBack, context);
            Log.v("base64 IC:", base64);
            photoList.add(new Photo("InsuranceCard", base64, "image/gif"));
            CameraFunctionality.deleteImageAfterUploading(insuFront.toString(), context);
            CameraFunctionality.deleteImageAfterUploading(insuBack.toString(), context);
            //CameraFunctionality.deleteImage(insuFront, context);
        }
        if (!TextUtils.isEmpty(selfie)) {
            Bitmap bitmapImage = CameraFunctionality.getBitmapFromFile(selfie, context);
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, context);
            Log.v("base64 selfie:", base64);
            photoList.add(new Photo("Selfie", base64, "image/gif"));
            CameraFunctionality.deleteImageAfterUploading(selfie.toString(), context);
            //CameraFunctionality.deleteImage(selfie, context);
        }
        return photoList;

    }

    private ArrayList<Identifier> creatIdentifires(Context context) {
        //for driving liesense
        ArrayList<Identifier> identifiers = new ArrayList<>();
        ArrayList<Coding> listCoding = new ArrayList<>();
        listCoding.add(new Coding("http://hl7.org/fhir/v2/0203", "DL"));
        Type type = new Type(listCoding);
        Identifier idFir = new Identifier("usual", type, drivingLicence);
        identifiers.add(idFir);

        //For transactionId
        ArrayList<Coding> listCodingTransactionId = new ArrayList<>();
        listCodingTransactionId.add(new Coding("http://hl7.org/fhir/v2/0203", "PI"));
        Type typeTransaction = new Type(listCodingTransactionId);
        Identifier idFirTransactionId = new Identifier("official", typeTransaction, Util.getTransactionIdFromSharePref(context));
        identifiers.add(idFirTransactionId);

      /*    "identifier": [
        {
            "use": "usual",
            "type": {
                "coding": [
                    {
                        "system": "http://hl7.org/fhir/v2/0203",
                        "code": "DL"
                    }
                ]
            },
            "value": "12345"
        }

               {
            "use": "official",
            "type": {
                "coding": [
                    {
                        "system": "http://hl7.org/fhir/v2/0203",
                        "code": "PI"
                    }
                ]
            },
            "value": "<<Transaction ID generated at data motion>>"
        }

    ],,*/
        return identifiers;
    }

    private ArrayList<Extension> createExtensionList() {
        ArrayList<Extension> extensions = new ArrayList<>();

        Coding coding1 = new Coding("http: //hl7.org/fhir/v3/Race", "1096-7", "");
        Coding coding2 = new Coding("http: //hl7.org/fhir/v3/Ethnicity", "2162-6", "");
       /* ArrayList<Coding> coding = new ArrayList<>();
        ArrayList<Coding> listcoding2 = new ArrayList<>();
        coding.add(coding1);
        listcoding2.add(coding2);*/

        ValueCodeableConcept valueCodeableConceptObj = new ValueCodeableConcept(coding1);
        ValueCodeableConcept valueCodeableConceptObj1 = new ValueCodeableConcept(coding2);

        ArrayList<ValueCodeableConcept> valueCodeableConcept = new ArrayList<>();
        ArrayList<ValueCodeableConcept> valueCodeableConcept2 = new ArrayList<>();
        valueCodeableConcept.add(valueCodeableConceptObj);

        valueCodeableConcept2.add(valueCodeableConceptObj1);

        extensions.add(new Extension("http: //hl7.org/fhir/StructureDefinition/us-core-race", valueCodeableConcept));
        extensions.add(new Extension("http: //hl7.org/fhir/StructureDefinition/us-core-race", valueCodeableConcept2));
        extensions.add(new Extension(new ValueReference("Location/" + location), "https://www.hl7.org/fhir/location.html"));
/*

       /*  "extension": [
        {
            "valueCodeableConcept": [
                {
                    "coding": {
                        "system": "http://hl7.org/fhir/v3/Race",
                        "code": "1096-7"
                    }
                }
            ],
            "url": "http://hl7.org/fhir/StructureDefinition/us-core-race"
        },
        {
            "url": "https://www.hl7.org/fhir/location.html",
            "valueReference": {
                "reference": "Location/1110101211"
            }
        },
        {
            "url": "http://hl7.org/fhir/StructureDefinition/us-core-race",
            "valueCodeableConcept": [
                {
                    "coding": {
                        "system": "http://hl7.org/fhir/v3/Ethnicity",
                        "code": "2162-6"
                    }
                }
            ]
        }
    ],*/

        return extensions;

    }

    private Name creatName() {
       /* Name name = new ArrayList<>();
        ArrayList<String> family = new ArrayList<>();
        family.add("Kurian");

        ArrayList<String> given = new ArrayList<>();
        family.add("Bibin");

        name.add();*/
        return new Name(mFirstName, mLastname, "official");

        /*""name"": {
            ""use"": ""official"",
                    ""given"": ""Bibin"",
                    ""family"": ""Kurian""*/
    }

    private MaritalStatus createMaterialStatus() {

        ArrayList<Coding> coding = new ArrayList<>();
        String text = "Getrouwd";

        coding.add(new Coding("http: //hl7.org/fhir/v3/MaritalStatus", "U", "Unmarried"));
        MaritalStatus mateMaritalStatus = new MaritalStatus(coding, text);
        return mateMaritalStatus;
    }

    private ArrayList<Telecom> createTelecomeList() {
        ArrayList<Telecom> telecomLsit = new ArrayList<>();
    /*    telecomLsit.add(new Telecom("phone", mMobileNumber, "mobile"));// "408-945-5805"
        telecomLsit.add(new Telecom("email", mEmailId, "home"));*/
        return telecomLsit;

       /* "telecom"": [
        {
            ""system"": ""phone"",
                ""value"": ""408-913-5805"",
                ""use"": ""mobile""
        },
        {
            ""system"": ""email"",
                ""value"": ""bibinkurian@live.com"",
                ""use"": ""home""
        }*/

    }

    private ArrayList<Address> createAddressJson() {
        ArrayList<Address> addresses = new ArrayList<Address>();

       /* String use = "home";
        String state = "CA";
        String city = "Milpitas";
        String postalCode = "95035";
        String country = "en-US";*/


        String url = "http: //hl7.org/fhir/StructureDefinition/us-core-county";
        String valueString = "SantaClaraCounty";
        ArrayList<Extension> extension = new ArrayList<Extension>();
        extension.add(new Extension(url, valueString));


        ArrayList<String> line = new ArrayList<String>();
        line.add("1201SMainS");
        line.add(null);

        // addresses.add(new Address(use, line, state, city, postalCode, country, extension));
        return addresses;


    }

/*

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastname);
        dest.writeString(mBirthDate);
        dest.writeString(gender);
        dest.writeA
    }
*/

    @Override
    public String toString() {
        return "PatientJsonCreater{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mLastname='" + mLastname + '\'' +
                ", mBirthDate='" + mBirthDate + '\'' +
                ", telecom=" + telecom +
                ", address=" + address +
                ", gender='" + gender + '\'' +
                '}';
    }
}
