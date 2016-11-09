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

import com.example.nicolas.shoptic.core.Category;
import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.Product;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Created by guilhem on 07/11/16.
 */
public class ProductAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {
    private Context mContext;
    private ShopTicApplication app;
    private LayoutInflater inflater;
    private ArrayList<Product> items;
    private ArrayList<Product> itemsInList;
    private TreeMap<Category, Integer> itemsPerCategory;

    public ProductAdapter(Context context, int resource, ArrayList<Product> items, ArrayList<Product> itemsInList) {
        mContext = context;
        app = (ShopTicApplication) context.getApplicationContext();
        inflater = LayoutInflater.from(context);
        this.items = items;
        Collections.sort(this.items);
        itemsPerCategory = app.getCategoriesFromItems(items);
        if (itemsInList == null){

        }
    }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;

            if (convertView == null) {
                inflater = LayoutInflater.from(mContext);
                v = inflater.inflate(R.layout.productslist_item, parent, false);
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

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Product getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCountForHeader(int i) {
        ArrayList<Category> list = new ArrayList<>(itemsPerCategory.keySet());
        Collections.sort(list);
        return itemsPerCategory.get(list.get(i));
    }

    @Override
    public int getNumHeaders() {

        return itemsPerCategory.size();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.productlist_category, parent, false);
        }
        TextView cat = (TextView) convertView.findViewById(R.id.product_list_category);
        ArrayList<Category> list = new ArrayList<>(itemsPerCategory.keySet());
        Collections.sort(list);
        Category c = list.get(position);

        cat.setText(c.getName());


        return convertView;
    }
}

