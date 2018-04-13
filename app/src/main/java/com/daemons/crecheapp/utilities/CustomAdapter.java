package com.daemons.crecheapp.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daemons.crecheapp.R;

import java.util.ArrayList;

/**
 * Created by LOKESH on 31-03-2018.
 */

public class CustomAdapter extends ArrayAdapter {
    ArrayList<Child> childArrayList;
    public CustomAdapter(@NonNull Context context, int resource, ArrayList<Child> a) {
        super(context, 0, a);
        childArrayList = new ArrayList<>();
        childArrayList.addAll(a);
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitem = convertView;
        if (listitem == null) {
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.list_child_item, parent, false);
        }
        Child c = (Child) getItem(position);
        TextView name = (TextView) listitem.findViewById(R.id.child_name);
        TextView childId = (TextView) listitem.findViewById(R.id.child_Id);
        TextView workerId = (TextView) listitem.findViewById(R.id.worker_Id);
        name.setText("NAME: "+c.childName);
        childId.setText("CHILD ID:  "+c.childId+"");;
        workerId.setText("WORKER ID:  "+c.workerId+"");
        Log.i("lkjhg", c.childName);
        return listitem;
    }}
