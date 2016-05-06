package com.hackensack.umc.datastructure;

import android.util.Log;

import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public class SymptomDetail implements Serializable {


//    public String getId() {
//        return mId;
//    }
//
//    public void setId(String mId) {
//        this.mId = mId;
//    }
//
    public String getTitle() {
        return mTitle;
    }

    public String getAdviceText() {
        return mAdviceText;
    }

//
//    public void setTitle(String mTitle) {
//        this.mTitle = mTitle;
//    }
//
//    public String getSpecialTopic() {
//        return mSpecialTopic;
//    }
//
//    public void setSpecialTopic(String mSpecialTopic) {
//        this.mSpecialTopic = mSpecialTopic;
//    }
//
    public boolean getPediatric() {
        return mPediatric;
    }
//
//    public void setPediatric(String mPediatric) {
//        this.mPediatric = mPediatric;
//    }
//
//    public String getGender() {
//        return mGender;
//    }
//
//    public void setGender(String mGender) {
//        this.mGender = mGender;
//    }
//
//    public ArrayList<String> getBodyArea() {
//        return mBodyArea;
//    }
//
//    public void setBodyArea(ArrayList<String> mBodyArea) {
//        this.mBodyArea = mBodyArea;
//    }
//
//    public ArrayList<String> getKeywords() {
//        return mKeywords;
//    }
//
//    public void setKeywords(ArrayList<String> mKeywords) {
//        this.mKeywords = mKeywords;
//    }

//    private String mId = "" ;
    private String mTitle = "" ;
//    private String mSpecialTopic = "" ;
    private Boolean mPediatric = false ;
//    private String mGender = "" ;
//    private String mBackground = "" ;
//    private String mBodyAreas = "" ;
//    private String mAdviceTitle = "" ;
    private String mAdviceText = "" ;
//    private String mAdviceFooter = "" ;
//    private ArrayList<String> mBodyArea = new ArrayList<String>();
//    private ArrayList<String> mKeywords = new ArrayList<String>();

    public SymptomDetail(JSONObject jsonItem) {

        try{


            JSONObject item = jsonItem.getJSONObject(Constant.KEY_TOPIC);

            if (Util.isJsonDataAvailable(item, Constant.KEY_ADVICE)) {

                if (Util.isJsonDataAvailable(item, Constant.KEY_TITLE))
                    mTitle = item.getString(Constant.KEY_TITLE);

                if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_PEDIATRIC))
                    mPediatric = item.getBoolean(Constant.KEY_PEDIATRIC);

                item = item.getJSONObject(Constant.KEY_ADVICE);

                if (Util.isJsonDataAvailable(item, Constant.KEY_TEXT))
                    mAdviceText = item.getString(Constant.KEY_TEXT);

            }



//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_ID))
//                mId = jsonItem.getString(Constant.KEY_ID);
//
//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_TITLE))
//                mTitle = jsonItem.getString(Constant.KEY_TITLE);
//
//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_SPECIAL_TOPIC))
//                mSpecialTopic = jsonItem.getString(Constant.KEY_SPECIAL_TOPIC);
//
//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_PEDIATRIC))
//                mPediatric = jsonItem.getString(Constant.KEY_PEDIATRIC);
//
//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_GENDER))
//                mGender = jsonItem.getString(Constant.KEY_GENDER);

//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_BACKGROUND))
//                mAdviceText = jsonItem.getString(Constant.KEY_BACKGROUND);

//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_BODY_AREAS)) {
//
//                JSONArray bodyAreaArray = jsonItem.getJSONArray(Constant.KEY_BODY_AREAS);
//
//                for (int count = 0; count < bodyAreaArray.length(); count++) {
//
//                    String str = new String();
//                    str = bodyAreaArray.getString(count);
//                    mBodyArea.add(str);
//
//                }
//            }
//
//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_KEYWORDS)) {
//
//                JSONArray keywordArray = jsonItem.getJSONArray(Constant.KEY_KEYWORDS);
//
//
//                for (int keywordCount = 0; keywordCount < keywordArray.length(); keywordCount++) {
//
//                    String str = new String();
//                    str = keywordArray.getString(keywordCount);
//                    mKeywords.add(str);
//
//                }
//            }
//
//
//            if(Util.isJsonDataAvailable(jsonItem, Constant.KEY_ADVICE)) {
//
//                JSONObject adviceObj = jsonItem.getJSONObject(Constant.KEY_ADVICE);
//
//                mAdviceTitle = adviceObj.getString(Constant.KEY_TITLE) ;
//                mAdviceText = adviceObj.getString(Constant.KEY_TEXT) ;
//                mAdviceFooter = adviceObj.getString(Constant.KEY_FOOTER) ;
//
//            }

        }catch(Exception e) {

            Log.e("Error", "SymptomList Update : "+e.toString());

        }


//        class Advice {
//
//            String mTitle ;
//            String mText ;
//            String mFooter ;
//
//        }

    }

    public boolean isDataAvailable(JSONObject obj, String key) {

        boolean dataAvailable = false ;

        if(obj.has(key) && !obj.isNull(key)) {
            dataAvailable = true ;
        }

        return dataAvailable;
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



