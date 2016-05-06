package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bhagyashree_kumawat on 12/1/2015.
 */
public class InsuranceData implements Serializable {
    private String timestamp;
    private String organization;
    private String count;
    private ArrayList<Entity> entities;

    public InsuranceData(String timestamp, String organization, String count, ArrayList<Entity> entities) {
        this.timestamp = timestamp;
        this.organization = organization;
        this.count = count;
        this.entities = entities;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }
}
