package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.DBHelper.ScheduleContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Schedule;
import com.denimexpertexpo.denimexpo.R;

import java.util.ArrayList;

public class ScheduleActivity extends Activity implements AsyncHttpRequestHandler
        , LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {



    private static final int LOADER_ID = 233; //arbitrary loader id

    private final String[] FROM = {
            ScheduleContract.Column.EVENT_NAME,
            ScheduleContract.Column.START_TIME,
            ScheduleContract.Column.END_TIME
    };

    private final int[] TO = {
            R.id.schedule_list_item_event_name,
            R.id.schedule_list_item_start,
            R.id.schedule_list_item_end
    };

    private SimpleCursorAdapter mAdapter;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        mListView = (ListView)findViewById(R.id.schedule_list);

        //sending http request
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(this);
        asyncHttpClient.execute(AsyncHttpClient.SCHEDULE_API_URL);

        mAdapter = new SimpleCursorAdapter(this, R.layout.schdeule_list_row, null, FROM, TO, 0);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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


    /**
     * AsycHttpClient Callbacks
     */
    public void onResponseRecieved(String response)
    {
        ArrayList<Schedule> arrayList =  JsonParserHelper.parseDownTheSchedules(response);

        ContentValues contentValues = new ContentValues();

        int deletedRow = getContentResolver().delete(ScheduleContract.CONTENT_URI, null, null);

        for(Schedule schedule : arrayList) {
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

    }
    public void onHttpErrorOccured()
    {
        Toast.makeText(this, "Error while syncing...", Toast.LENGTH_SHORT).show();
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


    /*
    next phase devlopment, webServices reply must be processed in an asyncTask
     */
    public class RefreshSchedule extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
