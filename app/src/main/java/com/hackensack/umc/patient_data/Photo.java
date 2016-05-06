package com.hackensack.umc.patient_data;

import java.io.Serializable;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Photo implements Serializable
{
    private String title;

    private String data;

    private String contentType;

    public Photo(String title, String data, String contentType) {
        this.title = title;
        this.data = data;
        this.contentType = contentType;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getData ()
    {
        return data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    public String getContentType ()
    {
        return contentType;
    }

    public void setContentType (String contentType)
    {
        this.contentType = contentType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", data = "+data+", contentType = "+contentType+"]";
    }
}


