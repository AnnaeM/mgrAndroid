package com.example.mgrAndroid.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.example.mgrAndroid.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MgrDatabaseHelper {
    private static String DB_PATH = "/data/data/com.example.mgrAndroid/databases/";
    private static String JOURNAL = "-journal";

    private Context context;
    private Map<String, SQLiteDatabase> databases;

    public MgrDatabaseHelper(Context context) {
        this.context = context;
    }

    public List<String> loadDatabases() {
        List<String> names = new ArrayList<>();
        databases = new HashMap<>();
        //get databases directory
        File dir = new File(DB_PATH);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File database : files) {
                String name = database.getName();
                if (!name.contains(JOURNAL)) {
                    names.add(name);
                    databases.put(name, openOrCreateDatabase(name));
                }
            }
        }
        return names;
    }

    public SQLiteDatabase openOrCreateDatabase(String name) {
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(name, context.MODE_PRIVATE, null);
        databases.put(name, sqLiteDatabase);
        return sqLiteDatabase;
    }

    public SQLiteDatabase getDatabase(String name) {
        return databases.get(name);
    }

    public void removeDatabase(String name) {
        context.deleteDatabase(name);
        databases.remove(name);
    }

    public String executeQuery(String name, String query) {
        SQLiteDatabase sqLiteDatabase = getDatabase(name);
        if (query.toUpperCase().contains("SELECT")) {
            return executeSelect(sqLiteDatabase, query);
        } else {
            return executeOther(sqLiteDatabase, query);
        }
    }

    private String executeSelect(SQLiteDatabase database, String query) {
        try {
            Cursor cursor = database.rawQuery(query, null);
            StringBuilder builder = new StringBuilder();
            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String columnName = cursor.getColumnName(i);
                        builder.append(columnName)
                                .append(" = ")
                                .append(cursor.getString(i));
                        if (i < cursor.getColumnCount() - 1) {
                            builder.append(", ");
                        }
                    }
                    builder.append("\n");

                } while (cursor.moveToNext());
            }
            return builder.toString();
        } catch (SQLiteException e) {
            return context.getResources().getString(R.string.DB_SQL_ERROR, e.getMessage());
        }
    }

    private String executeOther(SQLiteDatabase database, String query) {
        String message = null;
        try {
            database.beginTransaction();
            database.execSQL(query);
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (SQLiteException e) {
            //incorrect query, send message with details
            message = context.getResources().getString(R.string.DB_SQL_ERROR, e.getMessage());
        }
        return message;
    }

    public File getDatabaseFile(String name) {
        String path = databases.get(name).getPath();
        return new File(path);
    }

}
