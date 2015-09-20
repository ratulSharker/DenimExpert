package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ratul on 9/14/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationResponse {

    /*
    {"success":true,"id":3783,"generated_id":"Z3783-4938"}
    {"success":false,"error":"same email exists"}
     */

    public static boolean isSuccess(String success)
    {
        return success.toLowerCase().compareTo("true") == 0;
    }


    @JsonProperty("success")        public String mIsSuccess;
    @JsonProperty("id")             public String mId;
    @JsonProperty("generated_id")   public String mGenId;
    @JsonProperty("error")          public String mErrMsg;
}
