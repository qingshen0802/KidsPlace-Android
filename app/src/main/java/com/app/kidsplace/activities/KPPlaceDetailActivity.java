package com.app.kidsplace.activities;

import android.os.Bundle;

import com.app.kidsplace.R;

import butterknife.ButterKnife;

/**
 * Created by admin on 3/10/2018.
 */

public class KPPlaceDetailActivity extends KPBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
    }
}
