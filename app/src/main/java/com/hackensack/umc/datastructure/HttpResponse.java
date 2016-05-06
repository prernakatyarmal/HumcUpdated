package com.hackensack.umc.datastructure;

/**
 * Created by gaurav_salunkhe on 10/1/2015.
 */
public class HttpResponse {

    public Object getDataObject() {
        return mDataObject;
    }

    public void setDataObject(Object mDataObject) {
        this.mDataObject = mDataObject;
    }

    public void setDataObject(Object mDataObject, Object filter) {
        this.mDataObject = mDataObject;
        this.mFilter = filter;
    }

    public String getNextHttpUrl() {
        return mNextHttpUrl;
    }

    public void setNextHttpUrl(String mNextHttpUrl) {
        this.mNextHttpUrl = mNextHttpUrl;
    }

    private Object mFilter;
    private Object mDataObject;
    private String mNextHttpUrl;
    private int requestType;

    private int mResponseCode = 0;

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int response) {
        this.mResponseCode = response;
    }

    public Object getFilter() {
        return mFilter;
    }

    public void setFilter(int filter) {
        this.mFilter = filter;
    }

}
