package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.Constants.DenimContstants;
import com.denimexpertexpo.denimexpo.DBHelper.ExhibitorContract;
import com.denimexpertexpo.denimexpo.DBHelper.VisitorContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.FeedbackReply;
import com.denimexpertexpo.denimexpo.Fragments.FeedbackFormFragment;
import com.denimexpertexpo.denimexpo.Fragments.SuccessThumbFragment;
import com.denimexpertexpo.denimexpo.Interfaces.FeedbackEventHandler;
import com.denimexpertexpo.denimexpo.R;

import java.io.UnsupportedEncodingException;

public class FeedbackActivity extends Activity implements FeedbackEventHandler{

    private EditText mNameField, mEmailField, mCommentField;
    private RatingBar mRatingBar;
    private Button  mSendBtn;

    private static final String FEED_FORM_FRAG_TAG = "FRAG_FORM";
    private static final String THUMBS_FRAG_TAG   = "THUMBS_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        FragmentManager fragMgr = getFragmentManager();
        FragmentTransaction fragTrans = fragMgr.beginTransaction();
        FeedbackFormFragment feedbackFormFragment = new FeedbackFormFragment();

        feedbackFormFragment.setFeedbackEventHandler(this);

        //set the delegate so that is feedback posted successfully than show the thumb
        fragTrans.add(R.id.feedback_holder, feedbackFormFragment, FeedbackActivity.FEED_FORM_FRAG_TAG);
        fragTrans.commit();
    }


    public void feedbackPostedSuccessfully(String msg)
    {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        //remove the previous feedback form fragment
        Fragment fragment = getFragmentManager().findFragmentByTag(FEED_FORM_FRAG_TAG);
        if(fragment != null)
        {
            fragmentTransaction.remove(fragment);
        }


        SuccessThumbFragment feedPostedFrag = new SuccessThumbFragment();

        Bundle bundle = new Bundle();
        bundle.putString(SuccessThumbFragment.MSG_KEY, msg);
        feedPostedFrag.setArguments(bundle);

        fragmentTransaction.add(R.id.feedback_holder, feedPostedFrag, FeedbackActivity.THUMBS_FRAG_TAG);
        fragmentTransaction.commit();
    }

}
