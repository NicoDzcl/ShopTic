package com.example.nicolas.shoptic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.Product;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ProductsListFragment.IOnProductSelected, ItemsInListFragment.IOnProductRemoved{

    ShopTicApplication app = null;
    List list = null;
    ListPagerAdapter adapter;
    private final int SELECT_PICTURE_DIALOG_NEW_PRODUCT = 2;
    private ImageView  dialogNewProductImage;
    private String dialogNewProductImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (ShopTicApplication) getApplicationContext();
        list = (List) getIntent().getSerializableExtra(ShopTicApplication.INTENT_MESSAGE_LIST);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        setTitle(list.getName());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ListPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton addProduct = (FloatingActionButton) findViewById(R.id.fab_add_product);
        if (addProduct != null) {
            addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alert;

                    alert = getAddProductDialog();

                    if (alert != null){
                        alert.show();
                    }
                }
            });
        }
    }

    @Override
    public void OnProductSelected() {
        Fragment fragment1 = adapter.getItem(0);
        if (fragment1 instanceof ItemsInListFragment){
            ((ItemsInListFragment) fragment1).notifyDataSetChanged();
        }
    }

    @Override
    public void OnProductRemoved() {
        Fragment fragment2 = adapter.getItem(1);
        if (fragment2 instanceof ProductsListFragment){
            ((ProductsListFragment) fragment2).refreshAdapter();
        }
    }

    private class ListPagerAdapter extends FragmentPagerAdapter{
        private final static int NB_TAB = 2;
        private String[] tabTitles = {"Liste", "Produits"};
        private Fragment[] fragments;

        public ListPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[NB_TAB];
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                if (position == 0) {
                    fragments[position] = new ItemsInListFragment();
                } else {
                    fragments[position] = new ProductsListFragment();
                }
                Bundle args = new Bundle();
                args.putSerializable("list", list);
                fragments[position].setArguments(args);
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return NB_TAB;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    private AlertDialog getAddProductDialog(){

        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_add_product, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final ArrayList<String> productsNameArray = app.getAllProductsName();
        final AlertDialog alertDialog;

        alert.setTitle("Ajouter une nouveau produit");
        alert.setView(inflator);

        final AutoCompleteTextView productName = (AutoCompleteTextView) inflator.findViewById(R.id.add_product_name);
        dialogNewProductImage = (ImageView) inflator.findViewById(R.id.add_product_image);
        final AutoCompleteTextView categoryName = (AutoCompleteTextView) inflator.findViewById(R.id.add_product_category);
        productName.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productsNameArray));
        productName.setThreshold(1);

        categoryName.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, app.getAllCategoriesName()));
        categoryName.setThreshold(1);

        dialogNewProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_DIALOG_NEW_PRODUCT);
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
                } else if (!app.addProduct(name, dialogNewProductImageUri, cat)) {
                    app.addProductToList(name, list);
                    OnProductSelected();
                    OnProductRemoved();
                    AlertDialog.Builder alert_name = new AlertDialog.Builder(alert.getContext());
                    alert_name.setMessage("Le produit " + name + " existe déjà ! Nous l'ajoutons pour " +
                            "vous. La prochaine fois, selectionnez le dans la liste des produits !");
                    alert_name.setPositiveButton("OK", null);
                    alert_name.show();
                } else {
                    app.addProductToList(name, list);
                    OnProductSelected();
                    OnProductRemoved();
                }
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialogNewProductImage = null;
                dialogNewProductImageUri = null;
            }
        });


        alertDialog = alert.create();
        productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                app.addProductToList((String) parent.getItemAtPosition(position), list);
                OnProductRemoved();
                OnProductSelected();
                alertDialog.dismiss();

            }
        });



        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE_DIALOG_NEW_PRODUCT) {
                Uri selectedImageUri = data.getData();
                dialogNewProductImage.setImageURI(selectedImageUri);
                dialogNewProductImageUri = selectedImageUri.toString();
            }
        }
    }
}


