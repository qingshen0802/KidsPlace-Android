package www.kidsplace.at.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import www.kidsplace.at.interfaces.KPLocationCatchListener;

/**
 * Created by admin on 3/20/2018.
 */

public class KPLocationManager {

    static KPLocationManager locationManager;

    LocationManager mLocationManager;
    LocationListener mLocationListener;
    KPLocationCatchListener catchListener;

    boolean gps_enabled;
    boolean network_enabled;

    Context context;

    public static KPLocationManager sharedLocationManager(Context context, KPLocationCatchListener catchListener) {
        locationManager = new KPLocationManager(context, catchListener);
        return locationManager;
    }

    public KPLocationManager(Context mContext, KPLocationCatchListener mCatchListener) {
        context = mContext;
        catchListener = mCatchListener;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new KPLocationListener();
    }

    public void searchCurrentLocation() {
        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            catchListener.catchLocationFailed("Location loading is failed");
            return;
        }

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        if (network_enabled) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        }
    }

    class KPLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocationManager.removeUpdates(mLocationListener);
                catchListener.catchLocationSuccess(location);
            } else {
                catchListener.catchLocationFailed("Location loading is failed");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
