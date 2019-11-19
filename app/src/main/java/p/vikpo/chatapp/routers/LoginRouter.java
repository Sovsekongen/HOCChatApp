package p.vikpo.chatapp.routers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import p.vikpo.chatapp.contracts.LoginContract;
import p.vikpo.chatapp.views.activities.ChatroomActivity;
import p.vikpo.chatapp.views.activities.LoginActivity;

public class LoginRouter implements LoginContract.Router
{
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "ChatApp - LoginRouter";
    private Activity activity;

    public LoginRouter(Activity activity)
    {
        this.activity = activity;
    }

    /**
     * onClickFunction for the google button.
     * Starts an signIn intent provided by the google signInClient.
     */

    @Override
    public void unregister()
    {
        activity = null;
    }

    @Override
    public void startLogin()
    {
        Log.e(TAG, "Start Login");

        Intent returnToLogin = new Intent(activity, LoginActivity.class);
        activity.startActivity(returnToLogin);
    }

    @Override
    public void startChatroom()
    {
        Log.e(TAG, "Start Chatroom");

        Intent chatRoomIntent = new Intent(activity, ChatroomActivity.class);
        activity.startActivity(chatRoomIntent);
    }

    @Override
    public void signInGoogle(GoogleSignInClient gsc)
    {
        Log.e(TAG, "Sign in Google Intent");

        Intent signInIntent = gsc.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
