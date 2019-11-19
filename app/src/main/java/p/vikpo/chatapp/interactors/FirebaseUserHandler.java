package p.vikpo.chatapp.interactors;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;
import p.vikpo.chatapp.entities.UserWrapper;

/**
 * Class for handling the firebase user when logged in. Downloads avatars so that they are ready
 * for being used in the chats. Uses a viewmodel to store bitmaps between fragments.
 * Handles checking whether or not the user is logged in.
 */
public class FirebaseUserHandler
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private FirebaseUser mUser;
    private AvatarViewModel imageHolder;
    private Map<String, UserWrapper> users;

    private static final String TAG = "ChatApp - FirebaseUserHandler";
    private static final String COLLECTION_USER = "users";
    private static final String DOCUMENT_FIELD_USERID = "mUid";

    /**
     * Constructor initializing the different firebase components that are later used in the class
     * @param imageHolder the ViewModel that stores the bitmaps.
     */
    public FirebaseUserHandler(AvatarViewModel imageHolder)
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        this.imageHolder = imageHolder;
        users = new HashMap<>();
    }

    /**
     * No-arg constructor for instantiating in an activity where the AvatarViewModel isnt needed.
     */
    public FirebaseUserHandler()
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    /**
     * Checks whether a firebase user is instantiated.
     * @return boolean value based on log-in status.
     */
    public boolean isUserLoggedIn()
    {
        return mUser != null;
    }

    /**
     * Returns the map-containing all the bitmaps with corresponding userids
     * @param userId the userId for the currently requested avatar bitmap
     * @param userUrl the URL for the current requested avatar bitmap
     * @return a LiveData object containing a reference to the hashmap with the bitmaps.
     */
    public LiveData<HashMap<String, Bitmap>> getAvatarMap(String userId, String userUrl)
    {
        return imageHolder.getMap(userId, userUrl);
    }

    /**
     * Functions that downloads all the bitmaps for all users currently instantiated in the DB.
     * Not scaleable - should be changed to each individual chatroom but for small-scale application
     * it seems fine.
     */
    public void loadAllAvatars()
    {
        Query query = mDatabase.collection(COLLECTION_USER);

        query.get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                for(QueryDocumentSnapshot document : task.getResult())
                {
                    imageHolder.addUser(document.get("mUid").toString(), document.get("mUrl").toString());
                }
            }
            else
            {
                Log.e(TAG, "Something went wrong", task.getException());
            }
        });
    }

    /**
     * Each time a new user is logged in the Uid and the URL for the avatar is saved in a firebase collection
     * Uses a UserWrapper class for containing the necessary information.
     */
    public void addUserToDB()
    {
        UserWrapper newUser = new UserWrapper(mUser.getPhotoUrl().toString(), mUser.getUid(),
                new HashMap<>());

        Query query = mDatabase.collection(COLLECTION_USER).whereEqualTo(DOCUMENT_FIELD_USERID, newUser.getmUid());

        query.get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful() && task.getResult().isEmpty())
            {
                mDatabase.collection(COLLECTION_USER).add(newUser);
                users.put(mUser.getUid(), newUser);
            }
        });
    }

    public UserWrapper getCurrentUser()
    {
        return users.get(mUser.getUid());
    }
}