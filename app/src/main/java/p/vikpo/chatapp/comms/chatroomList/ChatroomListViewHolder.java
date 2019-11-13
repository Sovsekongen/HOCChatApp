package p.vikpo.chatapp.comms.chatroomList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatroomListViewHolder extends RecyclerView.ViewHolder
{
    private ChatroomListLayout itemView;

    public ChatroomListViewHolder(@NonNull ChatroomListLayout itemView)
    {
        super(itemView);
        this.itemView = itemView;
    }

    public void bind(String title, final ChatroomListAdapter.OnItemClickListener listener)
    {
        itemView.setTitle(title);
        itemView.setOnClickListener(v -> listener.onItemClick(itemView));
    }
}
