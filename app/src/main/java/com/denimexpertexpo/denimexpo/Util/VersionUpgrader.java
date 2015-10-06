package com.denimexpertexpo.denimexpo.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by ratul on 10/6/15.
 */
public class VersionUpgrader extends AsyncTask <String, String, String>
{
    //CONSTANTS
    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";

    //MEMBERS
    private Context mAppContext;
    private AppStoreVersionFetchListener listener;



    public interface AppStoreVersionFetchListener
    {
        void versionFetchCompleted(String remoteVersion, Boolean isError);
    }

    public VersionUpgrader(Context ctx)
    {
        mAppContext = ctx;
    }



    /*
     * THIS IS A UTILITY FUNCTION WHICH WILL
     * GRAB THE CURRENTLY RUNNING APP
     * VERSION DECLARED IN MANIFEST / GRADLE BUILD
     */
    public String getRunningAppVersion() {
        String versionName = null;
        try {
            versionName = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            Log.e("version name not found", ex.toString());
            ex.printStackTrace();
        } finally {
            return versionName;
        }
    }

   /*
    * THIS IS A UTILITY FUNCTION WHICH WILL
    * FETCH THE PLAY STORE RUNNING VERSION
    *
    */
    public void fetchPlayStoreAppVersion(AppStoreVersionFetchListener listener)
    {
        String finalURL = PLAY_STORE_URL + mAppContext.getPackageName();
        Log.e("requesting url", finalURL);

        this.listener = listener;
        this.execute(finalURL);
    }

    @Override
    protected String doInBackground(String... uris) {

        try{
            Document doc = Jsoup.connect(uris[0]).get();

            Elements elements = doc.select("div[class=content][itemprop=softwareVersion]");

            if(elements.size() == 1)
            {
                return  elements.first().html();
            }
            else
            {
                return null;
            }


        }catch (IOException ex)
        {
            Log.e("Ecetopp", ex.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if(result == null)
        {
            if(listener != null)
            {
                listener.versionFetchCompleted("", true);
            }
        }
        else
        {
            if(listener != null)
            {
                listener.versionFetchCompleted(result, false);
            }
        }
    }
}
