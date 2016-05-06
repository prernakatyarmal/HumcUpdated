package com.hackensack.umc.datastructure;

import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONObject;

/**
 * Created by prerana_katyarmal on 10/25/2015.
 */
public class UserToken {

    private String mAccessToken;
    private int mExpireIn;
    private String mTokenType;
    private String mMRN_Id;
    private String mRefresh_Token;

    @Override
    public String toString() {
        return "UserToken{" +
                "mAccessToken='" + mAccessToken + '\'' +
                ", mExpireIn=" + mExpireIn +
                ", mTokenType='" + mTokenType + '\'' +
                ", mMRN_Id='" + mMRN_Id + '\'' +
                ", mRefresh_Token='" + mRefresh_Token + '\'' +
                '}';
    }

    public UserToken(JSONObject jsonData) {

        try {

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_ACCESS))
                mAccessToken = jsonData.getString(Constant.TOKEN_KEY_ACCESS);

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_EXPIRE_IN))
                mExpireIn = jsonData.getInt(Constant.TOKEN_KEY_EXPIRE_IN);

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_TYPE))
                mTokenType = jsonData.getString(Constant.TOKEN_KEY_TYPE);

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_MRN_ID))
                mMRN_Id = jsonData.getString(Constant.TOKEN_KEY_MRN_ID);

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_REFRESH))
                mRefresh_Token = jsonData.getString(Constant.TOKEN_KEY_REFRESH);

        }catch(Exception e) {

        }

    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public int getExpireInTime() {
        return mExpireIn;
    }

    public String getMRNId() {
        return mMRN_Id;
    }

    public String getRefreshToken() {
        return mRefresh_Token;
    }

}
