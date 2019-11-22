package p.vikpo.chatapp.interactors.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import p.vikpo.chatapp.interactors.FirebaseStorageInteractor;

/**
 * ViewModel for storing the bitmaps for the avatars. Loads them before a chatroom is opened.
 */
public class AvatarViewModel extends ViewModel
{
    private MutableLiveData<HashMap<String, Bitmap>> avatarMap;
    private FirebaseStorageInteractor interactor;
    private static final String TAG = "ChatApp - AvatarViewModel";
    private static final String IMAGE_PATH_USER = "user/";

    /**
     * Instantiating the LiveData object storing the hashmap containing a key of the userId
     * and the value of the given bitmap.
     * Possibly not a good idea to store in memory except if the app remains this small.
     * @param userId userId for the currently requested avatar bitmap
     * @return a LiveData object containing a HashMap with key userId and value Bitmap
     */
    public LiveData<HashMap<String, Bitmap>> getMap(String userId)
    {
        interactor = new FirebaseStorageInteractor();
        if(avatarMap == null)
        {
            avatarMap = new MutableLiveData<>();
            avatarMap.setValue(new HashMap<>());
            downloadUserBitmap(userId);
        }

        if(!avatarMap.getValue().containsKey(userId))
        {
            downloadUserBitmap(userId);
        }

        return avatarMap;
    }

    /**
     * Driver function for accessing the download function for downloading a specific bitmap
     * @param userId userId for the currently requested avatar bitmap
     */
    public void addUser(String userId)
    {
        if(avatarMap == null)
        {
            getMap(userId);
        }

        if(!avatarMap.getValue().containsKey(userId))
        {
            downloadUserBitmap(userId);
        }
    }

    /**
     * Functions that downloads the given AvatarBitmap from the Firestore-storage. Adds the image to
     * the ViewModel-HashMap.
     * @param userId userId for the currently requested avatar bitmap
     */
    private void downloadUserBitmap(String userId)
    {
        if(avatarMap != null)
        {
            if(!avatarMap.getValue().containsKey(userId))
            {
                interactor.getImage(IMAGE_PATH_USER + userId, result ->
                        avatarMap.getValue().put(userId, result));
            }
        }
    }
}
