package www.kidsplace.at.interfaces;

import www.kidsplace.at.models.KPPlace;

/**
 * Created by admin on 3/23/2018.
 */

public interface OnLoadPlaceListener {
    public abstract void onAddedPlace(KPPlace place);
    public abstract void onUpdatedPlace(KPPlace place);
    public abstract void onRemovedPlace(KPPlace place);
    public abstract void onMovedPlace(KPPlace place);
}
