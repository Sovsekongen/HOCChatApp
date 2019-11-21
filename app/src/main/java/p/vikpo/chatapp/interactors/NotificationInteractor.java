package p.vikpo.chatapp.interactors;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.NotificationContract;

public class NotificationInteractor implements NotificationContract.Interactor
{
    private Activity activity;
    private NotificationManager notificationManager;

    private static final String CHANNEL_ID = "ChatAppNotificationChannel";
    private static final String CHANNEL_NAME = "ChatApp: Notification Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel for handling notification for the ChatApp";
    private static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    private static final boolean CHANNEL_ENABLE_VIBRATE = true;
    private static final int CHANNEL_LOCKSCREEN_VISIBILITY = 1;

    public NotificationInteractor(Activity activity)
    {
        notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        this.activity = activity;
    }

    /**
     * Initiates the notification channel ID.
     */
    public void initChannel()
    {
        if(notificationManager.getNotificationChannel(CHANNEL_ID) == null)
        {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationChannel.enableVibration(CHANNEL_ENABLE_VIBRATE);
            notificationChannel.setLockscreenVisibility(CHANNEL_LOCKSCREEN_VISIBILITY);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Shows a notification in the app. Only local.
     * @param intent the pending intent for what should happend when the notification is clicked.
     * @param document the document that should be opened and from which there is a new message.
     */
    public void showNotification(PendingIntent intent, String document)
    {
        Notification notification = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setContentTitle(document + " Message Received")
                .setContentText("You have received a message from " + document)
                .setSmallIcon(R.drawable.ic_chevron_right_black_24dp)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }

    @Override
    public void unregister()
    {
        activity = null;
        notificationManager = null;
    }
}
