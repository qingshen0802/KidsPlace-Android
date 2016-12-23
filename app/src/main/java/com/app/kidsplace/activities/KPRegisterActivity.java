package com.app.kidsplace.activities;

import android.app.Activity;
import android.content.Intent;
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

public class KPRegisterActivity extends KPBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_bt_signup)
    public void onSignup() {
        signup();
    }

    @OnClick(R.id.register_bt_signup_google)
    public void onSignupWithGoogle() {
        signupWithGoogle();
    }

    @OnClick(R.id.register_bt_signup_facebook)
    public void onSignupWithFacebook() {
        signupWithFacebook();
    }

    @OnClick(R.id.activity_register_bt_back)
    public void onBack() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void signup() {
        signupSuccess();
    }

    private void signupWithGoogle() {
        signupSuccess();
    }

    private void signupWithFacebook() {
        signupSuccess();
    }

    private void signupSuccess() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
