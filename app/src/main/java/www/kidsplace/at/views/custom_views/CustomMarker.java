package www.kidsplace.at.views.custom_views;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzp;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.utils.UtilsMethods;

public class CustomMarker implements Target {

    Marker mMarker;
    Callback mDownloadListenerCallback;

    public CustomMarker(Marker marker, Callback downloaderCallback) {
        mMarker = marker;
        mDownloadListenerCallback = downloaderCallback;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomMarker) {
            Marker marker = ((CustomMarker) o).mMarker;
            return mMarker.equals(marker);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return mMarker.hashCode();
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
        mDownloadListenerCallback.onSuccess();
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
