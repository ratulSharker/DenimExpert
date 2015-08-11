package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ratul on 8/8/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarcodeTypeChecker {
    @JsonProperty("count") public String mCount;
    @JsonProperty("usertype") public String mUsertype;
}
