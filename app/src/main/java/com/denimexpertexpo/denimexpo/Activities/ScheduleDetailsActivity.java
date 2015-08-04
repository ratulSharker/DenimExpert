package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.DBHelper.ScheduleContract;
import com.denimexpertexpo.denimexpo.R;

public class ScheduleDetailsActivity extends Activity {

    public static final String ID_KEY = "id_key";

    private TextView mEventName;
    private TextView mStartTime;
    private TextView mEndTime;
    private TextView mDuration;
    private TextView mDetails;
    private TextView mLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);

        //gather the view reference
        mEventName = (TextView) findViewById(R.id.schedule_detail_event_name);
        mStartTime = (TextView) findViewById(R.id.schedule_detail_start_time);
        mEndTime = (TextView) findViewById(R.id.schedule_detail_end_time);
        mDuration = (TextView) findViewById(R.id.schedule_detail_duration);
        mDetails = (TextView) findViewById(R.id.schedule_detail_details);
        mLastUpdate = (TextView) findViewById(R.id.schedule_detail_last_update);

        long id = getIntent().getExtras().getLong(ID_KEY);

        Uri detailsUri = ContentUris.withAppendedId(ScheduleContract.CONTENT_URI, id);
        Cursor cursor = getContentResolver().query(detailsUri, null, null, null, null);

        if(cursor.moveToFirst())
        {
            //set the values :)
            String eventName = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.EVENT_NAME));
            String startTime = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.START_TIME));
            String endTime  = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.END_TIME));
            String duration = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.DURATION));
            String details = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.DETAILS));
            String lastUpdate = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.ADD_TIME));


            mEventName.setText(mEventName.getText() + eventName);
            mStartTime.setText(mStartTime.getText() +  startTime);
            mEndTime.setText(mEndTime.getText() + endTime);
            mDuration.setText(mDuration.getText() + duration);
            mDetails.setText(details);
            mLastUpdate.setText(mLastUpdate.getText() + lastUpdate);
        }

        Toast.makeText(this, id + " clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule_details, menu);
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
