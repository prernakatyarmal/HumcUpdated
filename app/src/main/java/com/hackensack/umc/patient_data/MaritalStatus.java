package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

public class MaritalStatus implements Serializable {
	private ArrayList<Coding> coding;
    private String text;

    public MaritalStatus(ArrayList<Coding> coding, String text) {
        this.coding = coding;
        this.text = text;
    }





}
