package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.Adapters.MainMenuListAdapter;
import com.denimexpertexpo.denimexpo.Constants.DenimContstants;
import com.denimexpertexpo.denimexpo.R;


public class MainMenuActivity extends Activity implements android.widget.AdapterView.OnItemClickListener {

    //FULL LIST ITEMS
    private final static int LIST_ITEM_REGISTERED_EXIBITORS     = 4;
    private final static int LIST_ITEM_REGISTERED_VISITORS      = 5;
    private final static int LIST_ITEM_REGISTERED_DIRECTION     = 6;
    private final static int LIST_ITEM_REGISTERED_SITEMAP       = 7;
    private final static int LIST_ITEM_REGISTERED_SCHEDULE      = 8;
    private final static int LIST_ITEM_REGISTERED_BARCODE       = 9;
    private final static int LIST_ITEM_REGISTERED_FEEDBACK      = 10;
    private final static int LIST_ITEM_REGISTERED_LOGOUT        = 11;

    private final static int LIST_ITEM_UNREGISTERED_EXIBITORS   = 4;
    private final static int LIST_ITEM_UNREGISTERED_VISITORS    = 5;
    private final static int LIST_ITEM_UNREGISTERED_DIRECTION   = 6;
    private final static int LIST_ITEM_UNREGISTERED_SITEMAP     = 7;
    private final static int LIST_ITEM_UNREGISTERED_SCHEDULE    = 8;
    private final static int LIST_ITEM_UNREGISTERED_FEEDBACK    = 9;

    private static final int[] REGISTERED_ICON_LIST = {
            -1,-1,-1,-1,
            R.drawable.main_menu_list_item_exhibitor,
            R.drawable.main_menu_list_item_visitor,
            R.drawable.main_menu_list_item_direction,
            R.drawable.main_menu_list_item_sitemap,
            R.drawable.main_menu_list_item_schedule,
            R.drawable.main_menu_list_item_barcode,
            R.drawable.main_menu_list_item_feedback,
            R.drawable.main_menu_list_item_logout
    };

    private static final int[] UNREGISTERED_ICON_LIST = {
            -1,-1,-1,-1,
            R.drawable.main_menu_list_item_exhibitor,
            R.drawable.main_menu_list_item_visitor,
            R.drawable.main_menu_list_item_direction,
            R.drawable.main_menu_list_item_sitemap,
            R.drawable.main_menu_list_item_schedule,
            R.drawable.main_menu_list_item_feedback
    };



    private Boolean     mIsRegistered;
    private ListView    mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.mListView = (ListView) findViewById(R.id.listView);

