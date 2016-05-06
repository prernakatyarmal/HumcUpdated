package com.hackensack.umc.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.hackensack.umc.response.AccessTokenResponse;
import com.hackensack.umc.response.DatamotionTokenCredential;
import com.hackensack.umc.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prerana_katyarmal on 11/26/2015.
 */
public class CommonAPICalls {

    private SharedPreferences getEpicAccessToken;
    private Context context;
    private SharedPreferences sharedPreferences;

    public CommonAPICalls(Context context) {
        initialisevariables(context);
        this.context = context;
    }

    /*public String getDatamotionAccsessToken() {
        // if (!isDataMotionTokenValid()) {
        //new getCredentialsForDatamotionCall().execute();
        //    return sendTokencallForDataMotion();

       *//* } else {
            return sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_DATA_MOTION, "");
        }*//*
        return null;
    }*/


    public String sendTokencallForDataMotion(DatamotionTokenCredential datamotionTokenCredential) {
        String response = HttpUtils.httpPost(context, ServerConstants.URL_TOKEN, createDataMotionTokenCallParameters(datamotionTokenCredential), createTokenCallHeaders());
        Log.v("DtokenResponse Is::", response);
        if (response != null) {
            response = parseJson(response);
        } else {
            return null;
        }
        return response;


    }

    private List<NameValuePair> createDataMotionTokenCallParameters(DatamotionTokenCredential datamotionTokenCredential) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(ServerConstants.SERVER_PARA_GRANT_TYPE, ServerConstants.SERVER_PARA_PASSWORD));
        nameValuePairs.add(new BasicNameValuePair(ServerConstants.SERVER_PARA_USER_NAME, datamotionTokenCredential.getUsername()));
        nameValuePairs.add(new BasicNameValuePair(ServerConstants.SERVER_PARA_PASSWORD, datamotionTokenCredential.getPassword()));
        return nameValuePairs;

    }

    private List<NameValuePair> createTokenCallHeaders() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));//header can be append here also
        return headers;
    }

    public List<NameValuePair> createTokenForDatamotionCall() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Authorization", " Bearer " + HttpDownloader.getToken(context)));//header can be append here also
        return headers;
    }

    private String parseJson(String response) {
        String token;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            JSONObject jsonObject = new JSONObject(response);
            token = jsonObject.getString("access_token");
            editor.putString(ServerConstants.ACCESS_TOKEN_DATA_MOTION, "Bearer " + token);
            editor.putInt(Constant._DATAMOTIONTOKE_EXPIRE_TIME_STRING, jsonObject.getInt(Constant.TOKEN_KEY_EXPIRE_IN));
            editor.putLong(Constant.DATAMOTION_TOKEN_CURRENT_TIME_STRING, System.currentTimeMillis());
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;

        }
        return token;
    }

    private void initialisevariables(Context context) {
        sharedPreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, context.MODE_PRIVATE);
    }


    public String getEpicAccessToken() {
        String accessTokenEpic = "";
        // if(!isEpicTokenValid()) {
        AccessTokenResponse epicAccessToken = null;
        String accessToken = HttpUtils.httpPost(context,
                ServerConstants.URL_TOKEN_FOR_EPIC, null,
                createHeaderEpicToken());
        // Log.v("Epic Token is::", accessToken);
        if (accessToken != null) {
            epicAccessToken = parseTokenResponse(accessToken);
            accessTokenEpic = epicAccessToken.getAccess_token();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ServerConstants.ACCESS_TOKEN_EPIC, accessTokenEpic);
            editor.putInt(Constant.EPIC_EXPIRE_TIME_STRING, epicAccessToken.getExpires_in());
            editor.putLong(Constant.EPIC_CURRENT_TIME_STRING, System.currentTimeMillis());
            editor.commit();
        }
        /*}else{
            accessTokenEpic=sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC,"");
        }*/

        return accessTokenEpic;

    }

    private List<NameValuePair> createHeaderEpicToken() {

        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Authorization",
                ServerConstants.CREDENTIAL_FOR_EPIC_TOKEN));
        return headers;

    }

    private AccessTokenResponse parseTokenResponse(String response) {
        //Log.v("response in parse", response);

        Gson gson = new Gson();
        AccessTokenResponse accessTokenResponse = gson.fromJson(response,
                AccessTokenResponse.class);
        return accessTokenResponse;

    }

    public boolean isDataMotionTokenValid() {
        long tokenTime = sharedPreferences.getLong(Constant.DATAMOTION_TOKEN_CURRENT_TIME_STRING, 0);
        tokenTime = System.currentTimeMillis() - tokenTime;
        if (System.currentTimeMillis() >= tokenTime) {

            int timeInSecond = (int) tokenTime / 1000;
            if (timeInSecond < sharedPreferences.getInt(Constant._DATAMOTIONTOKE_EXPIRE_TIME_STRING, 0)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean isEpicTokenValid() {
        long tokenTime = sharedPreferences.getLong(Constant.EPIC_CURRENT_TIME_STRING, 0);
        tokenTime = System.currentTimeMillis() - tokenTime;
        if (System.currentTimeMillis() >= tokenTime) {

            int timeInSecond = (int) tokenTime / 1000;
            if (timeInSecond < sharedPreferences.getInt(Constant.EPIC_EXPIRE_TIME_STRING, 0)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private class getCredentialsForDatamotionCall extends AsyncTask<Void, Void, DatamotionTokenCredential> {

        @Override
        protected DatamotionTokenCredential doInBackground(Void... params) {
            String response = HttpUtils.getHttpGetResponse(context, ServerConstants.GET_CREDENTIALS_FOR_ACCESS_TOKEN, createTokenForDatamotionCall());
            Log.v("DatamotionCallResponse:", response);

            DatamotionTokenCredential datamotionTokenCredential = null;
            Gson gson = new Gson();
            try {

            } catch (Exception e) {
                datamotionTokenCredential = gson.fromJson(response, DatamotionTokenCredential.class);
            }
            return datamotionTokenCredential;
        }

        @Override
        protected void onPostExecute(DatamotionTokenCredential datamotionTokenCredential) {
            super.onPostExecute(datamotionTokenCredential);
            if (datamotionTokenCredential != null) {
                new GetAccessTokenCallDataMotion().execute(datamotionTokenCredential);
            }
        }
    }

    private class GetAccessTokenCallDataMotion extends AsyncTask<DatamotionTokenCredential, Void, String> {

        @Override
        protected String doInBackground(DatamotionTokenCredential... datamotionTokenCredentials) {
            return sendTokencallForDataMotion(datamotionTokenCredentials[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("GetAccessToken", "GetAccessTokenCallDataMotion::");
            //  startProgress(getString(R.string.scanning_images));
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);
            //  dismissProgressDialog();
            if (token != null) {
                //if (checkValidationsBeforeCall()) {

            }
        }


    }

}

   /* Get Third Party API credentials

    Request URL:

    GET https://hackensackumc-prod.apigee.net/v1/api/credentials?client=datamotion//
    GET https://hackensackumc-prod.apigee.net/v1/api/credentials?client=selfcare

    Headers:

    Authorization: Bearer <access_token>

    Sample Response:

    {
        "username": "charles_cheskiewicz@persistent.com",
            "password": "oPxvapvsqO$f6QEuhY0DV"
    }

    Thanks,
    Bibin*/

