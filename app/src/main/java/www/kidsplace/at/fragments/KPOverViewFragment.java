package www.kidsplace.at.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.KPApplication;
import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPHomeActivity;
import www.kidsplace.at.adapters.KPAdsAdapter;
import www.kidsplace.at.adapters.KPPlacesAdapter;
import www.kidsplace.at.adapters.WalkthroughAdapter;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.interfaces.OnLoadBannerListener;
import www.kidsplace.at.interfaces.OnLoadPlaceListener;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.models.KPPlaceHeader;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPBannerUtill;
import www.kidsplace.at.utils.KPPlaceUtill;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.custom_views.CustomMapView;
import www.kidsplace.at.views.diloags.KPProgressDialog;

import static com.facebook.FacebookSdk.getCacheDir;

/**
 * Created by admin on 3/11/2018.
 */

public class KPOverViewFragment extends Fragment {
    @BindView(R.id.activity_home_bt_filter)
    ImageView btFilter;
    @BindView(R.id.activity_home_list)
    RecyclerView list;
    @BindView(R.id.activity_home_search_view)
    SearchView svSearch;
    @BindView(R.id.activity_home_map)
    CustomMapView mapView;
    @BindView(R.id.activity_home_ads)
    ViewPager vpAds;

//    GoogleMap mMap;
//    SupportMapFragment mapFragment;
    PopupMenu popup;
    KPProgressDialog dialog;

    ArrayList<KPPlace> places;
    KPPlacesAdapter adapter;

    ArrayList<KPBanner> banners;
    KPAdsAdapter adsAdapter;
    static int i=0;
    private final Handler handler = new Handler();

    String searchKey;
    ArrayList<String> currentCategories;
    boolean isMapReady;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_main, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        popup = new PopupMenu(getActivity(), btFilter);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.type_menu, popup.getMenu());
        popup.getMenu().findItem(R.id.filter_all).setChecked(true);

        initValues();
        setupList();
        setAds();
        setupSearchView();
        initMap();
        loadData(searchKey);
    }

    private void initValues() {
        places = new ArrayList<>();
        banners = new ArrayList<>();
        currentCategories = new ArrayList<>();
        currentCategories.add("All Categories");
        searchKey = "";
        isMapReady = false;
        dialog = KPProgressDialog.getInstance(getContext(), R.style.LoadingProgress);
    }

    private void initMap() {
        mapView.initializeMap();
    }

