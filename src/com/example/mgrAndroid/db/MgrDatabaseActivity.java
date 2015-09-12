package com.example.mgrAndroid.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mgrAndroid.R;

import java.util.List;

public class MgrDatabaseActivity extends Activity {

    private MgrDatabaseHelper databaseHelper;
    private Spinner spinner;
    private MgrDatabaseAdapter adapter;
    private TextView validationError;
    private TextView nameValidationError;
    private EditText create;
    private EditText execute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.DB);
        setContentView(R.layout.db_layout);

        databaseHelper = new MgrDatabaseHelper(this);
        spinner = (Spinner) findViewById(R.id.db_spinner);
        validationError = (TextView) findViewById(R.id.validation_error);
        nameValidationError = (TextView) findViewById(R.id.name_validation_error);
        create = (EditText) findViewById(R.id.db_name_edittext);
        execute = (EditText) findViewById(R.id.db_query);

        addTextWatchers(create, nameValidationError);
        addTextWatchers(execute, validationError);

        List<String> test = databaseHelper.loadDatabases();
        adapter = new MgrDatabaseAdapter(test, this);
        spinner.setAdapter(adapter);

    }

    private void addTextWatchers(EditText editText, TextView validationError) {
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

    public void onCreateDatabase(View v) {
        TextView textView = (TextView) findViewById(R.id.db_name_edittext);
        String name = textView.getText().toString();

        if(isValidName(name, nameValidationError)) {
            SQLiteDatabase sqLiteDatabase = databaseHelper.openOrCreateDatabase(name);
            adapter.addItem(name);
            adapter.notifyDataSetChanged();
            Log.e("aaa", sqLiteDatabase.getPath());
            String message = getResources().getString(R.string.DB_CREATED) +" "+ sqLiteDatabase.getPath();
            makeToast(message);
        }
    }

    private boolean isValidName(String name, TextView validationError) {
        if (isEmptyString(name)) {
            validationError.setVisibility(View.VISIBLE);
            validationError.setText(R.string.DB_NAME_EMPTY);
            return false;
        }
        return true;
    }

    private boolean isEmptyString(String name) {
        return name == null || name.equals("");
    }

    public void onExecuteQuery(View v) {

    }

    public void onSendDatabase(View v) {

    }

    public void onRemoveDatabase(View v) {

    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void refreshSpinner() {
        adapter = new MgrDatabaseAdapter(databaseHelper.loadDatabases(), this);
    }
}
