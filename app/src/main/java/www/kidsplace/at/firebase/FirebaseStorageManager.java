package www.kidsplace.at.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import www.kidsplace.at.interfaces.OnStorageCompleteListener;

/**
 * Created by admin on 3/21/2018.
 */

public class FirebaseStorageManager {
    static FirebaseStorageManager storageManager;

    private StorageReference mStorageRef;

    public static FirebaseStorageManager getInstance() {
        if (storageManager == null) {
            storageManager = new FirebaseStorageManager();
        }

        return storageManager;
    }

    public FirebaseStorageManager() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void uploadImages(byte[] imgData, String uploadPath, final OnStorageCompleteListener storageCompleteListener) {
        StorageReference mountainsRef = mStorageRef.child(uploadPath);
        UploadTask uploadTask = mountainsRef.putBytes(imgData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                storageCompleteListener.onUploadImageFail(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                storageCompleteListener.onUploadImageSuccess(downloadUrl.toString());
            }
        });
    }
}
