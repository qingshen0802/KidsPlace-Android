package www.kidsplace.at.interfaces;

import android.location.Location;

/**
 * Created by admin on 3/20/2018.
 */

public interface KPLocationCatchListener {
    public abstract void catchLocationFailed(String errMessage);
    public abstract void catchLocationSuccess(Location location);
}
