package www.kidsplace.at.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPAddBannerActivity;
import www.kidsplace.at.activities.KPHomeActivity;
import www.kidsplace.at.adapters.KPBannersAdapter;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.interfaces.OnLoadBannerListener;
import www.kidsplace.at.models.KPBanner;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPBannerUtill;
import www.kidsplace.at.views.diloags.KPProgressDialog;

/**
 * Created by admin on 3/11/2018.
 */

public class KPBannersFragment extends Fragment {

    @BindView(R.id.activity_banners_list)
    RecyclerView list;

    KPBannersAdapter adapter;
    ArrayList<KPBanner> banners;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_banners, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initValues();
        setupList();
        loadData();
    }

    private void initValues() {
        banners = new ArrayList<>();
    }

    private void setupList() {
        if (list.getAdapter() != null) {
            adapter.clear();
            adapter.addAll(banners);
        } else {
            adapter = new KPBannersAdapter(getContext(), banners, Constants.BANNER_EDIT_OPTION);
            list.setAdapter(adapter);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @OnClick(R.id.activity_banners_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity)getActivity()).controlDrawer();
    }

    @OnClick(R.id.activity_banners_bt_create)
    public void onCreateBanner() {
        Intent intent = new Intent(getActivity(), KPAddBannerActivity.class);
        intent.putExtra("type", "add");
        getActivity().startActivity(intent);
    }

    private void loadData() {
        FirebaseDatabaseManager.getInstance().loadBannersForUser(new OnLoadBannerListener() {
            @Override
            public void onAddedBanner(KPBanner banner) {
                if (banner.getVisible() > 0) {
                    banners.add(banner);
                    adapter.notifyItemInserted(adapter.getItemCount());
                }
            }

            @Override
            public void onUpdatedBanner(KPBanner banner) {
                if (banner.getVisible() > 0) {
                    int index = KPBannerUtill.getInstance().indexOf(banners, banner);
                    KPBannerUtill.getInstance().replaceBannerInList(banners, banner);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onRemovedBanner(KPBanner banner) {
                if (banner.getVisible() > 0) {
                    int index = KPBannerUtill.getInstance().indexOf(banners, banner);
                    banners.remove(index);
                    adapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onMovedBanner(KPBanner banner) {

            }
        });
    }
}
