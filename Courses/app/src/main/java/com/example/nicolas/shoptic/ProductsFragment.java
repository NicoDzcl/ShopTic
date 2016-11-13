package com.example.nicolas.shoptic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.nicolas.shoptic.core.Product;

import java.util.ArrayList;

/**
 * Created by guilhem on 13/11/16.
 */
public class ProductsFragment extends Fragment {

    private final int SELECT_PICTURE_DIALOG_MODIFY_PRODUCT = 3;
    private final int SELECT_PICTURE_DIALOG_ADD_NEW_PRODUCT = 4;
    private ShopTicApplication application;
    private GridView gridview;
    private ProductAdapter adapter;
    private ImageView dialogProductImage;
    private String dialogProductImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        application = (ShopTicApplication) getActivity().getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_product, container, false);
        gridview = (GridView) v.findViewById(R.id.gridview_product);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add_new_product);
        adapter = new ProductAdapter(getContext(), 0, application.getProducts(), null, false);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                getModifyProductDialog(adapter.getItem(position)).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddProductDialog().show();
            }
        });


        return v;
    }

    private AlertDialog.Builder getModifyProductDialog(final Product p){

        LayoutInflater linf = LayoutInflater.from(getContext());
        final View inflator = linf.inflate(R.layout.dialog_modify_product, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Modifier le produit " + p.getName());
        alert.setView(inflator);

        dialogProductImage = (ImageView) inflator.findViewById(R.id.dialog_modify_product_image);
        final EditText price = (EditText) inflator.findViewById(R.id.add_new_product_price);
        price.setText(String.valueOf(p.getPrice()));
        String productUri = p.getImageUri();
        if (productUri != null) {
            dialogProductImage.setImageURI(Uri.parse(productUri));
        }

        dialogProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_DIALOG_MODIFY_PRODUCT);
            }
        });

        alert.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (price.getText() != null) {
                    application.setProductPrice(p, Double.parseDouble(price.getText().toString()));
                }
                if (dialogProductImageUri != null) {
                    application.setProductImage(p, dialogProductImageUri);
                    dialogProductImageUri = null;
                    dialogProductImage = null;
                }
                adapter = new ProductAdapter(getContext(), 0, application.getProducts(), null, false);
                gridview.setAdapter(adapter);
            }
        });

        alert.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialogProductImageUri = null;
                dialogProductImage = null;
            }
        });

        if (p.isUserDefined()){
            alert.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    application.deleteProduct(p);
                    adapter = new ProductAdapter(getContext(), 0, application.getProducts(), null, false);
                    gridview.setAdapter(adapter);
                    dialogProductImageUri = null;
                    dialogProductImage = null;
                }
            });
        }

        return alert;
    }

    private AlertDialog getAddProductDialog(){

        LayoutInflater linf = LayoutInflater.from(getContext());
        final View inflator = linf.inflate(R.layout.dialog_add_product, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog;

        alert.setTitle("Ajouter une nouveau produit");
        alert.setView(inflator);

        final AutoCompleteTextView productName = (AutoCompleteTextView) inflator.findViewById(R.id.add_product_name);
        dialogProductImage = (ImageView) inflator.findViewById(R.id.add_product_image);
        final AutoCompleteTextView categoryName = (AutoCompleteTextView) inflator.findViewById(R.id.add_product_category);
        categoryName.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, application.getAllCategoriesName()));
        categoryName.setThreshold(1);

        dialogProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_DIALOG_ADD_NEW_PRODUCT);
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = productName.getText().toString();
                String cat = categoryName.getText().toString();
                if (name.length() == 0 || cat.length() == 0) {
                    AlertDialog.Builder alert_name = new AlertDialog.Builder(alert.getContext());
                    alert_name.setMessage("Merci de renseigner le nom et la catégorie du produit !");
                    alert_name.setPositiveButton("OK", null);
                    alert_name.show();
                } else if (!application.addProduct(name, dialogProductImageUri, cat)) {
                    AlertDialog.Builder alert_name = new AlertDialog.Builder(alert.getContext());
                    alert_name.setMessage("Le produit " + name + " existe déjà !");
                    alert_name.setPositiveButton("OK", null);
                    alert_name.show();
                }
                adapter = new ProductAdapter(getContext(), 0, application.getProducts(), null, false);
                gridview.setAdapter(adapter);
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialogProductImage = null;
                dialogProductImageUri = null;
            }
        });


        alertDialog = alert.create();
        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE_DIALOG_MODIFY_PRODUCT || requestCode == SELECT_PICTURE_DIALOG_ADD_NEW_PRODUCT) {
                Uri selectedImageUri = data.getData();
                dialogProductImage.setImageURI(selectedImageUri);
                dialogProductImageUri = selectedImageUri.toString();
            }
        }
    }
}
