package p.vikpo.chatapp.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.entities.ChatroomWrapper;
import p.vikpo.chatapp.presenters.chatroom.ChatroomListPresenter;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroomList.ChatroomListViewHolder;

/**
 * Fragment for containing the different chatrooms.
 */
public class ChatroomListFragment extends Fragment
{
    private static final String TAG = "ChatApp - List Fragment";

    private SwipeRefreshLayout pullToRefresh;
    private FirestoreRecyclerAdapter<ChatroomWrapper, ChatroomListViewHolder> adapter;

    public static ChatroomListFragment newInstance()
    {
        return new ChatroomListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_chatroom_list, container, false);

        ChatroomListPresenter presenter = new ChatroomListPresenter((AppCompatActivity) getActivity());
        adapter = presenter.getAdapter();

        RecyclerView chatroomView = v.findViewById(R.id.chatroom_view);
        pullToRefresh = v.findViewById(R.id.chatroom_swipe_view);
        pullToRefresh.setOnRefreshListener(() ->
        {
            adapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });

        chatroomView.setHasFixedSize(true);
        chatroomView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatroomView.setAdapter(adapter);

        return v;
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
