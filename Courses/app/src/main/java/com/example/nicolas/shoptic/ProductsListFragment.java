package com.example.nicolas.shoptic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.shoptic.core.List;

/**
 * Created by buisangu on 07/11/2016.
 */
public class ProductsListFragment extends Fragment {

    ShopTicApplication application;
    List list;
    IOnProductSelected mCallback;
    ProductAdapter adapter;

    public interface IOnProductSelected {
        void OnProductSelected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (IOnProductSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        application = (ShopTicApplication) getActivity().getApplicationContext();
        list = (List) getArguments().getSerializable("list");

        View v = inflater.inflate(R.layout.fragment_productslist, container, false);
        final GridView gridview = (GridView) v.findViewById(R.id.gridview_product);
        adapter = new ProductAdapter(getContext(), 0, application.getProducts(), list, false);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(application.isProductInList(application.getProducts().get(position), list)){
                    Toast.makeText(getContext(), "Le produit " + application.getProducts().get(position).getName()
                                    + " a été supprimé de la liste " + list.getName(),
                            Toast.LENGTH_SHORT).show();
                    application.removeProductFromList(application.getProducts().get(position), list);
                }else{
                    Toast.makeText(getContext(), "Le produit " + application.getProducts().get(position).getName()
                                    + " a été ajouté à la liste " + list.getName(),
                            Toast.LENGTH_SHORT).show();
                    application.addProductToList(application.getProducts().get(position), list);
                }
                adapter.notifyDataSetChanged();
                mCallback.OnProductSelected();
            }
        });
        return v;
    }

}

