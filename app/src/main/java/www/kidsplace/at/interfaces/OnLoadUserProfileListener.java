package www.kidsplace.at.interfaces;

import www.kidsplace.at.models.KPUser;

public interface OnLoadUserProfileListener {
    public abstract void onAddedUser(KPUser user);
}
