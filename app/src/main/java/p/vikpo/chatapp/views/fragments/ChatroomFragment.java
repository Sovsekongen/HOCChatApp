package p.vikpo.chatapp.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.presenters.chatroom.ChatroomPresenter;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroom.ChatroomViewHolder;
import p.vikpo.chatapp.views.activities.CameraActivity;
import p.vikpo.chatapp.entities.MessageImageWrapper;
import p.vikpo.chatapp.entities.MessageWrapper;
import p.vikpo.chatapp.interactors.FirebaseChatroomInteractor;
import p.vikpo.chatapp.interactors.FirebaseStorageInteractor;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;

/**
 * Fragment for containing each individual fragment - currently only supports a single chatroom.
 */
public class ChatroomFragment extends Fragment implements ChatroomContract.ChatroomView
{
    private static final String TAG = "ChatApp - Chatroom Fragment";
    private static final int REQUEST_RETURN_IMAGE = 2010;

    private EditText inputBox;
    private FirestoreRecyclerAdapter<MessageImageWrapper, ChatroomViewHolder> adapter;
    private ChatroomPresenter presenter;

    public static ChatroomFragment newInstance()
    {
        return new ChatroomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);

        presenter = new ChatroomPresenter(
                (AppCompatActivity) getActivity(),
                this,
                getChatroomDocument(),
                this);

        adapter = presenter.getAdapter();

        RecyclerView recyclerView = v.findViewById(R.id.chatroom_recycler);
        inputBox = v.findViewById(R.id.chatroom_inputbox);
        FloatingActionButton sendButton = v.findViewById(R.id.chatroom_message_fab);
        FloatingActionButton activateCameraButton = v.findViewById(R.id.chatroom_camera_fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(presenter.sendButtonOnClick);
        activateCameraButton.setOnClickListener(presenter.cameraButtonOnClick);

        return v;
    }

    /**
     * Access the bundle which the ChatroomFragment was created with. Returns the name of the wanted
     * chatroom.
     */
    private String getChatroomDocument()
    {
        Bundle chatroomBundle = getArguments();

        if(chatroomBundle != null)
        {
            return chatroomBundle.getString("chatroomName");
        }

        Toast.makeText(getActivity(), "Failed to receive Chatroom Name.", Toast.LENGTH_SHORT).show();
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult Request: " + requestCode + ", Result: " + resultCode);
        if (requestCode == REQUEST_RETURN_IMAGE)
        {
            Log.e(TAG, "Received result");
            presenter.sendImageMessage(data);
        }
        else
        {
            Toast.makeText(getActivity(), "Unable to get image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if(adapter != null)
        {
            adapter.startListening();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(adapter != null)
        {
            adapter.stopListening();
        }
    }

    @Override
    public void setInputBox(String text)
    {
        inputBox.setText(text);
    }

    @Override
    public String getInputBox()
    {
        return inputBox.getText().toString();
    }
}