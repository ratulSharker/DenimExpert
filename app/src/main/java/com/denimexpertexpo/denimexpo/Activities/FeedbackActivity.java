package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.Constants.DenimContstants;
import com.denimexpertexpo.denimexpo.DBHelper.ExhibitorContract;
import com.denimexpertexpo.denimexpo.DBHelper.VisitorContract;
import com.denimexpertexpo.denimexpo.DBHelper.VisitorProvider;
import com.denimexpertexpo.denimexpo.DenimDataClasses.FeedbackReply;
import com.denimexpertexpo.denimexpo.R;

import java.io.UnsupportedEncodingException;

public class FeedbackActivity extends Activity {

    private EditText mNameField, mEmailField, mCommentField;
    private RatingBar mRatingBar;
    private Button  mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //initialize all the buttons
        this.mNameField = (EditText) findViewById(R.id.feedback_name_field);
        this.mEmailField = (EditText) findViewById(R.id.feedback_email_field);
        this.mCommentField = (EditText) findViewById(R.id.feedback_comment_field);
        this.mRatingBar = (RatingBar) findViewById(R.id.feedback_rating_field);
        this.mSendBtn = (Button) findViewById(R.id.feedback_send_button);


        SharedPreferences sharedPreferences = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String userID = sharedPreferences.getString(DenimContstants.SHARED_PREFS_USER_ID, null);
        String userType = sharedPreferences.getString(DenimContstants.SHARED_PREFS_USER_TYPE, null);

        if(userID != null && userType != null)
        {
            String name= "", email = "";

            if( userType.toLowerCase().compareTo(DenimContstants.USER_TYPE_VISITOR) == 0)
            {
                Uri visitorDetails = ContentUris.withAppendedId(VisitorContract.CONTENT_URI, Long.parseLong(userID));
                Cursor cursor = getContentResolver().query(visitorDetails, null, null, null, null);

                if(cursor.moveToFirst())
                {
                    name = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.FULL_NAME));
                    email = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.EMAIL));
                }

                cursor.close();
            }
            else
            if(userType.toLowerCase().compareTo(DenimContstants.USER_TYPE_EXHIBITOR) == 0)
            {
                Uri exhibitorDetails = ContentUris.withAppendedId(ExhibitorContract.CONTENT_URI, Long.parseLong(userID));
                Cursor cursor = getContentResolver().query(exhibitorDetails, null, null, null, null);

                if(cursor.moveToFirst())
                {
                    name = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.FIRST_NAME)) + " " +
                            cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.LAST_NAME));

                    email = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.EMAIL));
                }

                cursor.close();
            }

            this.mNameField.setText(name);
            this.mEmailField.setText(email);
        }

        this.mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = checkEverythingIsOk();
                if(msg == null)
                {
                    //progress the api call
                   try{
                       String url =  AsyncHttpClient.BuildFeedbackApiUrl(mNameField.getText().toString(),
                               mEmailField.getText().toString(),
                               mCommentField.getText().toString(),
                               mRatingBar.getRating() + "");

                       final ProgressDialog progressDialog = new ProgressDialog(FeedbackActivity.this);
                       progressDialog.setTitle("Please wait...");
                       progressDialog.setMessage("processing your valuable feedback");
                       progressDialog.setCancelable(false);

                       progressDialog.show();

                       AsyncHttpClient asyncHttpClient = new AsyncHttpClient(new AsyncHttpRequestHandler() {
                           @Override
                           public void onResponseRecieved(String response) {
                               FeedbackReply reply = JsonParserHelper.parseFeedbackResponse(response);

                               AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
                               builder.setPositiveButton("ok", null);
                               builder.setNegativeButton("", null);

                               if(reply.isSuccess())
                               {
                                   builder.setTitle("Thanks");
                                   builder.setMessage("For your valuable opinion");
                               }
                               else
                               {

                                   builder.setTitle("Sorry");
                                   builder.setMessage("can't process the feedback right now, try later");
                               }

                               progressDialog.dismiss();
                               builder.create().show();
                           }

                           @Override
                           public void onHttpErrorOccured() {

                               progressDialog.dismiss();

                               AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
                               builder.setPositiveButton("ok", null);
                               builder.setNegativeButton("", null);
                               builder.setTitle("Sorry");
                               builder.setMessage("Internet connection required");
                               builder.create().show();

                           }
                       });
                       asyncHttpClient.execute(url);
                   }
                   catch (UnsupportedEncodingException ex)
                   {

                   }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
                    builder.setTitle("Sorry");
                    builder.setMessage(msg);
                    builder.setNegativeButton("", null);
                    builder.setPositiveButton("ok", null);
                    builder.create().show();
                }
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_feedback, menu);
//        return true;
//    }

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


    //it will return the error msg
    //return null when everything is ok
    private String checkEverythingIsOk()
    {
        if(mNameField.getText().toString().length() == 0)
        {
            return "Name field is empty";
        }
        else if(mEmailField.getText().toString().length() == 0)
        {
            return  "Email field is empty";
        }
        else if(mCommentField.getText().toString().length() == 0)
        {
            return "Please leave a comment";
        }
        else if(mRatingBar.getRating() == 0)
        {
            return "Please rate us";
        }

        else return null;
    }
}
