package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
import com.denimexpertexpo.denimexpo.DBHelper.ExhibitorContract;
import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncExhibitorHelper;
import com.denimexpertexpo.denimexpo.StaticStyling.CustomStyling;

public class ExhibitorActivity extends Activity implements AsyncHttpRequestHandler,
        LoaderManager.LoaderCallbacks<Cursor>, AsyncExhibitorHelper.AsyncExhibitorHelperListener,
        AdapterView.OnItemClickListener{


    private static final int LOADER_ID = 4321; //arbitrary loader id
    private static final long HOW_MANY_NUMBER_OF_EXHIBITOR_WILL_LOAD_AT_TIME = 250;
    private final String[] FROM = {
            ExhibitorContract.Column.COMPANY_NAME
    };
    private final int[] TO = {
            R.id.visito_list_row_name
    };

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;


    private AsyncExhibitorHelper mAsyncExhibitorHelper;
    private View mListFooterView;
    private Boolean mIsViewForeground;
    private long updateOffset;



    /*
     * Lifecycle Callbacks
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor);

        //adding the back button
        CustomStyling.addHomeBackButton(this, "Exhibitors");


        this.mListView = (ListView) findViewById(R.id.exhibitor_list_view);

        mListFooterView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_view, null, false);
        this.mListView.addFooterView(mListFooterView);

        mAsyncExhibitorHelper = new AsyncExhibitorHelper(this, this);

        mAdapter = new SimpleCursorAdapter(this, R.layout.visitor_list_row, null, FROM, TO, 0);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        /*
        // in first phase, client want their full name, but then want only company name
        // this code is a view binder implementation, which will append first & last name to the row view
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view.getId() == R.id.visito_list_row_name)
                {
                    String firstName = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.FIRST_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndex(ExhibitorContract.Column.LAST_NAME));
                    ((TextView)view).setText(firstName + " " + lastName );
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        */

        updateOffset = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mIsViewForeground = true;

        //getting data
        AsyncHttpClient httpClient = new AsyncHttpClient(this);
        httpClient.execute(AsyncHttpClient.BuildExhibitorApiUrl(updateOffset, HOW_MANY_NUMBER_OF_EXHIBITOR_WILL_LOAD_AT_TIME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mIsViewForeground = false;

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exhibitor, menu);
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



    /*
     * AsyncHttpRequestHandler callbacks
     */
    @Override
    public void onResponseRecieved(String response) {
        //Log.e("Echibitor respon", response);
        mAsyncExhibitorHelper.processRecievedExhibitorResponse(response);
    }

    @Override
    public void onHttpErrorOccured() {
        Toast.makeText(this,"No internet connection, cant get the latest echibitor", Toast.LENGTH_LONG).show();
    }


    /**
     * Loader realted callbacks
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, ExhibitorContract.CONTENT_URI, null, null, null, ExhibitorContract.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)  {
        mAdapter.swapCursor(null);
    }

    @Override
    public void exhibitorTableUpdatedWith(long numberOfExhibitor, long lastExhibitorID) {

        if(lastExhibitorID != -1)
            updateOffset = lastExhibitorID;

        if(numberOfExhibitor == HOW_MANY_NUMBER_OF_EXHIBITOR_WILL_LOAD_AT_TIME && this.mIsViewForeground)
        {
            AsyncHttpClient httpClient = new AsyncHttpClient(this);
            httpClient.execute(AsyncHttpClient.BuildExhibitorApiUrl(updateOffset, HOW_MANY_NUMBER_OF_EXHIBITOR_WILL_LOAD_AT_TIME));
        }
        else
        {
            this.mListView.removeFooterView(mListFooterView);
        }
    }

    /**
     * onItemClickListener Adpaterview click callback
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

        Intent detailsIntent = new Intent(ExhibitorActivity.this, ExhibitorDetailsActivity.class);
        detailsIntent.putExtra(ExhibitorDetailsActivity.FROM_DB, true);
        detailsIntent.putExtra(ExhibitorDetailsActivity.ID_KEY, id);
        Log.e("from list", id + "");
        startActivity(detailsIntent);
    }

}
