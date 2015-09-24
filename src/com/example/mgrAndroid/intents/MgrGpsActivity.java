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
            LocationListener locationListener = new MgrGspLocationListener();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        }
    }

    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
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

        /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
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


}
