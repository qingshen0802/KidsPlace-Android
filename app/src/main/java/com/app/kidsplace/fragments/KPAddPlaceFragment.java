package com.app.kidsplace.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.app.kidsplace.activities.KPPlaceReviewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 3/11/2018.
 */

public class KPAddPlaceFragment extends Fragment {

    @BindView(R.id.add_place_bt_slide)
    ImageView btCheckSlide;
    @BindView(R.id.add_place_bt_sandpit)
    ImageView btCheckSandpit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_add_place, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
         upDateCategory();
    }

    @OnClick(R.id.activity_add_place_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity)getActivity()).controlDrawer();
    }

    @OnClick(R.id.add_place_bt_create)
    public void onCreatePlace() {
        Intent intent = new Intent(getActivity(), KPPlaceReviewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.add_place_bt_slide)
    public void onSelectSlide() {
        btCheckSlide.setSelected(!btCheckSlide.isSelected());
        upDateCategory();
    }

    @OnClick(R.id.add_place_bt_sandpit)
    public void onSelectSandpit() {
        btCheckSandpit.setSelected(!btCheckSandpit.isSelected());
        upDateCategory();
    }

    private void upDateCategory() {
        updateSlide(btCheckSlide.isSelected());
        updateSandpit(btCheckSandpit.isSelected());
    }

    private void updateSlide(boolean isSelectable) {
        if (isSelectable) {
            btCheckSlide.setImageResource(R.drawable.check);
        } else {
            btCheckSlide.setImageResource(R.drawable.uncheck);
        }
    }

    private void updateSandpit(boolean isSelectable) {
        if (isSelectable) {
            btCheckSandpit.setImageResource(R.drawable.check);
        } else {
            btCheckSandpit.setImageResource(R.drawable.uncheck);
        }
    }
}
