package com.example.nicolas.shoptic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nicolas.shoptic.core.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SELECT_PICTURE_DIALOG_NEW_LIST = 1;
    Fragment openedFragment = null;
    ShopTicApplication application;
    private ImageView dialogAddListImage =null;
    private String dialogAddListImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Class fragmentClass = ListsListFragment.class;
        try {
            openedFragment = (Fragment)fragmentClass.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.flContent, openedFragment).commit();
        application = (ShopTicApplication) getApplicationContext();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        AlertDialog.Builder alert = null;

        switch (id){
            case R.id.action_add_list:
                if (openedFragment instanceof ListsListFragment){
                    alert = createAddListDialog();
                }

                if (alert != null){
                    alert.show();
                }


                return true;
            case R.id.search:
                return true;
            case R.id.list_menu_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_listslist) {
            // Handle the camera action
        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_products) {

        } else if (id == R.id.nav_fidelity_cards) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_DIALOG_NEW_LIST) {
                Uri selectedImageUri = data.getData();
                dialogAddListImage.setImageURI(selectedImageUri);
                dialogAddListImageUri = selectedImageUri.toString();
            }
        }
    }

    public AlertDialog.Builder createAddListDialog(){
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_create_list, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Ajouter une nouvelle liste");
        alert.setView(inflator);

        final EditText et1 = (EditText) inflator.findViewById(R.id.dialog_list_name);
        dialogAddListImage = (ImageView) inflator.findViewById(R.id.dialog_image_list);

        dialogAddListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_DIALOG_NEW_LIST);
            }
        });

        alert.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String s1 = et1.getText().toString();
                application.addList(new List(s1, dialogAddListImageUri));
                ((ListsListFragment) openedFragment).getAa().notifyDataSetChanged();
                dialogAddListImage = null;
                dialogAddListImageUri = null;

            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                dialogAddListImage = null;
                dialogAddListImageUri = null;
            }
        });
        return alert;
    }

}
