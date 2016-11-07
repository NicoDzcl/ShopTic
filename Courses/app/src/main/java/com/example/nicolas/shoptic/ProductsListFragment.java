package com.example.nicolas.shoptic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by buisangu on 07/11/2016.
 */
public class ProductsListFragment extends Fragment {

    ShopTicApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        application = (ShopTicApplication) getActivity().getApplicationContext();
        View v = inflater.inflate(R.layout.fragment_productslist, container, false);
        GridView gridview = (GridView) v.findViewById(R.id.gridview_product);
        gridview.setAdapter(new ProductAdapter(getContext(), 0, application.getProducts()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}

