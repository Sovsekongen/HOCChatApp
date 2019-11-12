package p.vikpo.chatapp.ui.activities;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.ui.fragments.SplashFragment;

public class MainActivity extends FragmentActivity
{
    private static final String TAG = "Chatapp MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance().signOut();

        Log.e(TAG, "Launched Main Activity");

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
}