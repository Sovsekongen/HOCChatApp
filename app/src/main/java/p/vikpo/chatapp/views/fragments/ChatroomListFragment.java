package p.vikpo.chatapp.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.interactors.FirebaseUserInteractor;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroomList.ChatroomListAdapter;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroomList.ChatroomListViewHolder;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;
import p.vikpo.chatapp.entities.ChatroomWrapper;
import p.vikpo.chatapp.interactors.FirebaseChatroom;

/**
 * Fragment for containing the different chatrooms.
 */
public class ChatroomListFragment extends Fragment
{
    private static final String TAG = "ChatApp - List Fragment";
    private RecyclerView chatroomView;
    private SwipeRefreshLayout pullToRefresh;
    private FirestoreRecyclerAdapter<ChatroomWrapper, ChatroomListViewHolder> adapter;
    private FirebaseChatroom firebaseChatroom;
    private FirebaseUserInteractor firebaseUserInteractor;
    private AvatarViewModel avatarViewModel;

    public static ChatroomListFragment newInstance()
    {
        return new ChatroomListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_chatroom_list, container, false);

        initUI(v);
        initAdapter();
        addListener();

        avatarViewModel = ViewModelProviders.of(this).get(AvatarViewModel.class);
        firebaseUserInteractor = new FirebaseUserInteractor(avatarViewModel);

        return v;
    }

    private void initUI(View v)
    {
        chatroomView = v.findViewById(R.id.chatroom_view);
        pullToRefresh = v.findViewById(R.id.chatroom_swipe_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatroomView.setHasFixedSize(true);
        chatroomView.setLayoutManager(layoutManager);

        firebaseChatroom = new FirebaseChatroom();
    }

    /**
     * Initializes the adapter given from the FirebaseChatroom class. Adds the adapter to the RecyclerView
     */
    private void initAdapter()
    {
        adapter = firebaseChatroom.getChatroomListAdapter(chatroomChoiceListener);
        chatroomView.setAdapter(adapter);
    }

    /**
     * The onclick listener for handling a click on a chatroom.
     */
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
        /*
         * Initializes variables needed for handling the transaction to the given chatroom.
         */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FirebaseChatroom firebaseChatroom = new FirebaseChatroom(title);
        Fragment chatFragment = ChatroomFragment.newInstance();
        Bundle chatroomBundle = new Bundle();

        chatroomBundle.putString("chatroomName", title);
        firebaseChatroom.updateChatroomSeen();
        chatFragment.setArguments(chatroomBundle);

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

    /**
     * Listener for handling the Pull-to-refresh action in the UI.
     */
    private void addListener()
    {
        pullToRefresh.setOnRefreshListener(() ->
        {
            adapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });
    }
}
