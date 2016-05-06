package com.hackensack.umc.datastructure;

import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONObject;

/**
 * Created by prerana_katyarmal on 10/25/2015.
 */
public class BaseToken {

    private String mAccessToken;
    private int mExpireIn;
    private String mTokenType;

    public BaseToken(JSONObject jsonData) {

        try {

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_ACCESS))
                mAccessToken = jsonData.getString(Constant.TOKEN_KEY_ACCESS);

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_EXPIRE_IN))
                mExpireIn = jsonData.getInt(Constant.TOKEN_KEY_EXPIRE_IN);

            if (Util.isJsonDataAvailable(jsonData, Constant.TOKEN_KEY_TYPE))
                mTokenType = jsonData.getString(Constant.KEY_TEXT);

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

}
