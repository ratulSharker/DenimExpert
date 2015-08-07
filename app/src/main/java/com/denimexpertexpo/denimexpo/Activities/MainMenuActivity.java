package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.Adapters.MainMenuListAdapter;
import com.denimexpertexpo.denimexpo.R;


public class MainMenuActivity extends Activity implements android.widget.AdapterView.OnItemClickListener {

    //FULL LIST ITEMS
    private final static int LIST_ITEM_EXIBITORS = 4;
    private final static int LIST_ITEM_VISITORS = 5;
    private final static int LIST_ITEM_DIRECTION = 6;
    private final static int LIST_ITEM_SITEMAP = 7;
    private final static int LIST_ITEM_SCHEDULE = 8;
    private final static int LIST_ITEM_BARCODE = 9;
    private final static int LIST_ITEM_FEEDBACK = 10;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.mListView = (ListView) findViewById(R.id.listView);

        //loading the menu list
        Resources resources = this.getResources();
        String[] titleItems = resources.getStringArray(R.array.menu_titles);
        String[] subTitleItems = resources.getStringArray(R.array.menu_sub_title);


        MainMenuListAdapter customAdapter = new MainMenuListAdapter(titleItems, subTitleItems, getApplicationContext());
        this.mListView.setAdapter(customAdapter);

        this.mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case LIST_ITEM_EXIBITORS: {
                Toast.makeText(this, "Exibitor gui not ready yet", Toast.LENGTH_LONG).show();
            }
            break;
            case LIST_ITEM_VISITORS:
            {
                Toast.makeText(this, "Visitor pressed", Toast.LENGTH_LONG).show();
                this.startActivity(VisitorActivity.class);
            }
            break;
            case LIST_ITEM_DIRECTION: {
                Toast.makeText(this, "Direction pressed", Toast.LENGTH_LONG).show();
                this.startActivity(DirectionActivity.class);
            }
            break;
            case LIST_ITEM_SITEMAP: {
                Toast.makeText(this, "Sitemap gui not ready yet", Toast.LENGTH_LONG).show();
            }
            break;
            case LIST_ITEM_SCHEDULE: {
                Toast.makeText(this, "Schedule pressed", Toast.LENGTH_LONG).show();
                this.startActivity(ScheduleActivity.class);
            }
            break;
            case LIST_ITEM_BARCODE: {
                Toast.makeText(this, "Barcode pressed", Toast.LENGTH_LONG).show();
                this.startActivity(BarcodeActivity.class);
            }
            break;
            case LIST_ITEM_FEEDBACK: {
                Toast.makeText(this, "Feedback gui not ready yet", Toast.LENGTH_LONG).show();
            }
            break;
            default:

        }
    }


    public void startActivity(Class cls) {
        Intent intent = new Intent(MainMenuActivity.this, cls);
        super.startActivity(intent);
    }
}
