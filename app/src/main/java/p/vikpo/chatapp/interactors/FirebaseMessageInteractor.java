package p.vikpo.chatapp.interactors;

import android.app.Activity;
import android.app.PendingIntent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import p.vikpo.chatapp.contracts.NotificationContract;

public class FirebaseMessageInteractor extends FirebaseMessagingService implements NotificationContract.Interactor
{
    private FirebaseInstanceId firebaseId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private NotificationInteractor interactor;

    private static final String TAG = "ChatApp - FirebaseMessageInteractor";
    private static final String COLLECTION_USER = "users";
    private static final String DOCUMENT_FIELD_USERID = "mUid";
    private static final String DOCUMENT_FIELD_TOKEN = "mMessageToken";

    public FirebaseMessageInteractor()
    {

    }

    public FirebaseMessageInteractor(Activity activity)
    {
        interactor = new NotificationInteractor(activity);
    }

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

    @Override
    public void onNewToken(String token)
    {
        Log.d(TAG, "Refreshed Token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token)
    {
        Log.d(TAG, "Registration to the server? " + token);

        Query query = mDatabase.collection(COLLECTION_USER).whereEqualTo(DOCUMENT_FIELD_USERID, mAuth.getCurrentUser().getUid());

        query.get().addOnCompleteListener(task -> mDatabase.collection(COLLECTION_USER).document(task.getResult().getDocuments().get(0).getId()).update(DOCUMENT_FIELD_TOKEN, token));
    }

    private void sendNotification(PendingIntent intent, String chatroomName)
    {
        interactor.showNotification(intent, chatroomName);
    }

    @Override
    public void unregister()
    {
        interactor.unregister();
        interactor = null;
    }
}
