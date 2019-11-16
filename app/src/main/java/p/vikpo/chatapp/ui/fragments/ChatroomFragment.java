package p.vikpo.chatapp.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.chatroom.ChatroomViewHolder;
import p.vikpo.chatapp.comms.chatroom.MessageWrapper;
import p.vikpo.chatapp.session.FirebaseChatroom;

/**
 * Fragment for containing each individual fragment - currently only supports a single chatroom.
 */
public class ChatroomFragment extends Fragment
{
    private static final String TAG = "ChatApp - Chatroom Fragment";

    private EditText inputBox;
    private FloatingActionButton activateCameraButton, sendButton;
    private RecyclerView recyclerView;

    private FirebaseUser mUser;
    private FirebaseChatroom firebaseConn;
    private FirestoreRecyclerAdapter<MessageWrapper, ChatroomViewHolder> adapter;

    private String document = "";

    static ChatroomFragment newInstance()
    {
        return new ChatroomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);
        Log.e(TAG, "View Created");

        /*
         * Initialize the UI and the recycler based on a specific document - currently only supports on document
         */
        initUI(v);
        getChatroomDocument();
        initAdapter();

        onBackCallback();

        return v;
    }

    /**
     * Initializes the UI elements based on the view and starts the firebase authentication.
     * @param v the view that contains the initialize able elements.
     */
    private void initUI(View v)
    {
        recyclerView = v.findViewById(R.id.chatroom_recycler);
        inputBox = v.findViewById(R.id.chatroom_inputbox);
        sendButton = v.findViewById(R.id.chatroom_message_fab);
        activateCameraButton = v.findViewById(R.id.chatroom_camera_fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sendButton.setOnClickListener(sendButtonOnClick);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    /**
     * Inits the recycler adapter and based on a query.
     * The query queries the firebase DB for information based on a specific given document.
     */
    private void initAdapter()
    {
        adapter = firebaseConn.getChatroomMessageAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getChatroomDocument()
    {
        Bundle chatroomBundle = getArguments();

        if(chatroomBundle != null)
        {
            document = chatroomBundle.getString("chatroomName");
        }

        firebaseConn = new FirebaseChatroom(document);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Handles on click for the send button. If the input is empty prompt the user with a "is empty"-toast
     * If it isn't empty send a message to the firebase database.
     */
    private View.OnClickListener sendButtonOnClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String message = inputBox.getText().toString();

            if(TextUtils.isEmpty(message))
            {
                Toast.makeText(getContext(), "Input text is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseConn.addMessage(new MessageWrapper(
                    mUser.getDisplayName(),
                    inputBox.getText().toString(),
                    mUser.getUid(),
                    System.currentTimeMillis(),
                    mUser.getPhotoUrl().toString()));

            firebaseConn.updateChatroomNew();

            inputBox.setText("");
        }
    };

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

    private void onBackCallback()
    {
        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.ChatRoomFragmentContainer, ChatroomListFragment.newInstance())
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}