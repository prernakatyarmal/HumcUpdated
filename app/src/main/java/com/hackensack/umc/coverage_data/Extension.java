package com.hackensack.umc.coverage_data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 1/18/2016.
 */
public class Extension implements Serializable{
    String url;

    public Extension(String url) {
        this.url = url;
    }

    private ArrayList<Value> value;

    public Extension(String url, ArrayList<Value> value) {
        this.url=url;
        this.value=value;
    }

    public ArrayList<Value> getValue() {
        return value;
    }

    public void setValue(ArrayList<Value> value) {
        this.value = value;
    }
}
