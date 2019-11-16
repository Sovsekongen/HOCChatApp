package p.vikpo.chatapp.comms.chatroomList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ChatroomListViewHolder for storing the ChatroomListLayout view and handling binding of information.
 */
public class ChatroomListViewHolder extends RecyclerView.ViewHolder
{
    private ChatroomListLayout chatroomListLayout;

    public ChatroomListViewHolder(@NonNull ChatroomListLayout chatroomListLayout)
    {
        super(chatroomListLayout);
        this.chatroomListLayout = chatroomListLayout;
    }

    /**
     * Binds the the title nad onClickListener to the ChatroomListLayout
     * @param title the title of the chatroom
     * @param listener the onClickListener for determening what happends when the chatroom is clicked.
     */
    public void bind(String title, String description, final ChatroomListAdapter.OnItemClickListener listener)
    {
        chatroomListLayout.setParams(title, description);
        chatroomListLayout.setOnClickListener(v -> listener.onItemClick(chatroomListLayout));
    }
}
