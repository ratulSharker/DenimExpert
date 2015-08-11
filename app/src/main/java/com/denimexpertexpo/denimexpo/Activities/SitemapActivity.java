package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpImageHelper;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.DenimDataClasses.SitemapUpdateInfo;
import com.denimexpertexpo.denimexpo.R;

import java.io.File;

public class SitemapActivity extends Activity implements AsyncHttpImageHelper.onAsyncHttpImageDownloadComplete, AsyncHttpRequestHandler{

    private SubsamplingScaleImageView mImageView;
    private TextView    mMsg;
    private String mImagePath;

    private static final String SHARED_PREFS_NAME = SitemapActivity.class.getName();
    private static final String SHARED_PREFS_LAST_EVENT_MAP_ID_KEY = "shared_pref_event_map_id";
    private static final String SHARED_PREFS_LAST_EVENT_MAP_LOCATION = "shared_pref_event_map_filesystem_path";


    //this id is used for saving in the savedInstance intent for retrieving the last retreived path
    private static final String SAVED_IMG_PATH_KEY = "image_path_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitemap);

        this.mImageView = (SubsamplingScaleImageView)findViewById(R.id.sitemap_image);
        this.mMsg = (TextView)findViewById(R.id.sitemap_msg);



        //whenever app rotated, then get the saved image path
        if(savedInstanceState != null && savedInstanceState.containsKey(SAVED_IMG_PATH_KEY))
        {
           mImagePath = savedInstanceState.getString(SAVED_IMG_PATH_KEY);
        }
        else
        {
            mImagePath = null;
        }


        if(mImagePath != null && new File(mImagePath).exists())
        {
            //app screen rotated
            Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
            //this.mImageView.setImageBitmap(bitmap);
            this.mImageView.setImage(ImageSource.bitmap(bitmap));
            this.mMsg.setVisibility(View.INVISIBLE);
        }
        else
        {
            //need a download checking for the eventMap ID

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(this);
            asyncHttpClient.execute(AsyncHttpClient.EVENTMAP_API_URL);

            //first time
            //AsyncHttpImageHelper asyncHttpImageHelper = new AsyncHttpImageHelper(this, this);
            //asyncHttpImageHelper.execute(AsyncHttpImageHelper.SITEMAP_IMAGE_URL);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(SAVED_IMG_PATH_KEY, this.mImagePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sitemap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    /*
     * onAsyncImageDownloadHelper methods
     */
    @Override
    public void downloadedImagePath(String path) {


        if(path != null && new File(path).exists() == true)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //this.mImageView.setImageBitmap(bitmap);
            this.mImageView.setImage(ImageSource.bitmap(bitmap));
            this.mMsg.setVisibility(View.INVISIBLE);

            //save the successfull valid path
            mImagePath = path;

            //save in stone :D
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(SHARED_PREFS_LAST_EVENT_MAP_LOCATION, mImagePath);
            editor.commit();

        }
        else
        {
            //show some dialouge
            this.mMsg.setText("Image Can't be shown because no offline image is available or internet connection unavailable");
        }
    }

    @Override
    public void onResponseRecieved(String response) {


        Log.e("event API response ", response);

        //parse the event id
        //check it with the existing saved id
        //then check the filepath, is filepath exists
        //if any of the above is false -- start the downloading thing
        //otherwise show the image thing

        SitemapUpdateInfo sitemapUpdateInfo = JsonParserHelper.parseSiteMapUpdateInfo(response);
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        String savedSitemapId = sharedPreferences.getString(SHARED_PREFS_LAST_EVENT_MAP_ID_KEY, null);
        String savedFilePath = sharedPreferences.getString(SHARED_PREFS_LAST_EVENT_MAP_LOCATION, null);

        if(sitemapUpdateInfo != null &&     //do the null checking
                savedSitemapId != null &&
                savedFilePath != null &&
                savedSitemapId.compareTo(sitemapUpdateInfo.mEventMapId) == 0 && //checking for id match
                new File(savedFilePath).exists()    //checking for file to be exists
                )
        {
            //local file exists and we are ok to show that filepath
            Bitmap bitmap = BitmapFactory.decodeFile(savedFilePath);
            //this.mImageView.setImageBitmap(bitmap);
            this.mImageView.setImage(ImageSource.bitmap(bitmap));
            this.mMsg.setVisibility(View.INVISIBLE);

            //save the successfull valid path
            mImagePath = savedFilePath;
        }
        else
        {
            //there are many different issue can happen, thus why we have to download the image again :)
            //1. server event Map api responses with updated event map id
            //2. local file exitst on the cache, it is very possible not to exists that file

            //what to do we do
            //save the event id in the sharedPreference
            //request for image download

            if(sitemapUpdateInfo != null && sitemapUpdateInfo.mEventMapId != null)
            {

                SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE).edit();
                //event map api id api return a valid somehing :)
                editor.putString(SHARED_PREFS_LAST_EVENT_MAP_ID_KEY, sitemapUpdateInfo.mEventMapId);
                editor.commit();

                AsyncHttpImageHelper asyncHttpImageHelper = new AsyncHttpImageHelper(this, this);
                asyncHttpImageHelper.execute(AsyncHttpImageHelper.SITEMAP_IMAGE_URL);
            }
            else
            {
                //show some dialouge
                this.mMsg.setText("Image Can't be shown because no offline image is available or internet connection unavailable");
            }


        }


    }

    @Override
    public void onHttpErrorOccured() {
        Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
    }
}
