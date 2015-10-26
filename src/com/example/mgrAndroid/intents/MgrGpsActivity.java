package com.example.mgrAndroid.intents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.mgrAndroid.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Wymaga ACCESS_FINE_LOCATION permission
 */
public class MgrGpsActivity extends Activity
{
    private TextView coordinate;
    private LocationManager manager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_layout);
        coordinate = (TextView) findViewById(R.id.gps_coordinate);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        prepareView();
    }

    private void prepareView() {
        View turnOnGps = findViewById(R.id.turn_on_gps);
        if(!isGpsOn()) {
            turnOnGps.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    turnGPSOn();
                }
            });
        } else {
            turnOnGps.setVisibility(View.GONE);
            findViewById(R.id.gps_container).setVisibility(View.VISIBLE);
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            locationListener = new MgrGspLocationListener();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 100, locationListener);

            //set last know location
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null) {
                String longitude = "Longitude: " + lastKnownLocation.getLongitude();
                String latitude = "Latitude: " + lastKnownLocation.getLatitude();
                String s = longitude + "\n" + latitude;
                coordinate.setText(s);
            }


        }
    }

    private boolean isGpsOn() {
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        return manager.isProviderEnabled( LocationManager.GPS_PROVIDER );
    }

    public void turnGPSOn()
    {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public class MgrGspLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            Log.e("aaa", "onLocationChanged");

            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();
            String s = longitude + "\n" + latitude;
            coordinate.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("aaa", "onStatusChanged");
        }
    }

    @Override
    protected void onDestroy() {
        manager.removeUpdates(locationListener);
        super.onDestroy();
    }
}
