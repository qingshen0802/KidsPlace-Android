package www.kidsplace.at.utils;

import java.util.ArrayList;

import www.kidsplace.at.models.KPPlace;

/**
 * Created by admin on 3/23/2018.
 */

public class KPPlaceUtill {

    static KPPlaceUtill placeUtill;

    public static KPPlaceUtill getInstance() {
        if (placeUtill == null) {
            placeUtill = new KPPlaceUtill();
        }

        return placeUtill;
    }

    public void copyPlace(KPPlace place1, KPPlace place2) {
        place1.setImageURL(place2.getImageURL());
        place1.setAddress(place2.getAddress());
        place1.setCategory(place2.getCategory());
        place1.setDescription(place2.getDescription());
        place1.setLocation(place2.getLocation());
        place1.setFeedbacks(place2.getFeedbacks());
    }

    public void replacePlaceInList(ArrayList<KPPlace> list, KPPlace place) {
        int index = indexOf(list, place);
        copyPlace(list.get(index), place);
    }

    public void removePlaceInList(ArrayList<KPPlace> list, KPPlace place) {
        int index = indexOf(list, place);
        if (index != -1)
            list.remove(index);
    }

    public int indexOf(ArrayList<KPPlace> list, KPPlace place) {
        int index = 0;
        boolean isContains = false;
        for (KPPlace p : list) {
            if (p.getPlaceID().equals(place.getPlaceID())) {
                isContains = true;
                break;
            }

            index ++;
        }

        if (isContains)
            return index;
        else
            return -1;
    }
}
