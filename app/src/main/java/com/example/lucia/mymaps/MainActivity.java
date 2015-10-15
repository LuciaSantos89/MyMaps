package com.example.lucia.mymaps;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final double SEATTLE_LAT=47.606, SEATTLE_LNG = -122.33, SYDNEY_LAT = -33.86,SYDNEY_LNG = 151.20,
                                NEWYORK_LAT = 40.714, NEWYORK_LNG = -74.0056;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (servicesOK()) {

            setContentView(R.layout.activity_map);
            if (initMap()) {
                Toast.makeText(this, "Ready to map", Toast.LENGTH_LONG).show();
                goToLocation(SEATTLE_LAT,SEATTLE_LNG,15);
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Map not connected", Toast.LENGTH_LONG).show();
            }
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean servicesOK() {
        int isAvailabe = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isAvailabe == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailabe)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailabe, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot connect to mapping service", Toast.LENGTH_LONG);
        }
        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
        }
        return (mMap != null);
    }

        private void goToLocation(double lat, double lng, float zoom){
            LatLng latLng = new LatLng(lat, lng);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,zoom);
            mMap.moveCamera(update);
        }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm =
                (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void geoLocate(View v) throws IOException {
        hideSoftKeyboard(v);
        TextView tv = (TextView) findViewById(R.id.editText1);
        String searchString = tv.getText().toString();
        Toast.makeText(this, "Searching for: " + searchString, Toast.LENGTH_SHORT).show();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(searchString, 1);

        if(list.size() > 0){
            Address addr = list.get(0);
            String locality = addr.getLocality();
            Toast.makeText(this, "Found" + locality, Toast.LENGTH_LONG);
            double lat = addr.getLatitude();
            double lng = addr.getLongitude();
            goToLocation(lat,lng,15);

        }
    }
}
