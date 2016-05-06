package com.hackensack.umc.patient_data;

import android.os.AsyncTask;
import android.util.Log;

import com.hackensack.umc.R;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.util.MychartLogin;
import com.hackensack.umc.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prerana_katyarmal on 3/2/2016.
 */
public class PatientMRN {
   /* "id": "HUM-004-7900",
            "idtype": "CEID"*/
    private String id;
    private String idtype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }
    /* URL : https://<Org>-<Env>.apigee.net/v1/api/Identifier?ID=<ID>&IDType=<IDType>
    Org : hackensackumc
    Env : dev/prod
    e.g. Consider values ID= 900702114 and IDType=HUMC*/


}
