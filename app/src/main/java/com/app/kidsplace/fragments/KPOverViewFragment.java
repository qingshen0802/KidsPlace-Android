package com.app.kidsplace.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.kidsplace.R;
import com.app.kidsplace.activities.KPHomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 3/11/2018.
 */

public class KPOverViewFragment extends Fragment {
    @BindView(R.id.activity_home_bt_filter)
    ImageView btFilter;
    PopupMenu popup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_main, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        popup = new PopupMenu(getActivity(), btFilter);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.type_menu, popup.getMenu());
        popup.getMenu().findItem(R.id.filter_all).setChecked(true);
    }

    @OnClick(R.id.activity_home_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity)getActivity()).controlDrawer();
    }

    @OnClick(R.id.activity_home_bt_filter)
    public void onSelectFilter() {
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                return false;
            }
        });

        popup.show();
    }
}
