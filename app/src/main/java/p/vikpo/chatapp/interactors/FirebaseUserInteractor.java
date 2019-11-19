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

import p.vikpo.chatapp.contracts.MainContract;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;
import p.vikpo.chatapp.entities.UserWrapper;

/**
 * Class for handling the firebase user when logged in. Downloads avatars so that they are ready
 * for being used in the chats. Uses a viewmodel to store bitmaps between fragments.
 * Handles checking whether or not the user is logged in.
 */
public class FirebaseUserInteractor implements MainContract.Interactor
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private FirebaseStorageInteractor mImageStorage;
    private FirebaseUser mUser;
    private AvatarViewModel imageHolder;

    private MainContract.InteractorOutput output;

    private static final String TAG = "ChatApp - FirebaseUserInteractor";
    private static final String COLLECTION_USER = "users";
    private static final String DOCUMENT_FIELD_USERID = "mUid";
    private static final String IMAGE_PATH_USER = "user/";

    /**
     * Constructor initializing the different firebase components that are later used in the class
     * @param imageHolder the ViewModel that stores the bitmaps.
     */
    public FirebaseUserInteractor(AvatarViewModel imageHolder)
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mImageStorage = new FirebaseStorageInteractor();
        mUser = mAuth.getCurrentUser();

        this.imageHolder = imageHolder;
    }

    /**
     * No-arg constructor for instantiating in an activity where the AvatarViewModel isnt needed.
     */
    public FirebaseUserInteractor(MainContract.InteractorOutput output)
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        mImageStorage = new FirebaseStorageInteractor();
        this.output = output;
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
     * @return a LiveData object containing a reference to the hashmap with the bitmaps.
     */
    public LiveData<HashMap<String, Bitmap>> getAvatarMap(String userId)
    {
        return imageHolder.getMap(userId);
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
                    imageHolder.addUser(document.get("mUid").toString());
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

                ImageDownloader imageDownloader = new ImageDownloader(result ->
                        mImageStorage.uploadImage(IMAGE_PATH_USER + mUser.getUid(), result));

                imageDownloader.execute(mUser.getPhotoUrl().toString());
            }
        });
    }

    @Override
    public void unregister()
    {
        output = null;
    }
}