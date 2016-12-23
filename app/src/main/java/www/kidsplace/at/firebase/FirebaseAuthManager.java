package www.kidsplace.at.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import www.kidsplace.at.interfaces.OnAuthCompleteListener;

/**
 * Created by admin on 3/20/2018.
 */

public class FirebaseAuthManager {

    /**
     * Properties
     */
    static FirebaseAuthManager firebaseUserManager;
    FirebaseAuth mAuth;

    /**
     * Static Methods
     */
    public static FirebaseAuthManager getInstance() {
        if (firebaseUserManager == null) {
            firebaseUserManager = new FirebaseAuthManager();
        }

        return firebaseUserManager;
    }

    public FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Sign In Methods
     */
    public void signinUserWithEmail(Activity activity, String email, String password, final OnAuthCompleteListener authCompleteListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authCompleteListener.onAuthComplete(task.isSuccessful(), mAuth.getCurrentUser());
                    }
                });
    }

    public void signinUserWithGoogle(Activity activity, GoogleSignInAccount account, final OnAuthCompleteListener authCompleteListener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authCompleteListener.onAuthComplete(task.isSuccessful(), mAuth.getCurrentUser());
                    }
                });
    }

    public void signinUserWithFacebook(Activity activity, AccessToken accessToken, final OnAuthCompleteListener authCompleteListener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authCompleteListener.onAuthComplete(task.isSuccessful(), mAuth.getCurrentUser());
                    }
                });
    }

    /**
     * Sign Up Methods
     */
    public void createUserWithEmail(Activity activity, String email, String password, final OnAuthCompleteListener authCompleteListener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authCompleteListener.onAuthComplete(task.isSuccessful(), mAuth.getCurrentUser());
                    }
                });
    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }
}