package p.vikpo.chatapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import java.util.Arrays;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.LoginContract;
import p.vikpo.chatapp.presenters.LoginPresenter;

/**
 * This class handles the login operations from Google and Facebook provided from Firebase.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View
{
    private LoginPresenter loginPresenter;
    private ProgressBar progressBar;

    private static final String TAG = "ChatApp - LoginActivity";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final int RC_SIGN_IN = 9001;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setTransition();
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenter(this, this);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        initGoogleLogin();
        initFacebookLogin();
    }

    /**
     * Initiates the required Google elements for logging in with the google servers.
     */
    private void initGoogleLogin()
    {
        //Registers the google sign in button and adds the onClickListener
        SignInButton googleSignInButton = findViewById(R.id.google_login_button);
        googleSignInButton.setOnClickListener(v -> loginPresenter.onGoogleLoginPressed());
    }

    /**
     * Initiates the facebook login callback manager and sets the callback for the facebook login button
     */
    private void initFacebookLogin()
    {
        LoginButton fbLoginButton = findViewById(R.id.fb_login_button);
        fbLoginButton.setPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
        fbLoginButton.registerCallback(loginPresenter.getCallbackManager(), loginPresenter.onFacebookLoginPressed());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * Handles logging the user in when the google intent results in a log-in.
         * Handles logging the user in when the FacebookCallback returns true.
         */
        if (requestCode == RC_SIGN_IN)
        {
            Log.e(TAG, "Logged in with google");
            loginPresenter.handleGoogleSignIn(data);
        }
        else
        {
            Log.e(TAG, "Logged in with facebook");
            loginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Makes sure that the activity passed to the other VIPER-elements is destroyed so that there is
     * no memory leak.
     */
    @Override
    protected void onDestroy()
    {
        Log.e(TAG, "Calling on destroy.");
        loginPresenter.onDestroy();
        loginPresenter = null;
        super.onDestroy();
    }

    /**
     * Adds the transition between the last activity and possibly this.
     */
    private void setTransition()
    {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade(Fade.IN));
        getWindow().setAllowReturnTransitionOverlap(true);
    }

    @Override
    public void showProgressBar()
    {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void hideProgressBar()
    {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
}
