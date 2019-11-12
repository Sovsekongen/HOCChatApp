package p.vikpo.chatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity
{
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Intent chatroomIntent;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "ChatApp - login activity";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);
        Log.e(TAG, "Launched Login Fragment");

        chatroomIntent = new Intent(this, ChatRoomActivity.class);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        LoginButton fbLoginButton = findViewById(R.id.fb_login_button);
        fbLoginButton.setPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
        fbLoginButton.registerCallback(callbackManager, getFacebookCallback());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton googleSignInButton = findViewById(R.id.google_login_button);
        googleSignInButton.setOnClickListener(v -> signIn());
    }

    @Override
    public void onStart()
    {
        super.onStart();
        isUserLoggedIn();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    private void isUserLoggedIn()
    {
        if(mAuth.getCurrentUser() != null)
        {
            startActivity(chatroomIntent);
        }
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
                firebaseAuthWithGoogle(account);
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

    private FacebookCallback<LoginResult> getFacebookCallback()
    {
        Log.e(TAG, "Received Callback");
        final Intent returnToLogin = new Intent(this, LoginActivity.class);

        return new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.e(TAG, "Successfully logged in.");
                //getGraphData(loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                Log.e(TAG, "Cancelled");
                startActivity(returnToLogin);
            }

            @Override
            public void onError(FacebookException error)
            {
                Log.e(TAG, "Error?", error);
                //Make dialog popup with error message of some kind
                startActivity(returnToLogin);
            }
        };
    }

    private void handleFacebookAccessToken(AccessToken token)
    {
        Log.e(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful())
            {
                // Sign in success, update UI with the signed-in user's information
                Log.e(TAG, "signInWithCredential:success");
                startActivity(chatroomIntent);
            }
            else
            {
                // If sign in fails, display a message to the user.
                Log.e(TAG, "signInWithCredential:failure", task.getException());
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                    {
                        Log.d(TAG, "signInWithCredential:success");
                        startActivity(chatroomIntent);
                    }
                    else
                    {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());

                    }
                });
    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*
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
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e)
            {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }*/
}
