package p.vikpo.chatapp.ui.fragments;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.session.ImageDownloader;

public class ChatRoomFragment extends Fragment
{
    private static final String TAG = "ChatApp - Chatroom Fragment";
    private TextView email;
    private ImageView avatar;

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
        Log.e(TAG, "ChatRoomFragment View Created");

        email = v.findViewById(R.id.eMail);
        avatar = v.findViewById(R.id.avatar);

        loadCurrentUser(FirebaseAuth.getInstance().getCurrentUser());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadCurrentUser(FirebaseUser user)
    {
        if(user != null)
        {
            email.setText(user.getDisplayName());

            ImageDownloader imageDownload = new ImageDownloader(output -> avatar.setImageBitmap(output));
            imageDownload.execute(user.getPhotoUrl().toString());
        }
    }
}