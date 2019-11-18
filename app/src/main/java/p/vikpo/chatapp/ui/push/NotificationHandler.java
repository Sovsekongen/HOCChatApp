package p.vikpo.chatapp.ui.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import p.vikpo.chatapp.R;

public class NotificationHandler
{
    private String channelName, channelDescription;
    private NotificationManager notificationManager;


    public NotificationHandler(String channelName, String channelDescription, NotificationManager notificationManager)
    {
        this.channelDescription = channelDescription;
        this.channelName = channelName;
        this.notificationManager = notificationManager;
    }


}