        //get the states
        SharedPreferences prefs = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE);
        mIsRegistered = prefs.getBoolean(DenimContstants.SHARED_PREFS_REGISTERED, false);

        Resources resources = this.getResources();
        MainMenuListAdapter customAdapter;

        if(mIsRegistered)
        {
            String[] subtitleArr = resources.getStringArray(R.array.menu_sub_title_registered);


            //get the logged in username from shared preference
            SharedPreferences sharedPreferences = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE);
            String username = sharedPreferences.getString(DenimContstants.SHARED_PREFS_USERNAME, null);

            //appending username @ the last of the subtitle string
            subtitleArr[subtitleArr.length - 1] =subtitleArr[subtitleArr.length - 1] +" "+  username;

            //loading registered menu item
            customAdapter = new MainMenuListAdapter(resources.getStringArray(R.array.menu_titles_registered),
                                                    subtitleArr,
                                                    REGISTERED_ICON_LIST,
                                                    getApplicationContext());

        }
        else
        {
            //loading registered menu item
            customAdapter = new MainMenuListAdapter(resources.getStringArray(R.array.menu_titles_unregistered),
                                                    resources.getStringArray(R.array.menu_sub_title_unregistered),
                                                    UNREGISTERED_ICON_LIST,
                                                    getApplicationContext());
        }

        this.mListView.setAdapter(customAdapter);
        this.mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(mIsRegistered)
        {
            //switch over the registered list
            switch (position) {
                case LIST_ITEM_REGISTERED_EXIBITORS: {
                    this.startActivity(ExhibitorActivity.class);
                }
                break;
                case LIST_ITEM_REGISTERED_VISITORS:
                {
                    //Toast.makeText(this, "Visitor pressed", Toast.LENGTH_LONG).show();
                    //this.startActivity(VisitorActivity.class);
                    this.startActivity(VisitorSummaryActivity.class);
                }
                break;
                case LIST_ITEM_REGISTERED_DIRECTION: {
                    //Toast.makeText(this, "Direction pressed", Toast.LENGTH_LONG).show();
                    this.startActivity(DirectionActivity.class);
                }
                break;
                case LIST_ITEM_REGISTERED_SITEMAP: {
                    //Toast.makeText(this, "Sitemap gui not ready yet", Toast.LENGTH_LONG).show();
                    this.startActivity(SitemapActivity.class);
                }
                break;
                case LIST_ITEM_REGISTERED_SCHEDULE: {
                    //Toast.makeText(this, "Schedule pressed", Toast.LENGTH_LONG).show();
                    this.startActivity(ScheduleActivity.class);
                }
                break;
                case LIST_ITEM_REGISTERED_BARCODE: {
                    //Toast.makeText(this, "Barcode pressed", Toast.LENGTH_LONG).show();
                    this.startActivity(BarcodeActivity.class);
                }
                break;
                case LIST_ITEM_REGISTERED_FEEDBACK: {
                    this.startActivity(FeedbackActivity.class);
                }
                break;

                case LIST_ITEM_REGISTERED_LOGOUT: {

                    SharedPreferences sharedPreferences = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE);
                    String username = sharedPreferences.getString(DenimContstants.SHARED_PREFS_USERNAME, null);

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainMenuActivity.this);
                    alertBuilder.setTitle("Warning (" + username + ")" );
                    alertBuilder.setMessage("You will be logged out, are you sure ?");
                    alertBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SharedPreferences.Editor editor = getSharedPreferences(DenimContstants.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.remove(DenimContstants.SHARED_PREFS_USERNAME);
                            editor.remove(DenimContstants.SHARED_PREFS_PASSWORD);
                            editor.remove(DenimContstants.SHARED_PREFS_REGISTERED);
                            editor.remove(DenimContstants.SHARED_PREFS_USER_TYPE);
                            editor.remove(DenimContstants.SHARED_PREFS_USER_ID);
                            editor.commit();

                            Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertBuilder.setNegativeButton("No", null);
                    alertBuilder.create().show();
                }
                break;
                default:
            }
        }
        else
        {
            //switch over the unregistered list
            //switch over the registered list
            switch (position) {
                case LIST_ITEM_UNREGISTERED_EXIBITORS: {
                    this.startActivity(ExhibitorActivity.class);
                }
                break;
                case LIST_ITEM_UNREGISTERED_VISITORS:
                {
                    this.startActivity(VisitorSummaryActivity.class);
                }
                break;
                case LIST_ITEM_UNREGISTERED_DIRECTION: {

                    this.startActivity(DirectionActivity.class);
                }
                break;
                case LIST_ITEM_UNREGISTERED_SITEMAP: {
                    this.startActivity(SitemapActivity.class);
                }
                break;
                case LIST_ITEM_UNREGISTERED_SCHEDULE: {
                    this.startActivity(ScheduleActivity.class);
                }
                break;
                case LIST_ITEM_UNREGISTERED_FEEDBACK: {
                    this.startActivity(FeedbackActivity.class);
                }
                break;
                default:
            }
        }
    }


    public void startActivity(Class cls) {
        Intent intent = new Intent(MainMenuActivity.this, cls);
        super.startActivity(intent);
    }
}
