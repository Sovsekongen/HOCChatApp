package p.vikpo.chatapp.ui.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.ui.fragments.SplashFragment;

/**
 * Main activity for the app. Contains a container containing a Fragment: SplashFragment.
 */
public class MainActivity extends FragmentActivity
{
    private static final String TAG = "ChatApp MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBackend();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.MainFragmentContainer, SplashFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.MainFragmentContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
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
}