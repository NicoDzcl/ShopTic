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

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;

            if (convertView == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.productslist_item, null);
            }else{
                v = convertView;
            }

            Product p = getItem(position);

            if (p != null) {
                TextView tt = (TextView) v.findViewById(R.id.title_product);
                ImageView iv = (ImageView) v.findViewById(R.id.thumbnail_product);

                if (tt != null){
                    tt.setText(p.getName());
                }

                if (iv != null){
                    if (p.getImageUri() != null) {
                        iv.setImageURI(Uri.parse(p.getImageUri()));
                    }
                }

                int dp = app.getPixelsFromDPs(75);
                v.setLayoutParams(new GridView.LayoutParams(dp, dp));
            }


            return v;
        }


}
