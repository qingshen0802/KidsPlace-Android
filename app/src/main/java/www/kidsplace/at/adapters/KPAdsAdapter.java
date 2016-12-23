package www.kidsplace.at.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import www.kidsplace.at.R;
import www.kidsplace.at.models.KPBanner;

public class KPAdsAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<KPBanner> data;

    public KPAdsAdapter(Context context, List<KPBanner> banners) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = banners;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.page_ads, container, false);
        ImageView thumb = itemView.findViewById(R.id.ads);
        final KPBanner banner = data.get(position);
        Picasso.get()
                .load(banner.getImageURL())
                .placeholder(R.drawable.img_example_cell_place)
                .error(R.drawable.img_example_cell_place)
                .into(thumb);
        container.addView(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = banner.getURL();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
