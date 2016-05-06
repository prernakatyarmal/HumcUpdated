package com.hackensack.umc.coverage_data;

/**
 * Created by prerana_katyarmal on 1/18/2016.
 */
public class SubscriberId {
    String use;
    String value;

    public SubscriberId(String use, String value) {
        this.use = use;
        this.value = value;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    /*"subscriberId": {

        "use": "office",
                "value": "<<SubscriberID>>"
    },*/
}
