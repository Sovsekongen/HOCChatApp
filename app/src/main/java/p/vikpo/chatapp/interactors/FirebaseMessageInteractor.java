package p.vikpo.chatapp.interactors;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.views.activities.MainActivity;

public class FirebaseMessageInteractor extends FirebaseMessagingService
{
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseChatroomInteractor chatroomInteractor;

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
        chatroomInteractor = new FirebaseChatroomInteractor();
    }

    /**
     * What should happen when a message is received. Not currently doing anything of value.
     * @param remoteMessage the message received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d(TAG, "Received Message: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            showNotification(remoteMessage);
        }

        if (remoteMessage.getNotification() != null)
        {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void showNotification(RemoteMessage message)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        String title = message.getData().get("title");
        String text = message.getData().get("text");
        String deepLink = message.getData().get("deepLink");
        String chatroom = message.getData().get("chatroom");

        if(chatroom != null)
        {
            chatroomInteractor.updateChatroomNew(translateTitleRev(chatroom));
        }

        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(deepLink));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("chatroom", chatroom);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

        //Configure Notification Channel
        notificationChannel.setDescription("Game Notifications");
        notificationChannel.enableLights(true);
        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        notificationChannel.enableVibration(true);

        notificationManager.createNotificationChannel(notificationChannel);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(1);


        notificationManager.notify(1, notificationBuilder.build());
    }

    /**
     * If a new token is registered add it to the server.
     * @param token the new token.
     */
    @Override
    public void onNewToken(@NonNull String token)
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
                if(topics.get(name) != null)
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(translateTitle(name))
                        .addOnCompleteListener(task ->
                        {
                            if (!task.isSuccessful())
                            {
                                Log.e(TAG, "Failed to subscribe", task.getException());
                            }

                            Log.d(TAG, "Subscribed successfully to " + translateTitle(name));
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
        Log.d(TAG, "Unsubscribing on all topics");
        for(String name : CHATROOM_NAMES)
        {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(translateTitle(name))
                    .addOnFailureListener(e ->
                            Log.e(TAG, "Cant unsubscribe from " + name, e));
        }
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

    /**
     * ReverseTransfer...
     */
    private String translateTitleRev(String document)
    {
        switch(document)
        {
            case "music":
                return "Music";
            case "sparetime":
                return "Spare Time";
            case "photo":
                return "Photography";
            case "series":
                return "Series";
            default:
                return document;
        }
    }
}
