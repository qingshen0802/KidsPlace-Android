package www.kidsplace.at.views.diloags;

import android.app.AlertDialog;
import android.content.Context;

import dmax.dialog.SpotsDialog;

/**
 * Created by admin on 3/27/2018.
 */

public class KPProgressDialog {

    AlertDialog dialog;
    public static KPProgressDialog getInstance(Context context, int style) {
        KPProgressDialog dialog = new KPProgressDialog(context, style);
        return dialog;
    }

    public KPProgressDialog(Context context, int style) {
        dialog = new SpotsDialog(context, style);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
