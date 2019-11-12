package p.vikpo.chatapp.comms.chatroom;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class ChatroomWrapper extends RecyclerView.ViewHolder
{
    private String title;
    ChatroomListView itemView;

    public ChatroomWrapper(@NonNull ChatroomListView itemView)
    {
        super(itemView);
        this.itemView = itemView;
    }

    public String getTitle()
    {
        return title;
    }
}
