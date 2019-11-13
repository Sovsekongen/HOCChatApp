package p.vikpo.chatapp.comms.chatroom;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatroomViewHolder extends RecyclerView.ViewHolder
{
    private ChatroomLayout chatroomLayout;

    public ChatroomViewHolder(@NonNull ChatroomLayout chatroomLayout)
    {
        super(chatroomLayout);
        this.chatroomLayout = chatroomLayout;
    }

    public void bind(String name, String date, String message)
    {
        chatroomLayout.setParams(name, date, message);
    }

    public void bindAvatar(Bitmap avatar)
    {
        chatroomLayout.setAvatar(avatar);
    }
}
