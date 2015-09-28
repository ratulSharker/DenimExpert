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

import com.denimexpertexpo.denimexpo.DBHelper.ExhibitorContract;
import com.denimexpertexpo.denimexpo.DBHelper.VisitorContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Exhibitors;
import com.denimexpertexpo.denimexpo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExhibitorDetailsActivity extends Activity {


    public static final String FROM_DB     = "data_from_db";
    public static final String FROM_OBJECT    = "data_from_web";

    public static final String ID_KEY      = "id_key";
    public static final String OBJECT_KEY  = "object_key";


    private TextView    mName;
    private TextView    mEmail;
    private TextView    mPhone;
    private TextView    mCompanyName;
    private TextView    mWebsite;
    private TextView    mCompanyAddress;
    private TextView    mMobile;
    private TextView    mIndustryType;
    private TextView    mAnnualTurnover;
    private TextView    mNumOfEmplyee;
    private TextView    mBuyerName;
    private TextView    mProductDetail;
    private TextView    mBuyerCountry;
    private TextView    mBusiness;
    private TextView    mProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_details);



        //get the reference
        this.mName = (TextView) findViewById(R.id.echibitor_details_name);
        this.mEmail = (TextView) findViewById(R.id.echibitor_details_email);
        this.mPhone = (TextView) findViewById(R.id.echibitor_details_phone);
        this.mCompanyName = (TextView) findViewById(R.id.echibitor_details_companyname);
        this.mWebsite = (TextView) findViewById(R.id.echibitor_details_website);
        this.mCompanyAddress = (TextView) findViewById(R.id.echibitor_details_company_address);
        this.mMobile = (TextView) findViewById(R.id.echibitor_details_mobile);
        this.mIndustryType = (TextView) findViewById(R.id.echibitor_details_industry_type);
        this.mAnnualTurnover = (TextView) findViewById(R.id.echibitor_details_annual_turnover);
        this.mNumOfEmplyee = (TextView) findViewById(R.id.echibitor_details_num_of_emplyees);
        this.mBuyerName = (TextView) findViewById(R.id.echibitor_details_buyers_nmae);
        this.mProductDetail = (TextView) findViewById(R.id.echibitor_details_product_details);
        this.mBuyerCountry = (TextView) findViewById(R.id.echibitor_details_product_buyer_country);
        this.mBusiness = (TextView) findViewById(R.id.echibitor_details_product_business);
        this.mProduct = (TextView) findViewById(R.id.echibitor_details_product);





        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(FROM_DB, false))
        {
            long id = intent.getLongExtra(ID_KEY, -1);
            if(id != -1)
            {
                //now run query to database
                Uri detailsUri = ContentUris.withAppendedId(ExhibitorContract.CONTENT_URI, id);
                Cursor cursor = getContentResolver().query(detailsUri, null, null, null, null);

                Log.e("from details", id + "");

                if(cursor.moveToFirst())
                {
                    String firstName = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.FIRST_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.LAST_NAME));
                    String email = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.EMAIL));
                    String phone = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.PHONE));
                    String comapnyName = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.COMPANY_NAME));
                    String website = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.WEBSITE));
                    String companyAddress = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.COMPANY_ADDRESS));
                    String mobile = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.MOBILE));
                    String industryType = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.INDUSTRY_TYPE));
                    String annualTurnover = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.ANNUAL_TURN_OVER));
                    String numOfEmpl = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.NUM_OF_EMPLOYEE));
                    String buyersName = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.BUYER_NAME));
                    String productDetails = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.PRODUCT_DETAILS));
                    String buyerCountry = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.BUYER_COUNTRY));
                    String business = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.BUSINESS));
                    String product = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.PRODUCT));



                    this.mName.setText(firstName + " " + lastName );
                    this.mEmail.setText(email);
                    this.mPhone.setText(phone);
                    this.mCompanyName.setText(comapnyName);
                    this.mWebsite.setText(website);
                    this.mCompanyAddress.setText(companyAddress.trim());
                    this.mMobile.setText(mobile);
                    this.mIndustryType.setText(industryType);
                    this.mAnnualTurnover.setText(annualTurnover);
                    this.mNumOfEmplyee.setText(numOfEmpl);
                    this.mBuyerName.setText(buyersName);
                    this.mProductDetail.setText(productDetails);
                    this.mBuyerCountry.setText(buyerCountry);
                    this.mBusiness.setText(business);
                    this.mProduct.setText(product);
                }
            }
        }
        else if(intent != null && intent.getBooleanExtra(FROM_OBJECT, false))
        {
            Exhibitors.Exhibitor exhibitor = (Exhibitors.Exhibitor)intent.getSerializableExtra(OBJECT_KEY);

            this.mName.setText(exhibitor.mFirstName + " " + exhibitor.mLastName );
            this.mEmail.setText(exhibitor.mEmail);
            this.mPhone.setText(exhibitor.mPhone);
            this.mCompanyName.setText(exhibitor.mCompanyName);
            this.mWebsite.setText(exhibitor.mWebsite);
            this.mCompanyAddress.setText(exhibitor.mCompanyAddress.trim());
            this.mMobile.setText(exhibitor.mMobile);
            this.mIndustryType.setText(exhibitor.mIndustryType);
            this.mAnnualTurnover.setText(exhibitor.mIndustryType);
            this.mNumOfEmplyee.setText(exhibitor.mNumOfEmployee);
            this.mBuyerName.setText(exhibitor.mBuyerName);
            this.mProductDetail.setText(exhibitor.mProductDetail);
            this.mBuyerCountry.setText(exhibitor.mBuyerCountry);
            this.mBusiness.setText(exhibitor.mBusiness);
            this.mProduct.setText(exhibitor.mProduct);

        }


    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exhibitor_details, menu);
        return true;
    }
    */

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
