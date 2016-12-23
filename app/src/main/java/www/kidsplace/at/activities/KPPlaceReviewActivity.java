package www.kidsplace.at.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import www.kidsplace.at.R;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.firebase.FirebaseStorageManager;
import www.kidsplace.at.interfaces.OnStorageCompleteListener;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.SerializeObject;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.diloags.KPProgressDialog;

/**
 * Created by admin on 3/13/2018.
 */

public class KPPlaceReviewActivity extends KPBaseActivity {

    @BindView(R.id.activity_reviewing_thumbnail)
    CircleImageView imgThumbnail;
    @BindView(R.id.activity_reviewing_address)
    TextView lbAddress;
    @BindView(R.id.activity_reviewing_latitude)
    TextView lbLatitude;
    @BindView(R.id.activity_reviewing_longitude)
    TextView lbLongitude;
    @BindView(R.id.activity_reviewing_description)
    TextView lbDescription;

    KPPlace currentPlace;
    Bitmap bmThumbnail;
    boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_review);
        ButterKnife.bind(this);

        getDataFromIntent();
        updateUIWithPlace();
    }

    private void getDataFromIntent() {
        currentPlace = (KPPlace) getIntent().getExtras().getParcelable("place");
        byte[] byteArray = getIntent().getByteArrayExtra("thumbnail");
        if (byteArray.length > 0)
            bmThumbnail = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        isUpdate = getIntent().getExtras().getBoolean("isUpdate");
    }

    private void updateUIWithPlace() {
        if (bmThumbnail != null)
            imgThumbnail.setImageBitmap(bmThumbnail);
        else if (!currentPlace.getImageURL().isEmpty()){
            Picasso.get()
                    .load(currentPlace.getImageURL())
                    .placeholder(R.drawable.img_example_cell_place)
                    .error(R.drawable.img_example_cell_place)
                    .into(imgThumbnail);
        }
        String address = "";
        if (!currentPlace.getAddress().isEmpty())
            address = currentPlace.getAddress();
        lbAddress.setText(address);
        Location location = UtilsMethods.getLocationFromString(currentPlace.getLocation());
        lbLatitude.setText(UtilsMethods.cutDouble(location.getLatitude()));
        lbLongitude.setText(UtilsMethods.cutDouble(location.getLongitude()));
        lbDescription.setText(currentPlace.getDescription());
    }

    private void success() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void fail() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @OnClick(R.id.activity_reviewing_bt_back)
    public void onBack() {
        fail();
    }

    @OnClick(R.id.activity_reviewing_bt_publish)
    public void onPublish() {
        final KPProgressDialog dialog;
        currentPlace.setStatus(Constants.PLACE_STATUS_REVIEW);
        if (isUpdate) {
            dialog = KPProgressDialog.getInstance(this, R.style.UpdatingProgress);
        } else {
            dialog = KPProgressDialog.getInstance(this, R.style.CreatingProgress);
        }
        String filePath = "places/" + KidsPlaceUser.sharedUser(this).currentUser.getUserID() + "_" + System.currentTimeMillis() + ".jpg";
        if (bmThumbnail == null) {
            if (isUpdate)
                FirebaseDatabaseManager.getInstance().updatePlaceForReview(currentPlace);
            else
                FirebaseDatabaseManager.getInstance().addPlaceForReview(currentPlace);
            dialog.dismiss();
            showMessageForReview();
        } else {
            FirebaseStorageManager.getInstance().uploadImages(UtilsMethods.convertBitmapToByteArray(bmThumbnail), filePath, new OnStorageCompleteListener() {
                @Override
                public void onUploadImageSuccess(String imageURL) {
                    Log.d("imageURL", imageURL);
                    currentPlace.setImageURL(imageURL);
                    if (isUpdate)
                        FirebaseDatabaseManager.getInstance().updatePlaceForReview(currentPlace);
                    else
                        FirebaseDatabaseManager.getInstance().addPlaceForReview(currentPlace);
                    dialog.dismiss();
                    showMessageForReview();
                }

                @Override
                public void onUploadImageFail(String errMessage) {
                    Log.d("imageURL", errMessage);
                    fail();
                    dialog.dismiss();
                }
            });
        }
    }

    private void showMessageForReview() {
        ArrayList<KPPlace> places = loadResources();
        places.add(currentPlace);
        saveResources(places);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("This place will be reviewed by Admin");
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                success();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void saveResources(ArrayList<KPPlace> resources)    {
        String ser = SerializeObject.objectToString(resources);
        if (ser != null && !ser.equalsIgnoreCase("")) {
            SerializeObject.WriteSettings(this, ser, "newPlace.dat");
        } else {
            SerializeObject.WriteSettings(this, "", "newPlace.dat");
        }
    }

    private ArrayList<KPPlace> loadResources()    {
        ArrayList<KPPlace> userList = new ArrayList<>();

        String ser = SerializeObject.ReadSettings(this, "newPlace.dat");
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
}
