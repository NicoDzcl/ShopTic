package com.example.nicolas.shoptic;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolas.shoptic.core.List;

import java.util.ArrayList;

/**
 * Created by guilhem on 31/10/16.
 */
public class ListAdapter extends ArrayAdapter<List> {

    ShopTicApplication app;

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        app = (ShopTicApplication) context.getApplicationContext();
    }

    public ListAdapter(Context context, int resource, ArrayList<List> items) {
        super(context, resource, items);
        app = (ShopTicApplication) context.getApplicationContext();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listslist_item, null);
        }


        List p = getItem(position);

        if (p != null) {
            TextView tt = (TextView) v.findViewById(R.id.list_name);
            ImageView iv = (ImageView) v.findViewById(R.id.list_image);
            ImageButton ib = (ImageButton) v.findViewById(R.id.list_button);

            if (tt != null) {
                tt.setText(p.getName());
            }

            if (iv != null) {
                if (p.getImage_path() == null) {
                    iv.setImageResource(R.drawable.ic_lists_list);
                }else{
                    iv.setImageURI(p.getImage_path());
                }
            }

            if (ib != null){
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(v, position);
                    }
                });
            }


        }

        return v;
    }


    public void showPopup(final View view, final int position) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.list_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.list_menu_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Supprimer la liste " + getItem(position).getName() + " ?")
                                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteList(position);
                                    }
                                })
                                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                }
                return false;
            }
        });
        popup.show();
    }

    private void deleteList(int position){
        app.deleteList(position);
        notifyDataSetChanged();
    }

}
