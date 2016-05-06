package com.hackensack.umc.patient_data;

import com.hackensack.umc.coverage_data.CoverageData;

import java.io.Serializable;

/**
 * Created by bhagyashree_kumawat on 12/1/2015.
 */
public class Entity implements Serializable {
    private String uuid;
    private String type;
    private CoverageData coverage;

    public Entity(String uuid, String type, CoverageData coverageData) {
        this.uuid = uuid;
        this.type = type;
        this.coverage = coverageData;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CoverageData getCoverageData() {
        return coverage;
    }

    public void setCoverageData(CoverageData coverageData) {
        this.coverage = coverageData;
    }
}
