package p.vikpo.chatapp.comms.chatroomList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ChatroomListViewHolder for storing the ChatroomListLayout view and handling binding of information.
 */
class ChatroomListViewHolder extends RecyclerView.ViewHolder
{
    private ChatroomListLayout itemView;

    ChatroomListViewHolder(@NonNull ChatroomListLayout itemView)
    {
        super(itemView);
        this.itemView = itemView;
    }

    /**
     * Binds the the title nad onClickListener to the ChatroomListLayout
     * @param title the title of the chatroom
     * @param listener the onClickListener for determening what happends when the chatroom is clicked.
     */
    void bind(String title, final ChatroomListAdapter.OnItemClickListener listener)
    {
        itemView.setTitle(title);
        itemView.setOnClickListener(v -> listener.onItemClick(itemView));
    }
}
