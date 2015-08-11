package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.denimexpertexpo.denimexpo.DBHelper.VisitorContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Visitors;
import com.denimexpertexpo.denimexpo.R;

public class VisitorDetailsActivity extends Activity {

    public static final String FROM_DB = "from_db";
    public static final String FROM_OBJECT = "from_object";

    public static final String ID_KEY = "id_key";
    public static final String OBJECT_KEY = "object_key";


    private TextView mName;
    private TextView mGenId;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mCompany;
    private TextView mWebsite;
    private TextView mAddress;
    private TextView mIndustryType;
    private TextView mJobTitle;
    private TextView mDepartment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_details);

        this.mName = (TextView) findViewById(R.id.visitor_details_name_field);
        this.mGenId = (TextView) findViewById(R.id.visitor_details_id_field);
        this.mEmail = (TextView) findViewById(R.id.visitor_details_email_field);
        this.mPhone = (TextView) findViewById(R.id.visitor_details_phone_field);
        this.mCompany = (TextView) findViewById(R.id.visitor_details_company_field);
        this.mWebsite = (TextView) findViewById(R.id.visitor_details_website_field);
        this.mAddress = (TextView) findViewById(R.id.visitor_details_address_field);
        this.mIndustryType = (TextView) findViewById(R.id.visitor_details_industry_type_field);
        this.mJobTitle = (TextView) findViewById(R.id.visitor_details_job_title_field);
        this.mDepartment = (TextView) findViewById(R.id.visitor_details_department_field);


        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(FROM_DB, false)) {
            long id = intent.getLongExtra(ID_KEY, -1);
            if (id != -1) {

                Uri detailsUri = ContentUris.withAppendedId(VisitorContract.CONTENT_URI, id);
                Cursor cursor = getContentResolver().query(detailsUri, null, null, null, null);

                if (cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.FULL_NAME));
                    String gId = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.GEN_ID));
                    String email = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.EMAIL));
                    String phone = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.PHONE));
                    String company = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.COMPANY_NAME));
                    String website = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.WEBSITE));
                    String address = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.ADDRESS));
                    String industry = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.INDUSTRY_TYPE));
                    String jobTitle = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.JOB_TITLE));
                    String department = cursor.getString(cursor.getColumnIndex(VisitorContract.Column.DEPARTMENT));


                    this.mName.setText(name);
                    this.mGenId.setText(gId);
                    this.mEmail.setText(email);
                    this.mPhone.setText(phone);
                    this.mCompany.setText(company);
                    this.mWebsite.setText(website);
                    this.mAddress.setText(address);
                    this.mIndustryType.setText(industry);
                    this.mJobTitle.setText(jobTitle);
                    this.mDepartment.setText(department);
                }
            }
        }
        else if(intent != null && intent.getBooleanExtra(FROM_OBJECT, false))
        {
            Visitors.Visitor visitor = (Visitors.Visitor)intent.getSerializableExtra(OBJECT_KEY);

            this.mName.setText(visitor.mFullName);
            this.mGenId.setText(visitor.mGenId);
            this.mEmail.setText(visitor.mEmail);
            this.mPhone.setText(visitor.mPhone);
            this.mCompany.setText(visitor.mCompanyName);
            this.mWebsite.setText(visitor.mWebsite);
            this.mAddress.setText(visitor.mAdress);
            this.mIndustryType.setText(visitor.mIndustryType);
            this.mJobTitle.setText(visitor.mJobtitle);
            this.mDepartment.setText(visitor.mDepartment);
        }
        else
        {
            Log.e("VisitorDetails Activity", "No Data Found");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visitor_details, menu);
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
}
