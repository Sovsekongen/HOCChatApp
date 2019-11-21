package p.vikpo.chatapp.routers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

import p.vikpo.chatapp.contracts.NotificationContract;
import p.vikpo.chatapp.views.activities.ChatroomActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class NotificationRouter implements NotificationContract.Router
{
    private static final String CHATROOM_NAME = "chatroomName";
    private Activity activity;

    public NotificationRouter(Activity activity)
    {
        this.activity = activity;
    }

    public PendingIntent getChatroomIntent(String document)
    {
        Intent launchIntent = new Intent(activity, ChatroomActivity.class);
        launchIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        launchIntent.putExtra(CHATROOM_NAME, document);
        return PendingIntent.getActivity(activity, 0, launchIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    @Override
    public void unregister()
    {
        activity = null;
    }
}
