package com.denimexpertexpo.denimexpo.DenimDataClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ratul on 8/19/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationForm {

    /*
     "title":"Mr.",
     "first_name":"Mahmudul",
     "last_name":"hassan",
     "email":"tuhiyyn.snnnm@gmail.com",
     "company_name":"INDOCHINE INTERNATIONAL",
     "country_code":" 880",
      "mobile":"191583926",
     "industry_type":"Apparel Buying House \/ Sourcing Office",
     "industry_type_other":"-",
     "job_title":"Merchandiser (General Manager)",
     "job_title_other":"-",
     "concerned_department":"Merchandising",
     "department_other":"-",
     "house_no":"",
     "house_name":"Crystal Palace,",
     "level":"7th Floor,",
     "road_no":"",
     "lane":"",
     "block":"",
     "sector":"Gulshan-1",
     "area":"Gulshan-1",
     "city":"Dhaka",
     "state":"Dhaka",
     "zip":"1212",
     "country":"Bangladesh",
     "web_url":"n\/a",
     "password":"V1165",
     "date":"7\/6\/2015 4:21:12 PM"
     */

    @JsonProperty("title")                  public String mTitle;
    @JsonProperty("first_name")             public String mFirstName;
    @JsonProperty("last_name")              public String mLastName;
    @JsonProperty("email")                  public String mEmail;
    @JsonProperty("company_name")           public String mCompanyName;
    @JsonProperty("country_code")           public String mCountryCode;
    @JsonProperty("mobile")                 public String mMobile;
    @JsonProperty("industry_type")          public String mIndustryType;
    @JsonProperty("industry_type_other")    public String mIndustryTypeOther;
    @JsonProperty("job_title")              public String mJobtitle;
    @JsonProperty("job_title_other")        public String mJobtitleOther;
    @JsonProperty("concerned_department")   public String mConcernedDepartment;
    @JsonProperty("department_other")       public String mDepartmentOther;
    @JsonProperty("house_no")               public String mHouseNo;
    @JsonProperty("house_name")             public String mHouseName;
    @JsonProperty("level")                  public String mLevel;
    @JsonProperty("road_no")                public String mRoadNo;
    @JsonProperty("lane")                   public String mLane;
    @JsonProperty("block")                  public String mBlock;
    @JsonProperty("sector")                 public String mSector;
    @JsonProperty("area")                   public String mArea;
    @JsonProperty("city")                   public String mCity;
    @JsonProperty("state")                  public String mState;
    @JsonProperty("zip")                    public String mZip;
    @JsonProperty("country")                public String mCountry;
    @JsonProperty("web_url")                public String mWebUrl;
    @JsonProperty("password")               public String mPassword;
    @JsonProperty("date")                   public String mDate;

    public RegistrationForm()
    {
        this.mTitle="";
        this.mFirstName="";
        this.mLastName="";
        this.mEmail="";
        this.mCompanyName="";
        this.mCountryCode="";
        this.mMobile="";
        this.mIndustryType="";
        this.mIndustryTypeOther="";
        this.mJobtitle="";
        this.mJobtitleOther="";
        this.mConcernedDepartment="";
        this.mDepartmentOther="";
        this.mHouseNo="";
        this.mHouseName="";
        this.mLevel="";
        this.mRoadNo="";
        this.mLane="";
        this.mBlock="";
        this.mSector="";
        this.mArea="";
        this.mCity="";
        this.mState="";
        this.mZip="";
        this.mCountry="";
        this.mWebUrl="";
        this.mPassword="";
        this.mDate="";
    }
}
