package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ratul on 8/25/2015.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class FeedbackReply {

    private static final String SUCCESS_TRUE = "TRUE";


    /*
    {"success":true}
     */
    @JsonProperty("success") public String mSuccess;


    public boolean isSuccess()
    {
        if(this.mSuccess.toLowerCase().compareTo(SUCCESS_TRUE.toLowerCase()) == 0)
            return true;
        else
            return false;
    }

}
