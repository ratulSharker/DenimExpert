package com.denimexpertexpo.denimexpo.SpecialAsyncTask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.DBHelper.ExhibitorContract;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Exhibitors;

/**
 * Created by ratul on 8/8/2015.
 */
public class AsyncExhibitorHelper{

    public static interface AsyncExhibitorHelperListener
    {
        public void exhibitorTableUpdatedWith(long numberOfExhibitor, long lastExhibitorID);
    }


    private Context context;
    AsyncExhibitorHelperListener asyncExhibitorHelperListener;

    public AsyncExhibitorHelper(Context ctx, AsyncExhibitorHelperListener listener) {
        this.context = ctx;
        this.asyncExhibitorHelperListener = listener;
    }


    public void processRecievedExhibitorResponse(String response) {
        AsyncExhibitorInserter asyncExhibitorInserter = new AsyncExhibitorInserter();
        asyncExhibitorInserter.execute(response);
    }

    private class AsyncExhibitorInserter extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... responses) {

            Exhibitors exhibitors = JsonParserHelper.parseDownTheExhibitors(responses[0]);

            //no data recieved
            if(Long.parseLong(exhibitors.mCount) == 0)
            {
                return null;
            }

            ContentValues contentValues = new ContentValues();
            long largestID = 0;

            for (Exhibitors.Exhibitor exhibitor : exhibitors.mExhibitorList) {

                long currentID = Long.parseLong(exhibitor.mId);

                if (currentID > largestID) {
                    largestID = currentID;
                }

                contentValues.put(ExhibitorContract.Column.ID, exhibitor.mId);
                contentValues.put(ExhibitorContract.Column.GEN_ID, exhibitor.mGenId);
                contentValues.put(ExhibitorContract.Column.FIRST_NAME, exhibitor.mFirstName);
                contentValues.put(ExhibitorContract.Column.LAST_NAME, exhibitor.mLastName);
                contentValues.put(ExhibitorContract.Column.EMAIL, exhibitor.mEmail);
                contentValues.put(ExhibitorContract.Column.PHONE, exhibitor.mPhone);
                contentValues.put(ExhibitorContract.Column.COMPANY_NAME, exhibitor.mCompanyName);
                contentValues.put(ExhibitorContract.Column.WEBSITE, exhibitor.mWebsite);
                contentValues.put(ExhibitorContract.Column.COMPANY_ADDRESS, exhibitor.mCompanyAddress);
                contentValues.put(ExhibitorContract.Column.MOBILE, exhibitor.mMobile);
                contentValues.put(ExhibitorContract.Column.INDUSTRY_TYPE, exhibitor.mIndustryType);
                contentValues.put(ExhibitorContract.Column.ANNUAL_TURN_OVER, exhibitor.mAnnualTurnover);
                contentValues.put(ExhibitorContract.Column.NUM_OF_EMPLOYEE, exhibitor.mNumOfEmployee);
                contentValues.put(ExhibitorContract.Column.BUYER_NAME, exhibitor.mBuyerName);
                contentValues.put(ExhibitorContract.Column.PRODUCT_DETAILS, exhibitor.mProductDetail);
                contentValues.put(ExhibitorContract.Column.BUYER_COUNTRY, exhibitor.mBuyerCountry);
                contentValues.put(ExhibitorContract.Column.BUSINESS, exhibitor.mBusiness);
                contentValues.put(ExhibitorContract.Column.PRODUCT, exhibitor.mProduct);

                context.getContentResolver().insert(ExhibitorContract.CONTENT_URI, contentValues);
            }

            return largestID + " " + exhibitors.mCount;
        }

        @Override
        protected void onPostExecute(String s) {
            //do in background return largest processed id & retrieved number of visitors

            if(s == null)
            {
                if(asyncExhibitorHelperListener != null)
                {
                    asyncExhibitorHelperListener.exhibitorTableUpdatedWith(-1, -1);
                }
                return;
            }

            String split[] = s.split(" ");
            long largestID = Long.parseLong(split[0]);
            long numOfVisitors = Long.parseLong(split[1]);

            Log.e("updated", s);
            if(asyncExhibitorHelperListener != null)
            {
                asyncExhibitorHelperListener.exhibitorTableUpdatedWith(numOfVisitors, largestID);
            }
        }
    }
}
