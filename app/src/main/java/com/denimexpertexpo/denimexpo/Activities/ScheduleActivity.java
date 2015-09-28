package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.DBHelper.ScheduleContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Schedule;
import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.StaticStyling.CustomStyling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleActivity extends Activity implements AsyncHttpRequestHandler
        , LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {


    private static final int LOADER_ID = 233; //arbitrary loader id

    private final String[] FROM = {
            ScheduleContract.Column.EVENT_NAME,
            ScheduleContract.Column.START_TIME
    };

    private final int[] TO = {
            R.id.schedule_list_item_event_name,
            R.id.schedule_list_item_time
    };

    private SimpleCursorAdapter mAdapter;
    private ListView mListView;
    private View mListFooterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        mListView = (ListView) findViewById(R.id.schedule_list);
        mListFooterView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_view, null, false);
        this.mListView.addFooterView(mListFooterView);

        //sending http request
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(this);
        asyncHttpClient.execute(AsyncHttpClient.SCHEDULE_API_URL);

        mAdapter = new SimpleCursorAdapter(this, R.layout.schdeule_list_row, null, FROM, TO, 0);

        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view.getId() == R.id.schedule_list_item_time)
                {
                    String startTime = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.START_TIME));
                    String endTime = cursor.getString(cursor.getColumnIndex(ScheduleContract.Column.END_TIME));

                    Log.e("start time & end time", startTime + "~~~" + endTime);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    try{
                        Date startDate = sdf.parse(startTime);
                        Date endDate = sdf.parse(endTime);

                        Calendar startCalendar = GregorianCalendar.getInstance();
                        startCalendar.setTime(startDate);

                        Calendar endCalendar = GregorianCalendar.getInstance();
                        endCalendar.setTime(endDate);


                        ((TextView) view).setText("(" + startCalendar.get(Calendar.HOUR) + " : " + startCalendar.get(Calendar.MINUTE) + " - " +
                                endCalendar.get(Calendar.HOUR) + " : " + endCalendar.get(Calendar.MINUTE) + ")");

                    }catch (ParseException ex)
                    {
                        Log.e("Schedule Activity", "Date format is wrong");
                    }finally {
                        return true;
                    }
                }
                else
                {
                    return false;
                }
            }
        });

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }



/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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


    /**
     * AsycHttpClient Callbacks
     */
    public void onResponseRecieved(String response) {


        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... strings) {

                String response = strings[0];

                ArrayList<Schedule> arrayList = JsonParserHelper.parseDownTheSchedules(response);
                ContentValues contentValues = new ContentValues();

                //clear the whole table first
                int deletedRow = getContentResolver().delete(ScheduleContract.CONTENT_URI, null, null);


                for (Schedule schedule : arrayList) {
                    contentValues.clear();

                    contentValues.put(ScheduleContract.Column.ID, schedule.mId);
                    contentValues.put(ScheduleContract.Column.EVENT_NAME, schedule.mEventName);
                    contentValues.put(ScheduleContract.Column.START_TIME, schedule.mTimeWhenStarted);
                    contentValues.put(ScheduleContract.Column.END_TIME, schedule.mTimeWhenEnd);
                    contentValues.put(ScheduleContract.Column.ADD_TIME, schedule.mTimeWhenAdded);
                    contentValues.put(ScheduleContract.Column.DETAILS, schedule.mDetails);
                    contentValues.put(ScheduleContract.Column.DURATION, schedule.mDuration);

                    getContentResolver().insert(ScheduleContract.CONTENT_URI, contentValues);
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mListView.removeFooterView(mListFooterView);
            }
        }.execute(response);
    }

    public void onHttpErrorOccured() {
        Toast.makeText(this, "No internet connection, cant get the latest schedules...", Toast.LENGTH_SHORT).show();
    }




    /*
    Background loader realted code
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(this, ScheduleContract.CONTENT_URI, null, null, null, ScheduleContract.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /*
    list view click listener
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        Intent detailsIntent = new Intent(ScheduleActivity.this, ScheduleDetailsActivity.class);
        detailsIntent.putExtra(ScheduleDetailsActivity.ID_KEY, id);
        startActivity(detailsIntent);
    }
}
