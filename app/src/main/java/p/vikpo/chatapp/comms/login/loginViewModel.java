package p.vikpo.chatapp.comms.login;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;

import org.json.JSONException;

import p.vikpo.chatapp.session.ImageDownloader;
import p.vikpo.chatapp.session.User;

public class LoginViewModel extends ViewModel
{
    public interface FBLoginCallback
    {
        void onFbLogin(LiveData<User> currentUser);
    }

    private MutableLiveData<User> user;
    private FBLoginCallback fbLogin;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ACCESS_TOKEN = "accesstoken";
    private static final String KEY_ID = "id";
    private static final String KEY_EMPTY = "";

    public FacebookCallback<LoginResult> getFacebookCallback()
    {
        return new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.e("ChatApp", "Successfully logged in.");
                getGraphData(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                Log.e("ChatApp", "cancelled?");
                //Handle this?
            }

            @Override
            public void onError(FacebookException error)
            {
                Log.e("ChatApp", "Error?");
                //Make dialog popup with error message of some kind
            }
        };
    }

    private void getGraphData(final AccessToken accessToken)
    {
        if(user == null)
        {
            user = new MutableLiveData<>();
        }

        GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
                    try
                    {
                        loginUser(object.getString(KEY_EMAIL), object.getString(KEY_ID), accessToken);
                    }
                    catch(JSONException jE)
                    {
                        Log.e("ChatApp", "JSON Fault in SessionHandler", jE);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void loginUser(String email, String id, AccessToken accessToken)
    {
        Log.e("ChatApp", "LoginUser");
        User currentUser = new User();

        ImageDownloader imageDownloader = new ImageDownloader(currentUser::setAvatar);
        imageDownloader.execute(id);

        currentUser.setAccessToken(accessToken);
        currentUser.setEmail(email);

        Log.e("ChatApp", currentUser.toString());

        user.setValue(currentUser);
        fbLogin.onFbLogin(user);
    }

    /*
    public LiveData<User> getUser()
    {
        if(user != null)
        {
            if (user.getValue().getAvatar() != null &&
                    !user.getValue().getEmail().equals("") &&
                    !user.getValue().getId().equals(""))
            {
                return user;
            }
        }

        return null;
    }*/

    public boolean isLoggedIn()
    {
        AccessToken accessToken = null;
        if(user != null && user.getValue().getAccessToken() != null)
        {
            accessToken = user.getValue().getAccessToken();
        }

        if(accessToken == null)
        {
            return false;
        }

        if(!accessToken.isExpired())
        {
            getGraphData(accessToken);
            return true;
        }

        return false;
    }
}