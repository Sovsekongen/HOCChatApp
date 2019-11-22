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

    @Override
    public void startLogin(String intent)
    {
        Log.d(TAG, "Logging in with intent to " + intent);
        Intent loginIntent = new Intent(activity, LoginActivity.class);
        if(intent != null)
        {
            loginIntent.putExtra("chatroom", intent);
        }
        load(loginIntent);
    }

    @Override
    public void startChat(String intent)
    {
        Log.d(TAG, "Is logged in with intent to " + intent);
        Intent chatIntent = new Intent(activity, ChatroomActivity.class);
        if(intent != null)
        {
            chatIntent.putExtra("chatroom", intent);
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
