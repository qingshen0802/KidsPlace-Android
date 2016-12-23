package www.kidsplace.at.pushevent.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import www.kidsplace.at.pushevent.services.BackgroundService;

/**
 * Created by alexey on 11/25/16.
 */

public class BootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, BackgroundService.class);
            context.startService(serviceIntent);
        }
    }

}
