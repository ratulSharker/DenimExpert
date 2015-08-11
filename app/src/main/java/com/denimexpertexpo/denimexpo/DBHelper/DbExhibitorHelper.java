package com.denimexpertexpo.denimexpo.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ratul on 8/8/2015.
 */
public class DbExhibitorHelper extends SQLiteOpenHelper {

    public DbExhibitorHelper(Context context){
        super(context, ExhibitorContract.DB_NAME, null, ExhibitorContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createSchedule = String.format("create table IF NOT EXISTS %s (%s int primary key," +
                        "%s text," +    //gen id
                        "%s text," +    //first name
                        "%s text," +    //last name
                        "%s text," +    //email
                        "%s text," +    //phone
                        "%s text," +    //company name
                        "%s text," +    //website
                        "%s text," +    //company_address
                        "%s text," +    //mobile
                        "%s text," +    //industry_type
                        "%s text," +    //annual turnover
                        "%s text," +    //num_of_employee
                        "%s text," +    //buyer name
                        "%s text," +    //product details
                        "%s text," +    //buyer country
                        "%s text," +    //business
                        "%s text)" ,    //product
                ExhibitorContract.TABLE_NAME,ExhibitorContract.Column.ID,
                ExhibitorContract.Column.GEN_ID,
                ExhibitorContract.Column.FIRST_NAME,
                ExhibitorContract.Column.LAST_NAME,
                ExhibitorContract.Column.EMAIL,
                ExhibitorContract.Column.PHONE,
                ExhibitorContract.Column.COMPANY_NAME,
                ExhibitorContract.Column.WEBSITE,
                ExhibitorContract.Column.COMPANY_ADDRESS,
                ExhibitorContract.Column.MOBILE,
                ExhibitorContract.Column.INDUSTRY_TYPE,
                ExhibitorContract.Column.ANNUAL_TURN_OVER,
                ExhibitorContract.Column.NUM_OF_EMPLOYEE,
                ExhibitorContract.Column.BUYER_NAME,
                ExhibitorContract.Column.PRODUCT_DETAILS,
                ExhibitorContract.Column.BUYER_COUNTRY,
                ExhibitorContract.Column.BUSINESS,
                ExhibitorContract.Column.PRODUCT
        );
        sqLiteDatabase.execSQL(createSchedule);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + ExhibitorContract.TABLE_NAME);
    }
}
