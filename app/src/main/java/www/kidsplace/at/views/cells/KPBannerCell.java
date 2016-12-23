package www.kidsplace.at.views.cells;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPAddBannerActivity;
import www.kidsplace.at.activities.KPReviewBannerActivity;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.utils.Constants;

/**
 * Created by admin on 3/19/2018.
 */

public class KPBannerCell extends RecyclerView.ViewHolder {

    @BindView(R.id.cell_banner_title)
    TextView lbTitle;
    @BindView(R.id.cell_banner_status)
    TextView lbStatus;

    KPBanner currentBanner;
    Context context;
    String optiion;

    public KPBannerCell(View itemView, Context mContext, String mOption) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        optiion = mOption;

        context = mContext;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optiion.equals(Constants.BANNER_EDIT_OPTION)) {
                    Intent intent = new Intent(context, KPAddBannerActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("banner", currentBanner);
                    context.startActivity(intent);
                } else if (optiion.equals(Constants.BANNER_REVIEW_OPTION)) {
                    Intent intent = new Intent(context, KPReviewBannerActivity.class);
                    intent.putExtra("banner", currentBanner);
                    context.startActivity(intent);
                }
            }
        });
    }

    public void updateTitle(KPBanner banner) {
        lbTitle.setText(banner.getURL());
        currentBanner = banner;

        String status = "";
        switch (banner.getStatus()) {
            case Constants.PLACE_STATUS_REVIEW:
                status = "Waiting for review";
                break;
            case Constants.PLACE_STATUS_PUBLISHED:
                status = "Published";
                break;
            case Constants.PLACE_STATUS_UNPUBLISHED:
                status = "UnPublished";
                break;
            default:
                status = "Waiting for review";
                break;
        }

        lbStatus.setText(status);
    }
}
