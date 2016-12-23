package www.kidsplace.at.views.diloags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import www.kidsplace.at.interfaces.KPImageDialogListener;
import www.kidsplace.at.utils.Constants;

/**
 * Created by admin on 3/20/2018.
 */

public class KPImagePickerDialog {

    public static AlertDialog.Builder generateImagePickerDialog(final Context context, final KPImageDialogListener imageDialogListener) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    imageDialogListener.onSelectDialog(Constants.IMAGE_DIALOG_FROM_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    imageDialogListener.onSelectDialog(Constants.IMAGE_DIALOG_FROM_GALLERY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        return builder;
    }
}