package www.kidsplace.at.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.List;

import www.kidsplace.at.KPApplication;
import www.kidsplace.at.interfaces.OnLoadBannerListener;
import www.kidsplace.at.interfaces.OnLoadPlaceListener;
import www.kidsplace.at.interfaces.OnLoadUserProfileListener;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.models.KPUser;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.utils.Constants;

/**
 * Created by admin on 3/21/2018.
 */

public class FirebaseDatabaseManager {
    static FirebaseDatabaseManager databaseManager;

    FirebaseDatabase database;
    KidsPlaceUser currentUser;

    public static FirebaseDatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new FirebaseDatabaseManager();
        }

        return databaseManager;
    }

    public FirebaseDatabaseManager() {
        database = FirebaseDatabase.getInstance();
        currentUser = KidsPlaceUser.sharedUser(KPApplication.DefaultContext);
    }

    public void updatePlace(KPPlace place) {
        DatabaseReference myRef = database.getReference().child("places").child(place.getPlaceID());
        myRef.setValue(place);
    }

    public void addPlaceForReview(KPPlace place) {
        long milionTimes = System.currentTimeMillis();
        DatabaseReference myRef = database.getReference().child("places").child("place_" + milionTimes);
        place.setPlaceID("place_" + milionTimes);
        myRef.setValue(place);
    }

    public void updatePlaceForReview(KPPlace place) {
        DatabaseReference myRef = database.getReference().child("places").child(place.getPlaceID());
        myRef.setValue(place);
    }

    public void addBanner(KPBanner banner) {
        long milionTimes = System.currentTimeMillis();
        DatabaseReference myRef = database.getReference().child("banners").child("banner" + milionTimes);
        banner.setBannerID("banner" + milionTimes);
        myRef.setValue(banner);
    }

    public void updateBanner(KPBanner banner) {
        DatabaseReference myRef = database.getReference().child("banners").child(banner.getBannerID());
        myRef.setValue(banner);
    }

    public void loadPublishedPlaces(final String searchKey, final List<String> categories, final OnLoadPlaceListener listener) {
        DatabaseReference myRef = database.getReference().child("places");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (!categories.contains("All Categories")) {
                    for (String category : categories) {
                        if (!place.getCategory().contains(category)) {
                            return;
                        }
                    }
                }
                if ((place.getAddress().toLowerCase().contains(searchKey.toLowerCase()) || searchKey.isEmpty()) && place.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onAddedPlace(place);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (!categories.contains("All Categories")) {
                    for (String category : categories) {
                        if (!place.getCategory().contains(category)) {
                            return;
                        }
                    }
                }
                if ((place.getAddress().toLowerCase().contains(searchKey.toLowerCase()) || searchKey.isEmpty()) && place.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onUpdatedPlace(place);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (!categories.contains("All Categories")) {
                    for (String category : categories) {
                        if (!place.getCategory().contains(category)) {
                            return;
                        }
                    }
                }
                if ((place.getAddress().toLowerCase().contains(searchKey.toLowerCase()) || searchKey.isEmpty()) && place.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onRemovedPlace(place);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (!categories.contains("All Categories")) {
                    for (String category : categories) {
                        if (!place.getCategory().contains(category)) {
                            return;
                        }
                    }
                }
                if ((place.getAddress().toLowerCase().contains(searchKey.toLowerCase()) || searchKey.isEmpty()) && place.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onMovedPlace(place);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error", "sync place data error");
            }
        });
    }

    public void loadPlacesForUser(final OnLoadPlaceListener listener) {
        DatabaseReference myRef = database.getReference().child("places");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getUserID().equals(currentUser.currentUser.getUserID())) {
                    listener.onAddedPlace(place);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getUserID().equals(currentUser.currentUser.getUserID())) {
                    listener.onUpdatedPlace(place);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getUserID().equals(currentUser.currentUser.getUserID())) {
                    listener.onRemovedPlace(place);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getUserID().equals(currentUser.currentUser.getUserID())) {
                    listener.onMovedPlace(place);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void loadPlacesForReview(final OnLoadPlaceListener listener) {
        DatabaseReference myRef = database.getReference().child("places");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onAddedPlace(place);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onUpdatedPlace(place);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onRemovedPlace(place);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                KPPlace place = dataSnapshot.getValue(KPPlace.class);
                if (place.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onMovedPlace(place);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadPublishedBanners(final OnLoadBannerListener listener) {
        DatabaseReference myRef = database.getReference().child("banners");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onAddedBanner(banner);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onUpdatedBanner(banner);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onRemovedBanner(banner);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_PUBLISHED)
                    listener.onMovedBanner(banner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadBannersForReview(final OnLoadBannerListener listener) {
        DatabaseReference myRef = database.getReference().child("banners");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onAddedBanner(banner);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onUpdatedBanner(banner);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onRemovedBanner(banner);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getStatus() == Constants.PLACE_STATUS_REVIEW)
                    listener.onMovedBanner(banner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadBannersForUser(final OnLoadBannerListener listener) {
        DatabaseReference myRef = database.getReference().child("banners");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getUserID().equals(currentUser.currentUser.getUserID()))
                    listener.onAddedBanner(banner);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getUserID().equals(currentUser.currentUser.getUserID()))
                    listener.onUpdatedBanner(banner);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getUserID().equals(currentUser.currentUser.getUserID()))
                    listener.onRemovedBanner(banner);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                KPBanner banner = dataSnapshot.getValue(KPBanner.class);
                if (banner.getUserID().equals(currentUser.currentUser.getUserID()))
                    listener.onMovedBanner(banner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveUserProfile(KPUser user) {
        DatabaseReference myRef = database.getReference().child("users").child("user_" + user.getUserID());
        myRef.setValue(user);
    }

    public void loadUserProfile(String userID, final OnLoadUserProfileListener listener) {
        DatabaseReference myRef = database.getReference().child("users");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KPUser user = dataSnapshot.getValue(KPUser.class);
                if (user.getUserID().equals(currentUser.currentUser.getUserID())) {
                    currentUser.currentUser = user;
                    currentUser.saveUser();
                    listener.onAddedUser(user);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initDatabase() {
        DatabaseReference myRef = database.getReference();
        myRef.setValue("");
    }

    public void initBanners() {
        DatabaseReference myRef = database.getReference().child("banners");
        myRef.setValue("");
    }
}