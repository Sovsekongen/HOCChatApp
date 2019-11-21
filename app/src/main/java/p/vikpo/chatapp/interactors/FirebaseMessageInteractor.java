package p.vikpo.chatapp.interactors;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.NotificationContract;

public class FirebaseMessageInteractor extends FirebaseMessagingService implements NotificationContract.Interactor
{
    private NotificationInteractor interactor;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    private static final String TAG = "ChatApp - FirebaseMessageInteractor";
    private static final String COLLECTION_USER = "users";
    private static final String DOCUMENT_FIELD_USERID = "mUid";
    private static final String DOCUMENT_FIELD_TOKEN = "mMessageToken";

    private static final String[] CHATROOM_NAMES = {"Music", "Photography", "Series", "Spare Time"};

    /**
     * Class for handling firebase messages
     */
    public FirebaseMessageInteractor()
    {
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseMessageInteractor(Activity activity)
    {
        interactor = new NotificationInteractor(activity);
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * What should happend when a message is received. Not currently doing anything of value.
     * @param remoteMessage the message received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d(TAG, "Received Message: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null)
        {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    /**
     * If a new token is registered add it to the server.
     * @param token the new token.
     */
    @Override
    public void onNewToken(String token)
    {
        Log.d(TAG, "Refreshed Token: " + token);

        sendRegistrationToServer(token);
    }

    /**
     * Method for linking the token to the user. Stored in the DB.
     * @param token the token to be stored in the DB
     */
    private void sendRegistrationToServer(String token)
    {
        Log.d(TAG, "Registration to the server? " + token);

        Query query = mDatabase.collection(COLLECTION_USER).whereEqualTo(DOCUMENT_FIELD_USERID, mAuth.getCurrentUser().getUid());
        query.get().addOnCompleteListener(task -> mDatabase.collection(COLLECTION_USER).document(task.getResult().getDocuments().get(0).getId()).update(DOCUMENT_FIELD_TOKEN, token));
    }

    /**
     * Method that subscribes to the topics based on the user preferences.
     * @param topics a map containing key-value pairs with key being the chatroom Strings and the boolean values being the notification preference.
     */
    public void subscribeToTopics(HashMap<String, Boolean> topics)
    {
        unsubscribeAll();

        for(String name : CHATROOM_NAMES)
        {
            if(topics.containsKey(name))
            {
                if(topics.get(name))
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(translateTitle(name))
                            .addOnCompleteListener(task ->
                            {
                                String msg = getString(R.string.msg_subscribed);

                                if (!task.isSuccessful())
                                {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }

                                Log.d(TAG, msg);
                            });
                }
            }
        }
    }

    /**
     * Method that unsubscribes the user from all topics.
     */
    private void unsubscribeAll()
    {
        for(String name : CHATROOM_NAMES)
        {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(translateTitle(name))
                    .addOnFailureListener(e ->
                            Log.e(TAG, "Cant unsubscribe from " + name, e));
        }
    }

    @Override
    public void unregister()
    {
        interactor.unregister();
        interactor = null;
    }

    /**
     * Methods for translating the titles of the chatrooms to the document id's in the db.
     * @param document the title of the chatroom in the app
     * @return the document name in the database
     */
    private String translateTitle(String document)
    {
        switch(document)
        {
            case "Music":
                return "music";
            case "Spare Time":
                return "sparetime";
            case "Photography":
                return "photo";
            case "Series":
                return "series";
            default:
                return document;
        }
    }
}
