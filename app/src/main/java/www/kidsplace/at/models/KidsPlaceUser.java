package www.kidsplace.at.models;

import android.content.Context;

import org.json.JSONObject;

import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.utils.UtilsMethods;

/**
 * Created by admin on 3/12/2018.
 */

public class KidsPlaceUser {

    static KidsPlaceUser user;

    public KPUser currentUser;
    int rounds;
    boolean purchased;
    Context context;

    public static KidsPlaceUser sharedUser(Context context) {
        if (user == null) {
            user = new KidsPlaceUser(context);
        }
        return user;
    }

    public KidsPlaceUser(Context mContext) {
        context = mContext;
        rounds = UtilsMethods.readIntFromPreference(context, "rounds");
        purchased = UtilsMethods.readBooleanFromPreference(context, "purchased");
        getUser();
    }

    public void unsetUser() {
        currentUser = null;
        UtilsMethods.writeToFile(context, "");
        user = null;
    }

    public void setRounds(int mRounds) {
        rounds = mRounds;
        UtilsMethods.writeIntToPreferenece(context, "rounds", mRounds);
    }

    public int getRounds() {
        return rounds;
    }

    public void setPurchased(Context context, boolean mPurchase) {
        purchased = mPurchase;
        UtilsMethods.writeBooleanFromPreference(context, "purchased", mPurchase);
    }

    public boolean getPurchased() {
        return purchased;
    }

    private void getUser() {
        JSONObject info = UtilsMethods.getJSONFromString(UtilsMethods.readFromFile(context));
        if (info.length() == 0) {
            currentUser = KPUser.createUser();
        } else if (info.length() > 0) {
//            currentUser = KPUser.createUser(info);
            currentUser = getUserFromInfo(info);
        }
    }

    public KPUser getUserFromInfo(JSONObject info) {
        KPUser user = KPUser.createUser();
        user.userID = UtilsMethods.getStringFromJSON(info, "user_id");
        user.userName = UtilsMethods.getStringFromJSON(info, "user_name");
        user.userProfile = UtilsMethods.getStringFromJSON(info, "thumb_url");
        return user;
    }

    public JSONObject getJSONFromUser() {
        JSONObject object = new JSONObject();
        UtilsMethods.addStringToJSON(object, "user_id", currentUser.userID);
        UtilsMethods.addStringToJSON(object, "user_name", currentUser.userName);
        UtilsMethods.addStringToJSON(object, "thumb_url", currentUser.userProfile);

        return object;
    }

    public void saveUser() {
        JSONObject info = getJSONFromUser();
        UtilsMethods.writeToFile(context, info.toString());
    }

    public boolean checkStatus() {
        return !currentUser.userID.isEmpty();
    }
}
