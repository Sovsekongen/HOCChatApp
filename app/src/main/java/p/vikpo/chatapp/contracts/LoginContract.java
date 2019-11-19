package p.vikpo.chatapp.contracts;

import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface LoginContract
{
    interface Presentor
    {
        void onDestroy();
        FacebookCallback<LoginResult> onFacebookLoginPressed();
        void onGoogleLoginPressed();
    }

    interface Interactor
    {
        void unregister();
    }

    interface InteractorOutput
    {
        void onLoginSuccess();
        void onLoginError(String error);
    }

    interface Router
    {
        void unregister();
        void startLogin();
        void startChatroom();
        void signInGoogle(GoogleSignInClient gsc);
    }
}
