package com.hackensack.umc.http;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.hackensack.umc.activity.BaseActivity;
import com.hackensack.umc.activity.ConsentResultActivity;
import com.hackensack.umc.activity.ConsentSearchActivity;
import com.hackensack.umc.activity.CreatePasswordActivity;
import com.hackensack.umc.activity.DoctorAppointmentSummaryActivity;
import com.hackensack.umc.activity.LoginActivity;
import com.hackensack.umc.activity.RegistrationDetailsActivity;
import com.hackensack.umc.activity.ViewAppointmentActivity;
import com.hackensack.umc.datastructure.AppointmentDetails;
import com.hackensack.umc.datastructure.BaseToken;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.UserToken;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.response.DatamotionTokenCredential;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.JsonParser;
import com.hackensack.umc.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import static com.hackensack.umc.util.Constant.DOCTOR_IMAGE_DATA;
import static com.hackensack.umc.util.Constant.DOCTOR_OFFICE_DATA;
import static com.hackensack.umc.util.Constant.DOCTOR_RESULT_DATA;
import static com.hackensack.umc.util.Constant.DOCTOR_SPECIALTY_DATA;

//import com.hackensack.umc.activity.ConsentResultActivity;


/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public class HttpDownloader {

    private static final String TAG = "HttpDownloader";
    private HttpDownloadCompleteListener mHTTPDownloadCompleteListener;

    private Context mContext;
    private String mUrl;
    private int mDataType;

    private ArrayList<Integer> mDoctorID;

    private HttpDownloadCompleteListener mCalledByObject;
    private Handler mHandler = new Handler(Looper.getMainLooper());
//    private String progressMessage="Loading....";
//    private AlertDialog mProgressDialog;

    public HttpDownloader(Context mContext, String url, int mDataType, HttpDownloadCompleteListener mCalledByObject) {
        this.mContext = mContext;
        mUrl = url;
        this.mDataType = mDataType;
        this.mCalledByObject = mCalledByObject;
    }

    public HttpDownloader(Context context, String url, int dataType, HttpDownloadCompleteListener calledByObject, String progressMessage) {

        mContext = context;
        mUrl = url;
        mDataType = dataType;
        mCalledByObject = calledByObject;
//        this.progressMessage=progressMessage;
    }

    public HttpDownloader(Context mContext, String url, int mDataType, HttpDownloadCompleteListener mCalledByObject, ArrayList<Integer> doctorID) {
        this.mContext = mContext;
        mUrl = url;
        this.mDataType = mDataType;
        this.mCalledByObject = mCalledByObject;
        mDoctorID = doctorID;
    }

    public boolean startDownloading() {

        boolean httpCallSuccess = false;

        /***
         * Test code for Tablets
         */
//        mHandler.post(new Runnable() {
//
//            public void run() {
//
//                (new HTTPDownloaderAsyncTask()).execute("url");
//
//            }
//        });

        if (Util.isNetworkAvailable(mContext)) {
            (new HTTPDownloaderAsyncTask()).execute(mUrl);
            httpCallSuccess = true;
			if(mDataType == Constant.LOGOUT_DATA) {
	            BaseActivity.isUserLogout = true;
                Util.stopUserSession(mContext);
	        }
        }
       /* else{

            Util.showNetworkOfflineDialog((Activity)mContext,mContext.getString(R.string.network_connect_title),mContext.getString(R.string.please_check_network));
        }*/

        return httpCallSuccess;
    }

    class HTTPDownloaderAsyncTask extends AsyncTask<String, Void, HttpResponse> {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog=builder.create();
//            mProgressDialog.setMessage(progressMessage);
//            mProgressDialog.show();
        }

        @Override
        protected HttpResponse doInBackground(String... url) {

            HttpResponse parseData = new HttpResponse();
            parseData.setRequestType(mDataType);
            try {

                StringBuilder serverData = new StringBuilder();
                String serverResponseFromHttpUtils = null;

                URI serverUrl = new URI(url[0]);

                switch (mDataType) {

                    case Constant.SYMPTOM_LIST_DATA_MALE:
                    case Constant.SYMPTOM_LIST_DATA_FEMALE:
                    case Constant.SYMPTOM_LIST_DATA_CHILD:
                    case Constant.SYMPTOM_ITEM_DATA:

                        URI uri = new URI(url[0]);
                        if (!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)) {
                            HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) uri.toURL().openConnection();
                            httpsUrlConnection.setRequestMethod("GET");
                            httpsUrlConnection.connect();
                            int symptomServerResponse = httpsUrlConnection.getResponseCode();
                            if (symptomServerResponse >= Constant.HTTP_OK && symptomServerResponse < Constant.HTTP_REDIRECT) {

                                BufferedReader br = new BufferedReader(new InputStreamReader(httpsUrlConnection.getInputStream()));

                                String line;
                                while ((line = br.readLine()) != null) {
                                    serverData.append(line + "\n");
                                }
                                br.close();

                            }
                        } else {
                            //Code for JellyBean and lower version
                            serverData = new StringBuilder(HttpUtils.getHttpGetResponse(mContext, uri.toURL().toString(), null));
                        }

                        break;

                    case Constant.DOCTOR_OFFICE_DATA:
                    case Constant.DOCTOR_RESULT_DATA:
                    case Constant.DOCTOR_SPECIALTY_DATA:
                    case Constant.READ_PATIENT_DATA:
                    case Constant.LOGOUT_DATA:
                    case Constant.VIEW_APPOINTMENT:

                        String token = getToken(mContext);

                        if (!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)) {


                            HttpsURLConnection urlHttpsConnection = (HttpsURLConnection) serverUrl.toURL().openConnection();

                            urlHttpsConnection.setRequestProperty("Authorization", "Bearer " + token);
                            urlHttpsConnection.setRequestProperty("Connection", "close");
                            if(mDataType == Constant.LOGOUT_DATA) {
                                urlHttpsConnection.setRequestMethod("DELETE");
                            }else {
                                urlHttpsConnection.setRequestMethod("GET");
                            }
//                        urlHttpsConnection.setRequestProperty("Content-length", "0");
                            urlHttpsConnection.setUseCaches(false);
                            urlHttpsConnection.setAllowUserInteraction(false);
//                            urlHttpsConnection.setConnectTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
//                            urlHttpsConnection.setReadTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
                            urlHttpsConnection.setDoInput(true);

//                        urlHttpsConnection.setRequestProperty("Content-Type", "application/json");
                            urlHttpsConnection.setRequestProperty("charset", "utf-8");

                            urlHttpsConnection.connect();

                            int httpsServerResponce = urlHttpsConnection.getResponseCode();
                            parseData.setResponseCode(httpsServerResponce);

                            if (httpsServerResponce >= Constant.HTTP_OK && httpsServerResponce < Constant.HTTP_REDIRECT) {

                                BufferedReader br = new BufferedReader(new InputStreamReader(urlHttpsConnection.getInputStream()));

                                String line;
                                while ((line = br.readLine()) != null) {
                                    serverData.append(line + "\n");
                                }
                                br.close();
                                serverData.toString();

                            }

                            if(mDataType == Constant.VIEW_APPOINTMENT) {

                                ArrayList<AppointmentDetails> obj = JsonParser.parsePatientAppointment(serverData);
                                ArrayList<Integer> mDoctorID;

                                mDoctorID = new ArrayList<Integer>();
                                for(AppointmentDetails doctor : obj) {

                                    Integer id = Integer.parseInt(doctor.getPractitioner());
                                    if(!mDoctorID.contains(id)) {
//                                        mDoctorID.add(id);

                                        StringBuilder serverData_new = new StringBuilder();

                                        URI new_url = new URI(ServerConstants.PRACTITIONER_URL+ "/" + id);

                                        HttpsURLConnection urlHttpsConnection_new = (HttpsURLConnection) new_url.toURL().openConnection();

                                        urlHttpsConnection_new.setRequestProperty("Authorization", "Bearer " + Util.getToken(mContext));
                                        urlHttpsConnection_new.setRequestProperty("Connection", "close");
                                        urlHttpsConnection_new.setRequestMethod("GET");
                                        urlHttpsConnection_new.setUseCaches(false);
                                        urlHttpsConnection_new.setAllowUserInteraction(false);
                                        urlHttpsConnection_new.setDoInput(true);

                                        urlHttpsConnection_new.setRequestProperty("charset", "utf-8");

                                        urlHttpsConnection_new.connect();

                                        int httpsServerResponce_new = urlHttpsConnection_new.getResponseCode();
                                        parseData.setResponseCode(httpsServerResponce_new);

                                        if (httpsServerResponce_new >= Constant.HTTP_OK && httpsServerResponce_new < Constant.HTTP_REDIRECT) {

                                            BufferedReader br = new BufferedReader(new InputStreamReader(urlHttpsConnection_new.getInputStream()));

                                            String line;
                                            while ((line = br.readLine()) != null) {
                                                serverData_new.append(line + "\n");
                                            }
                                            br.close();

                                        }

                                        //Wrong code
                                        doctor.setDoctorDetail(JsonParser.parseAppointmentDoctors(serverData_new));

                                    }

                                }
                                parseData.setDataObject(obj);

                            }


                        } else {
                            //Code for JellyBean and lower version

                            if (mDataType == Constant.LOGOUT_DATA) {
                                List<NameValuePair> headers = new ArrayList<NameValuePair>();
                                headers.add(new BasicNameValuePair("Authorization", "Bearer " + token));//header can be append here also
                                headers.add(new BasicNameValuePair("charset", "utf-8"));
                               // serverData = new StringBuilder(HttpUtils.getHttpGetResponse(mContext, serverUrl.toURL().toString(), headers));
                                org.apache.http.HttpResponse data=HttpUtils.httpDeletemethod(mContext, serverUrl.toURL().toString(), headers);
                                int httpsServerResponce = data.getStatusLine().getStatusCode();
                                Log.v("HttpDownloader",String.valueOf(httpsServerResponce));
                                parseData.setResponseCode(httpsServerResponce);
                                serverData=new StringBuilder(EntityUtils.toString(data.getEntity()));

                            } else {

                                List<NameValuePair> headers = new ArrayList<NameValuePair>();
                                headers.add(new BasicNameValuePair("Authorization", "Bearer " + token));
                                headers.add(new BasicNameValuePair("charset", "utf-8"));//header can be append here also
                                headers.add(new BasicNameValuePair("Connection", "close"));
                                org.apache.http.HttpResponse data = HttpUtils.httpGetmethod(mContext, serverUrl.toURL().toString(), headers);
                                int httpsServerResponce = data.getStatusLine().getStatusCode();
                                Log.v("HttpDownloader", String.valueOf(httpsServerResponce));
                                parseData.setResponseCode(httpsServerResponce);
                                serverData = new StringBuilder(EntityUtils.toString(data.getEntity()));

                                if(mDataType == Constant.VIEW_APPOINTMENT) {

                                    ArrayList<AppointmentDetails> obj = JsonParser.parsePatientAppointment(serverData);
                                    ArrayList<Integer> mDoctorID;

                                    mDoctorID = new ArrayList<Integer>();
                                    for(AppointmentDetails doctor : obj) {

                                        Integer id = Integer.parseInt(doctor.getPractitioner());
                                        if(!mDoctorID.contains(id)) {

                                            StringBuilder serverData_new = new StringBuilder();
                                            URI new_url = new URI(ServerConstants.PRACTITIONER_URL+ "/" + id);

                                            List<NameValuePair> headers_1 = new ArrayList<NameValuePair>();
                                            headers_1.add(new BasicNameValuePair("Authorization", "Bearer " + token));
                                            headers_1.add(new BasicNameValuePair("charset", "utf-8"));//header can be append here also
                                            headers_1.add(new BasicNameValuePair("Connection", "close"));
                                            org.apache.http.HttpResponse data_1 = HttpUtils.httpGetmethod(mContext, new_url.toURL().toString(), headers_1);
                                            int httpsServerResponce_1 = data_1.getStatusLine().getStatusCode();
                                            Log.v("HttpDownloader", String.valueOf(httpsServerResponce_1));
                                            parseData.setResponseCode(httpsServerResponce_1);
                                            serverData_new = new StringBuilder(EntityUtils.toString(data_1.getEntity()));
                                            doctor.setDoctorDetail(JsonParser.parseAppointmentDoctors(serverData_new));
                                        }

                                    }
                                    parseData.setDataObject(obj);

                                }

                            }
                        }

                        break;

                    case DOCTOR_IMAGE_DATA:

                        HttpURLConnection httpBitmapUrlConnection = (HttpURLConnection) serverUrl.toURL().openConnection();

                        httpBitmapUrlConnection.setRequestProperty("Connection", "close");
                        httpBitmapUrlConnection.setRequestMethod("GET");
                        httpBitmapUrlConnection.setRequestProperty("Content-length", "0");
                        httpBitmapUrlConnection.setUseCaches(false);
                        httpBitmapUrlConnection.setAllowUserInteraction(false);
                        httpBitmapUrlConnection.setConnectTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
                        httpBitmapUrlConnection.setReadTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);

                        InputStream input = httpBitmapUrlConnection.getInputStream();

                        parseData.setDataObject(BitmapFactory.decodeStream(input));

                        break;

                    case Constant.SCHEDULE_APPOINTMENT:
                    case Constant.CANCEL_APPOINTMENT:
                    case Constant.RESCHEDULE_APPOINTMENT:
                    case Constant.DOCTOR_EMAIL_SEARCH:
                    case Constant.DOCTOR_ALLOW:

                        if (!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)) {

                            String jsonData =  null;

                            HttpsURLConnection urlHttpsConnection = (HttpsURLConnection) serverUrl.toURL().openConnection();

                            urlHttpsConnection.setRequestProperty("Authorization", "Bearer " + Util.getToken(mContext));

//                            urlHttpsConnection.setRequestProperty("Connection", "close");
                            if(mDataType == Constant.CANCEL_APPOINTMENT) {
                                urlHttpsConnection.setRequestMethod("PUT");
                                jsonData = ((ViewAppointmentActivity) mCalledByObject).getAppointmentData();
                                urlHttpsConnection.setRequestProperty("Content-Type", "application/json");
                            }else if(mDataType == Constant.RESCHEDULE_APPOINTMENT) {
                                urlHttpsConnection.setRequestMethod("PUT");
                                jsonData = ((DoctorAppointmentSummaryActivity) mCalledByObject).getAppointmentData(Constant.RESCHEDULE_APPOINTMENT);
                                urlHttpsConnection.setRequestProperty("Content-Type", "application/json");
                            }else if(mDataType == Constant.DOCTOR_EMAIL_SEARCH) {
                                urlHttpsConnection.setRequestMethod("POST");
                                jsonData = ((ConsentSearchActivity) mCalledByObject).getData();
                                urlHttpsConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            }else if(mDataType == Constant.DOCTOR_ALLOW) {

                                urlHttpsConnection.setRequestMethod("POST");
                                jsonData = ((ConsentResultActivity) mCalledByObject).getJSONData();
                                urlHttpsConnection.setRequestProperty("Content-Type", "application/json");

                            }else {
                                urlHttpsConnection.setRequestMethod("POST");
                                jsonData = ((DoctorAppointmentSummaryActivity) mCalledByObject).getAppointmentData(Constant.SCHEDULE_APPOINTMENT);
                                urlHttpsConnection.setRequestProperty("Content-Type", "application/json");
                            }

                            urlHttpsConnection.setUseCaches(false);
                            urlHttpsConnection.setAllowUserInteraction(false);
//                            urlHttpsConnection.setConnectTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
//                            urlHttpsConnection.setReadTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
//                            urlHttpsConnection.setDoInput(true);
                            urlHttpsConnection.setDoOutput(true);

                            urlHttpsConnection.connect();

                            if(jsonData != null) {
                                DataOutputStream wr = new DataOutputStream(urlHttpsConnection.getOutputStream());
                                wr.writeBytes(jsonData);
                                wr.flush();
                                wr.close();
                            }

                            int httpsServerResponce = urlHttpsConnection.getResponseCode();
                            parseData.setResponseCode(httpsServerResponce);

                            if (httpsServerResponce >= Constant.HTTP_OK && httpsServerResponce < Constant.HTTP_REDIRECT) {

                                BufferedReader br = new BufferedReader(new InputStreamReader(urlHttpsConnection.getInputStream()));

                                String line;
                                while ((line = br.readLine()) != null) {
                                    serverData.append(line + "\n");
                                }
                                br.close();
                                serverData.toString();

                            }


                        } else {
                            String jsonData = null;
                            org.apache.http.HttpResponse httpResponse=null;
                            //Code for JellyBean and lower version
                            List<NameValuePair> headers = new ArrayList<NameValuePair>();
                            headers.add(new BasicNameValuePair("Authorization", "Bearer " + Util.getToken(mContext)));


                            if(mDataType == Constant.CANCEL_APPOINTMENT)
                            {
                                jsonData = ((ViewAppointmentActivity) mCalledByObject).getAppointmentData();
                                headers.add(new BasicNameValuePair("Content-Type", "application/json"));

                            }else if(mDataType == Constant.SCHEDULE_APPOINTMENT || mDataType == Constant.RESCHEDULE_APPOINTMENT){

                                jsonData = ((DoctorAppointmentSummaryActivity) mCalledByObject).getAppointmentData(Constant.SCHEDULE_APPOINTMENT);
                                headers.add(new BasicNameValuePair("Content-Type", "application/json"));

                            }else if(mDataType == Constant.DOCTOR_EMAIL_SEARCH) {

                                jsonData = ((ConsentSearchActivity) mCalledByObject).getData();
                                headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));

                            }else if(mDataType == Constant.DOCTOR_ALLOW) {

                                jsonData = ((ConsentResultActivity) mCalledByObject).getJSONData();
                                headers.add(new BasicNameValuePair("Content-Type", "application/json"));

                            }


                            if(mDataType == Constant.CANCEL_APPOINTMENT || mDataType == Constant.RESCHEDULE_APPOINTMENT) {
                                httpResponse = HttpUtils.sendHttpPutForUsingJsonObj(serverUrl.toURL().toString(), new JSONObject(jsonData), headers);
                            }else if(mDataType == Constant.DOCTOR_EMAIL_SEARCH) {
                                httpResponse = HttpUtils.sendHttpPostForUsingJsonObj(serverUrl.toURL().toString(), jsonData, headers);
                            }else {
                               httpResponse = HttpUtils.sendHttpPostForUsingJsonObj_1(serverUrl.toURL().toString(), new JSONObject(jsonData), headers);
                            }

                            parseData.setResponseCode(httpResponse.getStatusLine().getStatusCode());
                            String data=EntityUtils.toString(httpResponse.getEntity());
                            serverData=new StringBuilder(data);

                        }

                        break;

                    case Constant.LOGIN_DATA:
                    case Constant.LOGIN_REFRESH:

                        UserToken tokenObj = null;

                        if (!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)) {

                            HttpsURLConnection urlHttpsConnection = (HttpsURLConnection) serverUrl.toURL().openConnection();

                            urlHttpsConnection.setRequestProperty("Authorization", ServerConstants.CREDENTIAL_FOR_EPIC_TOKEN);

                            if (mDataType == Constant.LOGIN_REFRESH) {
                                urlHttpsConnection.setRequestProperty("refresh_token", Util.getRefreshToken(mContext));
                            }

//                            urlHttpsConnection.setRequestProperty("Connection", "close");
                            urlHttpsConnection.setRequestMethod("POST");
                            urlHttpsConnection.setUseCaches(false);
                            urlHttpsConnection.setAllowUserInteraction(false);
                            urlHttpsConnection.setConnectTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
                            urlHttpsConnection.setReadTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
                            urlHttpsConnection.setDoInput(true);
                            urlHttpsConnection.setDoOutput(true);

                            urlHttpsConnection.setRequestProperty("Content-Type", "application/json");
//                            urlHttpsConnection.setRequestProperty("charset", "utf-8");

                            urlHttpsConnection.connect();

                            if (mDataType == Constant.LOGIN_DATA) {
                                String jsonData = null;
                                if(mCalledByObject instanceof LoginActivity) {
                                    jsonData = ((LoginActivity) mCalledByObject).getUserData();
                                }else {
                                    jsonData = ((CreatePasswordActivity) mCalledByObject).getUserData();
                                }
                                DataOutputStream wr = new DataOutputStream(urlHttpsConnection.getOutputStream());
                                wr.writeBytes(jsonData);
                                wr.flush();
                                wr.close();
                            }

                            int httpsServerResponceCode = urlHttpsConnection.getResponseCode();
                            Log.v("ServerResponceLogin:", String.valueOf(httpsServerResponceCode));
                            parseData.setResponseCode(httpsServerResponceCode);


                            if (httpsServerResponceCode >= Constant.HTTP_OK && httpsServerResponceCode < Constant.HTTP_REDIRECT) {

                                BufferedReader br = new BufferedReader(new InputStreamReader(urlHttpsConnection.getInputStream()));

                                String line;
                                while ((line = br.readLine()) != null) {
                                    serverData.append(line + "\n");
                                }
                                br.close();
                                serverData.toString();

                            }

                        } else {
//                            //Code for JellyBean and lower version
                            List<NameValuePair> headers = new ArrayList<NameValuePair>();
                            headers.add(new BasicNameValuePair("Authorization", ServerConstants.CREDENTIAL_FOR_EPIC_TOKEN));
                            headers.add(new BasicNameValuePair("Content-Type", "application/json"));

                            String jsonData = null;
                            if(mCalledByObject instanceof LoginActivity) {
                                jsonData = ((LoginActivity) mCalledByObject).getUserData();
                            }else {
                                jsonData = ((CreatePasswordActivity) mCalledByObject).getUserData();
                            }

							if (mDataType == Constant.LOGIN_REFRESH) {
                                headers.add(new BasicNameValuePair("refresh_token", Util.getRefreshToken(mContext)));
                            }
                            Log.v("jsonFor Login", new JSONObject(jsonData).toString());

                            org.apache.http.HttpResponse httpResponse = HttpUtils.sendHttpPostForUsingJsonObj_1(serverUrl.toURL().toString(), new JSONObject(jsonData), headers);
                            parseData.setResponseCode(httpResponse.getStatusLine().getStatusCode());
                            String data=EntityUtils.toString(httpResponse.getEntity());
                            serverData=new StringBuilder(data);

                        }

                        tokenObj = JsonParser.parseUserToken(serverData);

                        if (tokenObj != null && tokenObj.getAccessToken() != null) {

                            Util.setToken(mContext, tokenObj.getAccessToken(), tokenObj.getRefreshToken(), tokenObj.getExpireInTime());
                            Util.setToken(mContext, tokenObj.getAccessToken(), tokenObj.getRefreshToken(), tokenObj.getExpireInTime(), Constant.USER_LOGIN);

                            if (mDataType == Constant.LOGIN_DATA) {
                                Util.setPatientMRNID(mContext, tokenObj.getMRNId());
                            }
                        }


                        break;
                    case Constant.GET_DATAMOTION_ACCESS_TOKEN: {
                       // serverResponseFromHttpUtils = new CommonAPICalls(mContext).getDatamotionAccsessToken();
                        serverResponseFromHttpUtils=HttpUtils.getHttpGetResponse(mContext, ServerConstants.GET_CREDENTIALS_FOR_ACCESS_TOKEN, new CommonAPICalls(mContext).createTokenForDatamotionCall());
                        Log.v("DatamotionCallResponse:", serverResponseFromHttpUtils);

                        DatamotionTokenCredential datamotionTokenCredential = null;
                        Gson gson = new Gson();
                        try {
                            datamotionTokenCredential = gson.fromJson(serverResponseFromHttpUtils, DatamotionTokenCredential.class);
                        } catch (Exception e) {

                        }
                        if(datamotionTokenCredential!=null){
                            serverResponseFromHttpUtils =new CommonAPICalls(mContext).sendTokencallForDataMotion(datamotionTokenCredential);
                        }
                    }

                    break;
                    case Constant.SEND_PATIENT_DATA_FOR_VARIFICATION: {
                        serverResponseFromHttpUtils = ((RegistrationDetailsActivity) mContext).sendHttpPostForRegistration();
                    }
                    break;
                }


                switch (mDataType) {

                    case DOCTOR_RESULT_DATA:

                        parseData.setDataObject(JsonParser.parseDoctorSearchData(serverData));
                        parseData.setNextHttpUrl(JsonParser.parseDoctorSearchNextUrl(serverData));

                        break;

                    case DOCTOR_SPECIALTY_DATA:

                        parseData.setDataObject(JsonParser.parseSpecialtyData(serverData));

                        break;

                    case DOCTOR_OFFICE_DATA:

                        parseData.setDataObject(JsonParser.parseDoctorOfficeData(serverData));

                        break;

                    case Constant.SYMPTOM_LIST_DATA_MALE:

                        parseData.setDataObject(JsonParser.parseSymptomListData(serverData, Constant.MALE_BODY));

                        break;

                    case Constant.SYMPTOM_LIST_DATA_FEMALE:

                        parseData.setDataObject(JsonParser.parseSymptomListData(serverData, Constant.FEMALE_BODY));

                        break;

                    case Constant.SYMPTOM_LIST_DATA_CHILD:

                        parseData.setDataObject(JsonParser.parseSymptomListData(serverData, Constant.CHILD_BODY));
//                        parseData.setDataObject(JsonParser.parseSymptomListData(serverData, Constant.MALE_BODY));

                        break;

                    case Constant.SYMPTOM_ITEM_DATA:

                        parseData.setDataObject(JsonParser.parseSymptomItemData(serverData));

                        break;

                    case Constant.LOGIN_DATA:

                        parseData.setDataObject(JsonParser.parseUserToken(serverData));

                        break;

                    case Constant.LOGOUT_DATA:

                        Util.stopUserSession(mContext);
                        Util.setToken(mContext, "", "", 0);
                        Util.setToken(mContext, "", "", 0, Constant.UNKNOWN_LOGIN);
                        Util.setPatientMRNID(mContext, "");
                        Util.setPatientJSON(mContext, "");
                        parseData.setDataObject("Logout");

                        break;

                    case Constant.READ_PATIENT_DATA:

                        parseData.setDataObject(JsonParser.parseUserData(serverData));
                        Util.setPatientJSON(mContext, serverData.toString());

                        break;

                    case Constant.VIEW_APPOINTMENT:

//                        parseData.setDataObject(JsonParser.parsePatientAppointment(serverData));
//                        parseData.setRequestType(mDataType);

                        break;

                    case Constant.CANCEL_APPOINTMENT:

//                        parseData.setDataObject(JsonParser.parsePatientAppointment(serverData));
//                        parseData.setRequestType(mDataType);

                        break;

                    case Constant.GET_EPIC_ACCESS_TOKEN:
                        parseData.setDataObject(serverResponseFromHttpUtils);
//                        parseData.setRequestType(Constant.GET_EPIC_ACCESS_TOKEN);
                        break;
                    case Constant.GET_DATAMOTION_ACCESS_TOKEN:
                        parseData.setDataObject(serverResponseFromHttpUtils);
                        parseData.setRequestType(Constant.GET_DATAMOTION_ACCESS_TOKEN);
                        break;

                    case Constant.SEND_PATIENT_DATA_FOR_VARIFICATION:
                        parseData.setDataObject(serverResponseFromHttpUtils);
                       parseData.setRequestType(Constant.SEND_PATIENT_DATA_FOR_VARIFICATION);
                        break;

                    case Constant.DOCTOR_EMAIL_SEARCH:

                        parseData.setDataObject(JsonParser.parseConsentDoctorDetails(serverData));
//                        parseData.setRequestType(mDataType);

                        break;
                }

            } catch (Exception e) {

                Log.e("HttpDownloader : " + mDataType, "Error : " + e.toString());

            }

            return parseData;
        }

        @Override
        protected void onPostExecute(HttpResponse data) {
//            mProgressDialog.dismiss();
            if (mCalledByObject != null)
                mCalledByObject.HttpDownloadCompleted(data);

        }

    }

    public static String getToken(Context context) {

        BaseToken tokenObj = null;
        String token = Util.getToken(context);
        StringBuilder serverData = new StringBuilder();

        if (token != null && token.length() > 1) {

            return token;

        } else {

            try {

//                Log.e("getToken()", "Data type -> "+mDataType+ ", Token -> "+token+" if Condition -> "+!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2));

                if (!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)) {

                    URI uri = new URI(ServerConstants.BASE_TOKEN_URL);

                    HttpsURLConnection urlHttpsConnection = (HttpsURLConnection) uri.toURL().openConnection();

                    urlHttpsConnection.setRequestProperty("Authorization", ServerConstants.CREDENTIAL_FOR_EPIC_TOKEN);
                    //                urlHttpsConnection.setRequestProperty("Connection", "close");
                    urlHttpsConnection.setRequestMethod("POST");
                    urlHttpsConnection.setUseCaches(false);
                    urlHttpsConnection.setAllowUserInteraction(false);
                    urlHttpsConnection.setConnectTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
                    urlHttpsConnection.setReadTimeout(Constant.DEFAULT_HTTP_TIMEOUT_INTERVAL);
                    urlHttpsConnection.setDoInput(true);
                    urlHttpsConnection.setDoOutput(true);

                    urlHttpsConnection.setRequestProperty("charset", "utf-8");

                    //            urlHttpsConnection.setSSLSocketFactory(getCertificate());

                    urlHttpsConnection.connect();

                    int httpsServerResponce = urlHttpsConnection.getResponseCode();

                    if (httpsServerResponce >= Constant.HTTP_OK && httpsServerResponce < Constant.HTTP_REDIRECT) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(urlHttpsConnection.getInputStream()));

                        String line;
                        while ((line = br.readLine()) != null) {
                            serverData.append(line + "\n");
                        }
                        br.close();

                        tokenObj = JsonParser.parseBaseTokenData(serverData.toString());

                        Log.e("getToken()", "tokenObj -> "+tokenObj);

                    }

                } else {
                    //Code for JellyBean and lower version
                    List<NameValuePair> headers = new ArrayList<NameValuePair>();
                    headers.add(new BasicNameValuePair("Authorization", ServerConstants.CREDENTIAL_FOR_EPIC_TOKEN));
                    String str = HttpUtils.httpPost(context, ServerConstants.BASE_TOKEN_URL, null, headers);
                    tokenObj = JsonParser.parseBaseTokenData(str);
                }

                if (tokenObj != null) {
                    Util.setToken(context, tokenObj.getAccessToken(), "", tokenObj.getExpireInTime());
                    Util.setToken(context, tokenObj.getAccessToken(), "", tokenObj.getExpireInTime(), Constant.GUEST_LOGIN);
                }

            } catch (Exception e) {

//                Log.e("HttpDownloader : " + mDataType, "Error : " + e.toString());

            }

        }

        return tokenObj.getAccessToken();
    }

    public SSLSocketFactory getCertificate() {

        SSLSocketFactory certificate = null;

        try {

            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

//            InputStream caInput = new BufferedInputStream((mContext.getAssets()).open("hackensackUMC.cer"));
            InputStream caInput = new BufferedInputStream((mContext.getAssets()).open("apigee.cer"));

            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            certificate = context.getSocketFactory();

        } catch (Exception e) {

            Log.e("HttpDownloader : " + mDataType, "Error : " + e.toString());

        }

        return certificate;
    }

}
