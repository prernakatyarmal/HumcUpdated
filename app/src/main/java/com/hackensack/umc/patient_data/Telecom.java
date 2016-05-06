package com.hackensack.umc.patient_data;

import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONObject;

import java.io.Serializable;

public class Telecom implements Serializable {
    private String system;
    public static final String TELECOM_EMAIL = "email";
    public static final String TELECOM_PHONE = "phone";
    public static final String TELECOM_WORK_PHONE = "work";
    public static final String TELECOM_HOME_PHONE = "home";

    public String getTELECOM_EMAIL() {
        return TELECOM_EMAIL;
    }

    public static final String TELECOM_MOBILE_PHONE = "mobile";


    public Telecom(JSONObject json) {

        try {

            this.system = Util.getJsonData(json, Constant.TELECOM_SYSTEM);
            this.value = Util.getJsonData(json, Constant.TELECOM_VALUE);
            this.use = Util.getJsonData(json, Constant.TELECOM_USE);

        } catch (Exception e) {

        }

    }

    public Telecom(String system, String value, String use) {
        this.system = system;
        this.value = value;
        this.use = use;
    }

    private String value;
    private String use;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    @Override
    public String toString() {
        return "Telecom [system=" + system + ", value=" + value + ", use="
                + use + "]";
    }

}
