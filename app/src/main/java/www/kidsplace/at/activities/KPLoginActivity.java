package www.kidsplace.at.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.R;
import www.kidsplace.at.firebase.FirebaseAuthManager;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.interfaces.OnAuthCompleteListener;
import www.kidsplace.at.models.KPUser;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.views.diloags.KPProgressDialog;

public class KPLoginActivity extends KPAuthActivity {

    @BindView(R.id.login_et_email)
    EditText email_input;
    @BindView(R.id.login_et_password)
    EditText password_input;

    KPProgressDialog dialog;

    GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;

    private final static int SIGN_UP_REQUEST = 10000;
    private final static int SIGN_IN_WITH_GOOGLE = 1000;
    private final static int SIGN_IN_WITH_FACEBOOK = 1001;

    KidsPlaceUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        user = KidsPlaceUser.sharedUser(this);
        if (user.checkStatus()) {
            loginSuccess();
        }

        dialog = KPProgressDialog.getInstance(this, R.style.SigningProgress);
        loadHashKey();
        setupSocialApis();
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
        String email = email_input.getText().toString();
        String password = password_input.getText().toString();
        if (email.isEmpty()) {
            email_input.setError(getResources().getString(R.string.activity_login_email_blank_message));
            return;
        }

        if (!checkEmail(email)) {
            email_input.setError(getResources().getString(R.string.activity_login_email_invalid_message));
            return;
        }

        if (password.isEmpty()) {
            password_input.setError(getResources().getString(R.string.activity_login_password_empty_message));
            return;
        }

        dialog.show();
        userManager.signinUserWithEmail(this, email, password, new OnAuthCompleteListener() {
            @Override
            public void onAuthComplete(boolean result, FirebaseUser user) {
                dialog.dismiss();
                if (result) {
                    loginSuccess();
                }
            }
        });
    }

    private void loginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, SIGN_IN_WITH_GOOGLE);
    }

    private void loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(KPLoginActivity.this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("MainActivity", "@@@onSuccess");
                dialog.show();
                FirebaseAuthManager.getInstance().signinUserWithFacebook(KPLoginActivity.this, loginResult.getAccessToken(), new OnAuthCompleteListener() {
                    @Override
                    public void onAuthComplete(boolean result, FirebaseUser user) {
                        dialog.dismiss();
                        if (result)
                            loginSuccess();
                    }
                });
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void loginSuccess() {
        String userID = userManager.getUser().getUid();
        user.currentUser.setUserID(userID);
        user.saveUser();
        FirebaseDatabaseManager.getInstance().saveUserProfile(user.currentUser);
        Intent intent = new Intent(KPLoginActivity.this, KPHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void signup() {
        Intent intent = new Intent(KPLoginActivity.this, KPRegisterActivity.class);
        startActivityForResult(intent, SIGN_UP_REQUEST);
    }

    private void setupSocialApis() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Configure Facebook Sign In
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_UP_REQUEST) {
            if (resultCode == RESULT_OK) {
                loginSuccess();
            }
        } else if (requestCode == SIGN_IN_WITH_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                dialog.show();
                userManager.signinUserWithGoogle(this, account, new OnAuthCompleteListener() {
                    @Override
                    public void onAuthComplete(boolean result, FirebaseUser user) {
                        dialog.dismiss();
                        if (result)
                            loginSuccess();
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void loadHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "www.kidsplace.at",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
