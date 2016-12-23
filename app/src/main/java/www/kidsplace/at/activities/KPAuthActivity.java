package www.kidsplace.at.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import www.kidsplace.at.firebase.FirebaseAuthManager;
import www.kidsplace.at.utils.UtilsMethods;

/**
 * Created by admin on 3/20/2018.
 */

public class KPAuthActivity extends KPBaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    FirebaseAuthManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = FirebaseAuthManager.getInstance();
    }

    public boolean checkEmail(String email) {
        if (email.isEmpty()) {
            return false;
        }

        if (!UtilsMethods.checkEmailValid(email)) {
            return false;
        }

        return true;
    }

    public boolean checkPassword(String password) {
        return !password.isEmpty();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
