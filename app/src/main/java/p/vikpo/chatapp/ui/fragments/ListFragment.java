package p.vikpo.chatapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.comms.chatroom.ChatroomAdapter;
import p.vikpo.chatapp.comms.chatroom.ChatroomWrapper;

public class ListFragment extends Fragment
{
    private List<String> wrapperList = Arrays.asList("Hestemakker", "Hestemisser", "Hestetroels");
    private ChatroomAdapter chatroomAdapter;
    private RecyclerView chatroomView;
    private RecyclerView.LayoutManager layoutManager;

    public static ListFragment newInstance()
    {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        chatroomAdapter = new ChatroomAdapter(wrapperList);

        chatroomView = v.findViewById(R.id.chatroom_view);
        chatroomView.setHasFixedSize(true);
        chatroomView.setLayoutManager(layoutManager);
        chatroomView.setAdapter(chatroomAdapter);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }
}
