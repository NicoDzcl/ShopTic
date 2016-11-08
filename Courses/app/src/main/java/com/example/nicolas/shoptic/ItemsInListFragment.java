package com.example.nicolas.shoptic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.ListItem;
import com.example.nicolas.shoptic.core.Product;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by buisangu on 08/11/2016.
 */
public class ItemsInListFragment extends Fragment {

        ShopTicApplication application;
        List list;
    GridView gridview;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            application = (ShopTicApplication) getActivity().getApplicationContext();
            list = (List) getArguments().getSerializable("list");
            ArrayList<ListItem> listItems = application.getItemsInList(list);
            ArrayList<Product> products = new ArrayList<>();
            for (ListItem i: listItems){
                products.add(i.getProduct());
            }

            View v = inflater.inflate(R.layout.fragment_productslist, container, false);
            gridview = (GridView) v.findViewById(R.id.gridview_product);
            ProductAdapter adapter = new ProductAdapter(getContext(), 0, products);
            gridview.setAdapter(adapter);

            return v;

        }

    public void notifyDataSetChanged() {
        ArrayList<ListItem> listItems = application.getItemsInList(list);
        ArrayList<Product> products = new ArrayList<>();
        for (ListItem i: listItems){
            products.add(i.getProduct());
        }
        ProductAdapter adapter = new ProductAdapter(getContext(), 0, products);
        gridview.setAdapter(adapter);
    }
}

