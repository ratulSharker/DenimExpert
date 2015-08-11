package com.denimexpertexpo.denimexpo.SpecialAsyncTask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.DBHelper.VisitorContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Visitors;

/**
 * Created by ratul on 8/7/2015.
 */
public class AsyncVisitorHelper {

    private Context context;
    AsyncVisitorHelperListener asyncVisitorHelperListener;

    public AsyncVisitorHelper(Context ctx, AsyncVisitorHelperListener listener) {
        this.context = ctx;
        this.asyncVisitorHelperListener = listener;
    }


    public void processRecievedVisitorResponse(String response) {
        AsyncVisitorInserter asyncVisitorInserter = new AsyncVisitorInserter();
        asyncVisitorInserter.execute(response);
    }


    private class AsyncVisitorInserter extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... responses) {

            Visitors visitors = JsonParserHelper.parseDownTheVisitors(responses[0]);

            if(Long.parseLong(visitors.mCount) == 0)
            {
                return null;
            }

            ContentValues contentValues = new ContentValues();
            long largestID = 0;

            for (Visitors.Visitor visitor : visitors.mVisitorList) {


                long currentID = Long.parseLong(visitor.mId);

                if (currentID > largestID) {
                    largestID = currentID;
                }

                contentValues.put(VisitorContract.Column.ID, visitor.mId);
                contentValues.put(VisitorContract.Column.GEN_ID, visitor.mGenId);
                contentValues.put(VisitorContract.Column.FIRST_NAME, visitor.mFirstName);
                contentValues.put(VisitorContract.Column.LAST_NAME, visitor.mLastName);
                contentValues.put(VisitorContract.Column.FULL_NAME, visitor.mFullName);
                contentValues.put(VisitorContract.Column.EMAIL, visitor.mEmail);
                contentValues.put(VisitorContract.Column.PHONE, visitor.mPhone);
                contentValues.put(VisitorContract.Column.COMPANY_NAME, visitor.mCompanyName);
                contentValues.put(VisitorContract.Column.WEBSITE, visitor.mWebsite);
                contentValues.put(VisitorContract.Column.ADDRESS, visitor.mAdress);
                contentValues.put(VisitorContract.Column.INDUSTRY_TYPE, visitor.mIndustryType);
                contentValues.put(VisitorContract.Column.JOB_TITLE, visitor.mJobtitle);
                contentValues.put(VisitorContract.Column.DEPARTMENT, visitor.mDepartment);
                contentValues.put(VisitorContract.Column.DATE, visitor.mDate);

                context.getContentResolver().insert(VisitorContract.CONTENT_URI, contentValues);
            }

            return largestID + " " + visitors.mCount;
        }

        @Override
        protected void onPostExecute(String s) {
            //do in background return largest processed id & retrieved number of visitors

            if(s == null)
            {
                if(asyncVisitorHelperListener != null)
                {
                    asyncVisitorHelperListener.visitorTableUpdatedWith(-1, -1);
                }

                return;
            }

            String split[] = s.split(" ");
            long largestID = Long.parseLong(split[0]);
            long numOfVisitors = Long.parseLong(split[1]);

            Log.e("updated", s);
            if(asyncVisitorHelperListener != null)
            {
                asyncVisitorHelperListener.visitorTableUpdatedWith(numOfVisitors, largestID);
            }
        }
    }


}
