package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.denimexpertexpo.denimexpo.R;

public class BarcodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        getActionBar().setTitle(R.string.main_menu_back_title);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_barcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:


                Intent intent = new Intent(BarcodeActivity.this, BarcodeResultActivity.class);
                startActivity(intent);

                return true;


            case android.R.id.home:
                //back pressed
                NavUtils.navigateUpFromSameTask(this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
