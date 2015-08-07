package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.DBHelper.VisitorContract;
import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncVisitorHelper;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncVisitorHelperListener;

public class VisitorActivity extends Activity implements AsyncHttpRequestHandler,
        LoaderManager.LoaderCallbacks<Cursor>, AsyncVisitorHelperListener, AdapterView.OnItemClickListener{

    private static final int LOADER_ID = 404; //arbitrary loader id

    private static final long HOW_MANY_NUMBER_OF_VISITOR_WILL_LOAD_AT_TIME = 250;

    private final String[] FROM = {
            VisitorContract.Column.FULL_NAME
    };

    private final int[] TO = {
            R.id.visito_list_row_name
    };


    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private AsyncVisitorHelper mAsyncVisitorHelper;
    private View mListFooterView;
    private Boolean mIsViewForeground;


    /**
     * Life cycle callback
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);


        this.mListView = (ListView) this.findViewById(R.id.visitor_list);

        mListFooterView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_view, null, false);
        this.mListView.addFooterView(mListFooterView);

        mAsyncVisitorHelper = new AsyncVisitorHelper(this, this);

        //getting first 100 fields
        AsyncHttpClient httpClient = new AsyncHttpClient(this);
        httpClient.execute(AsyncHttpClient.BuildVisitorApiUrl(0, HOW_MANY_NUMBER_OF_VISITOR_WILL_LOAD_AT_TIME));

        mAdapter = new SimpleCursorAdapter(this, R.layout.visitor_list_row, null, FROM, TO, 0);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mIsViewForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mIsViewForeground = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visitor, menu);
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
    @Override
    public void onResponseRecieved(String response) {
        Toast.makeText(this,response, Toast.LENGTH_SHORT).show();
        mAsyncVisitorHelper.processRecievedVisitorResponse(response);
    }

    @Override
    public void onHttpErrorOccured() {
        Toast.makeText(this,"No internet connection, cant get the latest visitors", Toast.LENGTH_LONG).show();
    }






    /*
     *  Background loader realted code
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(this, VisitorContract.CONTENT_URI, null, null, null, VisitorContract.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /**
     * AsyncVisitorLoaderListerner Callback
     */
    @Override
    public void visitorTableUpdatedWith(long numberOfVisitor, long lastVisitorID) {

        if(numberOfVisitor == HOW_MANY_NUMBER_OF_VISITOR_WILL_LOAD_AT_TIME && this.mIsViewForeground)
        {
            AsyncHttpClient httpClient = new AsyncHttpClient(this);
            httpClient.execute(AsyncHttpClient.BuildVisitorApiUrl(lastVisitorID, HOW_MANY_NUMBER_OF_VISITOR_WILL_LOAD_AT_TIME));
        }
        else
        {
            this.mListView.removeFooterView(mListFooterView);
        }
    }


    /*
    list view click listener
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {


        Intent detailsIntent = new Intent(VisitorActivity.this, VisitorDetailsActivity.class);
        detailsIntent.putExtra(VisitorDetailsActivity.ID_KEY, id);
        startActivity(detailsIntent);
    }
}
