package com.hackensack.umc.patient_data;

import com.hackensack.umc.coverage_data.CoverageDataNew;

import java.io.Serializable;

/**
 * Created by bhagyashree_kumawat on 1/20/2016.
 */
public class Entry implements Serializable {
    private CoverageDataNew resource;

    public CoverageDataNew getResource() {
        return resource;
    }

    public void setResource(CoverageDataNew resource) {
        this.resource = resource;
    }

    public Entry(CoverageDataNew resource) {

        this.resource = resource;
    }
}