//    private void initMap() {
//        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.activity_home_map);
//        if (mapFragment == null) {
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            mapFragment = SupportMapFragment.newInstance();
//            ft.replace(R.id.activity_home_map, mapFragment).commit();
//        }
//
//        mapFragment.getMapAsync(this);
//        dialog.show();
//    }

    private void setupList() {
        if (list.getAdapter() != null) {
            adapter.clear();
            adapter.addAll(places);
        } else {
            adapter = new KPPlacesAdapter(getContext(), places, Constants.PLACE_VIEW_OPTION);
            list.setAdapter(adapter);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void setupSearchView() {
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svSearch.clearFocus();
                places.clear();
                dialog.show();
                adapter.notifyDataSetChanged();
                loadData(searchKey);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchKey = newText;
                return false;
            }
        });

        svSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchKey = "";
                places.clear();
                dialog.show();
                adapter.notifyDataSetChanged();
                loadData(searchKey);
                return false;
            }
        });
    }

    @OnClick(R.id.activity_home_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity)getActivity()).controlDrawer();
    }

    @OnClick(R.id.activity_home_bt_filter)
    public void onSelectFilter() {
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    if (!currentCategories.contains(item.getTitle().toString())) {
                        currentCategories.add(item.getTitle().toString());
                    }
                } else {
                    if (currentCategories.contains(item.getTitle().toString())) {
                        currentCategories.remove(item.getTitle().toString());
                    }
                }
                places.clear();
                dialog.show();
                adapter.notifyDataSetChanged();
                loadData(searchKey);
                return false;
            }
        });

        popup.show();
    }

    private void loadData(final String searchKey) {
        dialog.dismiss();
        FirebaseDatabaseManager.getInstance().loadPublishedPlaces(searchKey, currentCategories, new OnLoadPlaceListener() {
            @Override
            public void onAddedPlace(KPPlace place) {
                places.add(place);
                showPlaceInMap(place);
                adapter.notifyItemInserted(adapter.getItemCount());
            }

            @Override
            public void onUpdatedPlace(KPPlace place) {
                int index = KPPlaceUtill.getInstance().indexOf(places, place);
                if (index > -1) {
                    KPPlaceUtill.getInstance().replacePlaceInList(places, place);
                    adapter.notifyItemChanged(index);
                } else {
                    places.add(place);
                    adapter.notifyItemInserted(adapter.getItemCount());
                }
            }

            @Override
            public void onRemovedPlace(KPPlace place) {
                int index = KPPlaceUtill.getInstance().indexOf(places, place);
                if (index > -1) {
                    places.remove(index);
                    adapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onMovedPlace(KPPlace place) {

            }
        });

        FirebaseDatabaseManager.getInstance().loadPublishedBanners(new OnLoadBannerListener() {
            @Override
            public void onAddedBanner(KPBanner banner) {
                if (searchKey.isEmpty() || banner.getAddress().toLowerCase().contains(searchKey.toLowerCase())) {
                    banners.add(banner);
                    if (banners.size() == 2)
                        startAdsAnimation();
                    adsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onUpdatedBanner(KPBanner banner) {
                if (searchKey.isEmpty() || banner.getAddress().toLowerCase().contains(searchKey.toLowerCase())) {
                    int index = KPBannerUtill.getInstance().indexOf(banners, banner);
                    if (index > -1) {
                        KPBannerUtill.getInstance().replaceBannerInList(banners, banner);
                    } else {
                        banners.add(banner);
                    }
                    adsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRemovedBanner(KPBanner banner) {
                if (searchKey.isEmpty() || banner.getAddress().toLowerCase().contains(searchKey.toLowerCase())) {
                    int index = KPBannerUtill.getInstance().indexOf(banners, banner);
                    if (index > -1) {
                        KPBannerUtill.getInstance().removeBannerInList(banners, banner);
                    } else {
                        banners.add(banner);
                    }

                    adsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMovedBanner(KPBanner banner) {

            }
        });
    }

    private void showPlaceInMap(final KPPlace place) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                setOsmMap(place);
            }
        };
        new Handler(Looper.getMainLooper()).post(task);
    }

    private void setOsmMap(KPPlace place) {
        Location location = UtilsMethods.getLocationFromString(place.getLocation());
        OpenStreetMapTileProviderConstants.setCachePath(getCacheDir().getAbsolutePath());
        mapView.setEventCenter(place, KPApplication.DefaultContext);
        mapView.moveToLocation(location);
    }

    private void setAds() {
        adsAdapter = new KPAdsAdapter(getContext(), banners);
        vpAds.setAdapter(adsAdapter);
    }

    private void startAdsAnimation() {
        handler.postDelayed(ViewPagerVisibleScroll, 2000);
    }

    Runnable ViewPagerVisibleScroll= new Runnable() {
        @Override
        public void run() {
            if(i <= adsAdapter.getCount()-1)
            {
                vpAds.setCurrentItem(i, true);
                handler.postDelayed(ViewPagerVisibleScroll, 2000);
                i++;
            } else {
                i = 0;
                vpAds.setCurrentItem(i, true);
                handler.postDelayed(ViewPagerVisibleScroll, 2000);
            }
        }
    };

//    private void setOsmMap(KPPlace place) {
//        Location location = UtilsMethods.getLocationFromString(place.getLocation());
//        LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 14.0f));
//        generateMarker(place);
//    }

//    private void generateMarker(KPPlace place) {
//        Location location = UtilsMethods.getLocationFromString(place.getLocation());
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions()
//                .snippet(place.getAddress())
//                .position(latLng);
//        Marker marker = mMap.addMarker(markerOptions);
//
//        try {
//            CustomMarker markerTarget = new CustomMarker(marker, mImageLoadingCallback);
////            mPicassoMarkerTargetList.add(markerTarget);
//            Picasso.get()
//                    .load(place.getImageURL())
//                    .into(markerTarget);
//        } catch (IllegalArgumentException ignore) {
//            // Do nothing
//        }
//    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//
//        if (!isMapReady) {
//            loadData("");
//            isMapReady = true;
//        }
//    }
}
