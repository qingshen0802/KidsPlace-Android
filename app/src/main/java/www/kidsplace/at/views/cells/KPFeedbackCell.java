package www.kidsplace.at.views.cells;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.kidsplace.at.R;
import www.kidsplace.at.models.KPFeedback;

public class KPFeedbackCell extends RecyclerView.ViewHolder {

    @BindView(R.id.cell_feedback_comment)
    TextView lbFeedback;
    @BindView(R.id.cell_feedback_rating)
    RatingBar btRatingBar;

    public KPFeedbackCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateTitle(KPFeedback feedback) {

    }
}
