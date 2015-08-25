package com.denimexpertexpo.denimexpo.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.denimexpertexpo.denimexpo.Adapters.SpinnerHintAdapter;
import com.denimexpertexpo.denimexpo.DenimDataClasses.RegistrationForm;
import com.denimexpertexpo.denimexpo.Interfaces.RegistrationEventHandler;
import com.denimexpertexpo.denimexpo.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Struct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ratul on 7/8/2015.
 */
public class RegistrationFormFragment extends Fragment {

    private RegistrationEventHandler registrationEventHandler;



    //personal detail
    private Spinner     spnTitleSelect;
    private EditText    mEditFirstName;
    private EditText    mEditLastName;

    //contact details
    private EditText    mEmail;
    private EditText    mMobile;
    private EditText    mIndustryType;
    private EditText    mJobTitle;
    private EditText    mConcernedDepartment;

    //company details
    private EditText    mCompanyWebpagae;

    //House info
    private EditText    mHouseName;
    private EditText    mHouseNo;
    private EditText    mHouseLevel;
    private EditText    mRoadNo;
    private EditText    mSector;
    private EditText    mArea;
    private EditText    mCity;
    private EditText    mStateOrProvince;
    private EditText    mZipCode;


    //select country
    private Spinner     spnCountrySelect;


    //register button
    private Button      btnRegister;

    private ArrayList<String> mTitleArr;
    private ArrayList<String> mCountryArr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View regForm = inflater.inflate(R.layout.fragment_registration_form, null);

        //getting all the reference
        spnTitleSelect = (Spinner) regForm.findViewById(R.id.register_title_spinner);
        spnCountrySelect = (Spinner) regForm.findViewById(R.id.register_country_spinner);
        btnRegister = (Button) regForm.findViewById(R.id.registration_register_button);

        //get the edit texts
        this.mEditFirstName = (EditText) regForm.findViewById(R.id.registration_first_name);
        this.mEditLastName  = (EditText) regForm.findViewById(R.id.registration_last_name);
        this.mEmail = (EditText) regForm.findViewById(R.id.registration_email);
        this.mMobile = (EditText) regForm.findViewById(R.id.registration_mobile);
        this.mIndustryType = (EditText) regForm.findViewById(R.id.registration_industry_type);
        this.mJobTitle = (EditText) regForm.findViewById(R.id.registration_job_title);
        this.mConcernedDepartment = (EditText) regForm.findViewById(R.id.registration_concerned_department);
        this.mCompanyWebpagae = (EditText) regForm.findViewById(R.id.registration_company_webpage);

        this.mHouseName = (EditText) regForm.findViewById(R.id.registration_house_name);
        this.mHouseNo   = (EditText) regForm.findViewById(R.id.registration_house_no);
        this.mHouseLevel = (EditText) regForm.findViewById(R.id.registration_level);
        this.mRoadNo = (EditText) regForm.findViewById(R.id.registration_road_no);
        this.mSector = (EditText) regForm.findViewById(R.id.registration_sector);
        this.mArea = (EditText) regForm.findViewById(R.id.registration_area);
        this.mCity = (EditText) regForm.findViewById(R.id.registration_city);
        this.mStateOrProvince = (EditText) regForm.findViewById(R.id.registration_state_province);
        this.mZipCode = (EditText) regForm.findViewById(R.id.registration_zipcode);


        this.setupTheViews();


