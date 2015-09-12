package com.example.mgrAndroid.db;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ania on 2015-09-12.
 */
public class MgrDatabaseAdapter extends BaseAdapter {

    private List<String> names;
    private Context context;

    public MgrDatabaseAdapter(List<String> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = new TextView(context);
        }
        
        ((TextView)convertView).setText(names.get(position));
        convertView.setPadding(20,20,20,20);
        return convertView;
    }

    public void addItem(String name) {
        names.add(name);
    }

    public void removeItem(String name) {
        names.remove(name);
    }
}
