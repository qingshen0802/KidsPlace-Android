package com.app.kidsplace.activities;

import android.content.Intent;
import android.os.Bundle;

import com.app.kidsplace.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class KPLoginActivity extends KPBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick(R.id.login_bt_signin)
    public void onSignin() {
        login();
    }

    @OnClick(R.id.login_bt_google)
    public void onSigninWithGoogle() {
        loginWithGoogle();
    }

    @OnClick(R.id.login_bt_facebook)
    public void onSigninWithFacebook() {
        loginWithFacebook();
    }

    @OnClick(R.id.bt_signup)
    public void onSignup() {
        signup();
    }

    private void login() {
        loginSuccess();
    }

    private void loginWithGoogle() {
        loginSuccess();
    }

    private void loginWithFacebook() {
        loginSuccess();
    }

    private void loginSuccess() {
        Intent intent = new Intent(KPLoginActivity.this, KPHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void signup() {
        Intent intent = new Intent(KPLoginActivity.this, KPRegisterActivity.class);
        startActivityForResult(intent, 10000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10000) {
            if (resultCode == RESULT_OK) {
                loginSuccess();
            }
        }
    }
}
