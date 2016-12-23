package www.kidsplace.at.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import www.kidsplace.at.R;

/**
 * Created by admin on 3/10/2018.
 */

public class KPSplashActivity extends KPBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestPermissions();
    }

    private void pushView() {
        Intent intent = new Intent(KPSplashActivity.this, KPLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(KPSplashActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pushView();
                } else {
                    pushView();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
