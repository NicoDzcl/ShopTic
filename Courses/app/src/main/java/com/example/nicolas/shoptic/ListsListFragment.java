package com.example.nicolas.shoptic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nicolas.shoptic.core.List;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListsListFragment extends ListFragment {

    private int PICK_IMAGE = 6;
    private ShopTicApplication application;
    private ListAdapter aa;
    private ArrayList<List> items;


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

    public ListAdapter getAa() {
        return aa;
    }
}