package com.hackensack.umc.util;

import android.util.Log;

import com.hackensack.umc.datastructure.Address;
import com.hackensack.umc.datastructure.AppointmentDetails;
import com.hackensack.umc.datastructure.BaseToken;
import com.hackensack.umc.datastructure.ConsentDoctorDetails;
import com.hackensack.umc.datastructure.DoctorDetails;
import com.hackensack.umc.datastructure.DoctorOfficeDetail;
import com.hackensack.umc.datastructure.LocationDetails;
import com.hackensack.umc.datastructure.LoginUserData;
import com.hackensack.umc.datastructure.ScheduleDetails;
import com.hackensack.umc.datastructure.SymptomDetail;
import com.hackensack.umc.datastructure.SymptomList;
import com.hackensack.umc.datastructure.TimeSlotDetails;
import com.hackensack.umc.datastructure.UserToken;
import com.hackensack.umc.patient_data.Telecom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public class JsonParser {

    public static String getNextHttpUrl(StringBuilder serverData) {

        String KEY_LINK = "link";
        String KEY_RELATION = "relation";
        String KEY_URL = "url";

        try {

            Map<String, Object> keys = new HashMap<String, Object>();

            JSONObject jsonObject = new JSONObject(serverData.toString()); // HashMap
            Iterator<?> keyset = jsonObject.keys(); // HM

            /***
             * Link Data
             */
            JSONArray linkJsonArr = jsonObject.getJSONArray(KEY_LINK);



            for (int i = 0; i < linkJsonArr.length(); i++) {

                JSONObject json = linkJsonArr.getJSONObject(i);

                if(isDataAvailable(json, KEY_RELATION)) {
                    if(json.getString(KEY_RELATION).equals("next")) {
                        if(isDataAvailable(json, KEY_URL)) {
                            String str = json.getString(KEY_URL);
                        }
                    }
                }

            }

        }catch(Exception e) {

            Log.e("Error in LINK", ""+e.toString());

        }

        return "" ;
    }

    public static boolean isDataAvailable(JSONObject obj, String key) {

        boolean dataAvailable = false ;

        if(obj.has(key) && !obj.isNull(key)) {
            dataAvailable = true ;
        }

        return dataAvailable;
    }

    public static ArrayList<SymptomList> parseSymptomListData(StringBuilder serverData, String gender) {

        ArrayList<SymptomList> symptomArrayList = new ArrayList<SymptomList>();

        try{

//            String json1 = "{\"titles\": [{\"id\": \"1\",\"title\": \"Bee1 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"3\",\"title\": \"Bee3 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"4\",\"title\": \"Bee4 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"5\",\"title\": \"Bee5 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"6\",\"title\": \"Bee6 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"7\",\"title\": \"Bee7 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"8\",\"title\": \"Bee8 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"9\",\"title\": \"Bee9 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"10\",\"title\": \"Bee10 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]},{\"id\": \"2\",\"title\": \"Bee2 or Yellow Jacket Sting \",\"specialtopic\": false,\"pediatric\": \"true\",\"gender\": \"B\",\"bodyareas\": [\"Skin\"],\"keywords\": [\"BEE\"]}]}";

            JSONObject jsonObject = new JSONObject(serverData.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.KEY_TITLES);

            for (int count = 0 ; count < jsonArray.length() ; count ++) {

                JSONObject json = jsonArray.getJSONObject(count);

                String genderStr = null ;
                if(Util.isJsonDataAvailable(json, Constant.KEY_GENDER)) {
                    genderStr = json.getString(Constant.KEY_GENDER);
//                    if(genderStr.equals(Constant.CHILD_BODY)) { // Gaurav Salunkhe : Last moment
                    if(gender.equals(Constant.CHILD_BODY)) {
                        SymptomList symptomItem = new SymptomList(json);
                        symptomArrayList.add(symptomItem);
                    }else if(genderStr.equals(gender) || genderStr.equals(Constant.COMMON_BODY)) {
                        SymptomList symptomItem = new SymptomList(json);
                        symptomArrayList.add(symptomItem);
                    }
                }

            }


        }catch(Exception e) {

            Log.e("parseSymptomListData", "Error -> " + e.toString());

        }

        return symptomArrayList;
    }

    public static BaseToken parseBaseTokenData(String serverData) {

        BaseToken token = null ;

        try {

            return new BaseToken(new JSONObject(serverData.toString()));

        }catch(Exception e) {

        }

        return null;
    }

    public static SymptomDetail parseSymptomItemData(StringBuilder serverData) {

        SymptomDetail symptomDetail = null ;
        try {

            JSONObject jsonObject = new JSONObject(serverData.toString());

            if (Util.isJsonDataAvailable(jsonObject, Constant.KEY_TOPIC)) {

                symptomDetail = new SymptomDetail(jsonObject);

            }

        }catch(Exception e) {

        }

        return symptomDetail ;
    }

    public static String[] parseSpecialtyData(StringBuilder serverData) {

        String str = serverData.toString().replace("[", "");
        str = str.replace("]", "");
        str = str.replace("\"", "");

        String[] specialty = str.split(",") ;

        return specialty ;

    }

    public static ArrayList<DoctorDetails> parseDoctorSearchData(StringBuilder serverData) {

        ArrayList<DoctorDetails> doctorArrayList = new ArrayList<DoctorDetails>();

        try {

            Map<String, Object> keys = new HashMap<String, Object>();

          /***
          * Doctor Data
          */
//            String json1 = "[{\"humcId\":\"P1LA0UK4KM\",\"npi\":\"1316996093\",\"fullname\":\"Jacob Haft\",\"speciality\":\"Cardiology\",\"degree\":\"No Value Specified\",\"imageUrl\":\"physician.hackensackumc.org/image/th_1316996093.jpg\",\"address\":[{\"state\":\"NJ\",\"suite\":\"Suite 719\",\"zip\":\"07601\",\"phone\":\"2013438505\"},{\"state\":\"NJ\",\"suite\":null,\"zip\":\"07102\",\"phone\":\"2013438505\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"D2730R5MXU\",\"npi\":\"1689790669\",\"fullname\":\"Mostafa El Khashab\",\"speciality\":\"Neurosurgery\",\"degree\":null,\"imageUrl\":null,\"address\":[{\"state\":\"NJ\",\"suite\":\"Suite 905\",\"zip\":\"07601\",\"phone\":\"2014570044\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK55B\",\"npi\":\"1033106117\",\"fullname\":\"Joseph Friedlander\",\"speciality\":\"Internal Medicine - General\",\"degree\":\"Medical Doctor\",\"imageUrl\":\"physician.hackensackumc.org/image/th_1033106117.jpg\",\"address\":[{\"state\":\"NJ\",\"suite\":null,\"zip\":\"07666\",\"phone\":\"2018338840\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK4P9\",\"npi\":\"1457480113\",\"fullname\":\"Bruce Friedman\",\"speciality\":\"Pediatric Intensive Care\",\"degree\":\"Medical Doctor\",\"imageUrl\":\"physician.hackensackumc.org/image/th_1457480113.jpg\",\"address\":[{\"state\":\"NJ\",\"suite\":\"30 Prospect Avenue\",\"zip\":\"07601\",\"phone\":\"5519965303\"},{\"state\":\"NJ\",\"suite\":\"Pediatric Critical Care\",\"zip\":\"07601\",\"phone\":\"5519968335\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK55I\",\"npi\":\"1720037609\",\"fullname\":\"Luminita Fulop\",\"speciality\":\"Internal Medicine - General\",\"degree\":\"Medical Doctor\",\"imageUrl\":null,\"address\":[{\"state\":\"NJ\",\"suite\":null,\"zip\":\"07607\",\"phone\":\"2018459053\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK4HN\",\"npi\":\"1053344077\",\"fullname\":\"Ramin Cocoziello\",\"speciality\":\"Obstetrics & Gynecology\",\"degree\":\"Medical Doctor\",\"imageUrl\":null,\"address\":[{\"state\":\"NJ\",\"suite\":\"Suite E\",\"zip\":\"07410\",\"phone\":\"2017940910\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK4HU\",\"npi\":\"1417035403\",\"fullname\":\"Robert Cohen\",\"speciality\":\"Gynecology\",\"degree\":\"Medical Doctor\",\"imageUrl\":\"physician.hackensackumc.org/image/th_1417035403.jpg\",\"address\":[{\"state\":\"NJ\",\"suite\":\"Suite 3\",\"zip\":\"07601\",\"phone\":\"2019969449\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK53V\",\"npi\":\"1750336038\",\"fullname\":\"Diego Coira\",\"speciality\":\"Psychiatry\",\"degree\":\"Medical Doctor\",\"imageUrl\":\"physician.hackensackumc.org/image/th_1750336038.jpg\",\"address\":[{\"state\":\"NJ\",\"suite\":\"Room 6706\",\"zip\":\"07601\",\"phone\":\"5519963428\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK4OH\",\"npi\":\"1194863407\",\"fullname\":\"Thomas Connor\",\"speciality\":\"Pediatric Cardiology\",\"degree\":\"Medical Doctor\",\"imageUrl\":null,\"address\":[{\"state\":\"NJ\",\"suite\":\"Suite 102\",\"zip\":\"07410\",\"phone\":\"2017941366\"},{\"state\":\"NJ\",\"suite\":\"Suite 204\",\"zip\":\"07920\",\"phone\":\"9737315550\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}},{\"humcId\":\"P1LA0UK4R8\",\"npi\":\"1639148802\",\"fullname\":\"John Cozzone\",\"speciality\":\"Plastic Surgery\",\"degree\":\"Medical Doctor\",\"imageUrl\":\"physician.hackensackumc.org/image/th_1639148802.jpg\",\"address\":[{\"state\":\"NJ\",\"suite\":\"Ste 302\",\"zip\":\"07652\",\"phone\":\"2016891570\"},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null},{\"state\":null,\"suite\":null,\"zip\":null,\"phone\":null}],\"slot\":{\"2015-10-02T12:16:02.627Z\":[\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\",\"12:00\"]}}]";
//            JSONArray jsonArray = new JSONArray(json1); // HashMap

//            JSONArray jsonArray = new JSONArray(serverData.toString()); // HashMap

            JSONObject jsonObj = new JSONObject(serverData.toString());

            JSONArray jsonArray = jsonObj.getJSONArray(Constant.KEY_DOCTOR_LIST);

            for ( int count = 0 ; count < jsonArray.length() ; count ++) {

                JSONObject json = jsonArray.getJSONObject(count);

                DoctorDetails doctorItem = new DoctorDetails(json);

                doctorArrayList.add(doctorItem);

            }

            Log.i("JsonParser", "JSON -> " + doctorArrayList.toString());

        }catch(Exception e) {

            Log.e("JsonParser", "Error -> " + e.toString());

        }

        return doctorArrayList;
    }

    public static String parseDoctorSearchNextUrl(StringBuilder serverData) {

        String url = null ;
        try {

            JSONObject jsonObj = new JSONObject(serverData.toString());

            url = jsonObj.getString(Constant.KEY_DOCTOR_SEARCH_NEXT_URL);

        }catch(Exception e) {

            Log.e("JsonParser", "Error -> " + e.toString());

        }

        return url;
    }

    public static ArrayList<DoctorOfficeDetail> parseDoctorOfficeData(StringBuilder serverData) {
//    public static HttpResponse parseDoctorOfficeData(StringBuilder serverData) {

//        HttpResponse response = new HttpResponse();

        ArrayList<DoctorOfficeDetail> doctorOffices = new ArrayList<DoctorOfficeDetail>();
//        ArrayList<String> visitTypeFilter = new ArrayList<String>();

        try {

            JSONObject jsonObj = new JSONObject(serverData.toString());

            JSONArray jsonArray = jsonObj.getJSONArray(Constant.KEY_DOCTOR_LOCATION_ARRAY);

            ArrayList<ScheduleDetails> doctorSchedules = new ArrayList<ScheduleDetails>();
            ArrayList<TimeSlotDetails> doctorTimeSlots = new ArrayList<TimeSlotDetails>();

            for ( int count = 0 ; count < jsonArray.length() ; count ++) {

                JSONObject json = jsonArray.getJSONObject(count);

                JSONObject resource = Util.getJsonObject(json, Constant.KEY_DOCTOR_LOCATION_RESOURCE);

                String type = Util.getJsonData(resource, Constant.KEY_DOCTOR_LOCATION_RESOURCE_TYPE);

                if(type.equals(Constant.RESOURCE_TYPE_LOCATION)) {

                    DoctorOfficeDetail doctorOfficeItem = new DoctorOfficeDetail();

                    doctorOfficeItem.setID(Util.getJsonData(resource, Constant.KEY_DOCTOR_ID));

                    ArrayList<Telecom> telecoms = new ArrayList<Telecom>();
                    JSONArray telecomArray = Util.getJsonArray(resource, Constant.TELECOM_ARRAY);
                    if(telecomArray != null && telecomArray.length() > 0) {
                        for (int telecount = 0; telecount < telecomArray.length(); telecount++) {

                            Telecom telecom = new Telecom(telecomArray.getJSONObject(telecount));
                            telecoms.add(telecom);

                        }
                    }

                    doctorOfficeItem.setTelecom(telecoms);

                    JSONObject address = Util.getJsonObject(resource, Constant.KEY_DOCTOR_ADDRESS);
                    doctorOfficeItem.setAddress(new Address(address));

                    doctorOffices.add(doctorOfficeItem);

                }else if(type.equals(Constant.RESOURCE_TYPE_SCHEDULE)) {

                    ScheduleDetails schedule = new ScheduleDetails();
                    schedule.setID(Util.getJsonData(resource, Constant.KEY_DOCTOR_ID));

                    JSONArray extensionArray = Util.getJsonArray(resource, Constant.SCHEDULE_EXTENSION);
                    for( int extcount = 0 ; extcount < extensionArray.length() ; extcount ++ ) {

                        JSONObject extObj = extensionArray.getJSONObject(extcount);
                        String[] arr = (Util.getJsonData(Util.getJsonObject(extObj, Constant.SCHEDULE_VALUE_REFERENCE), Constant.SLOT_REFERENCE)).split("/");
                        if(arr.length == 2)
                            schedule.setLocationValueReference(arr[1]);

                    }

                    String[] arr = Util.getJsonData(Util.getJsonObject(resource, Constant.SCHEDULE_ACTOR), Constant.SCHEDULE_ACTOR_DISPLAY).split("/");
                    if(arr.length == 2)
                        schedule.setPractitionerValueReference(arr[1]);

//                    schedule.setPractitionerValueReference(Util.getJsonData(Util.getJsonObject(resource, Constant.SCHEDULE_PLANNING_HORIZON), Constant.SCHEDULE_DATE));
                    schedule.setDate(Util.getJsonData(Util.getJsonObject(resource, Constant.SCHEDULE_PLANNING_HORIZON), Constant.SCHEDULE_DATE));

                    JSONArray typeArray = Util.getJsonArray(resource, Constant.SCHEDULE_TYPE);
                    for( int extcount = 0 ; extcount < typeArray.length() ; extcount ++ ) {

                        JSONObject typeObj = typeArray.getJSONObject(extcount);
                        JSONArray codeArray = Util.getJsonArray(typeObj, Constant.SCHEDULE_CODING);
                        for( int codingcount = 0 ; codingcount < codeArray.length() ; codingcount ++ ) {

                            JSONObject codingObj = codeArray.getJSONObject(extcount);
                            schedule.setSystem(Util.getJsonData(codingObj, Constant.TELECOM_SYSTEM));
                            schedule.setCode(Util.getJsonData(codingObj, Constant.SCHEDULE_CODE));
                            //New change
                            String visitType = Util.getJsonData(codingObj, Constant.SCHEDULE_ACTOR_DISPLAY);
                            schedule.setDisplay(visitType);

//                            boolean isAvailable = visitTypeFilter.contains(visitType);
////                            for(String visittype : visitTypeFilter) {
////
////                                if(visittype != null && visittype.equals(type) && visittype.length() == visittype.length()) {
////                                    isAvailable = true;
////                                    break;
////                                }
////
////                            }
//
//                            if(!isAvailable) {
//
//                                visitTypeFilter.add(visitType);
//
//                            }


                        }

                    }

                    doctorSchedules.add(schedule);

                }else if(type.equals(Constant.RESOURCE_TYPE_SLOT)) {

                    TimeSlotDetails slot = new TimeSlotDetails();
                    slot.setID(Util.getJsonData(resource, Constant.KEY_DOCTOR_ID));

                    slot.setScheduleReference(Util.getJsonData(Util.getJsonObject(resource, Constant.SLOT_SCHEDULE), Constant.SLOT_REFERENCE));
                    slot.setFreeBusyType(Util.getJsonData(resource, Constant.SLOT_FREE_BUSY_TYPE));
                    slot.setStart(Util.getJsonData(resource, Constant.SCHEDULE_DATE));

                    doctorTimeSlots.add(slot);
                }

            }

            for(ScheduleDetails scheduleObj : doctorSchedules) {

                for(TimeSlotDetails slotObj : doctorTimeSlots) {

                    if(scheduleObj.getID().equals(slotObj.getScheduleReference())) {
                        scheduleObj.addSlot(slotObj);
                    }

                }

            }

            for(DoctorOfficeDetail doctorOfficeObj : doctorOffices) {

                for(ScheduleDetails scheduleObj : doctorSchedules) {
                    if (doctorOfficeObj.getID().equals(scheduleObj.getLocationValueReference())) {

                        doctorOfficeObj.addSchedule(scheduleObj);

                        boolean isAvailable = doctorOfficeObj.getVisitTypeFilter().contains(scheduleObj.getDisplay());
                        if(!isAvailable) {

                            doctorOfficeObj.addVisitType(scheduleObj.getDisplay());

                        }
                    }
                }

            }

//            Log.i("JsonParser", "JSON -> " + visitTypeFilter.toString());

        }catch(Exception e) {

            Log.e("JsonParser", "Error -> " + e.toString());

        }

//        response.setDataObject(doctorOffices, null);

        return doctorOffices;
    }

    public static UserToken parseUserToken(StringBuilder serverData) {

        UserToken patient = null;

        try {

            JSONObject jsonObj = new JSONObject(serverData.toString());
            patient = new UserToken(jsonObj);

        }catch(Exception e) {

            Log.e("JsonParser", "Error -> " + e.toString());

        }

        return patient;

    }

    public static LoginUserData parseUserData(StringBuilder serverData) {

        LoginUserData patient = null;

        try {

            JSONObject jsonObj = new JSONObject(serverData.toString());
            patient = new LoginUserData(jsonObj);

        }catch(Exception e) {

            Log.e("JsonParser", "Error -> " + e.toString());

        }

        return patient;

    }

    public static ConsentDoctorDetails parseConsentDoctorDetails (StringBuilder serverData) {

        ConsentDoctorDetails consentDoctorDetails = null;

        try {
            JSONObject json = new JSONObject(serverData.toString());

            JSONArray entryArray = Util.getJsonArray(json, Constant.KEY_DOCTOR_LOCATION_ARRAY);

            json = entryArray.getJSONObject(0);
            json = Util.getJsonObject(json, Constant.KEY_DOCTOR_LOCATION_RESOURCE);
            consentDoctorDetails = new ConsentDoctorDetails(json);

        }catch(Exception e) {

        }
        return consentDoctorDetails;
    }

    public static ArrayList<AppointmentDetails> parsePatientAppointment (StringBuilder serverData) {

        ArrayList<AppointmentDetails> doctorAppointment = new ArrayList<AppointmentDetails>();

        try {

            JSONObject jsonObj = new JSONObject(serverData.toString());

            JSONArray jsonArray = jsonObj.getJSONArray(Constant.KEY_DOCTOR_LOCATION_ARRAY);

            ArrayList<LocationDetails> doctorLocation = new ArrayList<LocationDetails>();

            for ( int count = 0 ; count < jsonArray.length() ; count ++) {

                JSONObject json = jsonArray.getJSONObject(count);

                JSONObject resource = Util.getJsonObject(json, Constant.KEY_DOCTOR_LOCATION_RESOURCE);

                String type = Util.getJsonData(resource, Constant.KEY_DOCTOR_LOCATION_RESOURCE_TYPE);

                if(type.equals(Constant.RESOURCE_TYPE_APPOINTMENT)) {

                    AppointmentDetails appointmentItem = new AppointmentDetails();

                    appointmentItem.setID(Util.getJsonData(resource, Constant.KEY_DOCTOR_ID));
                    appointmentItem.setStartDate(Util.getJsonData(resource, Constant.SCHEDULE_DATE));

                    JSONArray participantArray = Util.getJsonArray(resource, Constant.PARTICIPANT_TYPE);
                    if(participantArray != null && participantArray.length() > 0) {

                        for( int participantCount = 0 ; participantCount < participantArray.length() ; participantCount ++ ) {

                            String[] arr = Util.getJsonData(Util.getJsonObject(participantArray.getJSONObject(participantCount), Constant.SCHEDULE_ACTOR), Constant.SLOT_REFERENCE).split("/");

                            if(arr.length > 1) {
                                if(arr[0].equals(Constant.RESOURCE_TYPE_LOCATION)) {
                                    appointmentItem.setLocationID(arr[1]);
                                }else if(arr[0].equals(Constant.KEY_PRACTITIONER)) {
                                    appointmentItem.setPractitioner(arr[1]);
                                }
                            }

                        }

                    }

                    doctorAppointment.add(appointmentItem);

                }else if(type.equals(Constant.RESOURCE_TYPE_LOCATION)) {

                    LocationDetails location = new LocationDetails();

                    location.setID(Util.getJsonData(resource, Constant.KEY_DOCTOR_ID));
                    location.setName(Util.getJsonData(resource, Constant.KEY_NAME));

                    ArrayList<Telecom> teleArrayList = new ArrayList<Telecom>();
                    JSONArray telecomArray = Util.getJsonArray(resource, Constant.TELECOM_ARRAY);
                    for( int telecount = 0 ; telecount < telecomArray.length() ; telecount ++ ) {

                        Telecom telephone = new Telecom(telecomArray.getJSONObject(telecount));
                        teleArrayList.add(telephone);
                    }

                    location.setTelephone(teleArrayList);


                    JSONObject address = Util.getJsonObject(resource, Constant.KEY_DOCTOR_ADDRESS);
                    location.setAddress(new Address(address));

                    doctorLocation.add(location);

                }
            }

            for(AppointmentDetails appointmentObj : doctorAppointment) {

                for(LocationDetails locationObj : doctorLocation) {
                    if (locationObj.getID().equals(appointmentObj.getLocationID())) {
                        appointmentObj.setLocation(locationObj);
                    }
                }

            }

//            Log.i("JsonParser", "JSON -> " + doctorOffices.toString());

        }catch(Exception e) {

            Log.e("JsonParser", "Error -> " + e.toString());

        }

        return doctorAppointment;
    }

    public static DoctorDetails parseAppointmentDoctors (StringBuilder serverData) {

//        DoctorDetails doctor = new DoctorDetails();

        try {

            JSONObject jsonObj = new JSONObject(serverData.toString());

            return new DoctorDetails(jsonObj);


        }catch(Exception e) {

        }

        return null;
    }

//    public static ArrayList<DoctorDetails> objectNode(String jsonString) throws JSONException {
//
//        ArrayList<DoctorDetails> doctorArrayList = new ArrayList<DoctorDetails>();
//
//        Map<String, Object> keys = new HashMap<String, Object>();
//
//        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString); // HashMap
//        Iterator<?> keyset = jsonObject.keys(); // HM
//
//        /***
//         * Link Data
//         */
//        JSONArray linkJsonArray = jsonObject.getJSONArray(JSON_LINK);
//
//
//        /***
//         * Doctor Data
//         */
//        JSONArray jsonArray = jsonObject.getJSONArray(JSON_ENTRY);
////        JSONObject jsonObj = jsonObject.getJSONObject(JSON_ENTRY);
//
//        int count = 0;
//
//        for ( ; count < jsonArray.length() ; count ++) {
//
//            JSONObject item = jsonArray.getJSONObject(count);
//
//            DoctorDetails doctor = new DoctorDetails(item);
//
//            doctorArrayList.add(doctor);
//        }
//
//
//
////        while (keyset.hasNext()) {
////
////            String key = (String) keyset.next();//JsonObject, JsonArray, JsonString, and JsonNumber are subtypes of JsonValue.
////            Object value = jsonObject.get(key);
//////            Log.i("KEY", key);
//////            Log.i("VALUE", value.toString());
////
////            if (value instanceof org.json.JSONObject) {
////                keys.put(key, objectNode(value.toString()));
////            } else if (value instanceof org.json.JSONArray) {
////                JSONArray arrayOFKeys = jsonObject.getJSONArray(key);
////                keys.put(key, arrayNode(arrayOFKeys));
////            } else {
////                keys.put(key, value);
////            }
////
//////            Log.i("KEY", key.toString());
////        }
//
//        return doctorArrayList;
//    }

//    public static List<Object> arrayNode(JSONArray arrayOFKeys) throws JSONException {
//
//        List<Object> array2List = new ArrayList<Object>();
//        for (int i = 0; i < arrayOFKeys.length(); i++) {
//            if (arrayOFKeys.opt(i) instanceof JSONObject) {
//                Map<String, Object> subJSONObj2Map = objectNode(arrayOFKeys.opt(i).toString());
//                array2List.add(subJSONObj2Map);
//            } else if (arrayOFKeys.opt(i) instanceof JSONArray) {
//                List<Object> subarray2List = arrayNode((JSONArray) arrayOFKeys.opt(i));
//                array2List.add(subarray2List);
//            } else {
//                array2List.add(arrayOFKeys.opt(i));
//            }
//        }
//        Log.i("ArrayList", array2List.toString());
//
//        return array2List;
//    }

}
