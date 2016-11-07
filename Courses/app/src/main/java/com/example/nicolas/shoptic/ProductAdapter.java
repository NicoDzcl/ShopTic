package com.example.nicolas.shoptic;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by guilhem on 07/11/16.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private ShopTicApplication app;

    public ProductAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        app = (ShopTicApplication) context.getApplicationContext();
    }

    public ProductAdapter(Context context, int resource, ArrayList<Product> items) {
        super(context, resource, items);
        app = (ShopTicApplication) context.getApplicationContext();
    }

        public int getCount() {
            return 3;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v;

            if (convertView == null) {
                v = new TextView(getContext());
            }else{
                v = (TextView) convertView;
            }

            Product p = getItem(position);

            if (p != null) {
                v.setText(p.getName());
            }


            return v;
        }
}
