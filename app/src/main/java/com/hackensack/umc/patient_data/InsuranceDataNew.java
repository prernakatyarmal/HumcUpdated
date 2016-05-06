package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bhagyashree_kumawat on 1/20/2016.
 */
public class InsuranceDataNew implements Serializable {
  private String resourceType;
    private String type;
    private int total;
      private ArrayList<Entry> entry;

    public InsuranceDataNew(String resourceType, String type, int total, ArrayList<Entry> entry) {
        this.resourceType = resourceType;
        this.type = type;
        this.total = total;
        this.entry = entry;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Entry> getEntry() {
        return entry;
    }

    public void setEntry(ArrayList<Entry> entry) {
        this.entry = entry;
    }
}
