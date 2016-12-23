package www.kidsplace.at.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import www.kidsplace.at.R;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.views.cells.KPBannerCell;

/**
 * Created by admin on 3/16/2018.
 */

public class KPBannersAdapter extends RecyclerView.Adapter<KPBannerCell>{

    List<KPBanner> banners;
    Context context;
    String option;

    public KPBannersAdapter(Context mContext, List<KPBanner> data, String mOption) {
        context = mContext;
        banners = data;
        option = mOption;
    }

    @Override
    public KPBannerCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_banner, parent, false);
        return new KPBannerCell(view, context, option);
    }

    @Override
    public void onBindViewHolder(KPBannerCell holder, int position) {
        holder.updateTitle(banners.get(position));
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public void addAll(List<KPBanner> data) {
        banners = new ArrayList<>();
        for (KPBanner banner : data) {
            banners.add(banner);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        banners = new ArrayList<>();
        notifyDataSetChanged();
    }
}
