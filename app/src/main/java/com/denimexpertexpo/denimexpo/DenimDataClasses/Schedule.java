package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by ratul on 7/26/2015.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {


    /*
    "id": "2",
    "eventname": "Program 1",
    "timeadded": "2015-07-07 05:10:55",
    "starttime": "2015-07-07 00:00:00",
    "endtime": "2015-07-30 00:00:00",
    "details": "Its Aprogram",
    "duration": "552"
    * */



    @JsonProperty("id")
    public String mId;

    @JsonProperty("eventname")
    public String mEventName;

    @JsonProperty("timeadded")
    public String mTimeWhenAdded;

    @JsonProperty("starttime")
    public String mTimeWhenStarted;

    @JsonProperty("endtime")
    public String mTimeWhenEnd;

    @JsonProperty("details")
    public String mDetails;

    @JsonProperty("duration")
    public String mDuration;


    /**
     * we need some processing of data conversion
     * when doing operation using this values
     */

}
