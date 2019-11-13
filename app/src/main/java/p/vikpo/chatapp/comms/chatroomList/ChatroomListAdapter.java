package p.vikpo.chatapp.comms.chatroomList;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatroomListAdapter extends RecyclerView.Adapter<ChatroomListViewHolder>
{
    public interface OnItemClickListener
    {
        void onItemClick(ChatroomListLayout item);
    }

    private List<String> data;
    private OnItemClickListener listener;

    public ChatroomListAdapter(List<String> data, OnItemClickListener listener)
    {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatroomListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ChatroomListLayout chatroomListLayout = new ChatroomListLayout(parent.getContext());
        chatroomListLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ChatroomListViewHolder(chatroomListLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomListViewHolder holder, int position)
    {
        holder.bind(data.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
