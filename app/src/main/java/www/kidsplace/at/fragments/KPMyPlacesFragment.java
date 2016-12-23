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
import www.kidsplace.at.activities.KPAddPlaceActivity;
import www.kidsplace.at.activities.KPHomeActivity;
import www.kidsplace.at.adapters.KPPlacesAdapter;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.interfaces.OnLoadPlaceListener;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.KPPlaceUtill;
import www.kidsplace.at.views.diloags.KPProgressDialog;

public class KPMyPlacesFragment extends Fragment {

    @BindView(R.id.activity_my_places_list)
    RecyclerView list;

    KPProgressDialog dialog;

    ArrayList<KPPlace> places;
    KPPlacesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_my_places, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initValues();
        setupList();
        loadData();
    }

    private void initValues() {
        places = new ArrayList<>();
        dialog = KPProgressDialog.getInstance(getContext(), R.style.LoadingProgress);
    }

    private void setupList() {
        if (list.getAdapter() != null) {
            adapter.clear();
            adapter.addAll(places);
        } else {
            adapter = new KPPlacesAdapter(getContext(), places, Constants.PLACE_UPDATE_OPTION);
            list.setAdapter(adapter);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void loadData() {
        FirebaseDatabaseManager.getInstance().loadPlacesForUser(new OnLoadPlaceListener() {
            @Override
            public void onAddedPlace(KPPlace place) {
                places.add(place);
                adapter.notifyItemInserted(adapter.getItemCount());
            }

            @Override
            public void onUpdatedPlace(KPPlace place) {
                int index = KPPlaceUtill.getInstance().indexOf(places, place);
                if (index > -1) {
                    KPPlaceUtill.getInstance().replacePlaceInList(places, place);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onRemovedPlace(KPPlace place) {
                int index = KPPlaceUtill.getInstance().indexOf(places, place);
                if (index > -1) {
                    places.remove(index);
                    adapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onMovedPlace(KPPlace place) {

            }
        });
    }

    @OnClick(R.id.activity_my_places_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity)getActivity()).controlDrawer();
    }

    @OnClick(R.id.activity_my_places_bt_create)
    public void onCreateBanner() {
        Intent intent = new Intent(getActivity(), KPAddPlaceActivity.class);
        intent.putExtra("type", "add");
        getActivity().startActivity(intent);
    }
}
