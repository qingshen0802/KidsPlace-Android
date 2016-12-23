package www.kidsplace.at.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.KPApplication;
import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPHomeActivity;
import www.kidsplace.at.adapters.KPPlacesAdapter;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.interfaces.KPLocationCatchListener;
import www.kidsplace.at.interfaces.OnLoadPlaceListener;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPLocationManager;
import www.kidsplace.at.utils.KPPlaceUtill;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.custom_views.CustomMapView;
import www.kidsplace.at.views.diloags.KPProgressDialog;

import static com.facebook.FacebookSdk.getCacheDir;

/**
 * Created by admin on 3/11/2018.
 */

public class KPRadarFragment extends Fragment {

    @BindView(R.id.activity_radar_list)
    RecyclerView list;
    @BindView(R.id.activity_radar_map)
    CustomMapView mapView;

//    GoogleMap mMap;
//    SupportMapFragment mapFragment;
    KPProgressDialog dialog;

    ArrayList<KPPlace> places;
    KPPlacesAdapter adapter;

    Location currentLocation;
    boolean isMapReady;

    private static final int REQUEST_ENABLE_GPS = 2000;

    KidsPlaceUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_radar, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initValues();
        setupList();
        initMap();
        dialog.show();
        onLoadLocation();
    }

    private void initValues() {
        places = new ArrayList<>();
        isMapReady = false;
        dialog = KPProgressDialog.getInstance(getContext(), R.style.LoadingProgress);
        user = KidsPlaceUser.sharedUser(getContext());
    }

    private void initMap() {
        mapView.initializeMap();
        mapView.enableMyLocation();
    }

//    private void initMap() {
//        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.activity_radar_map);
//        if (mapFragment == null) {
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            mapFragment = SupportMapFragment.newInstance();
//            ft.replace(R.id.activity_radar_map, mapFragment).commit();
//        }
//
//        dialog.show();
//        mapFragment.getMapAsync(this);
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

    private void loadData() {
        dialog.dismiss();
        FirebaseDatabaseManager.getInstance().loadPublishedPlaces("", new ArrayList<String>(), new OnLoadPlaceListener() {
            @Override
            public void onAddedPlace(KPPlace place) {
                Location location = UtilsMethods.getLocationFromString(place.getLocation());
                if (currentLocation != null) {
                    float distance = location.distanceTo(currentLocation);
                    if (distance <= user.getRounds() * 1000 && distance >= 0) {
                        places.add(place);
                        showPlaceInMap(place);
                        adapter.notifyItemInserted(adapter.getItemCount());
                    }
                }
            }

            @Override
            public void onUpdatedPlace(KPPlace place) {
                Location location = UtilsMethods.getLocationFromString(place.getLocation());
                if (currentLocation != null) {
                    float distance = location.distanceTo(currentLocation);
                    if (distance <= user.getRounds() && distance <= 0) {
                        int index = KPPlaceUtill.getInstance().indexOf(places, place);
                        if (index > -1) {
                            KPPlaceUtill.getInstance().replacePlaceInList(places, place);
                            adapter.notifyItemChanged(index);
                        } else {
                            places.add(place);
                            adapter.notifyItemInserted(adapter.getItemCount());
                        }
                    }
                }
            }

            @Override
            public void onRemovedPlace(KPPlace place) {
                Location location = UtilsMethods.getLocationFromString(place.getLocation());
                if (currentLocation != null) {
                    float distance = location.distanceTo(currentLocation);
                    if (distance <= user.getRounds() && distance <= 0) {
                        int index = KPPlaceUtill.getInstance().indexOf(places, place);
                        if (index > -1) {
                            places.remove(index);
                            adapter.notifyItemRemoved(index);
                        }
                    }
                }
            }

            @Override
            public void onMovedPlace(KPPlace place) {

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

        if (currentLocation == null) {
            mapView.moveToLocation(location);
        }
    }

//    private void setOsmMap(KPPlace place) {
//        Location location = UtilsMethods.getLocationFromString(place.getLocation());
//        LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(newPosition).title(place.getAddress()));
//    }

    private void onLoadLocation() {
        KPLocationManager.sharedLocationManager(getContext(), new KPLocationCatchListener() {
            @Override
            public void catchLocationFailed(String errMessage) {
                Log.d("location status", errMessage);
                dialog.dismiss();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Please enable your location");
                alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent gpsOptionsIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(gpsOptionsIntent, REQUEST_ENABLE_GPS);
                    }
                });
                alertDialog.setNeutralButton("No", null);
                alertDialog.show();
            }

            @Override
            public void catchLocationSuccess(Location location) {
                currentLocation = location;
                mapView.moveToLocation(location);
                loadData();
            }
        }).searchCurrentLocation();
    }

    @OnClick(R.id.activity_radar_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity) getActivity()).controlDrawer();
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        if (!isMapReady) {
//            isMapReady = true;
//        }
//        onLoadLocation();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.show();
        onLoadLocation();
    }
}
