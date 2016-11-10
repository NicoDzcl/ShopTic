package com.example.nicolas.shoptic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
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

            gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder b = getQuantityChoiceDialog(adapter.getItem(position));

                    b.show();
                    return true;

                }

            });

            return v;

        }

    public void notifyDataSetChanged() {
        ArrayList<Product> products = application.getProductsInList(list);
        adapter = new ProductAdapter(getContext(), 0, products, list, true);
        gridview.setAdapter(adapter);
    }

    private AlertDialog.Builder getQuantityChoiceDialog(final Product product){
        final LayoutInflater linf = LayoutInflater.from(getContext());
        final View inflator = linf.inflate(R.layout.dialog_select_quantity, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Choisir la quantité");
        alert.setView(inflator);

        final NumberPicker quantity_spinner = (NumberPicker) inflator.findViewById(R.id.dialog_quantity_spinner);
        final Spinner units_spinner = (Spinner) inflator.findViewById(R.id.dialog_quantity_units_spinner);

        quantity_spinner.setMinValue(1);
        quantity_spinner.setMaxValue(999);
        quantity_spinner.setValue(application.getIntQuantityProductInList(product, list));

        final ArrayAdapter<String> units_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, application.getAllQuantityUnits());
        units_spinner.setAdapter(units_adapter);

        alert.setPositiveButton("Enregistrer la quantité", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                application.setQuantityProductInList(product, list, quantity_spinner.getValue(), (String) units_spinner.getSelectedItem());
                adapter.notifyDataSetChanged();

            }
        });

        alert.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.setNegativeButton("Enlever le produit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                application.removeProductFromList(product, list);
                notifyDataSetChanged();
            }
        });
        return alert;
    }
}

