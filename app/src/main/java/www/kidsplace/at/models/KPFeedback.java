package www.kidsplace.at.models;

import android.os.Parcel;
import android.os.Parcelable;

public class KPFeedback implements Parcelable {

    String comment;
    float rating;

    public KPFeedback() {
        comment = "";
        rating = 0;
    }

    protected KPFeedback(Parcel in) {
        comment = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<KPFeedback> CREATOR = new Creator<KPFeedback>() {
        @Override
        public KPFeedback createFromParcel(Parcel in) {
            return new KPFeedback(in);
        }

        @Override
        public KPFeedback[] newArray(int size) {
            return new KPFeedback[size];
        }
    };

    public void setComment(String mComment) {
        comment = mComment;
    }

    public String getComment() {
        return comment;
    }

    public void setRating(float mRating) {
        rating = mRating;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeFloat(rating);
    }
}
