package com.example.mgrAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.mgrAndroid.db.MgrDatabaseActivity;
import com.example.mgrAndroid.intents.MgrGpsActivity;
import com.example.mgrAndroid.intents.MgrPhoneActivity;
import com.example.mgrAndroid.intents.MgrSmsActivity;
import com.example.mgrAndroid.rotation.MgrLifecycleActivity;

public class MgrStartActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(R.string.MAIN_TITLE);

        setOnClickListener(R.id.rotation, MgrLifecycleActivity.class);
        setOnClickListener(R.id.phone, MgrPhoneActivity.class);
        setOnClickListener(R.id.sms, MgrSmsActivity.class);
        setOnClickListener(R.id.database, MgrDatabaseActivity.class);
        setOnClickListener(R.id.gps, MgrGpsActivity.class);
    }

    private void setOnClickListener(int buttonId, Class activityClass) {
        findViewById(buttonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(activityClass);
            }
        });
    }

    private void startActivity(Class activityClass) {
        if(activityClass != null) {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        }
    }
}
