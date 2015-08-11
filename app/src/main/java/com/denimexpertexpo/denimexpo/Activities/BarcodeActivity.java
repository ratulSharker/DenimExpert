package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.CustomViews.BarcodeScannerView;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Exhibitors;
import com.denimexpertexpo.denimexpo.DenimDataClasses.Visitors;
import com.denimexpertexpo.denimexpo.Interfaces.ViewResizer;
import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.SpecialAsyncTask.AsyncExhibitorHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeActivity extends Activity implements ZXingScannerView.ResultHandler,
        ViewResizer, AsyncHttpRequestHandler {

    private TextView txtBarcodeStatus;
    private BarcodeScannerView viewScanner;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        getActionBar().setTitle(R.string.main_menu_back_title);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        this.txtBarcodeStatus = (TextView) this.findViewById(R.id.barcode_status_text);
        this.viewScanner = (BarcodeScannerView) this.findViewById(R.id.barcode_scanner_view);
        this.viewScanner.setViewResizer(this);

    }
    @Override
    protected void onPause() {
        super.onPause();
        viewScanner.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewScanner.setResultHandler(this);
        viewScanner.startCamera();
        this.txtBarcodeStatus.setText("Waiting for barcode...");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                //back pressed
                NavUtils.navigateUpFromSameTask(this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    This is the method where the barcode scanner posted it's result :)
     */
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        //Log.v(TAG, rawResult.getText()); // Prints scan results
        //Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        this.txtBarcodeStatus.setText("Scan completed");
        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_LONG).show();


        //TODO -- add a http request to make request to server
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true);


        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(this);
        asyncHttpClient.execute(AsyncHttpClient.BuildBarcodeApiUrl(rawResult.getText()));
    }
    @Override
    public void heightWidthKnown(int height, int width) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        float screen_width = size.x;
        float screen_height = size.y;


        RelativeLayout.LayoutParams existingParams = (RelativeLayout.LayoutParams) viewScanner.getLayoutParams();
        existingParams.width = (int) ((screen_width / screen_height) * height);

        viewScanner.setLayoutParams(existingParams);
    }


    @Override
    public void onResponseRecieved(String response) {
        Log.e("barcode response ", response);
        this.progressDialog.dismiss();

        int userType = JsonParserHelper.parseAppropriateUserTypeFromBarcodeApi(response);

        Log.e("Usertype returned ", userType + " ");

        switch (userType)
        {
            case JsonParserHelper.INVALID_USER_TYPE:
            {
                //Toast.makeText(this, "Invalid user, no user registered with this barcode", Toast.LENGTH_LONG).show();

                AlertDialog.Builder alertDialougeBuilder = new AlertDialog.Builder(this);
                alertDialougeBuilder.setPositiveButton("Rescan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewScanner.setResultHandler(BarcodeActivity.this);
                        viewScanner.startCamera();
                        txtBarcodeStatus.setText("Waiting for barcode...");
                    }
                });
                alertDialougeBuilder.setNegativeButton("Main menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                });
                alertDialougeBuilder.setTitle("Sorry");
                alertDialougeBuilder.setMessage("No user found to show");

                alertDialougeBuilder.show();
            }
            break;
            case JsonParserHelper.EXHIBITOR_USER_TYPE:
            {
                //show exhibitor details page
                Exhibitors exhibitors = JsonParserHelper.parseDownTheExhibitors(response);
                if(exhibitors != null &&
                        exhibitors.mExhibitorList != null &&
                        exhibitors.mExhibitorList.size() == 1)
                {
                    Intent exhibitorDetails = new Intent(BarcodeActivity.this, ExhibitorDetailsActivity.class);
                    exhibitorDetails.putExtra(ExhibitorDetailsActivity.FROM_OBJECT, true);
                    exhibitorDetails.putExtra(ExhibitorDetailsActivity.OBJECT_KEY, exhibitors.mExhibitorList.get(0));

                    startActivity(exhibitorDetails);
                }
            }
            break;
            case JsonParserHelper.VISITOR_USER_TYPE:
            {
                //show visitor details page
                Visitors visitors = JsonParserHelper.parseDownTheVisitors(response);
                if(visitors != null &&
                        visitors.mVisitorList != null &&
                        visitors.mVisitorList.size() == 1)
                {
                    Intent visitorDetails = new Intent(BarcodeActivity.this, VisitorDetailsActivity.class);
                    visitorDetails.putExtra(VisitorDetailsActivity.FROM_OBJECT, true);
                    visitorDetails.putExtra(VisitorDetailsActivity.OBJECT_KEY, visitors.mVisitorList.get(0));
                    startActivity(visitorDetails);
                }
            }
            break;
        }
    }

    @Override
    public void onHttpErrorOccured() {
        this.progressDialog.dismiss();
        Toast.makeText(this, "No internet connection, cant fetch the barcode result", Toast.LENGTH_LONG).show();
    }
}
