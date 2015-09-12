package com.example.mgrAndroid.intents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.mgrAndroid.R;

public class MgrPhoneActivity extends Activity {

    private EditText phoneNumber;
    private TextView validationError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_layout);
        setTitle(R.string.PHONE);

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        validationError = (TextView) findViewById(R.id.validation_error);

        findViewById(R.id.phone_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallNow();
            }
        });

        findViewById(R.id.phone_use_intent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUseIntent();
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validationError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onCallNow() {
        String phoneNumber = getPhoneNumber();
        if(isValid(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        }
    }

    private void onUseIntent() {
        String phoneNumber = getPhoneNumber();
        if(isValid(phoneNumber)) {
            // <uses-permission android:name="android.permission.CALL_PHONE" />
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.PHONE_CHOOSE_INTENT)));
        }
    }

    private String getPhoneNumber() {
        return phoneNumber.getText().toString();
    }

    private boolean isValid(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.equals("")) {
            validationError.setVisibility(View.VISIBLE);
            validationError.setText(R.string.PHONE_EMPTY_NUMBER);
            return false;
        }
        return true;
    }
}
