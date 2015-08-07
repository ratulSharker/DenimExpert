package com.denimexpertexpo.denimexpo.DenimDataClasses;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by ratul on 8/5/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Visitors {


    /*
      "id": "1",
      "generated_id": "Z1-1156",
      "first_name": "Maruf",
      "last_name": "Ahmed",
      "full_name": "Mr. Maruf Ahmed",
      "email": "maruf1990@gmail.com",
      "phone": "+8801676798306",
      "company_name": "S M Style Ltd.",
      "website": "http:\\/\\/smartex-bd.com\\/",
      "address": "\\\nRoad No:5 House No:18\\\nLevel:3\\\nLane:\\\nBlock:F\\\nSector:2\\\nArea:Mirpur\\\nDhaka 1216\\\nMirpur\\\nBangladesh",
      "industry_type": "Apparel Retailer",
      "job_title": "Designer",
      "department": "Research & Development",
      "date": "7\\/10\\/2015 1:04:43 PM"
     */

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Visitor{
    @JsonProperty("id")
    public String mId;

    @JsonProperty("generated_id")
    public String mGenId;

    @JsonProperty("first_name")
    public String mFirstName;

    @JsonProperty("last_name")
    public String mLastName;

    @JsonProperty("full_name")
    public String mFullName;

    @JsonProperty("email")
    public String mEmail;

    @JsonProperty("phone")
    public String mPhone;

    @JsonProperty("company_name")
    public String mCompanyName;

    @JsonProperty("website")
    public String mWebsite;

    @JsonProperty("address")
    public String mAdress;

    @JsonProperty("industry_type")
    public String mIndustryType;

    @JsonProperty("job_title")
    public String mJobtitle;

    @JsonProperty("department")
    public String mDepartment;

    @JsonProperty("date")
    public String mDate;


    public Visitor(@JsonProperty("id") String  Id,
                   @JsonProperty("generated_id") String  GenId,
                   @JsonProperty("first_name") String  FirstName,
                   @JsonProperty("last_name") String  LastName,
                   @JsonProperty("full_name") String  FullName,
                   @JsonProperty("email") String  Email,
                   @JsonProperty("phone") String  Phone,
                   @JsonProperty("company_name") String  CompanyName,
                   @JsonProperty("website") String  Website,
                   @JsonProperty("address") String  Adress,
                   @JsonProperty("industry_type") String  IndustryType,
                   @JsonProperty("job_title") String  Jobtitle,
                   @JsonProperty("department") String  Department,
                   @JsonProperty("date") String  date)
    {
      mId = Id;
      mGenId = GenId;
      mFirstName = FirstName;
      mLastName= LastName;
      mFullName= FullName;
      mEmail= Email;
      mPhone= Phone;
      mCompanyName= CompanyName;
      mWebsite= Website;
      mAdress= Adress;
      mIndustryType= IndustryType;
      mJobtitle= Jobtitle;
      mDepartment= Department;
      mDate= date;
    }
  }


  @JsonProperty("count") public String mCount;


  @JsonProperty("data")  public ArrayList<Visitor> mVisitorList;

  @JsonCreator
  public Visitors(  @JsonProperty("count") String Count,
                    @JsonProperty("data") ArrayList<Visitor> visitorList)
  {
    mCount = Count;
    mVisitorList = visitorList;
  }
}
