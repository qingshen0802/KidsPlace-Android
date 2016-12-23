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
import www.kidsplace.at.interfaces.KPImageDialogListener;
import www.kidsplace.at.interfaces.KPLocationCatchListener;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPLocationManager;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.diloags.KPImagePickerDialog;

public class KPAddPlaceActivity extends KPBaseActivity {

    @BindView(R.id.add_place_bt_camera)
    ImageView btCamera;
    @BindView(R.id.add_place_img)
    ImageView imgPotfolio;
    @BindView(R.id.add_place_bt_slide)
    ImageView btCheckSlide;
    @BindView(R.id.add_place_bt_sandpit)
    ImageView btCheckSandpit;
    @BindView(R.id.add_place_et_description)
    EditText etDescription;
    @BindView(R.id.add_place_tv_latitude)
    TextView lbLatitude;
    @BindView(R.id.add_place_tv_longitude)
    TextView lbLongitude;
    @BindView(R.id.add_place_lb_create_title)
    TextView btCreateTitle;

    Location currentLocation;
    ArrayList<String> categories;
    Bitmap currentBitmap;

    KPPlace currentPlace;
    boolean isUpdate;

    private final static int REVIEW_PLACE = 100;
    private static final int REQUEST_ENABLE_GPS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        ButterKnife.bind(this);

        initValues();
        upDateCategory();
        updateLocationLabel();
        checkType();
        onLoadLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @OnClick(R.id.add_place_bt_camera)
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

    @OnClick(R.id.add_place_img)
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

    @OnClick(R.id.activity_add_place_bt_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.add_place_bt_create)
    public void onCreatePlace() {
        String description = etDescription.getText().toString();

        if (description.isEmpty()) {
            etDescription.setError(getResources().getString(R.string.activity_add_place_description_empty));
            return;
        }

        currentPlace.setDescription(description);

        String categoryStr = UtilsMethods.convertArrayToString(categories);
        currentPlace.setCategory(categoryStr);

        if (currentLocation != null) {
            currentPlace.setLocation(UtilsMethods.getStringFromLocation(currentLocation));
            String address = UtilsMethods.getAddressFromLocation(this, currentLocation);
            currentPlace.setAddress(address);
        }

        currentPlace.setUserID(KidsPlaceUser.sharedUser(this).currentUser.getUserID());

        if (currentBitmap == null) {
            currentPlace.setImageURL(currentPlace.getImageURL());
        }

        Intent intent = new Intent(KPAddPlaceActivity.this, KPPlaceReviewActivity.class);
        intent.putExtra("place", currentPlace);
        intent.putExtra("thumbnail", UtilsMethods.convertBitmapToByteArray(currentBitmap));
        intent.putExtra("isUpdate", isUpdate);
        startActivityForResult(intent, REVIEW_PLACE);
    }

    @OnClick(R.id.add_place_bt_slide)
    public void onSelectSlide() {
        btCheckSlide.setSelected(!btCheckSlide.isSelected());
        upDateCategory();
    }

    @OnClick(R.id.add_place_bt_sandpit)
    public void onSelectSandpit() {
        btCheckSandpit.setSelected(!btCheckSandpit.isSelected());
        upDateCategory();
    }

    private void checkType() {
        String type = getIntent().getExtras().getString("type");
        if (type.equals(Constants.PLACE_UPDATE_OPTION)) {
            currentPlace = (KPPlace) getIntent().getExtras().get("place");
            isUpdate = true;
            updateUIWithPlace();
        } else {
            isUpdate = false;
            currentPlace = new KPPlace();
        }
    }

    private void updateUIWithPlace() {
        etDescription.setText(currentPlace.getDescription());
        btCamera.setVisibility(View.GONE);
        Picasso.get()
                .load(currentPlace.getImageURL())
                .placeholder(R.drawable.img_example_cell_place)
                .error(R.drawable.img_example_cell_place)
                .into(imgPotfolio);
        String categories = currentPlace.getCategory();
        if (categories.contains(getResources().getString(R.string.category_sandpit))) {
            btCheckSandpit.setSelected(true);
            updateSandpit(true);
        }

        if (categories.contains(getResources().getString(R.string.category_slide)))  {
            btCheckSlide.setSelected(true);
            updateSlide(true);
        }

        btCreateTitle.setText(R.string.activity_edit_banner_bt_title);
    }

    private void initValues() {
        categories = new ArrayList<>();
        currentBitmap = null;
    }

    private void upDateCategory() {
        updateSlide(btCheckSlide.isSelected());
        updateSandpit(btCheckSandpit.isSelected());
    }

    private void updateSlide(boolean isSelectable) {
        if (isSelectable) {
            btCheckSlide.setImageResource(R.drawable.check);
            if (!categories.contains(getResources().getString(R.string.category_slide)))
                categories.add(getResources().getString(R.string.category_slide));
        } else {
            btCheckSlide.setImageResource(R.drawable.uncheck);
            if (categories.contains(getResources().getString(R.string.category_slide))) {
                categories.remove(getResources().getString(R.string.category_slide));
            }
        }
    }

    private void updateSandpit(boolean isSelectable) {
        if (isSelectable) {
            btCheckSandpit.setImageResource(R.drawable.check);
            if (!categories.contains(getResources().getString(R.string.category_sandpit)))
                categories.add(getResources().getString(R.string.category_sandpit));
        } else {
            btCheckSandpit.setImageResource(R.drawable.uncheck);
            if (categories.contains(getResources().getString(R.string.category_sandpit))) {
                categories.remove(getResources().getString(R.string.category_sandpit));
            }
        }
    }

    private void updateLocationLabel() {
        if (currentLocation == null) {
            lbLongitude.setText("");
            lbLatitude.setText("");
        } else {
            double longitude = currentLocation.getLongitude();
            double latitued = currentLocation.getLatitude();
            lbLatitude.setText(UtilsMethods.cutDouble(latitued));
            lbLongitude.setText(UtilsMethods.cutDouble(longitude));
        }
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
            else if (requestCode == REVIEW_PLACE) {
                finish();
            } else if (requestCode == REQUEST_ENABLE_GPS) {
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
        imgPotfolio.setImageBitmap(bm);
        currentPlace.setImageURL("");
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bm = UtilsMethods.getBitmapFromIntentWithCameray(data);
        currentBitmap = bm;
        if (bm != null) {
            btCamera.setVisibility(View.GONE);
        }
        imgPotfolio.setImageBitmap(bm);
        String filePath = UtilsMethods.saveImage(bm);
        currentPlace.setImageURL("");
    }

    private void onLoadLocation(){
        KPLocationManager.sharedLocationManager(this, new KPLocationCatchListener() {
            @Override
            public void catchLocationFailed(String errMessage) {
                Log.d("location status", errMessage);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(KPAddPlaceActivity.this);
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
                updateLocationLabel();
            }
        }).searchCurrentLocation();
    }
}
