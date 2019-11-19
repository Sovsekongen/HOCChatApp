package p.vikpo.chatapp.interactors.Login;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import p.vikpo.chatapp.contracts.LoginContract;
import p.vikpo.chatapp.routers.LoginRouter;

public class FacebookInteractor implements LoginContract.Interactor
{
    private LoginRouter loginRouter;
    private LoginContract.InteractorOutput output;

    private FirebaseAuth mAuth;
    private Activity activity;
    private static final String TAG = "ChatApp - FacebookInteractor";

    public FacebookInteractor(Activity activity, LoginContract.InteractorOutput output)
    {
        loginRouter = new LoginRouter(activity);
        mAuth = FirebaseAuth.getInstance();
        this.activity = activity;
        this.output = output;
    }

    @Override
    public void unregister()
    {
        activity = null;
        loginRouter = null;
    }

    /**
     * When the FacebookCallback successfully logs in the AccessToken is registered with Firebase.
     * If the token is handled successfully the function will launch the chatroom activity.
     * Currently shows a ProgressDialog -> should be ProgressBar UI element instead.
     * @param token the AccessToken returned from a successful Facebook login.
     */
    private void handleFacebookAccessToken(AccessToken token)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, task ->
        {
            if (task.isSuccessful())
            {
                Log.e(TAG, "Task is Successful");
                output.onLoginSuccess();
            }
            else
            {
                output.onLoginError(task.getException().toString());
            }
        });
    }

    /**
     * Registers the facebook login callback and handles eventual errors that may arise during the process.
     * if the process is cancelled or encountered an error the function will start the LoginActivity again.
     * @return a new facebookCallback for the facebook login button.
     */
    public FacebookCallback<LoginResult> getFacebookCallback(LoginContract.View view)
    {
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
                view.showProgressBar();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                Toast.makeText(activity, "Login cancelled. Try again!", Toast.LENGTH_SHORT).show();
                loginRouter.startLogin();
            }

            @Override
            public void onError(FacebookException error)
            {
                output.onLoginError(error.toString());
            }
        };
    }
}
