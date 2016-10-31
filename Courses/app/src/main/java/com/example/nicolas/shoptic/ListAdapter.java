package com.example.nicolas.shoptic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolas.shoptic.core.List;

import java.util.ArrayList;

/**
 * Created by guilhem on 31/10/16.
 */
public class ListAdapter extends ArrayAdapter<List> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, ArrayList<List> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listslist_item, null);
        }

        List p = getItem(position);

        if (p != null) {
            TextView tt = (TextView) v.findViewById(R.id.list_name);
            ImageView iv = (ImageView) v.findViewById(R.id.list_image);

            if (tt != null) {
                tt.setText(p.getName());
            }

            if (iv != null) {
                if (p.getImage_path() == null) {
                    iv.setImageResource(R.drawable.ic_lists_list);
                }else{
                    iv.setImageURI(p.getImage_path());
                }
            }

        }

        return v;
    }

}
