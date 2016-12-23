package www.kidsplace.at.utils;


import java.util.ArrayList;

import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.models.KPBanner;

/**
 * Created by admin on 3/23/2018.
 */

public class KPBannerUtill {

    static KPBannerUtill bannerUtill;

    public static KPBannerUtill getInstance() {
        if (bannerUtill == null) {
            bannerUtill = new KPBannerUtill();
        }

        return bannerUtill;
    }

    public void copyBanner(KPBanner banner1, KPBanner banner2) {
        banner1.setURL(banner2.getURL());
        banner1.setImageURL(banner2.getImageURL());
        banner1.setVisible(banner2.getVisible());
        banner1.setUserID(banner2.getUserID());
        banner1.setStatus(banner2.getStatus());
        banner1.setBannerID(banner2.getBannerID());
        banner1.setLocation(banner2.getLocation());
        banner1.setAddress(banner2.getAddress());
    }

    public void replaceBannerInList(ArrayList<KPBanner> list, KPBanner banner) {
        int index = indexOf(list, banner);
        if (index > -1)
            copyBanner(list.get(index), banner);
    }

    public void removeBannerInList(ArrayList<KPBanner> list, KPBanner banner) {
        int index = indexOf(list, banner);
        if (index > -1)
            list.remove(index);
    }

    public int indexOf(ArrayList<KPBanner> list, KPBanner banner) {
        int index = 0;
        boolean isContains = false;
        for (KPBanner b : list) {
            if (b.getBannerID().equals(banner.getBannerID())) {
                isContains = true;
                break;
            }

            index ++;
        }

        if (isContains)
            return index;
        else
            return -1;
    }

    public void updateVisiblityForBanner(KPBanner banner, boolean isVisible) {
        banner.setVisible(isVisible ? 1 : 0);
        FirebaseDatabaseManager.getInstance().updateBanner(banner);
    }
}
