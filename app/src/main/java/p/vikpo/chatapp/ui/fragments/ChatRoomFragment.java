package p.vikpo.chatapp.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.login.LoginViewModel;
import p.vikpo.chatapp.session.ImageDownloader;
import p.vikpo.chatapp.session.User;

public class ChatRoomFragment extends Fragment implements LoginViewModel.FBLoginCallback
{
    private TextView email;
    private ImageView avatar;
    private User currentUser;
    private LoginViewModel loginViewModel;

    public static ChatRoomFragment newInstance()
    {
        return new ChatRoomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
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
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        Log.e("ChatApp", "ChatRoomFragment View Created");

        email = (TextView) v.findViewById(R.id.eMail);
        avatar = (ImageView) v.findViewById(R.id.avatar);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadCurrentUser(User user)
    {
        if(currentUser != null)
        {
            email.setText(user.getEmail());
            avatar.setImageBitmap(user.getAvatar());
        }
    }

    @Override
    public void onFbLogin(LiveData<User> currentUser)
    {
        loadCurrentUser(currentUser.getValue());
    }
}
