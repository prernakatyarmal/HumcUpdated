package com.hackensack.umc.datastructure;

import java.io.Serializable;

/**
 * Created by Bhagyashree_kumawat on 9/29/2015.
 */
public class DirectionList implements Serializable{
    private String name;
    private String addressStr;

    public DirectionList(String name, String addressStr, String placeStr, double lat, double lon) {
        this.name = name;
        this.addressStr = addressStr;
        this.placeStr = placeStr;
        this.lat = lat;
        this.lon = lon;
    }

    private String placeStr;
    private double lat;
    private double lon;

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public String getPlaceStr() {
        return placeStr;
    }

    public void setPlaceStr(String placeStr) {
        this.placeStr = placeStr;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
