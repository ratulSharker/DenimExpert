package com.denimexpertexpo.denimexpo.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ratul on 8/6/2015.
 */
public class DbVisitorHelper extends SQLiteOpenHelper {

    public DbVisitorHelper(Context context){
        super(context, VisitorContract.DB_NAME, null, VisitorContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSchedule = String.format("create table IF NOT EXISTS %s (%s int primary key," +
                        "%s text," +    //gen id
                        "%s text," +    //first name
                        "%s text," +    //last name
                        "%s text," +    //full name
                        "%s text," +    //email
                        "%s text," +    //phone
                        "%s text," +    //company name
                        "%s text," +    //website
                        "%s text," +    //address
                        "%s text," +    //industry type
                        "%s text," +    //job title
                        "%s text," +    //department
                        "%s text)",     //date
                VisitorContract.TABLE_NAME,VisitorContract.Column.ID,
                VisitorContract.Column.GEN_ID,
                VisitorContract.Column.FIRST_NAME,
                VisitorContract.Column.LAST_NAME,
                VisitorContract.Column.FULL_NAME,
                VisitorContract.Column.EMAIL,
                VisitorContract.Column.PHONE,
                VisitorContract.Column.COMPANY_NAME,
                VisitorContract.Column.WEBSITE,
                VisitorContract.Column.ADDRESS,
                VisitorContract.Column.INDUSTRY_TYPE,
                VisitorContract.Column.JOB_TITLE,
                VisitorContract.Column.DEPARTMENT,
                VisitorContract.Column.DATE
        );
        sqLiteDatabase.execSQL(createSchedule);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + VisitorContract.TABLE_NAME);
    }
}
