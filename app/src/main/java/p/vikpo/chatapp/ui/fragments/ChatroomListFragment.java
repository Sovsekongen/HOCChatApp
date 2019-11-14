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

import java.util.Arrays;
import java.util.List;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.chatroomList.ChatroomListAdapter;

/**
 * Fragment for containing the different chatrooms.
 */
public class ChatroomListFragment extends Fragment
{
    private List<String> wrapperList = Arrays.asList("Hestemakker", "Hestemisser", "Hestetroels");
    private static final String TAG = "ChatApp - List Fragment";

    public static ChatroomListFragment newInstance()
    {
        return new ChatroomListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*
         * Initializes the view elements and initializes the ChatroomListAdapter
         */
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ChatroomListAdapter chatroomListAdapter = new ChatroomListAdapter(wrapperList, chatroomChoiceListener);

        RecyclerView chatroomView = v.findViewById(R.id.chatroom_view);
        chatroomView.setHasFixedSize(true);
        chatroomView.setLayoutManager(layoutManager);
        chatroomView.setAdapter(chatroomListAdapter);

        return v;
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

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ChatRoomFragmentContainer, ChatroomFragment.newInstance());
        fragmentTransaction.commit();
    }
}
