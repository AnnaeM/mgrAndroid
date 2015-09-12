package com.example.mgrAndroid.rotation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.mgrAndroid.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Anna on 15.08.2015.
 */
public class MgrRotationActivity extends Activity
{
    private static final String MESSAGE = "MESSAGE";
    private static final String RESTORE_STATE = "RESTORE_STATE";
    private static final String TEST_CHECKBOX_STATE = "TEST_CHECKBOX_STATE";

    private TextView activityMessage;
    private StringBuilder messageBuilder;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotation_layout);
        setTitle(R.string.ROTATION);
        activityMessage = (TextView) findViewById(R.id.activity_message);
        messageBuilder = new StringBuilder();

        if(savedInstanceState != null)
        {
            //re-creating window
            Boolean restore = savedInstanceState.getBoolean(RESTORE_STATE);
            String message = savedInstanceState.getString(MESSAGE);
            if (message != null && restore)
            {
                messageBuilder = new StringBuilder(message);
                addMessage(R.string.ROTATION_RECREATE_REBUILD);
//                CheckBox testCheckbox = (CheckBox) findViewById(R.id.test_checkbox);
//                testCheckbox.setChecked(savedInstanceState.getBoolean(TEST_CHECKBOX_STATE));
            }
            else
            {
                addMessage(R.string.ROTATION_RECREATE_DATA_LOST);
            }
        } else {
            //first-open window
            addMessage(R.string.ROTATION_CREATE);
        }

        findViewById(R.id.clear_logs).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                messageBuilder = new StringBuilder();
                activityMessage.setText("");
            }
        });
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        addMessage(R.string.ROTATION_ON_CREATE_VIEW);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume()
    {
        addMessage(R.string.ROTATION_ON_RESUME);
        super.onResume();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        addMessage(R.string.ROTATION_ON_RESTART);
    }

    @Override
    protected void onPause()
    {
        addMessage(R.string.ROTATION_ON_PAUSE);
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        addMessage(R.string.ROTATION_ON_CONFIGURATION_CHANGE);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        addMessage(R.string.ROTATION_ON_SAVE_INSTANCE);
        super.onSaveInstanceState(outState);
        outState.putString(MESSAGE, messageBuilder.toString());
        CheckBox saveInstance = (CheckBox) findViewById(R.id.save_instance);
//        CheckBox testCheckbox = (CheckBox) findViewById(R.id.test_checkbox);
        outState.putBoolean(RESTORE_STATE, saveInstance.isChecked());
//        outState.putBoolean(TEST_CHECKBOX_STATE, testCheckbox.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        addMessage(R.string.ROTATION_ON_RESTORE_INSTANCE);
    }

    @Override
    protected void onStop()
    {
        addMessage(R.string.ROTATION_ON_STOP);
        super.onStop();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        addMessage(R.string.ROTATION_ON_START);
    }

    @Override
    protected void onDestroy()
    {
        addMessage(R.string.ROTATION_ON_DESTROY);
        super.onDestroy();
    }

    private void addMessage(int messageRes) {
        if(messageBuilder == null) {
            return;
        }
        String message = getResources().getString(messageRes);
        messageBuilder.append(message);
        messageBuilder.append("\n");
        if(activityMessage != null)
        {
            activityMessage.setText(messageBuilder.toString());
        }

        if(timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        messageBuilder.append("Finished");
                        messageBuilder.append("\n\n");
                        if(activityMessage != null)
                        {
                            activityMessage.setText(messageBuilder.toString());
                        }
                    }
                });

            }
        }, 500);



    }

    public static void setAutoOrientationEnabled(Context context, boolean enabled)
    {
        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }
}
