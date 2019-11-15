package p.vikpo.chatapp.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.Login.FirebaseLogin;

/**
 * Main activity for the app.
 */
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "ChatApp MainActivity";
    private final Handler delayHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setTransition();
        setContentView(R.layout.activity_splash);

        initBackend();

        isLoggedIn();
    }

    /**
     * Function that initialises the firebase backend and currently logs the user out.
     */
    private void initBackend()
    {
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    private void startNewActivity(Intent intent)
    {
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void isLoggedIn()
    {
        if (FirebaseLogin.isUserLoggedIn())
        {
            Log.e(TAG, "Is logged In");
            load(new Intent(this, ChatroomActivity.class));
        }
        else
        {
            Log.e(TAG, "Is not logged In");
            load(new Intent(this, LoginActivity.class));
        }
    }

    /**
     * Basic delay just for the sake of it
     */
    private void load(Intent intent)
    {
        delayHandler.postDelayed(() ->
                startNewActivity(intent), 4000);
    }

    private void setTransition()
    {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade(Fade.OUT));
        getWindow().setAllowEnterTransitionOverlap(true);
    }
}