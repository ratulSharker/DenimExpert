package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Display;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.CustomViews.BarcodeScannerView;
import com.denimexpertexpo.denimexpo.Interfaces.ViewResizer;
import com.denimexpertexpo.denimexpo.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeActivity extends Activity implements ZXingScannerView.ResultHandler, ViewResizer {

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


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Intent intent = new Intent(BarcodeActivity.this, BarcodeResultActivity.class);
                startActivity(intent);
            }
        };

        //move to details
        new Handler().postDelayed(runnable, 3000);


        //move to details view
        //TODO -- add a http request to make request to server
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

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
}
