package com.example.mgrAndroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MgrDatabaseHelper {
    private static String DB_PATH = "/data/data/com.example.mgrAndroid/databases/";
    private static String JOURNAL = "-journal";

    private Context context;
    private List<SQLiteDatabase> databases;

    public MgrDatabaseHelper(Context context) {
        this.context = context;
    }

    public List<String> loadDatabases() {
        databases = new ArrayList<>();
        List<String> names = new ArrayList<>();
        //get databases directory
        File dir = new File(DB_PATH);
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            for(File database : files) {
                String name = database.getName();
                if(!name.contains(JOURNAL)) {
                    names.add(name);
                    databases.add(context.openOrCreateDatabase(name, context.MODE_PRIVATE, null));
                }
            }
        }
        return names;
    }

    public List<SQLiteDatabase> getDatabases() {
        return databases;
    }

    public SQLiteDatabase openOrCreateDatabase(String name) {
        return context.openOrCreateDatabase(name, context.MODE_PRIVATE, null);
    }
}
