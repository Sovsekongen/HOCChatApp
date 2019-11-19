package p.vikpo.chatapp.interactors.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import p.vikpo.chatapp.interactors.ImageDownloader;

/**
 * ViewModel for storing the bitmaps for the avatars. Loads them before a chatroom is opened.
 */
public class AvatarViewModel extends ViewModel
{
    private MutableLiveData<HashMap<String, Bitmap>> avatarMap;
    private static final String TAG = "ChatApp - AvatarViewModel";

    /**
     * Possibly for instansiating the LiveData object storing the hashmap containing a key of the userId
     * and the value of the given bitmap.
     * @param userId userId for the currently requested avatar bitmap
     * @param userUrl userUrl for the currently requested avatar bitmap
     * @return a LiveData object containing a HashMap with key userId and value Bitmap
     */
    public LiveData<HashMap<String, Bitmap>> getMap(String userId, String userUrl)
    {
        if(avatarMap == null)
        {
            avatarMap = new MutableLiveData<>();
            avatarMap.setValue(new HashMap<>());
            downloadUserBitmap(userId, userUrl);
        }

        if(!avatarMap.getValue().containsKey(userId))
        {
            downloadUserBitmap(userId, userUrl);
        }

        return avatarMap;
    }

    /**
     * Driver function for accessing the download function for downloading a specific bitmap
     * @param userId userId for the currently requested avatar bitmap
     * @param userUrl userUrl for the currently requested avatar bitmap
     */
    public void addUser(String userId, String userUrl)
    {
        if(avatarMap == null)
        {
            getMap(userId, userUrl);
        }

        if(!avatarMap.getValue().containsKey(userId))
        {
            downloadUserBitmap(userId, userUrl);
        }
    }

    /**
     * Function that calls the asynctask class ImageDownloader that downloads the image.
     * @param userId userId for the currently requested avatar bitmap
     * @param userUrl userUrl for the currently requested avatar bitmap
     */
    private void downloadUserBitmap(String userId, String userUrl)
    {
        if(avatarMap != null)
        {
            if(!avatarMap.getValue().containsKey(userId))
            {
                ImageDownloader imageDownloader = new ImageDownloader(result ->
                        avatarMap.getValue().put(userId, result));

                imageDownloader.execute(userUrl);
            }
        }
    }
}
