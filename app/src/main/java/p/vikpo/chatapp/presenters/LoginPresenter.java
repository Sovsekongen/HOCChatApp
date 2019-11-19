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
import p.vikpo.chatapp.interactors.Login.FacebookInteractor;
import p.vikpo.chatapp.interactors.Login.GoogleInteractor;
import p.vikpo.chatapp.routers.LoginRouter;

public class LoginPresenter implements LoginContract.Presentor, LoginContract.InteractorOutput
{
    private static final String TAG = "ChatApp - LoginPresenter";

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private CallbackManager callbackManager;
    private Activity activity;

    private LoginRouter loginRouter;
    private GoogleInteractor googleInteractor;
    private FacebookInteractor facebookInteractor;
    private LoginContract.View view;

    public LoginPresenter(Activity activity, LoginContract.View view)
    {
        googleInteractor = new GoogleInteractor(this);
        facebookInteractor = new FacebookInteractor(activity, this);
        loginRouter = new LoginRouter(activity);

        this.activity = activity;

        callbackManager = CallbackManager.Factory.create();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        this.mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        this.view = view;
    }

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

    @Override
    public FacebookCallback<LoginResult> onFacebookLoginPressed()
    {
        return facebookInteractor.getFacebookCallback(view);
    }

    @Override
    public void onGoogleLoginPressed()
    {
        view.showProgressBar();
        loginRouter.signInGoogle(mGoogleSignInClient);
    }

    public void handleGoogleSignIn(Intent data)
    {
        googleInteractor.onActivityResult(data, activity);
    }

    public CallbackManager getCallbackManager()
    {
        return callbackManager;
    }

    @Override
    public void onLoginSuccess()
    {
        view.hideProgressBar();
        loginRouter.startChatroom();
    }

    @Override
    public void onLoginError(String error)
    {
        view.hideProgressBar();
        loginRouter.startLogin();
        Log.e(TAG, error);
        Toast.makeText(activity, "Unable to sign in. Please try again later.", Toast.LENGTH_SHORT).show();
    }
}
