package com.denimexpertexpo.denimexpo.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denimexpertexpo.denimexpo.R;

/**
 * Created by ratul on 7/9/2015.
 */
public class SuccessThumbFragment extends Fragment {

    public static final String MSG_KEY = "MSG";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View regSuccess = inflater.inflate(R.layout.fragmemt_successful_thumbs, null);

        Bundle passedData = this.getArguments();
        String msg = passedData.getString(SuccessThumbFragment.MSG_KEY);
        TextView msgBox = (TextView)regSuccess.findViewById(R.id.thumbs_msg_box);



        msgBox.setText(msg);

        return regSuccess;
    }
}
