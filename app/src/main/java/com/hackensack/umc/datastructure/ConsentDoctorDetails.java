package com.hackensack.umc.datastructure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.hackensack.umc.R;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public class ConsentDoctorDetails implements Serializable {

    String mFirstName = "" ;
    String mSecondName = "" ;
    String mId = "" ;

    Address mAddress;

    String mGender = "" ;
    DoctorImage mDoctorImage = new DoctorImage();
    /**
     * Favorite code
     */
    private String mPhoneNum = "";
    private String mDocImageUrl = "";

    public String getDocImageUrl() {

        if(URLUtil.isHttpUrl(mDocImageUrl)) {
            Log.e("URL", "url -> "+mDocImageUrl);
            return mDocImageUrl;
        }else if(URLUtil.isHttpsUrl(mDocImageUrl)) {
            Log.e("URL", "url -> "+mDocImageUrl);
            return mDocImageUrl;
        }

        return null;
    }

    public void setmDocImageUrl(String mDocImageUrl) {
        this.mDocImageUrl = mDocImageUrl;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getmPhoneNum() {
        return mPhoneNum;
    }

    public void setmPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }

    public Address getAddress() {
        return mAddress;
    }


    public String getDoctorFirstName() {
        return mFirstName;
    }

    public void setDoctorFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getDoctorSecondName() {
        return mSecondName;
    }

    public void setDoctorSecondName(String secondName) {
        this.mSecondName = secondName;
    }

    public String getDoctorId() {
        return mId;
    }

    public void setDoctorId(String id) {
        this.mId = id;
    }

    public DoctorImage getDoctorImage() {
        return mDoctorImage;
    }

    public void setDoctorImage(DoctorImage doctorImage) {
        this.mDoctorImage = doctorImage;
    }

    public class DoctorImage implements Serializable, HttpDownloadCompleteListener {

        String mImageUrl = null ;
        transient ImageView mImageView ;
        transient Bitmap mDoctorImg = null;
        boolean isDoctorImgSet = false;
        String encodedImage = null;

        public void setDoctorImg(Context context, ImageView imageview, ArrayList<Integer> urlArray) {

                mImageView = imageview;

//                if (mGender != null && mGender.equals(Constant.FEMALE_BODY)) {
//                    mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.female));
//                } else {
//                    mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.male));
//                }

                if (!TextUtils.isEmpty(getDocImageUrl()))
                    mImageUrl = getDocImageUrl();

                if (!isDoctorImgSet) {
                    if (mImageUrl != null && mImageUrl.length() > 0) {
                        HttpDownloader http = new HttpDownloader(context, mImageUrl, Constant.DOCTOR_IMAGE_DATA, DoctorImage.this);
                        http.startDownloading();
                    } else {
                        isDoctorImgSet = true;
                    }
                    if (mGender != null && mGender.equals(Constant.FEMALE_BODY)) {
                        mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.female));
                    } else {
                        mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.male));
                    }
                }else {
//                    isDoctorImgSet = true;
                    if (mDoctorImg != null) {
                        mImageView.setImageBitmap(mDoctorImg);
                    }else if(encodedImage != null) {

                        byte[] imageAsBytes = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                        mImageView.setImageBitmap(bitmap);

                    }else {
                        if (mGender != null && mGender.equals(Constant.FEMALE_BODY)) {
                            mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.female));
                        } else {
                            mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.male));
                        }
                    }
//                else {
//                    HttpDownloader http = new HttpDownloader(context, mImageUrl, Constant.DOCTOR_IMAGE_DATA, DoctorImage.this);
//                    http.startDownloading();
//                }
                }
        }

        public boolean isDoctorImgSet() {
            return isDoctorImgSet;
        }

        @Override
        public void HttpDownloadCompleted(HttpResponse data) {

            Bitmap bitmap = (Bitmap) data.getDataObject();

            if(bitmap != null) {
//                if(mImageView.isShown()) {
//                    mImageView.setImageBitmap(bitmap);
//                }

                isDoctorImgSet = true;
                mDoctorImg = bitmap ;

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            }
        }

    }


    public ConsentDoctorDetails() {

    }

    public ConsentDoctorDetails(JSONObject jsonItem) {

        try{

            if(Util.isJsonDataAvailable(jsonItem, Constant.IDENTIFIER_USE)) {

                JSONArray identifierArray = jsonItem.getJSONArray(Constant.IDENTIFIER_USE);
                if (identifierArray != null && identifierArray.length() > 0) {

                    for (int identifiercount = 0; identifiercount < identifierArray.length(); identifiercount++) {

                        JSONObject obj = identifierArray.getJSONObject(identifiercount);
                        mId = Util.getJsonData(obj, Constant.TELECOM_VALUE);

                    }
                }
            }

            JSONObject jsonObj = jsonItem.getJSONObject(Constant.KEY_NAME);
            JSONArray firstArray = Util.getJsonArray(jsonObj, Constant.KEY_FIRST_NAME);
            if(firstArray != null && firstArray.length() > 0) {

                for(int count = 0; count < firstArray.length(); count++) {

                    mFirstName = firstArray.getString(count);

                }

            }
            JSONArray secondArray = Util.getJsonArray(jsonObj, Constant.KEY_LAST_NAME);
            if(secondArray != null && secondArray.length() > 0) {

                for(int count = 0; count < secondArray.length(); count++) {

                     mSecondName = secondArray.getString(count);

                }

            }

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_ADDRESS)) {

                JSONArray addressArray = jsonItem.getJSONArray(Constant.KEY_ADDRESS);

                for (int count = 0; count < addressArray.length(); count++) {

                    mAddress = new Address(addressArray.getJSONObject(count));

                }
            }

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_IMAGE_URL)) {
                mDoctorImage.mImageUrl = jsonItem.getString(Constant.KEY_IMAGE_URL);
                if (mDoctorImage.mImageUrl != null && !mDoctorImage.mImageUrl.startsWith("\"http://\"")) {
                    mDoctorImage.mImageUrl = "http://" + mDoctorImage.mImageUrl;
                }
                setmDocImageUrl(mDoctorImage.mImageUrl);
            }

        }catch(Exception e) {

            Log.e("Error", "DocotorDetail Update : "+e.toString());

        }

    }


//    @Override
//    public String toString() {
//
////        return "mFullUrl : "+mFullUrl+"\n mId : "+mId+"\n mResourceType : "+mResourceType+"\n mActive : "+mActive+"\n mGender : "+mGender+"\n mBirthDate : "+mBirthDate+
////                "\n mNameUse : "+mNameUse+"\n mNameFamily : "+mNameFamily.toString()+"\n mNameGiven : "+mNameGiven.toString()+"\n mNameSuffix : "+mNameSuffix.toString()+"\n mAddress : "+mAddress.toString()
////                +"\n mTelecom : "+mTelecom.toString()+"\n mSpeciality : "+mSpeciality.toString();
//        return "";
//    }

}


