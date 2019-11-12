package p.vikpo.chatapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.login.LoginViewModel;
import p.vikpo.chatapp.ui.activities.ChatRoomActivity;

public class LoginFragment extends Fragment
{
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String ID = "id";
    private LoginViewModel loginViewModel;


    public static LoginFragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        isUserLoggedIn();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, loginViewModel.getFacebookCallback());
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        LoginButton fbLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        fbLoginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE, ID));
        fbLoginButton.setFragment(this);

        isUserLoggedIn();

        Log.e("ChatApp", "Created Login View");
    }

    private void isUserLoggedIn()
    {
        if(loginViewModel.isLoggedIn())
        {
            Log.e("ChatApp", "User is logged in.");

            Intent launchChatActivity = new Intent(getContext(), ChatRoomActivity.class);
            startActivity(launchChatActivity);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
