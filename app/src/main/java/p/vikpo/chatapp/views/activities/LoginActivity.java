package p.vikpo.chatapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import java.util.Arrays;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.presentors.LoginPresentor;

/**
 * This class handles the login operations from Google and Facebook provided from Firebase.
 */
public class LoginActivity extends AppCompatActivity
{
    private LoginPresentor loginPresentor;

    private static final String TAG = "ChatApp - login activity";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final int RC_SIGN_IN = 9001;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setTransition();
        setContentView(R.layout.activity_login);

        loginPresentor = new LoginPresentor(this);

        initGoogleLogin();
        initFacebookLogin();
    }

    /**
     * Initiates the required Google elements for logging in with the google servers.
     */
    private void initGoogleLogin()
    {
        //Registers the google sing in button and adds the onClickListener
        SignInButton googleSignInButton = findViewById(R.id.google_login_button);
        googleSignInButton.setOnClickListener(v -> loginPresentor.onGoogleLoginPressed());
    }

    /**
     * Initiates the facebook login callback manager and sets the callback for the facebook login button
     */
    private void initFacebookLogin()
    {
        LoginButton fbLoginButton = findViewById(R.id.fb_login_button);
        fbLoginButton.setPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
        fbLoginButton.registerCallback(loginPresentor.getCallbackManager(), loginPresentor.onFacebookLoginPressed());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            Log.e(TAG, "Logged in with google");
            loginPresentor.handleGoogleSignIn(data);
        }
        else
        {
            Log.e(TAG, "Logged in with facebook");
            loginPresentor.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy()
    {
        Log.e(TAG, "Calling on destroy.");
        loginPresentor.onDestroy();
        loginPresentor = null;
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
}
