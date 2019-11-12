package p.vikpo.chatapp.comms.chatroom;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import p.vikpo.chatapp.R;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomWrapper>
{
    private List<String> data;

    public ChatroomAdapter(List<String> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public ChatroomWrapper onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ChatroomListView chatroomListView = new ChatroomListView(parent.getContext());
        chatroomListView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ChatroomWrapper(chatroomListView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomWrapper holder, int position)
    {
        holder.itemView.setTitle(data.get(position));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
