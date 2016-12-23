package www.kidsplace.at.models;

public class KPPlaceHeader extends KPPlace {

    String thmAds;
    String urlAds;

    public KPPlaceHeader() {
        thmAds = "";
        urlAds = "";
    }

    public void setThmAds(String mImgURL) {
        thmAds = mImgURL;
    }

    public String getThmAds() {
        return thmAds;
    }

    public void setUrlAds(String mURL) {
        urlAds = mURL;
    }

    public String getUrlAds() {
        return urlAds;
    }
}
