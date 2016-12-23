package www.kidsplace.at.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import www.kidsplace.at.R;
import www.kidsplace.at.activities.KPHomeActivity;
import www.kidsplace.at.firebase.FirebaseDatabaseManager;
import www.kidsplace.at.firebase.FirebaseStorageManager;
import www.kidsplace.at.interfaces.KPImageDialogListener;
import www.kidsplace.at.interfaces.OnStorageCompleteListener;
import www.kidsplace.at.models.KidsPlaceUser;
import www.kidsplace.at.utils.Constants;
import www.kidsplace.at.utils.UtilsMethods;
import www.kidsplace.at.views.diloags.KPImagePickerDialog;
import www.kidsplace.at.views.diloags.KPProgressDialog;

/**
 * Created by admin on 3/11/2018.
 */

public class KPSettingsFragment extends Fragment {

    @BindView(R.id.activity_settings_radius)
    EditText etRound;
    @BindView(R.id.seek1)
    SeekBar sbRound;
    @BindView(R.id.activity_settings_profile)
    CircleImageView imgProfile;
    @BindView(R.id.activity_settings_et_username)
    EditText etUserName;

    KidsPlaceUser user;
    Bitmap currentBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_settings, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        user = KidsPlaceUser.sharedUser(getContext());
        etRound.setText(KidsPlaceUser.sharedUser(getContext()).getRounds() + "");
        sbRound.setProgress(KidsPlaceUser.sharedUser(getContext()).getRounds());
        sbRound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                etRound.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        loadProfile();
    }

    private void loadProfile() {
        String userName = user.currentUser.getUserName();
        if (userName.length() > 0) {
            etUserName.setText(userName);
        }

        String userProfile = user.currentUser.getUserProfile();
        if (userProfile.length() > 0) {
            Picasso.get()
                    .load(userProfile)
                    .placeholder(R.drawable.icon_user)
                    .error(R.drawable.img_example_cell_place)
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.icon_user);
        }
    }

    @OnClick(R.id.activity_settings_bt_menu)
    public void onNavMenu() {
        ((KPHomeActivity)getActivity()).controlDrawer();
    }

    @OnClick(R.id.activity_settings_bt_save)
    public void onSave() {
        user.setRounds(Integer.parseInt(etRound.getText().toString()));
        user.currentUser.setUserName(etUserName.getText().toString());
        final KPProgressDialog dialog = KPProgressDialog.getInstance(getContext(), R.style.SavingProgress);
        dialog.show();
        if (currentBitmap == null) {
            FirebaseDatabaseManager.getInstance().saveUserProfile(user.currentUser);
            dialog.dismiss();
            saveUser();
            ((KPHomeActivity)getActivity()).updateNavigationHeader();
        } else {
            String filePath = "users/" + user.currentUser.getUserID() + "_" + System.currentTimeMillis() + ".jpg";
            FirebaseStorageManager.getInstance().uploadImages(UtilsMethods.convertBitmapToByteArray(currentBitmap), filePath, new OnStorageCompleteListener() {
                @Override
                public void onUploadImageSuccess(String imageURL) {
                    Log.d("imageURL", imageURL);
                    user.currentUser.setUserProfile(imageURL);
                    FirebaseDatabaseManager.getInstance().saveUserProfile(user.currentUser);
                    dialog.dismiss();
                    saveUser();
                    ((KPHomeActivity)getActivity()).updateNavigationHeader();
                }

                @Override
                public void onUploadImageFail(String errMessage) {
                    Log.d("imageURL", errMessage);
                    dialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.activity_settings_profile)
    public void onTakePhoto() {
        KPImagePickerDialog.generateImagePickerDialog(getContext(), new KPImageDialogListener() {
            @Override
            public void onSelectDialog(int status) {
                if (status == Constants.IMAGE_DIALOG_FROM_GALLERY) {
                    getImageFromGallery();
                } else {
                    getImageFromCamera();
                }
            }
        }).show();
    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.IMAGE_DIALOG_FROM_CAMERA);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.IMAGE_DIALOG_FROM_GALLERY);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = UtilsMethods.getBitmapFromIntentWithGallery(data);
        currentBitmap = bm;
        imgProfile.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bm = UtilsMethods.getBitmapFromIntentWithCameray(data);
        currentBitmap = bm;

        imgProfile.setImageBitmap(bm);
        String filePath = UtilsMethods.saveImage(bm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.IMAGE_DIALOG_FROM_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.IMAGE_DIALOG_FROM_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void saveUser() {
        FirebaseDatabaseManager.getInstance().saveUserProfile(user.currentUser);
        user.saveUser();
    }
}
