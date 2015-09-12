package com.example.mgrAndroid.intents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mgrAndroid.R;

public class MgrSmsActivity extends Activity {

    private EditText phoneNumber;
    private EditText smsMessage;
    private TextView validationError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_layout);
        setTitle(R.string.SMS);

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        smsMessage = (EditText) findViewById(R.id.sms_message);
        validationError = (TextView) findViewById(R.id.validation_error);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendNow();
            }
        });

        findViewById(R.id.send_use_intent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUseIntent();
            }
        });

        addTextWatchers(phoneNumber);
        addTextWatchers(smsMessage);
    }

    private void addTextWatchers(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
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

    private void onSendNow() {
        String phoneNumber = getPhoneNumber();
        String smsMessage = getSmsMessage();
        if(isValid(phoneNumber, smsMessage)) {
            //<uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(this, R.string.SMS_SENDED, Toast.LENGTH_LONG).show();
        }
    }

    private void onUseIntent() {
        String phoneNumber = getPhoneNumber();
        String smsMessage = getSmsMessage();
        if(isValid(phoneNumber, smsMessage)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null));
            intent.putExtra("sms_body", smsMessage);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.PHONE_CHOOSE_INTENT)));
        }
    }

    private String getPhoneNumber() {
        return phoneNumber.getText().toString();
    }

    private String getSmsMessage() {
        return smsMessage.getText().toString();
    }

    private boolean isValid(String phoneNumber, String message) {
        if(isStringEmpty(phoneNumber)) {
            validationError.setVisibility(View.VISIBLE);
            validationError.setText(R.string.PHONE_EMPTY_NUMBER);
            return false;
        }
        if(isStringEmpty(message)) {
            validationError.setVisibility(View.VISIBLE);
            validationError.setText(R.string.SMS_EMPTY_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isStringEmpty(String s) {
        return s == null || s.equals("");
    }
}
