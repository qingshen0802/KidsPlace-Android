package www.kidsplace.at.pushevent.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import java.util.ArrayList;

import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPHomeActivity;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.interfaces.OnLoadBannerListener;
import www.kidsplace.at.interfaces.OnLoadPlaceListener;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPBannerUtill;
import www.kidsplace.at.utils.KPPlaceUtill;
import www.kidsplace.at.utils.SerializeObject;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by alexey on 11/23/16.
 */

public class ObjectSearchController {

    Context context;
    boolean isAdmin;

    KidsPlaceUser user;

    static ObjectSearchController instance;
    public static ObjectSearchController getInstance(Context context)   {
        if (instance == null)
            instance = new ObjectSearchController(context);
        return instance;
    }
    public ObjectSearchController(Context context)  {
        this.context = context;
        user = KidsPlaceUser.sharedUser(context);
        isAdmin = user.currentUser.getUserID().equals(Constants.ADMIN_USER_ID);
    }

    public void startSearch()   {
        if (isAdmin) {
            FirebaseDatabaseManager.getInstance().loadPlacesForReview(new OnLoadPlaceListener() {
                @Override
                public void onAddedPlace(KPPlace place) {
                    String message = "Have a place to request to review.";
                    showPush(message);
                }

                @Override
                public void onUpdatedPlace(KPPlace place) {

                }

                @Override
                public void onRemovedPlace(KPPlace place) {

                }

                @Override
                public void onMovedPlace(KPPlace place) {

                }
            });

            FirebaseDatabaseManager.getInstance().loadBannersForReview(new OnLoadBannerListener() {
                @Override
                public void onAddedBanner(KPBanner banner) {
                    String message = "Have a banner to request to review.";
                    showPush(message);
                }

                @Override
                public void onUpdatedBanner(KPBanner banner) {

                }

                @Override
                public void onRemovedBanner(KPBanner banner) {

                }

                @Override
                public void onMovedBanner(KPBanner banner) {

                }
            });

        } else  {
            FirebaseDatabaseManager.getInstance().loadPlacesForUser(new OnLoadPlaceListener() {
                @Override
                public void onAddedPlace(KPPlace place) {
                    if (place.getStatus() != Constants.PLACE_STATUS_REVIEW && user.currentUser.getUserID().equals(place.getUserID())) {
                        ArrayList<KPPlace> places = loadResourcesForPlace();
                        if (KPPlaceUtill.getInstance().indexOf(places, place) > 0) {
                            KPPlaceUtill.getInstance().removePlaceInList(places, place);
                            saveResourcesForPlace(places);

                            if (place.getStatus() == Constants.PLACE_STATUS_PUBLISHED) {
                                String message = "Have a published place";
                                showPush(message);
                            } else if (place.getStatus() == Constants.PLACE_STATUS_UNPUBLISHED) {
                                String message = "Have a unpublished place";
                                showPush(message);
                            }
                        }
                    }
                }

                @Override
                public void onUpdatedPlace(KPPlace place) {
                    if (user.currentUser.getUserID().equals(place.getUserID())) {
                        ArrayList<KPPlace> places = loadResourcesForPlace();
                        if (KPPlaceUtill.getInstance().indexOf(places, place) > 0) {
                            KPPlaceUtill.getInstance().removePlaceInList(places, place);
                            saveResourcesForPlace(places);

                            if (place.getStatus() == Constants.PLACE_STATUS_PUBLISHED) {
                                String message = "Have a published place";
                                showPush(message);
                            } else if (place.getStatus() == Constants.PLACE_STATUS_UNPUBLISHED) {
                                String message = "Have a unpublished place";
                                showPush(message);
                            }
                        }
                    }
                }

                @Override
                public void onRemovedPlace(KPPlace place) {

                }

                @Override
                public void onMovedPlace(KPPlace place) {

                }
            });

            FirebaseDatabaseManager.getInstance().loadBannersForUser(new OnLoadBannerListener() {
                @Override
                public void onAddedBanner(KPBanner banner) {
                    if (banner.getStatus() != Constants.PLACE_STATUS_REVIEW && user.currentUser.getUserID().equals(banner.getUserID())) {
                        ArrayList<KPBanner> banners = loadResourcesForBanner();
                        if (KPBannerUtill.getInstance().indexOf(banners, banner) > 0) {
                            KPBannerUtill.getInstance().removeBannerInList(banners, banner);
                            saveResourcesForBannser(banners);

                            if (banner.getStatus() == Constants.PLACE_STATUS_PUBLISHED) {
                                String message = "Have a published banner";
                                showPush(message);
                            } else if (banner.getStatus() == Constants.PLACE_STATUS_UNPUBLISHED) {
                                String message = "Have a unpublished banner";
                                showPush(message);
                            }
                        }
                    }
                }

                @Override
                public void onUpdatedBanner(KPBanner banner) {
                    if (banner.getStatus() != Constants.PLACE_STATUS_REVIEW && user.currentUser.getUserID().equals(banner.getUserID())) {
                        ArrayList<KPBanner> banners = loadResourcesForBanner();
                        if (KPBannerUtill.getInstance().indexOf(banners, banner) > 0) {
                            KPBannerUtill.getInstance().removeBannerInList(banners, banner);
                            saveResourcesForBannser(banners);

                            if (banner.getStatus() == Constants.PLACE_STATUS_PUBLISHED) {
                                String message = "Have a published banner";
                                showPush(message);
                            } else if (banner.getStatus() == Constants.PLACE_STATUS_UNPUBLISHED) {
                                String message = "Have a unpublished banner";
                                showPush(message);
                            }
                        }
                    }
                }

                @Override
                public void onRemovedBanner(KPBanner banner) {

                }

                @Override
                public void onMovedBanner(KPBanner banner) {

                }
            });
        }
    }

