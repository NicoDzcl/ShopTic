package com.example.nicolas.shoptic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nicolas.shoptic.core.List;

public class ListActivity extends AppCompatActivity implements ProductsListFragment.IOnProductSelected, ItemsInListFragment.IOnProductRemoved{

    ShopTicApplication app = null;
    List list = null;
    ListPagerAdapter adapter;

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
            ((ProductsListFragment) fragment2).adapter.notifyDataSetChanged();
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
}


