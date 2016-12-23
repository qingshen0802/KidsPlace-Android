package www.kidsplace.at.pushevent.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import www.kidsplace.at.pushevent.controllers.ObjectSearchController;

/**
 * Created by alexey on 11/23/16.
 */

public class BackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ObjectSearchController.getInstance(getApplicationContext()).startSearch();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent("at.kidsplace.www.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
    }
}
