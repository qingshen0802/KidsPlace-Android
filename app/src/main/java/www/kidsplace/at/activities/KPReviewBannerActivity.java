package www.kidsplace.at.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.R;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.utils.Constants;

public class KPReviewBannerActivity extends KPBaseActivity {

    @BindView(R.id.activity_review_banner_img_thumb)
    ImageView thumbView;
    @BindView(R.id.activity_review_banner_url)
    TextView lbURL;

    KPBanner currentBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_banner);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        currentBanner = (KPBanner) getIntent().getExtras().get("banner");

        Picasso.get()
                .load(currentBanner.getImageURL())
                .placeholder(R.drawable.img_example_cell_place)
                .error(R.drawable.img_example_cell_place)
                .into(thumbView);

        lbURL.setText(currentBanner.getURL());
    }

    @OnClick(R.id.activity_review_banner_bt_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.activity_review_banner_bt_accept)
    public void onAccept() {
        currentBanner.setStatus(Constants.PLACE_STATUS_PUBLISHED);
        FirebaseDatabaseManager.getInstance().updateBanner(currentBanner);
        finish();
    }

    @OnClick(R.id.activity_review_banner_bt_reject)
    public void onReject() {
        currentBanner.setStatus(Constants.PLACE_STATUS_UNPUBLISHED);
        FirebaseDatabaseManager.getInstance().updateBanner(currentBanner);
        finish();
    }
}
