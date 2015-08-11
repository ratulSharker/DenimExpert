package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ratul on 8/7/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Exhibitors
{

    /*
     "id": "55",
      "generated_id": "E55-1210",
      "first_name": "Md. Mostafizur",
      "last_name": "Rahaman",
      "email": "mostafiz@mabgroup-bd.com",
      "phone": "88-02-9103062-3",
      "company_name": "MAB DENIM LTD",
      "website": "www.mabgroup-bd.com",
      "company_address": "8, Pantha Path, UTC Building, Level-9, Dhaka-1215\\\n8, Pantha Path, UTC Building, Level-9, Dhaka-1215\\\nRoad No: House No:\\\nDhaka 1215\\\nDhaka\\\nBangladesh",
      "mobile": "01713234163 01713234161",
      "industry_type": "Denim Fabric",
      "annual_turn_over": "32 Million",
      "no_of_employee": "700",
      "buyer_name": "ZARA, Pull & Bear, H&M,Tesco, Compliences, OVS, Best Siller K-MART, Target, Costco Lee, Forever-21, METALON, Texsport, Debenham, Auchan,Takko, orchest jennifer, okaidi, DP, Aldi, Carrefour",
      "product_detail": "100% Cotton regular, slub, Ring , C\\/H denim Cotton poly Regular,Slub, Ring, C\\/H Denim Cotton Spandex regular, Slub, Ring , C\\/H Denim cotton poly spandex regular, slub, Ring , C\\/H denim Coated, Printed, Color, Strip, RFD, Ecro  Denim",
      "buyer_country": "EUROP Austalia USA, CANADA Europ ",
      "business": "International Exposure,Knowledge Sharing and Networking,Exhibit our Capabilities,Export Opportunitie",
      "product": "Denim Fabric"
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Exhibitor implements Serializable
    {
        @JsonProperty("id") public String mId;
        @JsonProperty("generated_id") public String mGenId;
        @JsonProperty("first_name") public String mFirstName;
        @JsonProperty("last_name") public String mLastName;
        @JsonProperty("email") public String mEmail;
        @JsonProperty("phone") public String mPhone;
        @JsonProperty("company_name") public String mCompanyName;
        @JsonProperty("website") public String mWebsite;
        @JsonProperty("company_address") public String mCompanyAddress;
        @JsonProperty("mobile") public String mMobile;
        @JsonProperty("industry_type") public String mIndustryType;
        @JsonProperty("annual_turn_over") public String mAnnualTurnover;
        @JsonProperty("no_of_employee") public String mNumOfEmployee;
        @JsonProperty("buyer_name") public String mBuyerName;
        @JsonProperty("product_detail") public String mProductDetail;
        @JsonProperty("buyer_country") public String mBuyerCountry;
        @JsonProperty("business") public String mBusiness;
        @JsonProperty("product") public String mProduct;
    }

    @JsonProperty("count") public String mCount;
    @JsonProperty("data")  public ArrayList<Exhibitor> mExhibitorList;
}
