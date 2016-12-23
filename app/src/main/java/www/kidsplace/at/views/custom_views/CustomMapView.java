package www.kidsplace.at.views.custom_views;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Locale;

import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPPlaceDetailActivity;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.utils.ResourceProxy;
import www.kidsplace.at.utils.UtilsMethods;

/**
 * Created by Andreas on 29.07.2016.
 */
public class CustomMapView extends MapView {

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getController().setZoom(18);
        this.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.setTileSource(ResourceProxy.ARCGISONLINE);
        this.setBuiltInZoomControls(false);
        this.setMultiTouchControls(true);
    }

    public void setEventCenter(final KPPlace place, Context context){
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        Location location = UtilsMethods.getLocationFromString(place.getLocation());
        GeoPoint gp = new GeoPoint(location.getLatitude(), location.getLongitude());
        OverlayItem overlayItem = new OverlayItem(place.getAddress(), place.getAddress(), gp);
        overlayItem.setMarker(ResourcesCompat.getDrawable(this.getResources(), R.drawable.img_example_marker_place,null));

        items.add(overlayItem);

        ItemizedOverlayWithFocus<OverlayItem> overlay = new ItemizedOverlayWithFocus<OverlayItem>(context, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {

                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Intent intent = new Intent(getContext(), KPPlaceDetailActivity.class);
                        intent.putExtra("place", place);
                        getContext().startActivity(intent);
                        return true; // We 'handled' this event.
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        this.getOverlays().add(overlay);
        this.invalidate();
    }

    public void initializeMap() {
        this.getOverlays().clear();
        this.invalidate();
    }

    public void moveToLocation(Location location) {
        GeoPoint gp = new GeoPoint(location.getLatitude(), location.getLongitude());
        this.getController().setCenter(gp);
    }

    public void enableMyLocation() {
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), this);
        mLocationOverlay.enableMyLocation();
        getOverlays().add(mLocationOverlay);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_DOWN:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }}