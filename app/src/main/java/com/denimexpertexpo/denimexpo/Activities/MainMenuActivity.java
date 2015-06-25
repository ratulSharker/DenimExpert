package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.denimexpertexpo.denimexpo.Adapters.MainMenuListAdapter;
import com.denimexpertexpo.denimexpo.R;


public class MainMenuActivity extends Activity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.mListView = (ListView) findViewById(R.id.listView);


        Resources resources = this.getResources();

        String[] titleItems = resources.getStringArray(R.array.menu_titles);
        String[] subTitleItems = resources.getStringArray(R.array.menu_sub_title);

        MainMenuListAdapter customAdapter = new MainMenuListAdapter(titleItems, subTitleItems, getApplicationContext());

        this.mListView.setAdapter(customAdapter);
    }
}
