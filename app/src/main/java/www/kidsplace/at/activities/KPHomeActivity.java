package www.kidsplace.at.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import www.kidsplace.at.R;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.fragments.KPBannersFragment;
import www.kidsplace.at.fragments.KPMyPlacesFragment;
import www.kidsplace.at.fragments.KPOverViewFragment;
import www.kidsplace.at.fragments.KPRadarFragment;
import www.kidsplace.at.fragments.KPReviewBannerFragment;
import www.kidsplace.at.fragments.KPReviewFragment;
import www.kidsplace.at.fragments.KPSettingsFragment;
import www.kidsplace.at.interfaces.OnLoadBannerListener;
import www.kidsplace.at.interfaces.OnLoadUserProfileListener;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.models.KPUser;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.pushevent.services.BackgroundService;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPBannerUtill;

/**
 * Created by admin on 3/10/2018.
 */

public class KPHomeActivity extends KPBaseActivity {

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    private View navHeader;
    private TextView lbUserName;
    private CircleImageView imgUserProfile;

    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_MY_PLACE = "my_place";
    private static final String TAG_RADAR = "radar";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_BANNERS = "banners";
    private static final String TAG_REVIEWS = "reviews";
    private static final String TAG_REVIEW_BANNERS = "review banners";
    private static final String TAG_SUBSCRIPTION = "subscription";
    private static final String TAG_LOGOUT = "logout";

    public static String CURRENT_TAG = TAG_HOME;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    static final String ITEM_SKU ="kidsplace_banner_subs";
    BillingClient mBillingClient;
    Intent serviceIntent;

    KidsPlaceUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mHandler = new Handler();

        user = KidsPlaceUser.sharedUser(this);

        setupIAB();

        // initializing navigation menu
        setUpNavigationView();
        setNavMenuItemThemeColors(Color.parseColor("#FFFFFF"));
        setStatusBar();

        checkAdmin();
        startBackgroundService();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    @Override
    public void onDestroy() {
        if(serviceIntent != null){
            stopService(serviceIntent);
        }
        super.onDestroy();
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commit();
                //Closing drawer on item click
                drawer.closeDrawers();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                KPOverViewFragment homeFragment = new KPOverViewFragment();
                return homeFragment;
            case 1:
                // my places
                KPMyPlacesFragment myPlaceFragment = new KPMyPlacesFragment();
                return myPlaceFragment;
            case 2:
                // radar
                KPRadarFragment radarFragment = new KPRadarFragment();
                return radarFragment;
            case 3:
                // banners
                KPBannersFragment bannersFragment = new KPBannersFragment();
                return bannersFragment;
            case 4:
                KPReviewFragment reviewFragment = new KPReviewFragment();
                return reviewFragment;
            case 5:
                KPReviewBannerFragment reviewBannerFragment = new KPReviewBannerFragment();
                return reviewBannerFragment;
            case 6:
                // settings
                KPSettingsFragment settingsFragment = new KPSettingsFragment();
                return settingsFragment;
            default:
                return new KPOverViewFragment();
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {

        navHeader = navigationView.getHeaderView(0);
        lbUserName = (TextView) navHeader.findViewById(R.id.nav_header_user_name);
        imgUserProfile = (CircleImageView) navHeader.findViewById(R.id.nav_header_profile);

        updateNavigationHeader();

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_my:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MY_PLACE;
                        break;
                    case R.id.nav_radar:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_RADAR;
                        break;
                    case R.id.nav_banners:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BANNERS;
                        break;
                    case R.id.nav_reviews:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_REVIEWS;
                        break;
                    case R.id.nav_review_banners:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_REVIEW_BANNERS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_subscription:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_SUBSCRIPTION;
                        purchaseSubscription();
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_LOGOUT;
                        logout();
                        return true;
                    default:
                        navItemIndex = 0;
                        break;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });
    }

    public void updateNavigationHeader() {
        if (user.currentUser.getUserName().isEmpty() && user.currentUser.getUserProfile().isEmpty()) {
            FirebaseDatabaseManager.getInstance().loadUserProfile(user.currentUser.getUserID(), new OnLoadUserProfileListener() {
                @Override
                public void onAddedUser(KPUser user) {
                    initNavigationHeader();
                }
            });
        } else {
            initNavigationHeader();
        }
    }

    public void initNavigationHeader() {
        String userName = user.currentUser.getUserName();
        if (userName.length() > 0) {
            lbUserName.setText("Hello " + userName);
        } else {
            lbUserName.setText("Hello");
        }

        String userProfile = user.currentUser.getUserProfile();
        if (userProfile.length() > 0) {
            Picasso.get()
                    .load(userProfile)
                    .placeholder(R.drawable.icon_user)
                    .error(R.drawable.img_example_cell_place)
                    .into(imgUserProfile);
        } else {
            imgUserProfile.setImageResource(R.drawable.icon_user);
        }
    }

    public void setNavMenuItemThemeColors(int color){
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor = Color.parseColor("#FFFFFF");
        int navDefaultIconColor = Color.parseColor("#FFFFFF");

        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );

        //Defining ColorStateList for menu item Icon
        ColorStateList navMenuIconList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor
                }
        );

        navigationView.setItemTextColor(navMenuTextList);
        navigationView.setItemIconTintList(navMenuIconList);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    public void controlDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            //set the statusbarcolor transparent to remove the black shadow
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void hideItem(int id) {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(id).setVisible(false);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        user.unsetUser();
        Intent intent = new Intent(this, KPLoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * In app subscription Methods
     */
    private void setupIAB() {
        mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    Toast.makeText(KPHomeActivity.this, "Billing Success", Toast.LENGTH_LONG).show();
                    purchasedApp();
                } else {
                    unPurchasedApp();
                }
            }
        }).build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    checkIAB();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void checkIAB() {
        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,
                new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(int responseCode, List<com.android.billingclient.api.Purchase> purchasesList) {
                        if (responseCode == BillingClient.BillingResponse.OK && purchasesList != null && purchasesList.size() > 0) {
                            purchasedApp();
                        } else {
                            unPurchasedApp();
                        }
                    }
                });
    }

    private void purchaseSubscription() {
        BillingFlowParams.Builder builder = BillingFlowParams.newBuilder()
                .setSku(ITEM_SKU).setType(BillingClient.SkuType.SUBS);
        int responseCode = mBillingClient.launchBillingFlow(this, builder.build());
    }

    private void unPurchasedApp()   {
        user.setPurchased(this, false);
        hideItem(R.id.nav_banners);
        updateBanner(false);
    }

    private void purchasedApp() {
        user.setPurchased(this, true);
        hideItem(R.id.nav_subscription);
        updateBanner(true);
    }

    /**
     * Update Banners
     */
    private void updateBanner(final boolean isPurchased) {
        FirebaseDatabaseManager.getInstance().loadBannersForUser(new OnLoadBannerListener() {
            @Override
            public void onAddedBanner(KPBanner banner) {
                KPBannerUtill.getInstance().updateVisiblityForBanner(banner, isPurchased);
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
    }

    private void checkAdmin() {
        if (!user.currentUser.getUserID().equals(Constants.ADMIN_USER_ID)) {
            hideItem(R.id.nav_reviews);
            hideItem(R.id.nav_review_banners);
        }
    }

    private void startBackgroundService() {
        serviceIntent = new Intent(KPHomeActivity.this, BackgroundService.class);
        startService(serviceIntent);
    }

    private void showTestLog() {
        Toast.makeText(this, "This is Test debug text", Toast.LENGTH_LONG).show();
    }
}