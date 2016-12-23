package www.kidsplace.at.models;

import org.json.JSONObject;

import www.kidsplace.at.utils.UtilsMethods;

public class KPUser {
    String userID;
    String userName;
    String userProfile;

    public static KPUser createUser() {
        KPUser user = new KPUser();
        return user;
    }

//    public static KPUser createUser(JSONObject info) {
//        KPUser user = new KPUser(info);
//        return user;
//    }

    public KPUser() {
        userID = "";
        userName = "";
        userProfile = "";
    }

    public void setUserID(String mUserID) {
        userID = mUserID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserName(String mUserName) {
        userName = mUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserProfile(String imgURL) {
        userProfile = imgURL;
    }

    public String getUserProfile() {
        return userProfile;
    }

//    public JSONObject getJSONFromUser() {
//        JSONObject object = new JSONObject();
//        UtilsMethods.addStringToJSON(object, "user_id", userID);
//        UtilsMethods.addStringToJSON(object, "user_name", userName);
//        UtilsMethods.addStringToJSON(object, "thumb_url", userProfile);
//
//        return object;
//    }
}
