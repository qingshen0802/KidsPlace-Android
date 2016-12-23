package www.kidsplace.at.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import www.kidsplace.at.R;
import www.kidsplace.at.models.KPFeedback;
import www.kidsplace.at.views.cells.KPBannerCell;
import www.kidsplace.at.views.cells.KPFeedbackCell;

public class KPFeedbacksAdapter extends BaseAdapter {

    List<KPFeedback> feedbacks;
    Context context;

    public KPFeedbacksAdapter(Context context, List<KPFeedback> data)    {
        feedbacks = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return feedbacks.size();
    }

    @Override
    public Object getItem(int position) {
        return feedbacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KPFeedback feedback = feedbacks.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.cell_feedback, parent, false);
        TextView lbFeedback = (TextView) convertView.findViewById(R.id.cell_feedback_comment);
        RatingBar btRatingBar = (RatingBar) convertView.findViewById(R.id.cell_feedback_rating);
        lbFeedback.setText(feedback.getComment());
        btRatingBar.setRating(feedback.getRating());

        return convertView;
    }
}
