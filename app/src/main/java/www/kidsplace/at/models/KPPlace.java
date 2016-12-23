package www.kidsplace.at.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by admin on 3/12/2018.
 */

public class KPPlace implements Parcelable {

    String placeID;
    String userID;
    String imageURL;
    String category;
    String description;
    String location;
    String address;
    ArrayList<KPFeedback> feedbacks;
    int status;

    public KPPlace() {
        placeID = "";
        userID = "";
        imageURL = "";
        category = "";
        description = "";
        location = "";
        address = "";
        feedbacks = new ArrayList<>();
        status = 0;
    }

    protected KPPlace(Parcel in) {
        placeID = in.readString();
        userID = in.readString();
        imageURL = in.readString();
        category = in.readString();
        description = in.readString();
        location = in.readString();
        address = in.readString();
        feedbacks = in.createTypedArrayList(KPFeedback.CREATOR);
        status = in.readInt();
    }

    public static final Creator<KPPlace> CREATOR = new Creator<KPPlace>() {
        @Override
        public KPPlace createFromParcel(Parcel in) {
            return new KPPlace(in);
        }

        @Override
        public KPPlace[] newArray(int size) {
            return new KPPlace[size];
        }
    };

    public void setPlaceID(String mID) {
        placeID = mID;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setUserID(String mUserID) {
        userID = mUserID;
    }

    public String getUserID() {
        return userID;
    }

    public void setImageURL(String mURL) {
        imageURL = mURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setCategory(String mCategory) {
        category = mCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setDescription(String mDescription) {
        description = mDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setLocation(String mLocation) {
        location = mLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setAddress(String mAddress) {
        address = mAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setFeedbacks(ArrayList<KPFeedback> mFeedbacks) {
        feedbacks = mFeedbacks;
    }

    public ArrayList<KPFeedback> getFeedbacks() {
        return feedbacks;
    }

    public void setStatus(int mStatus) {
        status = mStatus;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeID);
        dest.writeString(userID);
        dest.writeString(imageURL);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(address);
        dest.writeTypedList(feedbacks);
        dest.writeInt(status);
    }
}
