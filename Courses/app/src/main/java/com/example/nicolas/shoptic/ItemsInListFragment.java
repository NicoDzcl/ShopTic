package com.example.nicolas.shoptic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.ListItem;
import com.example.nicolas.shoptic.core.Product;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by buisangu on 08/11/2016.
 */
public class ItemsInListFragment extends Fragment {

        ShopTicApplication application;
        List list;
    StickyGridHeadersGridView gridview;
    ProductAdapter adapter;

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
            gridview = (StickyGridHeadersGridView) v.findViewById(R.id.gridview_product);
            adapter = new ProductAdapter(getContext(),R.layout.productslist_item, products, list, true);
            gridview.setAdapter(adapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    application.toggleItemInList(adapter.getItem(position), list);
                    adapter.notifyDataSetChanged();
                }
            });

            return v;

        }

    public void notifyDataSetChanged() {
        ArrayList<Product> products = application.getProductsInList(list);
        adapter = new ProductAdapter(getContext(), 0, products, list, true);
        gridview.setAdapter(adapter);
    }
}

