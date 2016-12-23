package www.kidsplace.at.interfaces;

/**
 * Created by admin on 3/21/2018.
 */

public interface OnStorageCompleteListener {
    public abstract void onUploadImageSuccess(String imageURL);
    public abstract void onUploadImageFail(String errMessage);
}
