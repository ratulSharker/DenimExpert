package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.Constants.DenimContstants;
import com.denimexpertexpo.denimexpo.DenimDataClasses.VisitorsSummary;
import com.denimexpertexpo.denimexpo.R;

public class VisitorSummaryActivity extends Activity implements AsyncHttpRequestHandler{


    private TextView mNumOfApplicant,
    mNumOfVisitor,
    mNumOfCompany,
    mNumOfCountry;

    private ProgressDialog mProgressDialouge;
    private Boolean isWebServiceCalled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_summary);

        this.mNumOfApplicant = (TextView) findViewById(R.id.visitor_summary_number_of_applicant);
        this.mNumOfVisitor = (TextView) findViewById(R.id.visitor_summary_number_of_visitor);
        this.mNumOfCompany = (TextView) findViewById(R.id.visitor_summary_number_of_company);
        this.mNumOfCountry = (TextView) findViewById(R.id.visitor_summary_number_of_countries);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isWebServiceCalled = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && isWebServiceCalled == false) {
            //start an webrequest thing
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(this);
            asyncHttpClient.execute(AsyncHttpClient.VISITOR_SUMMARY_API_URL);

            mProgressDialouge = new ProgressDialog(this);
            mProgressDialouge.setTitle("Loading...");
            mProgressDialouge.setMessage("Updated data");
            mProgressDialouge.setCancelable(true);
            mProgressDialouge.show();

            isWebServiceCalled = true;
        }
    }
//            @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_visitor_summary, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }



    /*
    AsyncHttpRequestHandler callbacks
     */

    @Override
    public void onResponseRecieved(String response) {
        Log.e("response recieved", response);

        this.mProgressDialouge.dismiss();
        VisitorsSummary visitorsSummary = JsonParserHelper.parseVisitrorSummaryResponse(response);

        if(visitorsSummary != null)
        {
            int applicant = (int) (Float.parseFloat(visitorsSummary.mTotalVisitor) * 2.5);
            this.mNumOfApplicant.setText(applicant+"");
            this.mNumOfVisitor.setText(visitorsSummary.mTotalVisitor);
            this.mNumOfCompany.setText(visitorsSummary.mTotalCompany);
            this.mNumOfCountry.setText(visitorsSummary.mTotalCountry);


            //save the data into the Shared Preferences
            SharedPreferences.Editor editor = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_APPLICANT, applicant+"");
            editor.putString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_VISITOR,visitorsSummary.mTotalVisitor );
            editor.putString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_COMPANY, visitorsSummary.mTotalCompany);
            editor.putString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_COUNTRIES, visitorsSummary.mTotalCountry);
            editor.commit();

        }
        else
        {
            this.loadLocalSummary();
        }

        resizeTextAccordinToAvailableRect(mNumOfApplicant);
        resizeTextAccordinToAvailableRect(mNumOfVisitor);
        resizeTextAccordinToAvailableRect(mNumOfCompany);
        resizeTextAccordinToAvailableRect(mNumOfCountry);

    }

    @Override
    public void onHttpErrorOccured() {
        Log.e("http error", "Http error");
        this.mProgressDialouge.dismiss();

        this.loadLocalSummary();
        this.showOkAlertDialouge("Sorry", "Cant show updated data, due to no internet connection");


        resizeTextAccordinToAvailableRect(mNumOfApplicant);
        resizeTextAccordinToAvailableRect(mNumOfVisitor);
        resizeTextAccordinToAvailableRect(mNumOfCompany);
        resizeTextAccordinToAvailableRect(mNumOfCountry);
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

    private void loadLocalSummary()
    {
        //get the previously saved data
        SharedPreferences sharedPreference = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String applicant = sharedPreference.getString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_APPLICANT, null);
        String visitor = sharedPreference.getString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_VISITOR, null);
        String company = sharedPreference.getString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_COMPANY, null);
        String country = sharedPreference.getString(DenimContstants.SHARED_PREFS_VISITOR_SUMMARY_COUNTRIES, null);

        if(applicant != null && visitor != null && company != null && country != null)
        {
            this.mNumOfApplicant.setText(applicant);
            this.mNumOfVisitor.setText(visitor);
            this.mNumOfCompany.setText(company);
            this.mNumOfCountry.setText(country);
        }
    }


    private void resizeTextAccordinToAvailableRect(TextView targetTextView)
    {
        Paint textPaint = new TextPaint(targetTextView.getPaint());

        int height = 0;
        int width = 0;

        Log.e(targetTextView.getLayout().getHeight() + ":" + targetTextView.getLayout().getWidth(), textPaint.getTextSize() + "");

        for(int holderHeight = targetTextView.getMeasuredHeight(), holderWidth = targetTextView.getMeasuredWidth();
                holderHeight  > height + Math.abs(textPaint.ascent()) + Math.abs(textPaint.descent()) && holderWidth >width + 10;
                textPaint.setTextSize(textPaint.getTextSize() + 2))
        {
            Rect bounds = new Rect();
            textPaint.getTextBounds("0",0,"0".length(),bounds);
            height = bounds.height();
            width = bounds.width();

            Log.e(holderHeight + ":" + holderWidth, height + ":" + width + ":" + textPaint.getTextSize());

        }

        targetTextView.setTextSize(textPaint.getTextSize());
        Log.e("Setting font size", textPaint.getTextSize() + "");
    }
}
