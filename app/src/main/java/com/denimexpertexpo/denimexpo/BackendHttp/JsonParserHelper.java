package com.denimexpertexpo.denimexpo.BackendHttp;

import android.util.Log;

import com.denimexpertexpo.denimexpo.DenimDataClasses.BarcodeTypeChecker;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Exhibitors;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Schedule;
import com.denimexpertexpo.denimexpo.DenimDataClasses.SitemapUpdateInfo;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Visitors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


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
                    new TypeReference<ArrayList<Schedule>>() {

                    });

        }
        catch (Exception ex)
        {
            Log.d(JsonParserHelper.class.getSimpleName(), "problem with parseDownTheSchedules");
        }
        return schedules;
    }


    public static Visitors parseDownTheVisitors(String result)
    {
        Visitors  visitors = null;

        try{
            ObjectMapper mapper = new ObjectMapper();
            visitors = mapper.readValue(result, Visitors.class);
        }
        catch (Exception ex)
        {
            Log.d(JsonParserHelper.class.getSimpleName(), "problem with parseDownTheVisitors " + ex);
        }

        return visitors;
    }

    public static Exhibitors parseDownTheExhibitors(String result)
    {
        Exhibitors  exhibitors = null;

        try{
            ObjectMapper mapper = new ObjectMapper();
            exhibitors = mapper.readValue(result, Exhibitors.class);
        }
        catch (Exception ex)
        {
            Log.d(JsonParserHelper.class.getSimpleName(), "problem with parseDownTheVisitors " + ex);
        }

        return exhibitors;
    }

    public static final int INVALID_USER_TYPE = -1;
    public static final int VISITOR_USER_TYPE = 1;
    public static final int EXHIBITOR_USER_TYPE = 2;
    public static int parseAppropriateUserTypeFromBarcodeApi(String response)
    {
        BarcodeTypeChecker barcodeTypeChecker = null;

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            barcodeTypeChecker = mapper.readValue(response, BarcodeTypeChecker.class);

            if(barcodeTypeChecker != null && Long.parseLong(barcodeTypeChecker.mCount) == 1)
            {
                if(barcodeTypeChecker.mUsertype.equalsIgnoreCase("exhibitor"))
                {
                    return EXHIBITOR_USER_TYPE;
                }
                else if(barcodeTypeChecker.mUsertype.equalsIgnoreCase("visitor"))
                {
                    return VISITOR_USER_TYPE;
                }
            }
            else
                return INVALID_USER_TYPE;

            Log.e("barcode type checker", barcodeTypeChecker.mUsertype + " " + barcodeTypeChecker.mCount );

        }catch (Exception ex)
        {
            Log.d(JsonParserHelper.class.getSimpleName(), "problem with parseAppropriateUserTypeFromBarcodeApi " + ex);
        }

        return INVALID_USER_TYPE;
    }


    public static SitemapUpdateInfo parseSiteMapUpdateInfo(String result)
    {
        SitemapUpdateInfo sitemapUpdateInfo = null;

       try{
           ObjectMapper objectMapper = new ObjectMapper();
           sitemapUpdateInfo = objectMapper.readValue(result, SitemapUpdateInfo.class);

       }catch (Exception ex)
       {
           Log.e("Sitemap update info", ex.toString());
       }

        return sitemapUpdateInfo;
    }
}
