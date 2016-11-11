package com.example.nicolas.shoptic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.nicolas.shoptic.core.List;

import java.util.ArrayList;

/**
 * A fragment representing a list of Lists
 */
public class ListsListFragment extends ListFragment {

    private ShopTicApplication application;
    private ListAdapter aa;
    private ArrayList<List> items;
    private String dialogAddListImageUri = null;
    private ImageView dialogAddListImage =null;
    private static final int SELECT_PICTURE_DIALOG_NEW_LIST = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listslist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        application = (ShopTicApplication)getActivity().getApplicationContext();
        items = application.getLists();
        aa = new ListAdapter(getActivity(),
                R.layout.listslist_item, items);

        setListAdapter(aa);

        FloatingActionButton addList = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_list);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert;

                alert = createAddListDialog();

            if (alert != null){
                alert.show();
            }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        final List item = items.get(position);
        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra(ShopTicApplication.INTENT_MESSAGE_LIST, item);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    /**
     * The add list dialog builder. Just call return.show() on it.
     * @return The Builder of the add list alert dialog.
     */
    public AlertDialog.Builder createAddListDialog(){

        LayoutInflater linf = LayoutInflater.from(getContext());
        final View inflator = linf.inflate(R.layout.dialog_create_list, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Ajouter une nouvelle liste");
        alert.setView(inflator);

        final EditText et1 = (EditText) inflator.findViewById(R.id.dialog_list_name);
        dialogAddListImage = (ImageView) inflator.findViewById(R.id.dialog_image_list);

        dialogAddListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_DIALOG_NEW_LIST);
            }
        });

        alert.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String s1 = et1.getText().toString();
                if (!application.addList(new List(s1, dialogAddListImageUri))) {
                    AlertDialog.Builder alert_name = new AlertDialog.Builder(getContext());
                    alert_name.setMessage("Le nom " + s1 + " est déjà pris. Veuillez en choisir un autre");
                    alert_name.setPositiveButton("OK", null);
                    alert_name.show();
                }
                aa.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE_DIALOG_NEW_LIST) {
                Uri selectedImageUri = data.getData();
                dialogAddListImage.setImageURI(selectedImageUri);
                dialogAddListImageUri = selectedImageUri.toString();
            }
        }
    }
}