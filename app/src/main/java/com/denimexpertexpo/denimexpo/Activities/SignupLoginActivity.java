package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.Constants.DenimContstants;
import com.denimexpertexpo.denimexpo.DBHelper.VisitorContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.AuthenticationReply;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Visitors;
import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncExhibitorHelper;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncVisitorHelper;
import com.denimexpertexpo.denimexpo.StaticStyling.CustomStyling;

import java.io.UnsupportedEncodingException;


public class SignupLoginActivity extends Activity implements AsyncHttpRequestHandler{

    private EditText mEdittextUsername, mEdittextPassword;
    private Button mBtnRegister, mBtnLogin, mBtnSkiplogin;


    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);

        CustomStyling.setCustomFontToTextView(this, "big_title_gipsiero.otf", R.id.signup_title);

        //collect the reference of all of the button & editText
        this.mEdittextUsername = (EditText) findViewById(R.id.editText_username);
        this.mEdittextPassword = (EditText) findViewById(R.id.editText_password);

        this.mBtnRegister = (Button) findViewById(R.id.button_signup);
        this.mBtnLogin = (Button) findViewById(R.id.button_login);
        this.mBtnSkiplogin = (Button) findViewById(R.id.button_skip);

        this.mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle the register button request
                //Toast.makeText(SignupLoginActivity.this, "Register clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupLoginActivity.this, RegisrtationActivity.class);
                startActivity(intent);
            }
        });

        this.mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle the register button request
                if(verifyUsernameAndPasswordBoxLocally())
                {
                    try{
                        String buildedUrl = AsyncHttpClient.BuildLoginApiUrl(mEdittextUsername.getText().toString()
                                , mEdittextPassword.getText().toString());
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(SignupLoginActivity.this);
                        asyncHttpClient.execute(buildedUrl);

                        mProgressDialog = new ProgressDialog(SignupLoginActivity.this);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setMessage("Please wait...");
                        mProgressDialog.setTitle("Authenticating");
                        mProgressDialog.show();
                    }
                    catch (UnsupportedEncodingException ex)
                    {
                        Log.e("wrong", ex.toString());
                    }


                }
            }
        });

        this.mBtnSkiplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle the register button request
                //Toast.makeText(SignupLoginActivity.this, "Skip clicked", Toast.LENGTH_SHORT).show();

                proceedToMainMenuWithRegistration(false);
            }
        });
    }


    private Boolean verifyUsernameAndPasswordBoxLocally()
    {
        if(this.mEdittextUsername.getText().length() == 0)
        {
            this.showOkAlertDialouge("Sorry", "Username can't be empty");
            return false;
        }
        else if(this.mEdittextPassword.getText().length() == 0)
        {
            this.showOkAlertDialouge("Sorry", "Password cant be empty");
            return false;
        }

        return true;
    }





    /*
    AsyncHttpRequestHandler
     */
    @Override
    public void onResponseRecieved(String response) {
        Log.e("response", response);


        //parse the response
        final AuthenticationReply authenticationReply = JsonParserHelper.parseLoginTryResponse(response);
        if(authenticationReply != null && authenticationReply.mFound.compareTo("true")== 0)
        {
            mProgressDialog.setTitle("Authenticated");
            mProgressDialog.setMessage("getting user details");
            Log.e("Authenticated", "Authenticated");

            //save the last authorized credential to the sharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(DenimContstants.SHARED_PREFS_USERNAME, mEdittextUsername.getText().toString());
            editor.putString(DenimContstants.SHARED_PREFS_PASSWORD, mEdittextPassword.getText().toString());

            //saving the user type & id for later use -- dont forget to erase these before logout
            editor.putString(DenimContstants.SHARED_PREFS_USER_TYPE, authenticationReply.mUsertype.toLowerCase());
            editor.putString(DenimContstants.SHARED_PREFS_USER_ID, authenticationReply.mId);
            editor.commit();

            //now we get the userttpe
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

                    mProgressDialog.dismiss();

                    if(authenticationReply.mUsertype.toLowerCase().compareTo(DenimContstants.USER_TYPE_VISITOR) == 0)
                    {
                        AsyncVisitorHelper asyncVisitorHelper = new AsyncVisitorHelper(SignupLoginActivity.this, null);
                        asyncVisitorHelper.processRecievedVisitorResponse(response);

                    }
                    else if(authenticationReply.mUsertype.toLowerCase().compareTo(DenimContstants.USER_TYPE_EXHIBITOR) == 0)
                    {
                        //handle the response as exhibitor
                        AsyncExhibitorHelper asyncExhibitorHelper = new AsyncExhibitorHelper(SignupLoginActivity.this, null);
                        asyncExhibitorHelper.processRecievedExhibitorResponse(response);
                    }

                    //now we can proceed to the
                    proceedToMainMenuWithRegistration(true);
                }

                @Override
                public void onHttpErrorOccured() {
                    showOkAlertDialouge("Sorry", "Can't fetch the user details, try later");
                }
            });
            asyncHttpClient.execute(userDetailsURL);
        }
        else
        {
            mProgressDialog.dismiss();
            this.showOkAlertDialouge("Unauthorized", "Wrong username or password");
        }
    }

    @Override
    public void onHttpErrorOccured() {
        Log.e("Http error", "error on login activity");

        this.mProgressDialog.dismiss();
        this.showOkAlertDialouge("Sorry", "Can't authenticate due to no internet connection");
    }


    /*
    Private helper method
     */
    private void showOkAlertDialouge(String title, String msg)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Ok", null);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.create().show();
    }

    private void proceedToMainMenuWithRegistration(Boolean registered)
    {
        //disabling the registered flag
        SharedPreferences.Editor editor = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(DenimContstants.SHARED_PREFS_REGISTERED, registered);
        editor.commit();

        Intent intent = new Intent(SignupLoginActivity.this, MainMenuActivity.class);
        startActivity(intent);

        if(registered)
            SignupLoginActivity.this.finish();
    }
}
