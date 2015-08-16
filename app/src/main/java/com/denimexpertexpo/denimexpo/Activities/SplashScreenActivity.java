package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.Constants.DenimContstants;
import com.denimexpertexpo.denimexpo.DenimDataClasses.AuthenticationReply;
import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncExhibitorHelper;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncVisitorHelper;
import com.denimexpertexpo.denimexpo.StaticStyling.CustomStyling;

import java.io.UnsupportedEncodingException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends Activity implements AsyncHttpRequestHandler{

    private static final int AUTO_HIDE_DELAY_MILLIS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        CustomStyling.setCustomFontToTextView(this, "big_title_gipsiero.otf", R.id.splash_screen_title);
        //this.checkDPI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {

            SharedPreferences preferences = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE);
            String username = preferences.getString(DenimContstants.SHARED_PREFS_USERNAME, null);
            String password = preferences.getString(DenimContstants.SHARED_PREFS_PASSWORD, null);

            Log.e("Login credential", username + ")(" + password);

            if(username == null || password == null)
            {
                //no username & password in the sharedPreference
                //goto the login screen after the splash ended
                this.proceedToNextActivity(SignupLoginActivity.class);
            }
            else
            {
                //a valid username & password found in the sharedPreference
                //verify rhe username & password through login api
                try{
                    Log.e("Trying authenticating", "HTTP");
                    String buildedUrl = AsyncHttpClient.BuildLoginApiUrl(username, password);
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient(this);
                    asyncHttpClient.execute(buildedUrl);

                }
                catch (UnsupportedEncodingException ex)
                {
                    //can't encode username password, relogin
                    this.proceedToNextActivity(SignupLoginActivity.class);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //nothing happen on back pressed
    }



    /*
    Async Http request Handler
     */
    @Override
    public void onResponseRecieved(String response) {
        //login response returned
        //parse the response
        final AuthenticationReply authenticationReply = JsonParserHelper.parseLoginTryResponse(response);
        if(authenticationReply != null && authenticationReply.mFound.compareTo("true")== 0)
        {
            Log.e("Authenticated", "HTTP");
            //authorized

            //get the latest user details

            //depending on the usertype we do the request
            //then after fetching it, put it on the database, depending on which kind of user he is
            String userDetailsURL = "";
            if(authenticationReply.mUsertype.toLowerCase().compareTo(DenimContstants.USER_TYPE_VISITOR) == 0)
            {
                //build the visitor url
                userDetailsURL = AsyncHttpClient.BuildSpecificVisitorDetailsApiUrl(authenticationReply.mId);
            }
            else if(authenticationReply.mUsertype.toLowerCase().compareTo(DenimContstants.USER_TYPE_EXHIBITOR) == 0)
            {
                //build the exhibitor url
                userDetailsURL = AsyncHttpClient.BuildSpecificExhibitorDetailsApiUrl(authenticationReply.mId);
            }

            //now send the request with inplace AsyncHttpResponseHandler
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(new AsyncHttpRequestHandler() {
                @Override
                public void onResponseRecieved(String response) {

                    if(authenticationReply.mUsertype.toLowerCase().compareTo(DenimContstants.USER_TYPE_VISITOR) == 0)
                    {
                        AsyncVisitorHelper asyncVisitorHelper = new AsyncVisitorHelper(SplashScreenActivity.this, null);
                        asyncVisitorHelper.processRecievedVisitorResponse(response);

                    }
                    else if(authenticationReply.mUsertype.toLowerCase().compareTo(DenimContstants.USER_TYPE_EXHIBITOR) == 0)
                    {
                        //handle the response as exhibitor
                        AsyncExhibitorHelper asyncExhibitorHelper = new AsyncExhibitorHelper(SplashScreenActivity.this, null);
                        asyncExhibitorHelper.processRecievedExhibitorResponse(response);
                    }

                    //now we can proceed to the main menu
                    proceedToNextActivity(MainMenuActivity.class);
                }

                @Override
                public void onHttpErrorOccured() {
                    proceedToNextActivity(MainMenuActivity.class);
                }
            });

            if(userDetailsURL != null)
                asyncHttpClient.execute(userDetailsURL);
            else
                proceedToNextActivity(MainMenuActivity.class);

        }
        else
        {
            Log.e("UnAuthorized", "HTTP");
            //unauthorized
            //proceed to the login screen
            this.proceedToNextActivity(SignupLoginActivity.class);
        }
    }

    @Override
    public void onHttpErrorOccured() {

        Log.e("No internet connection", "HTTP");
        //no iinternet connection
        //proceed to the main menu
        this.proceedToNextActivity(MainMenuActivity.class);
    }

    /**
     * private helper methods
     */
    private void checkDPI()
    {
        int density= getResources().getDisplayMetrics().densityDpi;
        Context context = getApplicationContext();

        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(context, "LDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(context, "MDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(context, "HDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void proceedToNextActivity(final Class nextActivity)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, nextActivity);
                startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        };

        //move to login activity within 3 seconds
        new Handler().postDelayed(runnable, AUTO_HIDE_DELAY_MILLIS);
    }

}
