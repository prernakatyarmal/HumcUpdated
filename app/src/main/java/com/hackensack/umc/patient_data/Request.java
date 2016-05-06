package com.hackensack.umc.patient_data;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class Request
{
    private String method;

    private String url;

    public Request(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public String getMethod ()
    {
        return method;
    }

    public void setMethod (String method)
    {
        this.method = method;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [method = "+method+", url = "+url+"]";
    }
}

