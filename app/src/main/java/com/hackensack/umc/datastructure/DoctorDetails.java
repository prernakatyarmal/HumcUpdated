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
import java.util.Iterator;

/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public class DoctorDetails implements Serializable {


    String mDegree = "" ;
    String mFullName = "" ;
    String mHumcId = "" ;
    String mNpi = "" ;
    String mSpeciality = "" ;
    String mGender = "" ;
    DoctorImage mDoctorImage = new DoctorImage();
    ArrayList<Slot> mSlot = new ArrayList<Slot>();

    ArrayList<Address> mAddress = new ArrayList<Address>();
    /**
     * Favorite code
     */
    private String mFirstAddress = "";
    private String mPhoneNum = "";
    private String mDocImageUrl = "";
    private String mDocDistance="";
    public String getmDocDistance() {
        return mDocDistance;
    }

    Distance mDistance = new Distance();

    public void setmDocDistance(String mDocDistance) {
        this.mDocDistance = mDocDistance;
    }



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



    private long dbEntryId;

    public long getDbEntryId() {
        return dbEntryId;
    }

    public void setDbEntryId(long dbEntryId) {
        this.dbEntryId = dbEntryId;
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

    public String getmFirstAddress() {
        return mFirstAddress;
    }

    public void setmFirstAddress(String mFirstAddress) {
        this.mFirstAddress = mFirstAddress;
    }


    public String getDoctorDegree() {
        return mDegree;
    }

    public void setDoctorDegree(String mDegree) {
        this.mDegree = mDegree;
    }

    public String getDoctorFullName() {
        return mFullName;
    }

    public void setDoctorFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getDoctorHumcId() {
        return mHumcId;
    }

    public void setDoctorHumcId(String mHumcId) {
        this.mHumcId = mHumcId;
    }

    public DoctorImage getDoctorImage() {
        return mDoctorImage;
    }

    public void setDoctorImage(DoctorImage doctorImage) {
        this.mDoctorImage = doctorImage;
    }

    public String getDoctorNpi() {
        return mNpi;
    }

    public void setDoctorNpi(String mNpi) {
        this.mNpi = mNpi;
    }

    public String getDoctorSpeciality() {
        return mSpeciality;
    }

    public void setDoctorSpeciality(String mSpeciality) {
        this.mSpeciality = mSpeciality;
    }

    public ArrayList<Slot> getDoctorTimeSlot() {
        return mSlot;
    }

    public void setDoctorTimeSlot(ArrayList<Slot> mSlot) {
        this.mSlot = mSlot;
    }

    public String getDoctorFirstAddress() {

        if(mAddress!= null && mAddress.size() >= 1) {
            Address address = mAddress.get(0);
            if (address != null) {
                return address.getAddress();
            }
        }

        return "";
    }

    public String getDoctorFirstAddressWithZipcode(String zipcode) {

        String addressStr = "";

        if(mAddress!= null && mAddress.size() >= 1) {
            for(Address address : mAddress) {
                if (address != null && address.getZip().equals(zipcode)) {
                    addressStr = address.getAddress();
                    break;
                }
            }
//            Address address = mAddress.get(0);
//            if (address != null && address.getZip().equals(zipcode)) {
//                return address.getAddress();
//            }
        }

        return addressStr;
    }

    public String getDoctorFirstPhone() {

        if(mAddress!= null && mAddress.size() >= 1) {
            Address address = mAddress.get(0);
            if (address != null) {
                return address.getPhone();
            }
        }
        return "";
    }

    public ArrayList<Address> getAddress() {
        return mAddress;
    }

    public void setDoctorAddress(ArrayList<Address> mAddress) {
        this.mAddress = mAddress;
    }

    public class DoctorImage implements Serializable, HttpDownloadCompleteListener {

        String mImageUrl = null ;
        transient ImageView mImageView ;
        transient Bitmap mDoctorImg = null;
        boolean isDoctorImgSet = false;
        String encodedImage = null;
//        Integer npiNumber = 0;

        public void setDoctorImg(Context context, ImageView imageview, ArrayList<Integer> urlArray) {

            String npi = getDoctorNpi();
            Integer npiNumber = 0;

            try {
                npiNumber = Integer.parseInt(npi);
            }catch(Exception e) {
                npiNumber = -1;
            }

                mImageView = imageview;

//                mImageView.setId(npiNumber);

//                if (mGender != null && mGender.equals(Constant.FEMALE_BODY)) {
//                    mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.female));
//                } else {
//                    mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.male));
//                }

                if (!TextUtils.isEmpty(getDocImageUrl()))
                    mImageUrl = getDocImageUrl();

                if (!isDoctorImgSet) {
                    if (mImageUrl != null && mImageUrl.length() > 0) {
                        if(!urlArray.contains(npiNumber)) {
                            HttpDownloader http = new HttpDownloader(context, mImageUrl, Constant.DOCTOR_IMAGE_DATA, DoctorImage.this);
                            http.startDownloading();
                            
                            if(npiNumber != -1)
                                urlArray.add(npiNumber);
                        }
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
//                if(mImageView != null && mImageView.getId() == npiNumber) {
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

    public class Distance implements Serializable, HttpDownloadCompleteListener {

        public void setDistance() {

        }

        @Override
        public void HttpDownloadCompleted(HttpResponse data) {

            String distance = (String) data.getDataObject();

            if(distance != null) {
                mDocDistance = distance;
            }

        }
    }

    public class Address implements Serializable {

        String mPhone = "" ;
        String mState = "" ;
        String mSuite = "" ;
        String mZip = "" ;

        String mBuilding = "" ;
        String mCity = "" ;
        String mStreet = "" ;
        String mFax = "" ;

        String mGroupName = "" ;
        String mHA = "" ;

        public Address() {
        }

        public void setmPhone(String mPhone) {
            this.mPhone = mPhone;
        }

        public void setmState(String mState) {
            this.mState = mState;
        }

        public void setmSuite(String mSuite) {
            this.mSuite = mSuite;
        }

        public void setmZip(String mZip) {
            this.mZip = mZip;
        }

        public String getSuite() {
            return mSuite;
        }

        public String getState() {
            return mState;
        }

        public String getZip() {
            return mZip;
        }

        public String getAddress() {

            String address = "";
if(mStreet.length() > 1) {
                address = mStreet ;
            }
            if(mSuite.length() > 1) {
                address = address + ", " + mSuite ;
            }

            address = address + "\n" ;

            if(mState.length() > 1) {
                address = address + mState ;
            }
            if(mZip.length() > 1) {
                address = address + ", " + mZip ;
            }

            return address ;
        }

        public String getPhone() {
            return mPhone ;
        }


        public String getBuilding() {
            return mBuilding;
        }

        public void setBuilding(String mBuilding) {
            this.mBuilding = mBuilding;
        }

        public String getCity() {
            return mCity;
        }

        public void setCity(String mCity) {
            this.mCity = mCity;
        }

        public String getStreet() {
            return mStreet;
        }

        public void setStreet(String mStreet) {
            this.mStreet = mStreet;
        }

        public String getFax() {
            return mFax;
        }

        public void setFax(String mFax) {
            this.mFax = mFax;
        }

        public String getGroupName() {
            return mGroupName;
        }

        public void setGroupName(String mGroupName) {
            this.mGroupName = mGroupName;
        }

        public String getHA() {
            return mHA;
        }

        public void setHA(String mHA) {
            this.mHA = mHA;
        }

    }

    class Slot implements Serializable {

        String mDate = "";
        String[] mTime = {"", "",};

    }

    public DoctorDetails() {

    }

    public DoctorDetails(JSONObject jsonItem) {

        try{

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_DEGREE))
                mDegree = jsonItem.getString(Constant.KEY_DEGREE);

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_FULL_NAME))
                mFullName = jsonItem.getString(Constant.KEY_FULL_NAME);

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_HUMC_ID))
                mHumcId = jsonItem.getString(Constant.KEY_HUMC_ID);

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_IMAGE_URL)) {
                mDoctorImage.mImageUrl = jsonItem.getString(Constant.KEY_IMAGE_URL);
                if (mDoctorImage.mImageUrl != null && !mDoctorImage.mImageUrl.startsWith("\"http://\"")) {
                    mDoctorImage.mImageUrl = "http://" + mDoctorImage.mImageUrl;
                }
                setmDocImageUrl(mDoctorImage.mImageUrl);
            }
            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_NPI_ID))
                mNpi = jsonItem.getString(Constant.KEY_NPI_ID);

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_SPECIALTY))
                mSpeciality = jsonItem.getString(Constant.KEY_SPECIALTY);

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_GENDER))
                mGender = jsonItem.getString(Constant.KEY_GENDER);

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_ADDRESS)) {

                JSONArray addressArray = jsonItem.getJSONArray(Constant.KEY_ADDRESS);

                for (int count = 0; count < addressArray.length(); count++) {

                    Address address = new Address();

                    JSONObject jsonObj = addressArray.getJSONObject(count);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_SUITE))
                        address.mSuite = jsonObj.getString(Constant.KEY_ADDRESS_SUITE);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_STREET))
                        address.mStreet = jsonObj.getString(Constant.KEY_ADDRESS_STREET);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_STATE))
                        address.mState = jsonObj.getString(Constant.KEY_ADDRESS_STATE);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_PHONE))
                        address.mPhone = jsonObj.getString(Constant.KEY_ADDRESS_PHONE);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_ZIP))
                        address.mZip = jsonObj.getString(Constant.KEY_ADDRESS_ZIP);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_BUILDING))
                        address.mBuilding = jsonObj.getString(Constant.KEY_ADDRESS_BUILDING);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_CITY))
                        address.mCity = jsonObj.getString(Constant.KEY_ADDRESS_CITY);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_FAX))
                        address.mFax = jsonObj.getString(Constant.KEY_ADDRESS_FAX);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_GROUP_NAME))
                        address.mGroupName = jsonObj.getString(Constant.KEY_ADDRESS_GROUP_NAME);

                    if(Util.isJsonDataAvailable(jsonObj, Constant.KEY_ADDRESS_HA))
                        address.mHA = jsonObj.getString(Constant.KEY_ADDRESS_HA);

                    mAddress.add(address);
                }
            }

            JSONObject slotObj = null;

            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_SLOT))
                slotObj = jsonItem.getJSONObject(Constant.KEY_SLOT);


            if(slotObj != null) {

                Iterator<?> keyset = slotObj.keys();

                while (keyset.hasNext()) {

                    Slot slot = new Slot();
                    String key = (String) keyset.next();
                    String time = slotObj.getString(key);

                    String[] str = time.split(",");
                    slot.mDate = key;
                    slot.mTime = str;
                    Log.i("slot", "Time : " + slot.toString());

//                2015-10-02T12:16:02.627Z
//                ["7:00","8:00","9:00","10:00","11:00","12:00"]

                }
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


