package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.StaticStyling.CustomStyling;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends Activity {

    private static final int AUTO_HIDE_DELAY_MILLIS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        CustomStyling.setCustomFontToTextView(this, "big_title_gipsiero.otf", R.id.splash_screen_title);

        this.checkDPI();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, SignupLoginActivity.class);
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                }
            };

            //move to login activity within 3 seconds
            new Handler().postDelayed(runnable, AUTO_HIDE_DELAY_MILLIS);
        }
    }

    @Override
    public void onBackPressed() {
        //nothing happen on back pressed
    }


    /**
     * it let me know, in which device family i'm running
     */
    private void checkDPI()
    {
        int density= getResources().getDisplayMetrics().densityDpi;
        Context context = getApplicationContext();

        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(context, "LDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(context, "MDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(context, "HDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
