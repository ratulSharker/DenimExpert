package com.denimexpertexpo.denimexpo.Fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.denimexpertexpo.denimexpo.Adapters.SpinnerHintAdapter;
import com.denimexpertexpo.denimexpo.Interfaces.RegistrationEventHandler;
import com.denimexpertexpo.denimexpo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ratul on 7/8/2015.
 */
public class RegistrationFormFragment extends Fragment {

    private RegistrationEventHandler registrationEventHandler;

    private Spinner spnTitleSelect;

    private Spinner spnCountrySelect;
    private Button btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View regForm = inflater.inflate(R.layout.fragment_registration_form, null);

        //getting all the reference
        spnTitleSelect = (Spinner) regForm.findViewById(R.id.register_title_spinner);
        spnCountrySelect = (Spinner) regForm.findViewById(R.id.register_country_spinner);
        btnRegister = (Button)regForm.findViewById(R.id.registration_register_button);


        this.setupTheViews();


        return regForm;
    }



    private void setupTheViews(){



        Resources resources = this.getResources();
        String[] titleList = resources.getStringArray(R.array.human_title);
        ArrayList<String> allTitles = new ArrayList<String>();
        for(String country : titleList)
        {
            allTitles.add(country);
        }
        allTitles.add("Select a Title");

        SpinnerHintAdapter titlesAdapter = new SpinnerHintAdapter(this.getActivity(), android.R.layout.simple_spinner_item, allTitles);
        spnTitleSelect.setAdapter(titlesAdapter);
        spnTitleSelect.setSelection(allTitles.size()-1);





        String[] allCountryList = resources.getStringArray(R.array.all_countries);
        ArrayList<String> allCountry = new ArrayList<String>();
        for(String country : allCountryList)
        {
            allCountry.add(country);
        }
        allCountry.add("Select a Country");

        SpinnerHintAdapter countryAdapter = new SpinnerHintAdapter(this.getActivity(), android.R.layout.simple_spinner_item, allCountry);
        spnCountrySelect.setAdapter(countryAdapter);
        spnCountrySelect.setSelection(allCountry.size()-1);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO handle the registration process here (Http request)
                //TODO run following code on completion of the registration process with appropriate email id
                if(registrationEventHandler != null)
                {
                    //now time to provide signal to activity that registration process is completed
                    registrationEventHandler.registrationProcessCompleted("Sharker.ratul.08@gmail.com");
                }

            }
        });

    }




    /*
    delegate setter method
     */
    public void setRegistrationEventHandler(RegistrationEventHandler evtHndlr)
    {
        this.registrationEventHandler = evtHndlr;
    }
}
