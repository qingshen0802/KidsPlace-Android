package com.app.kidsplace.utils;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

/**
 * Created by Andreas on 28.07.2016.
 */
public class ResourceProxy {
    public static final OnlineTileSourceBase ARCGISONLINE = new OnlineTileSourceBase("ARCGisOnline",  0, 18, 256, "",
            new String[] { "http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/" }) {
        @Override
        public String getTileURLString(MapTile aTile) {
            String mImageFilenameEnding = ".png";
            return getBaseUrl() + aTile.getZoomLevel() + "/"
                    + aTile.getY() + "/" + aTile.getX()
                    + mImageFilenameEnding;
        }
    };

    public static final OnlineTileSourceBase ORTHO_TIROL = new XYTileSource("TMS Overlay",  0, 19, 256, "",
            new String[] { "http://map1.mapservices.eu/gdi/gdi_ortho/"+Constants.ORHTO_KEY+"/" }) {
        @Override
        public String getTileURLString(MapTile aTile) {
            String mImageFilenameEnding = ".jpg";
            int ymax = 1 << aTile.getZoomLevel();
            int invertedY = ymax - aTile.getY() -1;
            return getBaseUrl() + aTile.getZoomLevel() + "/" + aTile.getX() + "/" + invertedY
                    + mImageFilenameEnding;

        }
    };
}
