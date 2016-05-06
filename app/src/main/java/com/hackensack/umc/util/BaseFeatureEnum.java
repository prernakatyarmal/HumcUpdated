package com.hackensack.umc.util;

import android.content.Context;

import com.hackensack.umc.R;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Created by bhagyashree_kumawat on 2/2/2016.
 */
public enum BaseFeatureEnum {
    Make_Appointment,
    My_Appointments,
    Doctor_Search,
    Symptom_Checker,
    Directions,
    Test_Results,
    Health_Reminders,
    Messages,
    Medication,
    Health_Summary,
//    My_Chart_Settings,
    Consent;
    // Track_My_Health,

    /*
    Billing;*/

    public String getName(BaseFeatureEnum feature, Context context) {
        String[] menuItemsStringArray = context.getResources().getStringArray(R.array.menuitem_mychart_array);
        switch (feature.ordinal()) {
            case 0:
                return menuItemsStringArray[0];
            case 1:
                return menuItemsStringArray[1];
            case 2:
                return menuItemsStringArray[2];
            case 3:
                return menuItemsStringArray[3];
            case 4:
                return menuItemsStringArray[4];
            case 5:
                return menuItemsStringArray[5];
            case 6:
                return menuItemsStringArray[6];
            case 7:
                return menuItemsStringArray[7];
            case 8:
                return menuItemsStringArray[8];
            case 9:
                return menuItemsStringArray[9];

            case 10:
                return menuItemsStringArray[10];
//            case 11:
//                return menuItemsStringArray[11];
            /*case 11:
                return menuItemsStringArray[11];
            case 12:
                return menuItemsStringArray[12];*/
            default:
                return "";
        }
    }

    public int getDrawable(BaseFeatureEnum feature) {
        switch (feature.ordinal()) {
            case 0:
                return R.drawable.appointment_icon;
            case 1:
                return R.drawable.my_appointment_icon;
            case 2:
                return R.drawable.doctor_search_icon;
            case 3:
                return R.drawable.symptom_checker;
            case 4:
                return R.drawable.direction_icon;
            case 5:
                return R.drawable.test_result_icon;
            case 6:
                return R.drawable.health_reminders_icon;
            case 7:
                return R.drawable.message_icon;
            case 8:
                return R.drawable.medication_icon;
            case 9:
                return R.drawable.health_summary_icon;

//            case 10:
//                return R.drawable.track_my_health_icon;
            case 10:
                return R.drawable.health_records_icon;
           /* case 11:
                return R.drawable.surveys_icon;
            case 12:
                return R.drawable.billing;*/
            default:
                return -1;
        }
    }

    public static ArrayList<BaseFeatureEnum> getGuestFeature() {
        ArrayList<BaseFeatureEnum> guestFeature = new ArrayList<BaseFeatureEnum>();
        guestFeature.add(Make_Appointment);
        guestFeature.add(Doctor_Search);
        guestFeature.add(Symptom_Checker);
        guestFeature.add(Directions);
        return guestFeature;
    }

    public static ArrayList<BaseFeatureEnum> getAllFeature() {
        return new ArrayList<BaseFeatureEnum>(EnumSet.allOf(BaseFeatureEnum.class));
    }
}
