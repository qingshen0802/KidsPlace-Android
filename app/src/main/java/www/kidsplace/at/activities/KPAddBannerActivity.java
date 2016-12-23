package www.kidsplace.at.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.R;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.firebase.FirebaseStorageManager;
import www.kidsplace.at.interfaces.KPImageDialogListener;
import www.kidsplace.at.interfaces.KPLocationCatchListener;
import www.kidsplace.at.interfaces.OnStorageCompleteListener;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPLocationManager;
import www.kidsplace.at.utils.SerializeObject;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.diloags.KPImagePickerDialog;
import www.kidsplace.at.views.diloags.KPProgressDialog;

/**
 * Created by admin on 3/10/2018.
 */

public class KPAddBannerActivity extends KPBaseActivity {

    @BindView(R.id.activity_create_banner_title)
    TextView lbTitle;
    @BindView(R.id.add_banner_bt_title)
    TextView lbBtTitle;
    @BindView(R.id.add_banner_bt_camera)
    ImageView btCamera;
    @BindView(R.id.add_banner_thumb)
    ImageView imgThumbnail;
    @BindView(R.id.add_banner_et_url)
    EditText etURL;

    Bitmap currentBitmap;
    KPBanner currentBanner;
    Location currentLocation;

    private static final int REQUEST_ENABLE_GPS = 2000;

    boolean isUpdate;

    KidsPlaceUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banner);
        ButterKnife.bind(this);
        user = KidsPlaceUser.sharedUser(this);
        loadData();
        onLoadLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void loadData() {
        if (!isNew()) {
            currentBanner = (KPBanner) getIntent().getExtras().get("banner");
            updateUIForUpdate();
            isUpdate = true;
        } else {
            currentBanner = new KPBanner();
            updateUIForNew();
            isUpdate = false;
        }
    }

    private boolean isNew() {
        String type = getIntent().getExtras().getString("type");
        if (type.equals("update"))
            return false;
        else
            return true;
    }

    private void updateUIForUpdate() {
        lbTitle.setText(R.string.activity_edit_banner_title);
        lbBtTitle.setText(R.string.activity_edit_banner_bt_title);

        Picasso.get()
                .load(currentBanner.getImageURL())
                .placeholder(R.drawable.img_example_cell_place)
                .error(R.drawable.img_example_cell_place)
                .into(imgThumbnail);
        btCamera.setVisibility(View.GONE);

        etURL.setText(currentBanner.getURL());
    }

    private void updateUIForNew() {
        lbTitle.setText(R.string.activity_add_banner_title);
        lbBtTitle.setText(R.string.activity_add_place_bt_create);
    }

    @OnClick(R.id.add_banner_bt_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.add_banner_bt_create)
    public void onCreate() {
        String url = etURL.getText().toString();
        if (url.isEmpty()) {
            etURL.setError(getResources().getString(R.string.activity_add_banner_url_empty_message));
            return;
        }
        if (!UtilsMethods.checkURLValid(url)) {
            etURL.setError(getResources().getString(R.string.activity_add_banner_url_invalid_message));
            return;
        }
        currentBanner.setURL(url);
        currentBanner.setVisible(1);
        currentBanner.setStatus(Constants.PLACE_STATUS_REVIEW);
        currentBanner.setUserID(user.currentUser.getUserID());

        if (currentLocation != null) {
            currentBanner.setLocation(UtilsMethods.getStringFromLocation(currentLocation));
            currentBanner.setAddress(UtilsMethods.getAddressFromLocation(this, currentLocation));
        }
        createBanner();
    }

    @OnClick(R.id.add_banner_bt_camera)
    public void onTakePhoto() {
        KPImagePickerDialog.generateImagePickerDialog(this, new KPImageDialogListener() {
            @Override
            public void onSelectDialog(int status) {
                if (status == Constants.IMAGE_DIALOG_FROM_GALLERY) {
                    getImageFromGallery();
                } else {
                    getImageFromCamera();
                }
            }
        }).show();
    }

    @OnClick(R.id.add_banner_thumb)
    public void onUpdatePhoto() {
        KPImagePickerDialog.generateImagePickerDialog(this, new KPImageDialogListener() {
            @Override
            public void onSelectDialog(int status) {
                if (status == Constants.IMAGE_DIALOG_FROM_GALLERY) {
                    getImageFromGallery();
                } else {
                    getImageFromCamera();
                }
            }
        }).show();
    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.IMAGE_DIALOG_FROM_CAMERA);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.IMAGE_DIALOG_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.IMAGE_DIALOG_FROM_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.IMAGE_DIALOG_FROM_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == REQUEST_ENABLE_GPS) {
                onLoadLocation();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = UtilsMethods.getBitmapFromIntentWithGallery(data);
        currentBitmap = bm;
        if (bm != null) {
            btCamera.setVisibility(View.GONE);
        }
        imgThumbnail.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bm = UtilsMethods.getBitmapFromIntentWithCameray(data);
        currentBitmap = bm;
        if (bm != null) {
            btCamera.setVisibility(View.GONE);
        }
        imgThumbnail.setImageBitmap(bm);
        String filePath = UtilsMethods.saveImage(bm);
    }

    private void createBanner() {
        final KPProgressDialog dialog;
        if (isUpdate) {
            dialog = KPProgressDialog.getInstance(this, R.style.UpdatingProgress);
        } else {
            dialog = KPProgressDialog.getInstance(this, R.style.CreatingProgress);
        }

        String filePath = "banners/" + user.currentUser.getUserID() + "_" + System.currentTimeMillis() + ".jpg";
        dialog.show();
        if (currentBitmap == null) {
            if (isUpdate)
                FirebaseDatabaseManager.getInstance().updateBanner(currentBanner);
            else
                FirebaseDatabaseManager.getInstance().addBanner(currentBanner);
            dialog.dismiss();
            showMessageForReview();
        } else {
            FirebaseStorageManager.getInstance().uploadImages(UtilsMethods.convertBitmapToByteArray(currentBitmap), filePath, new OnStorageCompleteListener() {
                @Override
                public void onUploadImageSuccess(String imageURL) {
                    Log.d("imageURL", imageURL);
                    currentBanner.setImageURL(imageURL);
                    if (isUpdate)
                        FirebaseDatabaseManager.getInstance().updateBanner(currentBanner);
                    else
                        FirebaseDatabaseManager.getInstance().addBanner(currentBanner);
                    dialog.dismiss();
                    showMessageForReview();
                }

                @Override
                public void onUploadImageFail(String errMessage) {
                    Log.d("imageURL", errMessage);
                    dialog.dismiss();
                }
            });
        }
    }

    private void showMessageForReview() {
        ArrayList<KPBanner> places = loadResources();
        places.add(currentBanner);
        saveResources(places);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("This banner will be reviewed by Admin");
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void saveResources(ArrayList<KPBanner> resources)    {
        String ser = SerializeObject.objectToString(resources);
        if (ser != null && !ser.equalsIgnoreCase("")) {
            SerializeObject.WriteSettings(this, ser, "newBanner.dat");
        } else {
            SerializeObject.WriteSettings(this, "", "newBanner.dat");
        }
    }

    private ArrayList<KPBanner> loadResources()    {
        ArrayList<KPBanner> userList = new ArrayList<>();

        String ser = SerializeObject.ReadSettings(this, "newBanner.dat");
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

    private void onLoadLocation(){
        KPLocationManager.sharedLocationManager(this, new KPLocationCatchListener() {
            @Override
            public void catchLocationFailed(String errMessage) {
                Log.d("location status", errMessage);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(KPAddBannerActivity.this);
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
            }
        }).searchCurrentLocation();
    }
}