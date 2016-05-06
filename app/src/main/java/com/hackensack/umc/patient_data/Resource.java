package com.hackensack.umc.patient_data;

import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/27/2015.
 */
public class Resource {
    PatientForEpic patientResponse;
    ArrayList<Photo> Photo;
    ManagingOrganization managingOrganization;
    Request request;
    ArrayList<Identifier>identifiers;

    public Resource(PatientForEpic patientResponse, ManagingOrganization managingOrganization, ArrayList<com.hackensack.umc.patient_data.Photo> photo,  ArrayList<Identifier> identifiers) {
        this.patientResponse = patientResponse;
        this.managingOrganization = managingOrganization;
        Photo = photo;

        this.identifiers = identifiers;
    }
}
