package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ratul on 8/12/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationReply {

    /*
    {
    "found": true,
    "usertype": "visitor",
    "id": "4"
    }
     */

    @JsonProperty("found") public String mFound;
    @JsonProperty("usertype") public String mUsertype;
    @JsonProperty("id") public String mId;

}
