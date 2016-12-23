package www.kidsplace.at.activities;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import www.kidsplace.at.R;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.UtilsMethods;

public class KPReviewDetailActivity extends KPBaseActivity {

    @BindView(R.id.activity_review_detail_img_thumb)
    CircleImageView imgThumb;
    @BindView(R.id.activity_review_detail_address)
    TextView lbAddress;
    @BindView(R.id.activity_review_detail_description)
    TextView lbDescription;
    @BindView(R.id.activity_review_detail_latitude)
    TextView lbLatitude;
    @BindView(R.id.activity_review_detail_longitude)
    TextView lbLongitude;
    @BindView(R.id.activity_review_detail_category)
    TextView lbCategory;

    KPPlace currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        currentPlace = (KPPlace) getIntent().getExtras().get("place");
        updateView(currentPlace);
    }

    private void updateView(KPPlace place) {
        String imgURL = place.getImageURL();
        String address = place.getAddress();
        String description = place.getDescription();
        Location location = UtilsMethods.getLocationFromString(place.getLocation());

        Picasso.get()
                .load(imgURL)
                .placeholder(R.drawable.img_example_cell_place)
                .error(R.drawable.img_example_cell_place)
                .into(imgThumb);

        lbAddress.setText(address);
        lbDescription.setText(description);
        lbLatitude.setText(UtilsMethods.cutDouble(location.getLatitude()));
        lbLongitude.setText(UtilsMethods.cutDouble(location.getLongitude()));
        lbCategory.setText(place.getCategory());
    }

    @OnClick(R.id.activity_review_detail_bt_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.activity_review_detail_bt_reject)
    public void onReject() {
        currentPlace.setStatus(Constants.PLACE_STATUS_UNPUBLISHED);
        FirebaseDatabaseManager.getInstance().updatePlace(currentPlace);
        finish();
    }

    @OnClick(R.id.activity_review_detail_bt_accept)
    public void onAccept() {
        currentPlace.setStatus(Constants.PLACE_STATUS_PUBLISHED);
        FirebaseDatabaseManager.getInstance().updatePlace(currentPlace);
        finish();
    }
}
