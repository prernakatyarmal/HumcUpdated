package com.hackensack.umc.datastructure;

import android.util.Log;

import com.hackensack.umc.patient_data.Telecom;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/25/2015.
 */
public class LoginUserData {

    private String mMRN_Id;

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getGender() {
        return mGender;
    }

    public String getBirthDate() {
        return mBirthDate;
    }

    public ArrayList<Address> getAddress() {
        return mAddress;
    }

    public ArrayList<Telecom> getTelephone() {
        return mTelephone;
    }

    private String mFirstName;
    private String mLastName;
    private String mGender;
    private String mBirthDate;
    private String mDrivingLicense;

    public String getPhoto() {
        return mPhoto;
    }

    private String mPhoto;
    ArrayList<Telecom> mTelephone = new ArrayList<Telecom>();
    ArrayList<Address> mAddress = new ArrayList<Address>();

    public LoginUserData(JSONObject jsonData) {

        try {

            if (Util.isJsonDataAvailable(jsonData, Constant.KEY_DOCTOR_ID))
                mMRN_Id = jsonData.getString(Constant.KEY_DOCTOR_ID);

            JSONArray typeArray = Util.getJsonArray(jsonData, Constant.KEY_NAME);

            JSONObject typeObj = typeArray.getJSONObject(0);
            mFirstName = Util.getJsonData(typeObj, Constant.KEY_FIRST_NAME).replace("[", "").replace("]", "").replace("\"", "");
            mLastName = Util.getJsonData(typeObj, Constant.KEY_LAST_NAME).replace("[", "").replace("]", "").replace("\"", "");


            JSONArray telecomArray = Util.getJsonArray(jsonData, Constant.TELECOM_ARRAY);
            Log.v("Telecom", telecomArray.toString());
            if(telecomArray != null && telecomArray.length() > 0) {
                for (int telecount = 0; telecount < telecomArray.length(); telecount++) {

                    Telecom telecom = new Telecom(telecomArray.getJSONObject(telecount));
                    mTelephone.add(telecom);

                }
            }

            if (Util.isJsonDataAvailable(jsonData, Constant.KEY_GENDER))
                mGender = jsonData.getString(Constant.KEY_GENDER);

            if (Util.isJsonDataAvailable(jsonData, Constant.KEY_BIRTH_DAY))
                mBirthDate = jsonData.getString(Constant.KEY_BIRTH_DAY);


            JSONArray addressArray = Util.getJsonArray(jsonData, Constant.ADDRESS_ARRAY);
            if(addressArray != null && addressArray.length() > 0) {
                for (int addresscount = 0; addresscount < addressArray.length(); addresscount++) {

                    Address address = new Address(addressArray.getJSONObject(addresscount));
                    mAddress.add(address);

                }
            }

            JSONArray photoArray = Util.getJsonArray(jsonData, Constant.PHOTO_ARRAY);
            if(photoArray != null && photoArray.length() > 0) {
                for (int photocount = 0; photocount < photoArray.length(); photocount++) {

                    JSONObject photoItem = photoArray.getJSONObject(photocount);
                    String title = Util.getJsonData(photoItem, Constant.KEY_TITLE);

                    if(title != null && title.equals("Selfie")) {
                        mPhoto = Util.getJsonData(photoItem, Constant.PHOTO_DATA);
                    }

                }
            }

            JSONArray identifierArray = Util.getJsonArray(jsonData, Constant.IDENTIFIER_USE);
            if(identifierArray != null && identifierArray.length() > 0) {
                for (int identifiercount = 0; identifiercount < identifierArray.length(); identifiercount++) {

                    JSONObject obj = identifierArray.getJSONObject(identifiercount);

                    String lable = Util.getJsonData(obj, Constant.KEY_LABEL);

                    if(lable != null && lable.equals(Constant.DRIVING_VALUE)) {
                        mDrivingLicense = Util.getJsonData(obj, Constant.TELECOM_VALUE);
                    }

                }
            }


        }catch(Exception e) {

        }

    }

    public String getMRNId() {
        return mMRN_Id;
    }


    public String getDrivingLicense() {
        return mDrivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        mDrivingLicense = drivingLicense;
    }

}
