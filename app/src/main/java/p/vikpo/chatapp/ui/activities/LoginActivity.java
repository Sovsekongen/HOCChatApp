package p.vikpo.chatapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import p.vikpo.chatapp.R;

/**
 * This class handles the login operations from Google and Facebook provided from Firebase.
 */
public class LoginActivity extends AppCompatActivity
{
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Intent chatRoomIntent;
    private ProgressDialog pd;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "ChatApp - login activity";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        setTransition();
        setContentView(R.layout.activity_login);
        Log.e(TAG, "Launched Login Fragment");

        pd = new ProgressDialog(this);
        pd.setMessage("Logging in");
        chatRoomIntent = new Intent(this, ChatroomActivity.class);

        mAuth = FirebaseAuth.getInstance();

        initGoogleLogin();
        initFacebookLogin();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null)
                {
                    firebaseAuthWithGoogle(account);
                }
            }
            catch (ApiException e)
            {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Initiates the required Google elements for logging in with the google servers.
     */
    private void initGoogleLogin()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //Registers the google sing in button and adds the onClickListener
        SignInButton googleSignInButton = findViewById(R.id.google_login_button);
        googleSignInButton.setOnClickListener(v -> signIn());
    }

    /**
     * Initiates the facebook login callback manager and sets the callback for the facebook login button
     */
    private void initFacebookLogin()
    {
        callbackManager = CallbackManager.Factory.create();

        LoginButton fbLoginButton = findViewById(R.id.fb_login_button);
        fbLoginButton.setPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
        fbLoginButton.registerCallback(callbackManager, getFacebookCallback());
    }

    /**
     * onClickFunction for the google button.
     * Starts an signIn intent provided by the google signInClient.
     */
    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Registers the facebook login callback and handles eventual errors that may arise during the process.
     * if the process is cancelled or encountered an error the function will start the LoginActivity again.
     * @return a new facebookCallback for the facebook login button.
     */
    private FacebookCallback<LoginResult> getFacebookCallback()
    {
        final Intent returnToLogin = new Intent(this, LoginActivity.class);

        return new FacebookCallback<LoginResult>()
        {
            /**
             * Callback function for handling when the login is a success. If a success the AccessToken
             * is passed to the handleFacebookAccessTokenMethod.
             * @param loginResult the LoginResult received when the user is successfully logged in.
             */
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                startActivity(returnToLogin);
            }

            @Override
            public void onError(FacebookException error)
            {
                //Make dialog popup with error message of some kind
                startActivity(returnToLogin);
            }
        };
    }

    /**
     * When the FacebookCallback successfully logs in the AccessToken is registered with Firebase.
     * If the token is handled successfully the function will launch the chatroom activity.
     * Currently shows a ProgressDialog -> should be ProgressBar UI element instead.
     * @param token the AccessToken returned from a successful Facebook login.
     */
    private void handleFacebookAccessToken(AccessToken token)
    {
        pd.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful())
            {
                pd.dismiss();
                startActivity(chatRoomIntent);
            }
            else
            {
                pd.dismiss();
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This functions registers the google user with firebase.
     * The method is called from onActivityResult when the google signInActivity returns a corresponding
     * GoogleSignInAccount it is registered with firebase.
     * @param acct the GoogleSignInAccount to be authenticated with firebase.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        pd.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful())
            {
                pd.dismiss();
                startActivity(chatRoomIntent);
            }
            else
            {
                pd.dismiss();
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
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
