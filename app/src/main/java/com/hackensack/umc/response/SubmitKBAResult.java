package com.hackensack.umc.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/20/2015.
 */
public class SubmitKBAResult implements Serializable {
    private String IdNumber;
    private String IdResult;
    private ArrayList<String>Issues;
    private  ArrayList<String>Errors;
    private String AnswersReceived;
    private String KBAAnswerResult;
    private String KBAResult;

    public ArrayList<String> getIssues() {
        return Issues;
    }

    public void setIssues(ArrayList<String> issues) {
        Issues = issues;
    }

    public String getIdResult() {
        return IdResult;
    }

    public void setIdResult(String idResult) {
        IdResult = idResult;
    }

    public String getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(String idNumber) {
        IdNumber = idNumber;
    }

    public ArrayList<String> getErrors() {
        return Errors;
    }

    public void setErrors(ArrayList<String> errors) {
        Errors = errors;
    }

    public String getAnswersReceived() {
        return AnswersReceived;
    }

    public void setAnswersReceived(String answersReceived) {
        AnswersReceived = answersReceived;
    }

    public String getKBAAnswerResult() {
        return KBAAnswerResult;
    }

    public void setKBAAnswerResult(String KBAAnswerResult) {
        this.KBAAnswerResult = KBAAnswerResult;
    }

    public String getKBAResult() {
        return KBAResult;
    }

    public void setKBAResult(String KBAResult) {
        this.KBAResult = KBAResult;
    }

    @Override
    public String toString() {
        return "OuterResult{" +
                "IdNumber='" + IdNumber + '\'' +
                ", IdResult='" + IdResult + '\'' +
                ", Issues=" + Issues +
                ", Errors=" + Errors +
                ", AnswersReceived='" + AnswersReceived + '\'' +
                ", KBAAnswerResult='" + KBAAnswerResult + '\'' +
                ", KBAResult='" + KBAResult + '\'' +
                '}';
    }
}
