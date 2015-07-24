package com.denimexpertexpo.denimexpo.DirectionApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by ratul on 7/21/2015.
 */
public interface DirectionListener {

    public void onDirectionRecieved(ArrayList<LatLng> direction);

    public void onDirectionException(Exception ex);
}
