package com.example.nicolas.shoptic;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Adapter;
import android.widget.BaseAdapter;

/**
 * Created by guilhem on 31/10/16.
 */
public class DialogAddList extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_create_list, null))
                // Add action buttons
                .setPositiveButton("Ajouter la liste", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogAddList.this.getDialog().cancel();
                    }
                })
                .setTitle("Ajouter une liste");
        return builder.create();

    }
}
