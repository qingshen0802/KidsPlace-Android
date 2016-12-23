package www.kidsplace.at.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.R;
import www.kidsplace.at.adapters.KPFeedbacksAdapter;
import www.kidsplace.at.adapters.WalkthroughAdapter;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.models.KPFeedback;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.utils.UtilsMethods;

/**
 * Created by admin on 3/10/2018.
 */

public class KPPlaceDetailActivity extends KPBaseActivity {

    @BindView(R.id.activity_detail_address)
    TextView lbAddress;
    @BindView(R.id.activity_detail_comment)
    EditText etComment;
    @BindView(R.id.activity_detail_description)
    TextView lbDescription;
    @BindView(R.id.activity_detail_category)
    TextView lbCategory;
    @BindView(R.id.activity_detail_latitude)
    TextView lbLatitude;
    @BindView(R.id.activity_detail_longitude)
    TextView lbLongitude;
    @BindView(R.id.activity_detail_rating)
    RatingBar rtRating;
    @BindView(R.id.activity_detail_list)
    ListView list;
    @BindView(R.id.activity_detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.activity_detail_circles)
    CirclePageIndicator indicator;

    KPFeedbacksAdapter adapter;

    KPPlace currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        currentPlace = (KPPlace) getIntent().getExtras().get("place");
        updateView(currentPlace);
        list.setFocusable(false);
    }

    private void updateView(KPPlace place) {
        String address = place.getAddress();
        String description = place.getDescription();
        Location location = UtilsMethods.getLocationFromString(place.getLocation());

        lbAddress.setText(address);
        lbDescription.setText(description);
        lbLatitude.setText(UtilsMethods.cutDouble(location.getLatitude()));
        lbLongitude.setText(UtilsMethods.cutDouble(location.getLongitude()));
        lbCategory.setText(place.getCategory());

        setupListView(place.getFeedbacks());

        WalkthroughAdapter pagerAdapter = new WalkthroughAdapter(this, place);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);
    }

    private void setupListView(List<KPFeedback> feedbacks)    {
        if (list.getAdapter() != null)  {
            list.setAdapter(null);
        }

        adapter = new KPFeedbacksAdapter(this, feedbacks);
        list.setAdapter(adapter);
        if (feedbacks.size() > 0)
            setListViewHeightBasedOnChildren(list);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight=0;
        View view = null;

        int count_index = listAdapter.getCount();
        for (int i = 0; i < count_index; i++)
        {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new LinearLayout.LayoutParams(desiredWidth,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount())) + view.getMeasuredHeight();

        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @OnClick(R.id.activity_detail_bt_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.activity_detail_add_comment)
    public void addComment() {
        String comment = etComment.getText().toString();
        float rating = rtRating.getRating();

        KPFeedback feedback = new KPFeedback();
        feedback.setComment(comment);
        feedback.setRating(rating);

        currentPlace.getFeedbacks().add(feedback);
        FirebaseDatabaseManager.getInstance().updatePlace(currentPlace);
        adapter.notifyDataSetChanged();
    }
}
