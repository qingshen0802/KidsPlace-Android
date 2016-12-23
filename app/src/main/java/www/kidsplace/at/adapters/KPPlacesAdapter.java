package www.kidsplace.at.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import www.kidsplace.at.R;
import www.kidsplace.at.models.KPPlace;
import www.kidsplace.at.views.cells.KPPlaceCell;

/**
 * Created by admin on 3/16/2018.
 */

public class KPPlacesAdapter extends RecyclerView.Adapter<KPPlaceCell> {

    List<KPPlace> places;
    Context context;
    String option;

    public KPPlacesAdapter(Context mContext, List<KPPlace> data, String mOption) {
        context = mContext;
        places = data;
        option = mOption;
    }

    @Override
    public KPPlaceCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_place, parent, false);
        return new KPPlaceCell(view, context, option);
    }

    @Override
    public void onBindViewHolder(KPPlaceCell holder, int position) {
        holder.updateUI(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void addAll(List<KPPlace> lista){
        places.addAll(lista);
        notifyDataSetChanged();
    }

    /**
     Permite limpiar todos los elementos del recycler
     **/

    public void clear(){
        places.clear();
        notifyDataSetChanged();
    }
}
