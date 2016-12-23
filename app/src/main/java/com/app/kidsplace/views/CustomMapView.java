package com.app.kidsplace.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.app.kidsplace.utils.ResourceProxy;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

/**
 * Created by Andreas on 29.07.2016.
 */
public class CustomMapView extends MapView {

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getController().setZoom(18);
        //this.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.setTileSource(ResourceProxy.ARCGISONLINE);
        this.setBuiltInZoomControls(false);
        this.setMultiTouchControls(true);
    }

    public void setEventCenter(GeoPoint gp, Context context){
        final GeoPoint point = gp;
        this.getController().setCenter(gp);
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        OverlayItem overlayItem = new OverlayItem("", "", gp);
//        overlayItem.setMarker(ResourcesCompat.getDrawable(this.getResources(), R.drawable.marker,null));

        items.add(overlayItem);

        ItemizedOverlayWithFocus<OverlayItem> overlay = new ItemizedOverlayWithFocus<OverlayItem>(context, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {

                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true; // We 'handled' this event.
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        this.getOverlays().clear();
        this.getOverlays().add(overlay);
        this.invalidate();

  /*      setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() ==  MotionEvent.ACTION_UP)
                {
                    getController().setCenter(point);
                    return true;
                }
                if(motionEvent.getPointerCount() > 1)
                {
                    getController().setCenter(point);
                    return false;
                }
                else
                {
                    return true;
                }
            }
        });*/

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