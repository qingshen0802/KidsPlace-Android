package www.kidsplace.at.interfaces;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by admin on 3/20/2018.
 */

public interface OnAuthCompleteListener {
    public abstract void onAuthComplete(boolean result, FirebaseUser user);
}
