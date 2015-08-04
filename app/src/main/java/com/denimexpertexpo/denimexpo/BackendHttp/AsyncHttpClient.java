package com.denimexpertexpo.denimexpo.BackendHttp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.logging.Handler;

/**
 * Created by ratul on 7/13/2015.
 */
public class AsyncHttpClient extends AsyncTask <String, String, String>{


    //some external constant to be used outside the class
    public static final String LOCATION_API_URL = "http://apps.bangladeshdenimexpo.com/api/location.php";
    public static final String SCHEDULE_API_URL = "http://apps.bangladeshdenimexpo.com/api/schedule.php";


    private AsyncHttpRequestHandler mDelegate;


    //some internal constants
    private final String OK_MSG     = "OK";
    private final String ERROR_MSG  = "ERROR";

    private final String TAG        = "AsyncHttpClient.TAG";


    /**
     * Constructor for the AsyncHttpClient
     *
     * @param obj
     */
    public AsyncHttpClient(AsyncHttpRequestHandler obj)
    {
        this.mDelegate = obj;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {

            return ERROR_MSG;

        } catch (IOException e) {
            return ERROR_MSG;
        }
        return OK_MSG + responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //now time to call the handler's methods
        if(this.mDelegate != null)
        {
            if(result.startsWith(OK_MSG))
            {
                // got the exact result
                this.mDelegate.onResponseRecieved(result.substring(OK_MSG.length()));
            }
            else if(result.startsWith(ERROR_MSG))
            {
                //got the error result
                this.mDelegate.onHttpErrorOccured();
            }
        }
        else
        {
            Log.e(TAG, "this.mHandler is not assigned");
        }
    }
}
