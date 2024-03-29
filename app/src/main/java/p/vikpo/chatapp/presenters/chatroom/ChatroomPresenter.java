package p.vikpo.chatapp.presenters.chatroom;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.entities.MessageImageWrapper;
import p.vikpo.chatapp.entities.MessageWrapper;
import p.vikpo.chatapp.interactors.FirebaseChatroomInteractor;
import p.vikpo.chatapp.interactors.FirebaseStorageInteractor;
import p.vikpo.chatapp.interactors.FirebaseUserInteractor;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroom.ChatroomAdapter;
import p.vikpo.chatapp.routers.ChatroomRouter;

public class ChatroomPresenter
{
    private FirebaseChatroomInteractor firebaseChatroomInteractor;
    private FirebaseUserInteractor firebaseUserInteractor;
    private FirebaseUser mUser;
    private ChatroomRouter router;
    private ChatroomContract.ChatroomView view;
    private Fragment parent;
    private String chatroomName;
    private static final String IMAGE_LOCATION = "images/";
    private static final String TAG = "ChatApp - ChatroomPresenter";

    /**
     * Constructor instansiating the needed resources for maintaining this class.
     * @param activity parent activity
     * @param view interface for accessing ChatroomFragments methods
     * @param chatroomName name of the chatroom that needs to be opened
     * @param parent parent fragment
     */
    public ChatroomPresenter(AppCompatActivity activity, ChatroomContract.ChatroomView view, String chatroomName, Fragment parent)
    {
        firebaseChatroomInteractor = new FirebaseChatroomInteractor(
                chatroomName,
                ViewModelProviders.of(activity).get(AvatarViewModel.class),
                parent);
        router = new ChatroomRouter(activity);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        this.view = view;
        this.parent = parent;
        this.chatroomName = chatroomName;

        firebaseUserInteractor = new FirebaseUserInteractor();
        firebaseUserInteractor.initUser();

        onBackButton();
    }

    /**
     * Method for accessing the adapter made for this specific chatroom
     * @return the new adapter for populating the view.
     */
    public ChatroomAdapter getAdapter()
    {
        return firebaseChatroomInteractor.getChatroomMessageAdapter();
    }

    /**
     * Updates the given chatroom adapter with a new Query.
     * @param adapter the adapter to be updated
     */
    public void updateChatroomAdapter(ChatroomAdapter adapter)
    {
        firebaseChatroomInteractor.updateChatroomQuery(adapter);
    }

    /**
     * Handles what happens whenever the user clicks the BackButton inside the specific chatrooms
     */
    private void onBackButton()
    {
        router.onBackCallback(parent);
    }

    /**
     * Handles on click for the send button. If the input is empty prompt the user with a "is empty"-toast
     * If it isn't empty send a message to the firebase database.
     */
    public View.OnClickListener sendButtonOnClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            updatePermission();
            String message = view.getInputBox();

            if(TextUtils.isEmpty(message))
            {
                return;
            }

            firebaseChatroomInteractor.addMessage(
                    new MessageWrapper(mUser.getDisplayName(),
                    message,
                    mUser.getUid(),
                    System.currentTimeMillis(),
                    mUser.getPhotoUrl().toString()));

            view.setInputBox("");
        }
    };

    /**
     * handles what happens when the camera floating action buttons is clicked.
     * Starts the camera intent which handles returning the image to be sent.
     */
    public View.OnClickListener cameraButtonOnClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            router.startCameraIntent(parent);
        }
    };

    /**
     * Handles accessing information given from the camera activity. Takes the bitmap, uploads
     * it to the FirestoreStorage and adds the message with the correct location in the FirestoreStorage
     * @param data The intent containing the Bitmap taken from the CameraActivity.
     */
    public void sendImageMessage(Intent data)
    {
        updatePermission();
        String imageTitle = mUser.getUid() + System.currentTimeMillis();
        FirebaseStorageInteractor imageStorage = new FirebaseStorageInteractor();

        imageStorage.uploadImage(IMAGE_LOCATION + imageTitle, data.getParcelableExtra("messageImage"));

        firebaseChatroomInteractor.addMessage(
                new MessageImageWrapper(mUser.getDisplayName(),
                        view.getInputBox(),
                        mUser.getUid(),
                        System.currentTimeMillis(),
                        mUser.getPhotoUrl().toString(),
                        imageTitle));
    }

    /**
     * Launches a dialog if the user haven't set their preferences for notifications. If the user
     * has not set their preferences update the preferences for the user.
     */
    private void updatePermission()
    {
        if(firebaseUserInteractor.hasPermission(chatroomName) == 2)
        {
            router.showChoiceDialog(
                    (dialog, which) ->
                        firebaseUserInteractor.updateUserPermission(chatroomName, true),
                    (dialog, which) ->
                        firebaseUserInteractor.updateUserPermission(chatroomName, false)).show();
        }
    }
}
