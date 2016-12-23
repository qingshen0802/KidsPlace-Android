package www.kidsplace.at.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.R;
import www.kidsplace.at.interfaces.OnAuthCompleteListener;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.diloags.KPProgressDialog;

/**
 * Created by admin on 3/10/2018.
 */

public class KPRegisterActivity extends KPAuthActivity {

    @BindView(R.id.register_et_email)
    EditText email_input;
    @BindView(R.id.register_et_password)
    EditText password_input;
    @BindView(R.id.register_et_confirm)
    EditText confirm_input;

//    GoogleApiClient mGoogleApiClient;

//    private final static int SIGN_UP_WITH_GOOGLE = 1000;
//    private final static int SIGN_UP_WITH_FACEBOOK = 1001;
//    private final static int RESET_PASSWORD = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

//        setupSocialApis();
    }

    @OnClick(R.id.register_bt_signup)
    public void onSignup() {
        signup();
    }

//    @OnClick(R.id.register_bt_signup_google)
//    public void onSignupWithGoogle() {
//        signupWithGoogle();
//    }
//
//    @OnClick(R.id.register_bt_signup_facebook)
//    public void onSignupWithFacebook() {
//        signupWithFacebook();
//    }

    @OnClick(R.id.activity_register_bt_back)
    public void onBack() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void signup() {
        String email = email_input.getText().toString();
        String password = password_input.getText().toString();
        String confirm = confirm_input.getText().toString();

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

        if (!password.equals(confirm)) {
            confirm_input.setError(getResources().getString(R.string.activity_register_password_not_match_message));
            return;
        }

        final KPProgressDialog dialog = KPProgressDialog.getInstance(this, R.style.RegisteringProgress);
        dialog.show();
        userManager.createUserWithEmail(this, email, password, new OnAuthCompleteListener() {
            @Override
            public void onAuthComplete(boolean result, FirebaseUser user) {
                dialog.dismiss();
                if (result) {
                    signupSuccess();
                }
            }
        });
    }

//    private void signupWithGoogle() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, SIGN_UP_WITH_GOOGLE);
//    }

//    private void signupWithFacebook() {
//        CallbackManager callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
//
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                try {
//                                    String email = object.getString("email");
//                                    resetPassword(email);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//    }

    private void signupSuccess() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

//    private void setupSocialApis() {
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//    }
//
//    private void resetPassword(String email) {
//        Intent intent = new Intent(this, KPResetPasswordActivity.class);
//        intent.putExtra("email", email);
//        startActivityForResult(intent, RESET_PASSWORD);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SIGN_UP_WITH_GOOGLE) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                resetPassword(account.getEmail());
//            } catch (ApiException e) {
//
//            }
//        } else if (requestCode == RESET_PASSWORD) {
//            if (resultCode == Activity.RESULT_OK) {
//                String password = data.getExtras().getString("password");
//                String email = data.getExtras().getString("email");
//                userManager.createUserWithEmail(this, email, password, new OnAuthCompleteListener() {
//                    @Override
//                    public void onAuthComplete(boolean result, FirebaseUser user) {
//                        if (result)
//                            signupSuccess();
//                    }
//                });
//            }
//
//        }
    }
}
