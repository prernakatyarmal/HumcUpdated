package com.hackensack.umc.coverage_data;

import com.hackensack.umc.patient_data.*;

import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/26/2015.
 */
public class CoverageData
{
    private Text text;

    private String subPlan;

    private String dependent;

    private Period period;

    private Type type;

    private String Group;

    private String resourceType;

    private Issuer issuer;

    private String plan;

    private String sequence;

    private String subscriberId;

    private Subscriber subscriber;

    private ArrayList<Identifier> identifier;




    /*{
        "fullUrl":"urn:uuid:88f151c0-a954-468a-88bd-5ae15c08e059",
            "resource":{
        "resourceType":"Coverage",
                "text":{
            "status":"generated",
                    "div":"<div>Ahuman-readablerenderingofthecoverage</div>"
        },
        "issuer":{
            "extension":[
            {
                "url":"issuerName",
                    "valueString":"Shilpa"
            }
            ]
        },
        "period":{
            "start":"2011-05-23",
                    "end":"2012-05-23"
        },
        "type":{
            "system":"http: //hl7.org/fhir/v3/ActCode",
                    "code":"EHCPOL",
                    "display":"extendedhealthcare"
        },
        "identifier":[
        {
            "system":"http: //benefitsinc.com/certificate",
                "value":"12345"
        }
        ],
        "plan":"CBI35",
                "subPlan":"123",
                "Group":"group123",
                "dependent":1,
                "sequence":1,
                "subscriberId":"90023",
                "subscriber":{
            "extension":[
            {
                "url":"MemberName",
                    "valueString":"Shilpa"
            },
            {
                "url":"SubscriberName ",
                    "valueString":"shaha"
            },
            {
                "url":"SubscriberDOB",
                    "valueString":"2011-05-23"
            }
            ]
        }
    },
        "request":{
        "method":"POST",
                "url":"Coverage"
    }
    }*/

    public CoverageData(Text text, String subPlan, String dependent, Period period, String group, Type type, String resourceType, Issuer issuer, String plan, String sequence, String subscriberId, Subscriber subscriber, ArrayList<Identifier> identifier) {
        this.text = text;
        this.subPlan = subPlan;
        this.dependent = dependent;
        this.period = period;
        Group = group;
        this.type = type;
        this.resourceType = resourceType;
        this.issuer = issuer;
        this.plan = plan;
        this.sequence = sequence;
        this.subscriberId = subscriberId;
        this.subscriber = subscriber;
        this.identifier = identifier;
    }

    public CoverageData(String resourceType, Issuer issuer) {

        this.resourceType = resourceType;
        this.issuer = issuer;

    }
    public CoverageData(String resourceType, String group) {

        this.resourceType = resourceType;
        this.Group = group;

    }

    public CoverageData(String resourceType) {
        this.resourceType = resourceType;
    }


    public Text getText ()
    {
        return text;
    }

    public void setText (Text text)
    {
        this.text = text;
    }

    public String getSubPlan ()
    {
        return subPlan;
    }

    public void setSubPlan (String subPlan)
    {
        this.subPlan = subPlan;
    }

    public String getDependent ()
    {
        return dependent;
    }

    public void setDependent (String dependent)
    {
        this.dependent = dependent;
    }

    public Period getPeriod ()
    {
        return period;
    }

    public void setPeriod (Period period)
    {
        this.period = period;
    }

    public Type getType ()
    {
        return type;
    }

    public void setType (Type type)
    {
        this.type = type;
    }

    public String getGroup ()
    {
        return Group;
    }

    public void setGroup (String Group)
    {
        this.Group = Group;
    }

    public String getResourceType ()
    {
        return resourceType;
    }

    public void setResourceType (String resourceType)
    {
        this.resourceType = resourceType;
    }

    public Issuer getIssuer ()
    {
        return issuer;
    }

    public void setIssuer (Issuer issuer)
    {
        this.issuer = issuer;
    }

    public String getPlan ()
    {
        return plan;
    }

    public void setPlan (String plan)
    {
        this.plan = plan;
    }

    public String getSequence ()
    {
        return sequence;
    }

    public void setSequence (String sequence)
    {
        this.sequence = sequence;
    }

    public String getSubscriberId ()
    {
        return subscriberId;
    }

    public void setSubscriberId (String subscriberId)
    {
        this.subscriberId = subscriberId;
    }

    public Subscriber getSubscriber ()
    {
        return subscriber;
    }

    public void setSubscriber (Subscriber subscriber)
    {
        this.subscriber = subscriber;
    }

    public ArrayList<Identifier> getIdentifier ()
    {
        return identifier;
    }

    public void setIdentifier (ArrayList<Identifier> identifier)
    {
        this.identifier = identifier;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [text = "+text+", subPlan = "+subPlan+", dependent = "+dependent+", period = "+period+", type = "+type+", Group = "+Group+", resourceType = "+resourceType+", issuer = "+issuer+", plan = "+plan+", sequence = "+sequence+", subscriberId = "+subscriberId+", subscriber = "+subscriber+", identifier = "+identifier+"]";
    }
}

