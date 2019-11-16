package p.vikpo.chatapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.chatroomList.ChatroomListAdapter;
import p.vikpo.chatapp.comms.chatroomList.ChatroomListViewHolder;
import p.vikpo.chatapp.comms.chatroomList.ChatroomWrapper;
import p.vikpo.chatapp.session.FirebaseChatroom;

/**
 * Fragment for containing the different chatrooms.
 */
public class ChatroomListFragment extends Fragment
{
    private static final String TAG = "ChatApp - List Fragment";
    private RecyclerView chatroomView;
    private FirestoreRecyclerAdapter<ChatroomWrapper, ChatroomListViewHolder> adapter;
    private FirebaseChatroom firebaseChatroom;

    public static ChatroomListFragment newInstance()
    {
        return new ChatroomListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        initUI(v);
        initAdapter();

        return v;
    }

    private void initUI(View v)
    {
        chatroomView = v.findViewById(R.id.chatroom_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatroomView.setHasFixedSize(true);
        chatroomView.setLayoutManager(layoutManager);

        firebaseChatroom = new FirebaseChatroom();
    }

    private void initAdapter()
    {
        adapter = firebaseChatroom.getChatroomListAdapter(chatroomChoiceListener);
        chatroomView.setAdapter(adapter);
    }

    private ChatroomListAdapter.OnItemClickListener chatroomChoiceListener = v ->
            openChatRoom(v.getTitle().getText().toString());

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Opens the chosen chatroom fragment - or at this point just the ChatroomFragment.
     * @param title the title of the chatroom - currently what initializes the list based on a set of strings.
     */
    private void openChatRoom(String title)
    {
        Log.e(TAG, "Opening " + title);

        Bundle chatroomBundle = new Bundle();
        chatroomBundle.putString("chatroomName", title);

        FirebaseChatroom firebaseChatroom = new FirebaseChatroom(title);
        firebaseChatroom.updateChatroomSeen();

        Fragment chatFragment = ChatroomFragment.newInstance();
        chatFragment.setArguments(chatroomBundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ChatRoomFragmentContainer, chatFragment)
                .addToBackStack("listFragment")
                .commit();
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
