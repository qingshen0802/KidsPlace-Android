package www.kidsplace.at.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import de.hdodenhof.circleimageview.CircleImageView;
import www.kidsplace.at.KPApplication;
import www.kidsplace.at.R;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.custom_views.CustomMapView;

import static com.facebook.FacebookSdk.getCacheDir;

public class WalkthroughAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    KPPlace place;

    public WalkthroughAdapter(Context context, KPPlace mPlace) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        place = mPlace;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.page_item, container, false);

        CircleImageView imageView = (CircleImageView) itemView.findViewById(R.id.activity_detail_img_thumb);
        CustomMapView mapView = (CustomMapView) itemView.findViewById(R.id.activity_detail_map);

        if (position == 0) {
            imageView.setVisibility(View.VISIBLE);
            mapView.setVisibility(View.GONE);

            Picasso.get()
                    .load(place.getImageURL())
                    .placeholder(R.drawable.img_example_cell_place)
                    .error(R.drawable.img_example_cell_place)
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
            mapView.setVisibility(View.VISIBLE);

            mapView.initializeMap();
            mapView.enableMyLocation();
            Location location = UtilsMethods.getLocationFromString(place.getLocation());
            OpenStreetMapTileProviderConstants.setCachePath(getCacheDir().getAbsolutePath());
            mapView.setEventCenter(place, KPApplication.DefaultContext);
            mapView.moveToLocation(location);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
