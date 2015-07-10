package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
}
