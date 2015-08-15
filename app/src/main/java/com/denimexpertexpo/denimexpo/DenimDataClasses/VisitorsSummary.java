package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ratul on 8/15/2015.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class VisitorsSummary
{

    /*
    http://apps.bangladeshdenimexpo.com/api/visitor_summary.php
    {"total_visitor":2500,"total_company":225,"total_country":150}

     */

    @JsonProperty("total_visitor") public String mTotalVisitor;
    @JsonProperty("total_company") public String mTotalCompany;
    @JsonProperty("total_country") public String mTotalCountry;
}
