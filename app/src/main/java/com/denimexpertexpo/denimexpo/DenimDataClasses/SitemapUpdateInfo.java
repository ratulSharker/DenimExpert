package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ratul on 8/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SitemapUpdateInfo
{
    /*
    {
        "eventmap_id": "3"
    }
     */

    @JsonProperty("eventmap_id") public String mEventMapId;
}
