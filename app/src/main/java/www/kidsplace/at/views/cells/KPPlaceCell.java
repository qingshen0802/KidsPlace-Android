package www.kidsplace.at.views.cells;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPAddPlaceActivity;
import www.kidsplace.at.activities.KPPlaceDetailActivity;
import www.kidsplace.at.activities.KPReviewDetailActivity;
import www.kidsplace.at.models.KPFeedback;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.UtilsMethods;

/**
 * Created by admin on 3/16/2018.
 */

public class KPPlaceCell extends RecyclerView.ViewHolder {

    @BindView(R.id.cell_place_logo)
    CircleImageView btmThumbnail;
    @BindView(R.id.cell_place_address)
    TextView lbAddress;
    @BindView(R.id.cell_place_rating)
    RatingBar rtRating;
    @BindView(R.id.cell_place_status_container)
    LinearLayout vStatus;
    @BindView(R.id.cell_place_status)
    TextView lbStatus;

    Context context;
    KPPlace currentPlace;

    String option;

    public KPPlaceCell(View itemView, Context mContext, String mOption) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = mContext;
        option = mOption;

        if (option.equals(Constants.PLACE_UPDATE_OPTION)) {
            vStatus.setVisibility(View.VISIBLE);
        } else {
            vStatus.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (option.equals(Constants.PLACE_UPDATE_OPTION)) {
                    Intent intent = new Intent(context, KPAddPlaceActivity.class);
                    intent.putExtra("type", Constants.PLACE_UPDATE_OPTION);
                    intent.putExtra("place", currentPlace);
                    context.startActivity(intent);
                } else if (option.equals(Constants.PLACE_REVIEW_OPTION)){
                    Intent intent = new Intent(context, KPReviewDetailActivity.class);
                    intent.putExtra("place", currentPlace);
                    context.startActivity(intent);
                } else if (option.equals(Constants.PLACE_VIEW_OPTION)) {
                    Intent intent = new Intent(context, KPPlaceDetailActivity.class);
                    intent.putExtra("place", currentPlace);
                    context.startActivity(intent);
                }
            }
        });
    }

    public void updateUI(KPPlace place) {
        Picasso.get()
                .load(place.getImageURL())
                .placeholder(R.drawable.img_example_cell_place)
                .error(R.drawable.img_example_cell_place)
                .into(btmThumbnail);
        String address = place.getAddress();
        lbAddress.setText(address);
        if (place.getFeedbacks().size() > 0) {
            float rating;
            float sum = 0;
            for (KPFeedback feedback : place.getFeedbacks()) {
                sum += feedback.getRating();
            }
            rating = sum / place.getFeedbacks().size();
            rtRating.setRating(rating);
        } else {
            rtRating.setRating(0);
        }
        currentPlace = place;

        if (option.equals(Constants.PLACE_UPDATE_OPTION)) {
            String status = "";
            switch (place.getStatus()) {
                case Constants.PLACE_STATUS_REVIEW:
                    status = "Waiting for review";
                    break;
                case Constants.PLACE_STATUS_PUBLISHED:
                    status = "Published";
                    break;
                case Constants.PLACE_STATUS_UNPUBLISHED:
                    status = "Rejected";
                    break;
                default:
                    status = "Waiting for review";
                    break;
            }

            lbStatus.setText(status);
        }
    }

    @OnClick(R.id.cell_place_location_bt)
    public void onLoadDetailView() {
        Location location = UtilsMethods.getLocationFromString(currentPlace.getLocation());
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(%s)", location.getLatitude(), location.getLongitude(), location.getLatitude(), location.getLongitude(), currentPlace.getDescription());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }
}