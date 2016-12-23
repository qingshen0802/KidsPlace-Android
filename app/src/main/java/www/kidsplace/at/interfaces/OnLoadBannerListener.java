package www.kidsplace.at.interfaces;

import www.kidsplace.at.models.KPBanner;

/**
 * Created by admin on 3/23/2018.
 */

public interface OnLoadBannerListener {
    public abstract void onAddedBanner(KPBanner banner);
    public abstract void onUpdatedBanner(KPBanner banner);
    public abstract void onRemovedBanner(KPBanner banner);
    public abstract void onMovedBanner(KPBanner banner);
}
