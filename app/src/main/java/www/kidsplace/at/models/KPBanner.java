package www.kidsplace.at.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 3/16/2018.
 */

public class KPBanner implements Parcelable {
    String bannerID;
    String imageURL;
    String url;
    String userID;
    String location;
    String address;
    int visible;
    int status;

    public KPBanner() {
        bannerID = "";
        imageURL = "";
        url = "";
        userID = "";
        location = "";
        address = "";
        visible = 0;
        status = 0;
    }

    protected KPBanner(Parcel in) {
        bannerID = in.readString();
        imageURL = in.readString();
        url = in.readString();
        userID = in.readString();
        location = in.readString();
        address = in.readString();
        visible = in.readInt();
        status = in.readInt();
    }

    public static final Creator<KPBanner> CREATOR = new Creator<KPBanner>() {
        @Override
        public KPBanner createFromParcel(Parcel in) {
            return new KPBanner(in);
        }

        @Override
        public KPBanner[] newArray(int size) {
            return new KPBanner[size];
        }
    };

    public void setBannerID(String mID) {
        bannerID = mID;
    }

    public String getBannerID() {
        return bannerID;
    }

    public void setImageURL(String mImageURL) {
        imageURL = mImageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setURL(String mURL) {
        url = mURL;
    }

    public String getURL() {
        return url;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
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

    public void setVisible(int mVisible) {
        visible = mVisible;
    }

    public int getVisible() {
        return visible;
    }

    public void setStatus(int status) {
        this.status = status;
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
        dest.writeString(bannerID);
        dest.writeString(imageURL);
        dest.writeString(url);
        dest.writeString(userID);
        dest.writeString(location);
        dest.writeString(address);
        dest.writeInt(visible);
        dest.writeInt(status);
    }
}
