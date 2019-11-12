package p.vikpo.chatapp.comms.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel
{
    private MutableLiveData<FirebaseUser> user;

    public LiveData<FirebaseUser> getUser()
    {
        if(user == null)
        {
            user = new MutableLiveData<>();
            user.setValue(FirebaseAuth.getInstance().getCurrentUser());
        }

        return user;
    }

    public void setUser(FirebaseUser newUser)
    {
        if(newUser != null)
        {
            if(user == null)
            {
                user = new MutableLiveData<>();
            }

            user.setValue(newUser);
        }
    }

    /*
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

    /*
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
    }*/
}