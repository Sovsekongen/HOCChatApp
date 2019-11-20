package p.vikpo.chatapp.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.presenters.chatroom.ChatroomPresenter;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroom.ChatroomAdapter;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroom.ChatroomViewHolder;
import p.vikpo.chatapp.entities.MessageImageWrapper;

/**
 * Fragment for containing each individual fragment - currently only supports a single chatroom.
 */
public class ChatroomFragment extends Fragment implements ChatroomContract.ChatroomView
{
    private static final String TAG = "ChatApp - Chatroom Fragment";
    private static final int REQUEST_RETURN_IMAGE = 2010;

    private EditText inputBox;
    private SwipeRefreshLayout refreshLayout;
    private FirestoreRecyclerAdapter<MessageImageWrapper, ChatroomViewHolder> adapter;
    private ChatroomPresenter presenter;

    public static ChatroomFragment newInstance()
    {
        return new ChatroomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_chatroom, container, false);

        presenter = new ChatroomPresenter(
                (AppCompatActivity) getActivity(),
                this,
                getChatroomDocument(),
                this);

        adapter = presenter.getAdapter();

        RecyclerView recyclerView = v.findViewById(R.id.chatroom_recycler);
        refreshLayout = v.findViewById(R.id.chatroom_swipe_view);
        inputBox = v.findViewById(R.id.chatroom_inputbox);

        FloatingActionButton sendButton = v.findViewById(R.id.chatroom_message_fab);
        FloatingActionButton activateCameraButton = v.findViewById(R.id.chatroom_camera_fab);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        sendButton.setOnClickListener(presenter.sendButtonOnClick);
        activateCameraButton.setOnClickListener(presenter.cameraButtonOnClick);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnLayoutChangeListener((v1, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) ->
        {
            if(bottom < oldBottom)
            {
                recyclerView.postDelayed(() -> recyclerView.smoothScrollToPosition(0), 100);
            }
        });

        refreshLayout.setOnRefreshListener(() ->
                {
                    presenter.updateChatroomAdapter((ChatroomAdapter) adapter);
                    setRefresh(false);
                });

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
    public void setRefresh(boolean val)
    {
        refreshLayout.setRefreshing(val);
    }

    @Override
    public String getInputBox()
    {
        return inputBox.getText().toString();
    }
}