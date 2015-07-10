package com.denimexpertexpo.denimexpo.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denimexpertexpo.denimexpo.R;

/**
 * Created by ratul on 7/9/2015.
 */
public class RegistrationSuccessFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View regSuccess = inflater.inflate(R.layout.fragmemt_registration_successful, null);

        return regSuccess;
    }
}
