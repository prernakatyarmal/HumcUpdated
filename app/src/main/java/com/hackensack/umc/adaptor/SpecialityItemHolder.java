package com.hackensack.umc.adaptor;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class SpecialityItemHolder {

    private final String text;
    private final boolean isHint;

    public SpecialityItemHolder(String strItem, boolean flag) {
        this.isHint = flag;
        this.text = strItem;
    }

    public String getItemString() {
        return text;
    }

    public boolean isHint() {
        return isHint;
    }

}
