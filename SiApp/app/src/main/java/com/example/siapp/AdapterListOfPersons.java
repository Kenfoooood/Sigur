package com.example.siapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListOfPersons extends BaseAdapter {
    private static final int RESOURCE_LAYOUT = R.layout.element_of_list_of_persons;
    private LayoutInflater inflater;
    ArrayList<Personal> person_data;

    public AdapterListOfPersons(Context context, ArrayList<Personal> person_data) {
        this.person_data = person_data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return person_data.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(RESOURCE_LAYOUT, parent, false);
        String person = person_data.get(position).name;
        if (person != null) {
            ((TextView) view.findViewById(R.id.element_of_list)).setText(person);
        }
        return view;
    }
}
