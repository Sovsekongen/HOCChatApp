package p.vikpo.chatapp.comms.login;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;

import org.json.JSONException;

import p.vikpo.chatapp.session.User;

public class FacebookAuth
{
    public interface FBLoginCallback
    {
        void onFbLogin(LiveData<User> currentUser);
    }
}
