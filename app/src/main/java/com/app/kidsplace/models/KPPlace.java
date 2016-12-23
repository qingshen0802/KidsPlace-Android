package com.app.kidsplace.models;

/**
 * Created by admin on 3/12/2018.
 */

public class KPPlace {

    String imageURL;
    String category;
    String description;
    String comment;

    public KPPlace() {
        imageURL = "";
        category = "";
        description = "";
        comment = "";
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
}
