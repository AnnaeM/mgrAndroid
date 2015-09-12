package com.example.mgrAndroid.intents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import com.example.mgrAndroid.R;

/**
 * Wymaga ACCESS_FINE_LOCATION permission
 */
public class MgrGpsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_layout);

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
        }
    }

    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        return manager.isProviderEnabled( LocationManager.GPS_PROVIDER );
    }

    public void turnGPSOn()
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

}
