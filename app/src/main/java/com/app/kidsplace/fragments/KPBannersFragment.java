package com.app.kidsplace.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.kidsplace.R;
import com.app.kidsplace.activities.KPAddBannerActivity;
import com.app.kidsplace.activities.KPHomeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 3/11/2018.
 */

public class KPBannersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_banners, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @OnClick(R.id.activity_banners_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity)getActivity()).controlDrawer();
    }

    @OnClick(R.id.activity_banners_bt_create)
    public void onCreateBanner() {
        Intent intent = new Intent(getActivity(), KPAddBannerActivity.class);
        getActivity().startActivity(intent);
    }
}
