package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 11/26/2015.
 */
public class ErrorForEpicCreatePatient implements Serializable{
    private Text text;

    private ArrayList <Issue>issue;

    private String resourceType;

    public Text getText ()
    {
        return text;
    }

    public void setText (Text text)
    {
        this.text = text;
    }

    public ArrayList<Issue> getIssue ()
    {
        return issue;
    }

    public void setIssue (ArrayList<Issue> issue)
    {
        this.issue = issue;
    }

    public String getResourceType ()
    {
        return resourceType;
    }

    public void setResourceType (String resourceType)
    {
        this.resourceType = resourceType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [text = "+text+", issue = "+issue+", resourceType = "+resourceType+"]";
    }

   /* 11-26 11:00:49.691: V/HttpUtils(17069): ***************************************************** RESPONSE 2
            11-26 11:00:49.691: V/HttpUtils(17069): {
        11-26 11:00:49.691: V/HttpUtils(17069):     "resourceType":"OperationOutcome",
                11-26 11:00:49.691: V/HttpUtils(17069):     "text":{
            11-26 11:00:49.691: V/HttpUtils(17069):         "status":"generated",
                    11-26 11:00:49.691: V/HttpUtils(17069):         "div":"<div><h1>Operation Outcome</h1><table border=\"0\"><tr><td style=\"font-weight: bold;\">fatal</td><td>[]</td><td><pre>ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt@1095c92</pre></td>\n\t\t\t</tr>\n\t\t</table>\n\t</div>"
            11-26 11:00:49.691: V/HttpUtils(17069):     },
        11-26 11:00:49.691: V/HttpUtils(17069):     "issue":[
        11-26 11:00:49.691: V/HttpUtils(17069):         {
            11-26 11:00:49.691: V/HttpUtils(17069):             "severity":"fatal",
                    11-26 11:00:49.691: V/HttpUtils(17069):             "details":{
                11-26 11:00:49.691: V/HttpUtils(17069):                 "text":"Please provide valid email address."
                11-26 11:00:49.691: V/HttpUtils(17069):             }
            11-26 11:00:49.691: V/HttpUtils(17069):         }
        11-26 11:00:49.691: V/HttpUtils(17069):     ]
        11-26 11:00:49.691: V/HttpUtils(17069): }*/

}

