package com.app.kidsplace.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.kidsplace.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 3/13/2018.
 */

public class KPPlaceReviewActivity extends KPBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_review);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_reviewing_bt_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.activity_reviewing_bt_publish)
    public void onPublish() {
        finish();
    }
}
