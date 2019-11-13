package p.vikpo.chatapp.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.chatroom.ChatroomAdapter;
import p.vikpo.chatapp.comms.chatroom.ChatroomViewHolder;
import p.vikpo.chatapp.session.message.MessageWrapper;

public class ChatRoomFragment extends Fragment
{
    private static final String TAG = "ChatApp - Chatroom Fragment";
    private EditText inputBox;
    private FloatingActionButton activateCameraButton, sendButton;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private FirebaseUser mUser;
    private FirebaseFirestore mDatabase;
    private FirestoreRecyclerAdapter<MessageWrapper, ChatroomViewHolder> adapter;
    private Query query;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);
        Log.e(TAG, "View Created");

        recyclerView = v.findViewById(R.id.chatroom_recycler);
        inputBox = v.findViewById(R.id.chatroom_inputbox);
        sendButton = v.findViewById(R.id.chatroom_message_fab);
        activateCameraButton = v.findViewById(R.id.chatroom_camera_fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sendButton.setOnClickListener(sendButtonOnClick);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseFirestore.getInstance();

        query = mDatabase.collection("messages").orderBy("messageTimer").limit(50);
        query.addSnapshotListener((queryDocumentSnapshots, e) ->
        {
            if(e != null)
            {
                Log.e(TAG, "Encountered Query Failure", e);
            }

            if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty())
            {
                Log.e(TAG, "Updated query");
            }
            else
            {
                Log.e(TAG, "Something went wrong...");
                Log.e(TAG, queryDocumentSnapshots.getDocuments().toString());
            }
        });

        adapter = new ChatroomAdapter(query, mUser.getUid());

        recyclerView.setAdapter(adapter);

        return v;
    }

    private void initUI()
    {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

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

            mDatabase.collection("messages")
                    .add(new MessageWrapper(
                    mUser.getDisplayName(),
                    inputBox.getText().toString(),
                    mUser.getUid(),
                    System.currentTimeMillis(),
                    mUser.getPhotoUrl().toString()));

            inputBox.setText("");
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }
}