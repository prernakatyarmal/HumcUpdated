package com.hackensack.umc.datastructure;

/**
 * Created by gaurav_salunkhe on 9/23/2015.
 */
public class HackensackError {


    String mErrorMessage;
    int mErrorCode;

    public HackensackError(String message, int code) {

        mErrorMessage = message ;
        mErrorCode = code ;

    }


    public String getMessage() {
        return mErrorMessage;
    }

    public void setMessage(String mMessage) {
        this.mErrorMessage = mMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }



}
