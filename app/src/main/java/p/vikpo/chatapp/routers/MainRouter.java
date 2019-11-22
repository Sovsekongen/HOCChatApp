package p.vikpo.chatapp.routers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import p.vikpo.chatapp.contracts.MainContract;
import p.vikpo.chatapp.views.activities.ChatroomActivity;
import p.vikpo.chatapp.views.activities.LoginActivity;

public class MainRouter implements MainContract.Router
{
    private Activity activity;
    private final Handler delayHandler = new Handler();
    private static final String TAG = "ChatApp - MainRouter";

    public MainRouter(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void unregister()
    {
        activity = null;
    }

    /**
     * Starts the login activity - checks to see if the main fragment has been launched by an intent.
     * @param chatroomName the name of the chatroom that the intent has been launched with.
     */
    @Override
    public void startLogin(String chatroomName)
    {
        Log.d(TAG, "Logging in with intent to " + chatroomName);
        Intent loginIntent = new Intent(activity, LoginActivity.class);
        if(chatroomName != null)
        {
            loginIntent.putExtra("chatroom", chatroomName);
        }
        load(loginIntent);
    }

    /**
     * Starts the chat activity - checks to see if the main fragment has been launched by an intent
     * @param chatroomName the name of the chatroom that the intent has been launched with.
     */
    @Override
    public void startChat(String chatroomName)
    {
        Log.d(TAG, "Is logged in with intent to " + chatroomName);
        Intent chatIntent = new Intent(activity, ChatroomActivity.class);
        if(chatroomName != null)
        {
            chatIntent.putExtra("chatroom", chatroomName);
        }
        load(chatIntent);
    }

    private void startNewActivity(Intent intent)
    {
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
    }

    /**
     * Basic delay just for the sake of it
     */
    private void load(Intent intent)
    {
        delayHandler.postDelayed(() -> startNewActivity(intent), 2000);
    }
}
