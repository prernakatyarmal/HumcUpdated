package com.hackensack.umc.response;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/15/2015.
 */
public class OuterQuetions implements Serializable {
    private double IdNumber;


    private String IdResult;
    private ArrayList<String> Issues;
    private ArrayList<String>Errors;
    private ArrayList<Questions>Questions;

    public OuterQuetions(double idNumber, ArrayList<com.hackensack.umc.response.Questions> questions) {
        IdNumber = idNumber;
        Questions = questions;
    }



    protected OuterQuetions(Parcel in) {

        IdNumber = in.readDouble();
        IdResult = in.readString();
        Issues = in.createStringArrayList();
        Errors = in.createStringArrayList();
        this.Questions=in.readArrayList(Questions.class.getClassLoader());

    }

    /*public static final Creator<OuterQuetions> CREATOR = new Creator<OuterQuetions>() {
        @Override
        public OuterQuetions createFromParcel(Parcel in) {
            return new OuterQuetions(in);
        }

        @Override
        public OuterQuetions[] newArray(int size) {
            return new OuterQuetions[size];
        }
    };
*/
    public ArrayList<Questions> getQuestions() {
        return Questions;
    }


    public double getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(double idNumber) {
        IdNumber = idNumber;
    }
    public void setQuestions(ArrayList<Questions> questions) {
        Questions = questions;
    }

    @Override
    public String toString() {
        return "OuterQuetions{" +
                "IdNumber=" + IdNumber +
                ", IdResult='" + IdResult + '\'' +
                ", Issues='" + Issues + '\'' +
                ", Errors=" + Errors +
                ", Questions=" + Questions +
                '}';
    }

    /*@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(IdNumber);
        dest.writeString(IdResult);
        dest.writeString(Issues);
        dest.writeStringList(Errors);
        dest.writeList(Questions);
    }*/

    public ArrayList<String> getErrors() {
        return Errors;
    }

    public void setErrors(ArrayList<String> errors) {
        Errors = errors;
    }


    /*{
        "IdNumber": "1264823974",
            "IdResult": "PASS",
            "Issues": null,
            "Errors": [
        null
        ],
        "Questions": [
        {
            "prompt": "Which of the following people do you know?",
                "type": "person.known.fic",
                "answer": [
            "ELAINE RYAN",
                    "MIKE MAVARIDIS",
                    "MIKE FRONAL",
                    "None of the above"
            ]
        },
        {
            "prompt": "Which number goes with your address on LA GRANGE ST?",
                "type": "street.number.b",
                "answer": [
            "7603",
                    "8801",
                    "41",
                    "None of the above"
            ]
        },
        {
            "prompt": "Which number goes with your address on SATHERS DR?",
                "type": "street.number",
                "answer": [
            "3409",
                    "1192",
                    "1620",
                    "None of the above"
            ]
        }
        ]

    }*/

}
