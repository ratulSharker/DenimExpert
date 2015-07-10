package com.denimexpertexpo.denimexpo.StaticStyling;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Ra2l on 24/06/2015.
 * Intention of this class is to
 * separate the view related code from the activity body
 */
public class CustomStyling {

    /*
        This function will apply the given "fontName" to the textview with the id of "textViewId"
     */
    public static void setCustomFontToTextView(Activity activity, String fontName, int textViewId) {
        TextView appliedTextView = (TextView) activity.findViewById(textViewId);
        Typeface font = Typeface.createFromAsset(activity.getAssets(), fontName);
        appliedTextView.setTypeface(font);

    }
}
