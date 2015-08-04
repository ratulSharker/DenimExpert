package com.denimexpertexpo.denimexpo.DBHelper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ratul on 7/31/2015.
 */
public class DbScheduleHelper extends SQLiteOpenHelper {


    public DbScheduleHelper(Context context){
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create the SCHEDULE table
        String createSchedule = String.format("create table %s (%s int primary key," +
                "%s text," +    //event name
                "%s text," +    //time added
                "%s text," +    //start time
                "%s text," +    //end time
                "%s text," +    //details
                "%s text)",      //duration
                ScheduleContract.TABLE_NAME,ScheduleContract.Column.ID,
                ScheduleContract.Column.EVENT_NAME,
                ScheduleContract.Column.ADD_TIME,
                ScheduleContract.Column.START_TIME,
                ScheduleContract.Column.END_TIME,
                ScheduleContract.Column.DETAILS,
                ScheduleContract.Column.DURATION
                );

        Log.d("CREATE SCHEDULR SQL", createSchedule);

        sqLiteDatabase.execSQL(createSchedule);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("drop table if exists " + ScheduleContract.TABLE_NAME);
    }
}
