package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.Fragments.RegistrationFormFragment;
import com.denimexpertexpo.denimexpo.Fragments.RegistrationSuccessFragment;
import com.denimexpertexpo.denimexpo.Interfaces.RegistrationEventHandler;
import com.denimexpertexpo.denimexpo.R;

public class RegisrtationActivity extends Activity implements RegistrationEventHandler {


    private static final String REG_FORM_FRAG_TAG = "REG_FORM";
    private static final String REG_SUCESS_FRAG_TAG = "REG_COMPLETED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        /*
            By default show the registration form
         */
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RegistrationFormFragment regForm = new RegistrationFormFragment();
        regForm.setRegistrationEventHandler(this);
        fragmentTransaction.add(R.id.registration_holder, regForm, RegisrtationActivity.REG_FORM_FRAG_TAG);
        fragmentTransaction.commit();


        getActionBar().setTitle("Login");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /*
    RegistrationEventHandler callback implementer
     */
    public void registrationProcessCompleted(String emailAddr) {
        Toast.makeText(this, "Registration process completed : " + emailAddr, Toast.LENGTH_LONG).show();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        Fragment formFrgament = getFragmentManager().findFragmentByTag(REG_FORM_FRAG_TAG);
        if (formFrgament != null) {
            //remove the form frag
            fragmentTransaction.remove(formFrgament);
        }


        //add the success frag
        RegistrationSuccessFragment regCompFrag = new RegistrationSuccessFragment();
        fragmentTransaction.add(R.id.registration_holder, regCompFrag, RegisrtationActivity.REG_SUCESS_FRAG_TAG);

        fragmentTransaction.commit();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }

        return true;
    }
}
