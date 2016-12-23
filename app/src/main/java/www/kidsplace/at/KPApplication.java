package www.kidsplace.at;

import android.app.Application;
import android.content.Context;

import www.kidsplace.at.models.KidsPlaceUser;

/**
 * Created by admin on 3/20/2018.
 */

public class KPApplication extends Application {

    public static Context DefaultContext;
    static KPApplication app;

    public static KPApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DefaultContext = getApplicationContext();
    }
}
