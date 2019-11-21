package p.vikpo.chatapp.presenters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.LoginContract;
import p.vikpo.chatapp.interactors.login.FacebookInteractor;
import p.vikpo.chatapp.interactors.login.GoogleInteractor;
import p.vikpo.chatapp.routers.LoginRouter;

public class LoginPresenter implements LoginContract.Presentor, LoginContract.InteractorOutput
{
    private static final String TAG = "ChatApp - LoginPresenter";

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private Activity activity;

    private LoginRouter loginRouter;
    private GoogleInteractor googleInteractor;
    private FacebookInteractor facebookInteractor;
    private LoginContract.View view;

    /**
     * Instantiating the presenter based on the View interface and the parent activity
     * @param activity parent activity
     * @param view interface determaining which functions can be called from the view class.
     */
    public LoginPresenter(Activity activity, LoginContract.View view)
    {
        googleInteractor = new GoogleInteractor(this);
        facebookInteractor = new FacebookInteractor(activity, this);
        loginRouter = new LoginRouter(activity);

        this.activity = activity;

        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        this.mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        this.view = view;
    }

    /**
     * For de-allocating memory.
     */
    @Override
    public void onDestroy()
    {
        activity = null;
        loginRouter.unregister();
        loginRouter = null;
        facebookInteractor.unregister();
        facebookInteractor = null;
        googleInteractor.unregister();
        googleInteractor = null;
        view = null;

        mGoogleSignInClient = null;
    }

    /**
     * Getting the FacebookCallback method from the FacebookInteractor
     * @return FacebookCallback-function for handling possible click on the FB-login button
     */
    @Override
    public FacebookCallback<LoginResult> onFacebookLoginPressed()
    {
        return facebookInteractor.getFacebookCallback(view);
    }

    /**
     * Handles click events on the Google-login button. Shows the progressbar.
     */
    @Override
    public void onGoogleLoginPressed()
    {
        view.showProgressBar();
        loginRouter.signInGoogle(mGoogleSignInClient);
    }

    /**
     * Passing the Google onActivityResult method to the parents onActivityResult method.
     * @param data the intent in the onActivityResult method.
     */
    public void handleGoogleSignIn(Intent data)
    {
        googleInteractor.onActivityResult(data, activity);
    }

    /**
     * Callback manager for registering the Facebook-Callback to the facebook button.
     * @return instance of the CallbackManager
     */
    public CallbackManager getCallbackManager()
    {
        return callbackManager;
    }

    /**
     * Interface method for handling what to do when the login is successful.
     */
    @Override
    public void onLoginSuccess()
    {
        view.hideProgressBar();
        loginRouter.startChatroom();
    }

    /**
     * Interface method for handling what to do when the login has failed.
     * Restarts the LoginActivity and shows a Toast with an error message.
     * @param error The error passed to the function. The error gets logged.
     */
    @Override
    public void onLoginError(String error)
    {
        view.hideProgressBar();
        loginRouter.startLogin();
        Log.e(TAG, error);
        Toast.makeText(activity, "Unable to sign in. Please try again later.", Toast.LENGTH_SHORT).show();
    }
}
