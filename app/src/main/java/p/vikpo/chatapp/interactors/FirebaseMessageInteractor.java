package p.vikpo.chatapp.interactors;

import android.app.Activity;
import android.app.PendingIntent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import p.vikpo.chatapp.contracts.NotificationContract;

public class FirebaseMessageInteractor extends FirebaseMessagingService implements NotificationContract.Interactor
{
    private FirebaseInstanceId firebaseId;
    private String firebaseToken;
    private FirebaseUserInteractor userInteractor;
    private NotificationInteractor interactor;
    private static final String TAG = "ChatApp - FirebaseMessageInteractor";

    public FirebaseMessageInteractor()
    {

    }

    public FirebaseMessageInteractor(Activity activity)
    {
        interactor = new NotificationInteractor(activity);
        userInteractor = new FirebaseUserInteractor();
    }

    private void setFirebaseId(String token)
    {
        firebaseToken = token;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d(TAG, "Received Message: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (true)
            {
                scheduleJob();
            }
            else
            {
                handleNow();
            }

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

    private void scheduleJob()
    {
        /*OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance().beginWith(work).enqueue();*/
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token)
    {
        Log.d(TAG, "Registration to the server? " + token);
        userInteractor.getUserToken();
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
