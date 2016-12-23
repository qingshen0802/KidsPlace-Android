package com.app.kidsplace.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.kidsplace.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 3/10/2018.
 */

public class KPAddBannerActivity extends KPBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banner);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_banner_bt_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.add_banner_bt_create)
    public void onCreate() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(KPAddBannerActivity.this);
        dialog.setMessage("It will take a few times to verification this banner");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }
}
