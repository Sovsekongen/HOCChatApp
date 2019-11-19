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
public class ChatroomFragment extends Fragment
{
    private static final String TAG = "ChatApp - Chatroom Fragment";
    private static final String CHANNEL_ID = "";
    private static final int REQUEST_RETURN_IMAGE = 2010;
    private static final String IMAGE_LOCATION = "images/";

    private EditText inputBox;
    private FloatingActionButton activateCameraButton, sendButton;
    private RecyclerView recyclerView;

    private FirebaseUser mUser;
    private FirebaseChatroomInteractor firebaseConn;
    private FirestoreRecyclerAdapter<MessageImageWrapper, ChatroomViewHolder> adapter;
    private AvatarViewModel avatarViewModel;

    private String document = "";

    public static ChatroomFragment newInstance()
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
        activateCameraButton.setOnClickListener(v1 -> showDialog());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //createNotificationChannel();
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

    /**
     * Access the bundle which the ChatroomFragment was created with. Creates the proper FirebaseChatroomInteractor
     * based on the document given.
     */
    private void getChatroomDocument()
    {
        Bundle chatroomBundle = getArguments();

        if(chatroomBundle != null)
        {
            document = chatroomBundle.getString("chatroomName");
        }

        avatarViewModel = ViewModelProviders.of(getActivity()).get(AvatarViewModel.class);
        firebaseConn = new FirebaseChatroomInteractor(document, avatarViewModel, this);
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

            inputBox.setText("");
        }
    };

    /**
     * Function for instantiating the function that handles the proper reaction when the back button
     * is pressed in each chatroom.
     */
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

    /**
     * Shows the dialog for choosing how to import image.
     */
    private void showDialog()
    {
        Intent cameraIntent = new Intent(getContext(), CameraActivity.class);
        startActivityForResult(cameraIntent, REQUEST_RETURN_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Request: " + requestCode + ", Result: " + resultCode);

        /*
         * Handles accessing information given from the camera activity. Takes the bitmap, uploads
         * it to the FirestoreStorage and adds the message with the correct location in the FirestoreStorage
         */
        if (requestCode == REQUEST_RETURN_IMAGE)
        {
            String imageTitle = mUser.getUid() + System.currentTimeMillis();
            FirebaseStorageInteractor imageStorage = new FirebaseStorageInteractor();

            imageStorage.uploadImage(IMAGE_LOCATION + imageTitle, data.getParcelableExtra("messageImage"));
            firebaseConn.addMessage(
                    new MessageImageWrapper(mUser.getDisplayName(),
                            inputBox.getText().toString(),
                            mUser.getUid(),
                            System.currentTimeMillis(),
                            mUser.getPhotoUrl().toString(),
                            imageTitle));
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
}