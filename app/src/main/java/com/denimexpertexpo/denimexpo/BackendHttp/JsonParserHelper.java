package com.denimexpertexpo.denimexpo.BackendHttp;

import com.denimexpertexpo.denimexpo.DenimDataClasses.Schedule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ratul on 7/13/2015.
 */
public class JsonParserHelper  {


    /**
     * Used for process the location api
     * return null on error, otherwise everything is ok.
     * Actually two double value will be returned
     * at index 0 -> lattitude
     * at index 1 -> longitude
     */
    public static LatLng parseDownTheLocation(String result)
    {

        try{
            JSONObject jsonObject = new JSONObject(result);

            double[] latLong = new double[2];

            latLong[0] = jsonObject.getDouble("latitude");
            latLong[1] = jsonObject.getDouble("longitude");

            return new LatLng(latLong[0], latLong[1]);
        }
        catch (JSONException ex)
        {
            //could not parse the
            return null;
        }
    }


    public static ArrayList<Schedule> parseDownTheSchedules(String result)
    {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            schedules = mapper.readValue(result,
                    new TypeReference<ArrayList<Schedule>>() {});

        }
        catch (Exception ex)
        {

        }

        return schedules;
    }
}