        return regForm;
    }


    private void setupTheViews() {


        Resources resources = this.getResources();
        String[] titleList = resources.getStringArray(R.array.human_title);
        mTitleArr = new ArrayList<String>();
        for (String country : titleList) {
            mTitleArr.add(country);
        }
        mTitleArr.add("Select a Title");

        SpinnerHintAdapter titlesAdapter = new SpinnerHintAdapter(this.getActivity(), android.R.layout.simple_spinner_item, mTitleArr);
        spnTitleSelect.setAdapter(titlesAdapter);
        spnTitleSelect.setSelection(mTitleArr.size() - 1);


        String[] allCountryList = resources.getStringArray(R.array.all_countries);
        mCountryArr = new ArrayList<String>();
        for (String country : allCountryList) {
            mCountryArr.add(country);
        }
        mCountryArr.add("Select a Country");

        SpinnerHintAdapter countryAdapter = new SpinnerHintAdapter(this.getActivity(), android.R.layout.simple_spinner_item, mCountryArr);
        spnCountrySelect.setAdapter(countryAdapter);
        spnCountrySelect.setSelection(mCountryArr.size() - 1);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO handle the registration process here (Http request)
                //TODO run following code on completion of the registration process with appropriate email id

                if(checkIsAllFieldAreValid())
                {
                    //now we can send the register http request
                    /*
                    if (registrationEventHandler != null) {
                        //now time to provide signal to activity that registration process is completed
                        registrationEventHandler.registrationProcessCompleted("Sharker.ratul.08@gmail.com");
                    }*/

                    RegistrationForm registrationForm = new RegistrationForm();
                    registrationForm.mTitle= mTitleArr.get(spnTitleSelect.getSelectedItemPosition());
                    registrationForm.mFirstName=mEditFirstName.getText().toString();
                    registrationForm.mLastName=mEditLastName.getText().toString();
                    registrationForm.mEmail=mEmail.getText().toString();
                    registrationForm.mCompanyName="";
                    registrationForm.mCountryCode="";
                    registrationForm.mMobile=mMobile.getText().toString();
                    registrationForm.mIndustryType=mIndustryType.getText().toString();
                    registrationForm.mIndustryTypeOther="";
                    registrationForm.mJobtitle=mJobTitle.getText().toString();
                    registrationForm.mJobtitleOther="";
                    registrationForm.mConcernedDepartment=mConcernedDepartment.getText().toString();
                    registrationForm.mDepartmentOther="";
                    registrationForm.mHouseNo=mHouseNo.getText().toString();
                    registrationForm.mHouseName=mHouseName.getText().toString();
                    registrationForm.mLevel=mHouseLevel.getText().toString();
                    registrationForm.mRoadNo=mRoadNo.getText().toString();
                    registrationForm.mLane="";
                    registrationForm.mBlock="";
                    registrationForm.mSector=mSector.getText().toString();
                    registrationForm.mArea=mArea.getText().toString();
                    registrationForm.mCity=mCity.getText().toString();
                    registrationForm.mState=mStateOrProvince.getText().toString();
                    registrationForm.mZip=mZipCode.getText().toString();
                    registrationForm.mCountry=mCountryArr.get(spnCountrySelect.getSelectedItemPosition());
                    registrationForm.mWebUrl=mCompanyWebpagae.getText().toString();
                    //this.mPassword="";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    registrationForm.mDate = sdf.format(new Date());

                    ObjectMapper objectMapper = new ObjectMapper();

                    try{
                        Log.e("jackson", objectMapper.writeValueAsString(registrationForm));
                    }
                    catch (Exception ex)
                    {
                        Log.e("exception", ex.toString());
                    }

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationFormFragment.this.getActivity());
                    builder.setTitle("Sorry");
                    builder.setMessage("Some of the fields are still empty");
                    builder.setPositiveButton("ok", null);
                    builder.setNegativeButton("", null);
                    builder.create().show();
                }


            }
        });
    }


    /*
    delegate setter method
     */
    public void setRegistrationEventHandler(RegistrationEventHandler evtHndlr) {
        this.registrationEventHandler = evtHndlr;
    }

    private boolean checkIsAllFieldAreValid()
    {
        //personal detail
        //this.spnTitleSelect;

        if(this.mEditFirstName.getText().toString().length()            == 0 ||
                this.mEditLastName.getText().toString().length()        == 0 ||
                this.mEmail.getText().toString().length()               == 0 ||
                this.mMobile.getText().toString().length()              == 0 ||
                this.mIndustryType.getText().toString().length()        == 0 ||
                this.mJobTitle.getText().toString().length()            == 0 ||
                this.mConcernedDepartment.getText().toString().length() == 0 ||
                this.mCompanyWebpagae.getText().toString().length()     == 0 ||
                this.mHouseName.getText().toString().length()           == 0 ||
                this.mHouseNo.getText().toString().length()             == 0 ||
                this.mHouseLevel.getText().toString().length()          == 0 ||
                this.mRoadNo.getText().toString().length()              == 0 ||
                this.mSector.getText().toString().length()              == 0 ||
                this.mArea.getText().toString().length()                == 0 ||
                this.mCity.getText().toString().length()                == 0 ||
                this.mStateOrProvince.getText().toString().length()     == 0 ||
                this.mStateOrProvince.getText().toString().length()     == 0 ||
                this.mZipCode.getText().toString().length()             == 0 ||
                this.spnTitleSelect.getSelectedItemPosition() == this.mTitleArr.size()-1 ||
                this.spnCountrySelect.getSelectedItemPosition() == this.mCountryArr.size()-1)
        {
            return false;
        }
        else

            return true;


        //select country
        //spnTitleSelect
        //private Spinner     spnCountrySelect;
    }
}
