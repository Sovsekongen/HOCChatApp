package p.vikpo.chatapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.presenters.MainPresenter;

/**
 * Main activity for the app.
 */
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "ChatApp - MainActivity";
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setTransition();
        setContentView(R.layout.activity_splash);

        initBackend();

        Intent notificationIntent = getIntent();

        if(notificationIntent != null)
        {
            presenter.isLoggedIn(notificationIntent);
        }
        else
        {
            presenter.isLoggedIn(null);
        }
    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Function that initialises the firebase backend and currently logs the user out.
     */
    private void initBackend()
    {
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        presenter = new MainPresenter(this);
    }

    /**
     * Adds the transition between the last activity and possibly this.
     */
    private void setTransition()
    {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade(Fade.OUT));
        getWindow().setAllowEnterTransitionOverlap(true);
    }
}