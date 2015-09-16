package com.example.mgrAndroid.db;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import java.io.File;
import java.util.List;

public class MgrDatabaseActivity extends Activity {

    private MgrDatabaseHelper databaseHelper;
    private Spinner spinner;
    private MgrDatabaseAdapter adapter;
    private TextView validationError;
    private TextView nameValidationError;
    private EditText create;
    private EditText query;

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
        query = (EditText) findViewById(R.id.db_query);

        addTextWatchers(create, nameValidationError);
        addTextWatchers(query, validationError);

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

        if(isValid(name, nameValidationError, R.string.DB_NAME_EMPTY)) {
            SQLiteDatabase sqLiteDatabase = databaseHelper.openOrCreateDatabase(name);
            adapter.addItem(name);
            adapter.notifyDataSetChanged();
            Log.e("aaa", sqLiteDatabase.getPath());
            String message = getResources().getString(R.string.DB_CREATED) +" "+ sqLiteDatabase.getPath();
            makeToast(message);
        }
    }

    private boolean isValid(String name, TextView validationError, int messageRes) {
        if (isEmptyString(name)) {
            showError(messageRes, validationError);
            return false;
        }
        return true;
    }

    private void showError(int messageRes, TextView validationError) {
        validationError.setVisibility(View.VISIBLE);
        validationError.setText(messageRes);
    }

    private boolean isEmptyString(String name) {
        return name == null || name.equals("");
    }

    public void onExecuteQuery(View v) {
        String name = getValidedName();
        if(name != null) {
            String userQuery = query.getText().toString();
            if(isValid(userQuery, validationError, R.string.DB_SQL_EMPTY)) {
                String s = databaseHelper.executeQuery(name, userQuery);
                if(s == null) {
                    makeToast(getResources().getString(R.string.DB_SQL_DONE));
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.DB_EXECUTE)
                            .setMessage(s)
                            .setPositiveButton(R.string.OK, null)
                            .show();
                }
            }
        }
    }

    /**
     * Sending database in mail
     * @param v
     */
    public void onSendDatabase(View v) {
        String name = getValidedName();
        if(name != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/octet-stream");
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.DB_MAIL_TITLE, name));
            //can not attach file from private storage, copy database to public dir
            File databaseCopy = databaseHelper.exportDatabse(name);
            // the attachment
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(databaseCopy));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.SEND_MAIL)));
        }
    }

    public void onRemoveDatabase(View v) {
        String name = getValidedName();
        if(name != null) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.DB_REMOVE)
                    .setMessage(R.string.DB_REMOVE_QUESTION)
                    .setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseHelper.removeDatabase(name);
                            adapter.removeItem(name);
                            adapter.notifyDataSetChanged();
                            String string = getResources().getString(R.string.DB_REMOVED, name);
                            makeToast(string);
                        }
                    })
                    .setNegativeButton(R.string.NO, null)
                    .show();
        }
    }

    private String getValidedName() {
        String name = (String) spinner.getSelectedItem();
        if (name != null) {
            return name;
        } else {
            showError(R.string.DB_NO_SELECTED, validationError);
        }
        return name;
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        //remove databases copies
        databaseHelper.deleteDatabaseCopies();
        super.onDestroy();
    }
}
