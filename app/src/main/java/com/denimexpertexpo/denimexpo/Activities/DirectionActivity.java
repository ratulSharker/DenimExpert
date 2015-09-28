package com.denimexpertexpo.denimexpo.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpClient;
import com.denimexpertexpo.denimexpo.BackendHttp.AsyncHttpRequestHandler;
import com.denimexpertexpo.denimexpo.BackendHttp.JsonParserHelper;
import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.DirectionApi.DirectionListener;
import com.denimexpertexpo.denimexpo.DirectionApi.GetDirectionsAsyncTask;
import com.denimexpertexpo.denimexpo.StaticStyling.CustomStyling;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DirectionActivity extends FragmentActivity implements AsyncHttpRequestHandler, DirectionListener {

    /*
    view related object
     */
    private GoogleMap gMap;
    private ProgressDialog apiProgressDialouge;
    private Button btnLocateExhibition, btnRefreshDirection;
    private Marker markerExhibition, markerUser;

    /*
    data related objects
     */
    private LatLng serverLatLng;
    private LatLng userLatLng;


    //testing
    private Polyline newPolyline;

    private LocationManager locationManager;
    private LocationUpdateListener gpsLocationUpdateListener, networkLocationUpdateListener;


    private class LocationUpdateListener implements LocationListener
    {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.



            //we get a user location
            DirectionActivity.this.userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            //upon recieving a location we detach the gps listener
            locationManager.removeUpdates(gpsLocationUpdateListener);
            locationManager.removeUpdates(networkLocationUpdateListener);


            //now go for the server side locatiopn api
            apiProgressDialouge.setTitle("Loading");
            apiProgressDialouge.setMessage("Getting exhibition location");

            new AsyncHttpClient(DirectionActivity.this).execute(AsyncHttpClient.LOCATION_API_URL);
        }

        /*
        We dont need these functionality
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    }


    /*
    LifeCycle functionality
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);


        //adding the home back button
        CustomStyling.addHomeBackButton(this, "Direction");

        this.btnLocateExhibition = (Button)this.findViewById(R.id.button_locate_exhibition);
        this.btnRefreshDirection = (Button)this.findViewById(R.id.button_update_direction);

        this.btnLocateExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gMap != null && serverLatLng != null) {
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(serverLatLng, 15), 2500, null);
                    gMap.setMyLocationEnabled(true);
                }
            }
        });

        this.btnRefreshDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gMap != null) {
                    btnRefreshDirection.setEnabled(false);
                    btnLocateExhibition.setEnabled(false);
                    setUpMapIfNeeded();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnLocateExhibition.setEnabled(false);
        btnRefreshDirection.setEnabled(false);

        this.setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(gpsLocationUpdateListener != null && networkLocationUpdateListener != null)
        {
            this.locationManager.removeUpdates(gpsLocationUpdateListener);
            this.locationManager.removeUpdates(networkLocationUpdateListener);
        }
    }

    /*
        AsyncHttoRequestHandler Functionality
         */
    @Override
    public void onResponseRecieved(String response) {

        this.serverLatLng = JsonParserHelper.parseDownTheLocation(response);


        if (this.serverLatLng != null) {
            //go for direction api call
            this.apiProgressDialouge.setTitle("Loading");
            this.apiProgressDialouge.setMessage("Calculating direction");


            Map<String, String> map = new HashMap<String, String>();
            map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(userLatLng.latitude));
            map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(userLatLng.longitude));
            map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(serverLatLng.latitude));
            map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(serverLatLng.longitude));
            map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, "WALKING");

            GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
            asyncTask.execute(map);


        } else {
            this.apiProgressDialouge.dismiss();
            //Toast.makeText(this, response + " INVALID JSON", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onHttpErrorOccured() {
        this.apiProgressDialouge.dismiss();
        //Toast.makeText(this, "Error in retrieveing direction", Toast.LENGTH_LONG).show();
    }


    /*
    Menu option related functionality
     */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case R.id.action_settings:
                return true;

            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public void onDirectionRecieved(ArrayList<LatLng> directionPoints) {

        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.rgb(0, 200, 200));
        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }

        //removing the polyline if already added
        if (newPolyline != null) {
            newPolyline.remove();
        }
        newPolyline = gMap.addPolyline(rectLine);


        this.apiProgressDialouge.dismiss();


        if(markerUser == null)
        {
            //set camera animation here baby
            markerUser = gMap.addMarker(new MarkerOptions()
                    .position(userLatLng)
                    .title("You")
                    .snippet("Here are you")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_snowman)));
        }
        else
        {
            markerUser.setPosition(userLatLng);
        }


        if(markerExhibition == null)
        {
            markerExhibition = gMap.addMarker(new MarkerOptions()
                    .position(serverLatLng)
                    .title("Denim Exibition")
                    .snippet("Exhibition 2015")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_showroom)));
        }
        else
        {
            markerExhibition.setPosition(serverLatLng);
        }



        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15), 2500, null);
        gMap.setMyLocationEnabled(true);

        this.btnLocateExhibition.setEnabled(true);
        this.btnRefreshDirection.setEnabled(true);
    }


    public void onDirectionException(Exception ex) {

        this.apiProgressDialouge.dismiss();
        //Toast.makeText(this, "Getting direction exception thrown", Toast.LENGTH_LONG).show();
    }




    /*
    Map related default functionality
     */

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #gMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            gMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (gMap != null) {
                //do whatever you want :)
            }
        }

        //everytime calling it
        if (gMap != null) {
            this.setUpMap();
        }

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #gMap} is not null.
     */
    private void setUpMap() {

        /*
        Do here, whatever you want to do with the map object
        From reaching here we know that the map object is ready :)
         */


        //everything is perfect we can go for the client location :)
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            this.buildAlertMessageNoGps();
        } else {
            //go for the gps location

            this.gpsLocationUpdateListener = new LocationUpdateListener();
            this.networkLocationUpdateListener = new LocationUpdateListener();


            // Register the listener with the Location Manager to receive location updates
            if (this.apiProgressDialouge == null) {
                this.apiProgressDialouge = new ProgressDialog(this);
                this.apiProgressDialouge.setTitle("Loading");
                this.apiProgressDialouge.setMessage("Getting your location");
            }

            this.apiProgressDialouge.show();

            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, gpsLocationUpdateListener);
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, networkLocationUpdateListener);
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help us to locate you.")
                .setMessage("Your GPS / Wifi / cellular location seems to be disabled, please enable it to show the direction.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused")
                                        final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        Toast.makeText(getApplication(),"Please provide all available location provider for better experience", Toast.LENGTH_LONG).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