    private void showPush(String pushMessage) {
        Notification _notification = getMainNotification(pushMessage);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, _notification);
    }

    public boolean isEnabled()  {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enableNotification = pref.getBoolean("enableNotification", true);
        return enableNotification;
    }

    Notification getMainNotification(String title) {
        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, KPHomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{50, 50, 50, 50, 50, 50, 50, 50, 50, 1000})
                .build();
        return notification;
    }

    private void saveResourcesForPlace(ArrayList<KPPlace> resources)    {
        String ser = SerializeObject.objectToString(resources);
        if (ser != null && !ser.equalsIgnoreCase("")) {
            SerializeObject.WriteSettings(context, ser, "newPlace.dat");
        } else {
            SerializeObject.WriteSettings(context, "", "newPlace.dat");
        }
    }

    private ArrayList<KPPlace> loadResourcesForPlace()    {
        ArrayList<KPPlace> userList = new ArrayList<>();

        String ser = SerializeObject.ReadSettings(context, "newPlace.dat");
        if (ser != null && !ser.equalsIgnoreCase("")) {
            Object obj = SerializeObject.stringToObject(ser);
            // Then cast it to your object and
            if (obj instanceof ArrayList) {
                // Do something
                userList = (ArrayList<KPPlace>)obj;
            }
        }

        return userList;
    }

    private void saveResourcesForBannser(ArrayList<KPBanner> resources)    {
        String ser = SerializeObject.objectToString(resources);
        if (ser != null && !ser.equalsIgnoreCase("")) {
            SerializeObject.WriteSettings(context, ser, "newBanner.dat");
        } else {
            SerializeObject.WriteSettings(context, "", "newBanner.dat");
        }
    }

    private ArrayList<KPBanner> loadResourcesForBanner()    {
        ArrayList<KPBanner> userList = new ArrayList<>();

        String ser = SerializeObject.ReadSettings(context, "newBanner.dat");
        if (ser != null && !ser.equalsIgnoreCase("")) {
            Object obj = SerializeObject.stringToObject(ser);
            // Then cast it to your object and
            if (obj instanceof ArrayList) {
                // Do something
                userList = (ArrayList<KPBanner>)obj;
            }
        }

        return userList;
    }
}