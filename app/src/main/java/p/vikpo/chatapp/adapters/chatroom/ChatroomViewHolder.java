package p.vikpo.chatapp.adapters.chatroom;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ChatroomViewHolder for storing the ChatroomLayout view and handling binding of information.
 */
public class ChatroomViewHolder extends RecyclerView.ViewHolder
{
    private ChatroomLayout chatroomLayout;

    /**
     * Construtor for initializing the UI based on the ChatroomLayout
     * @param chatroomLayout the layout to be inflated
     */
    ChatroomViewHolder(@NonNull ChatroomLayout chatroomLayout)
    {
        super(chatroomLayout);
        this.chatroomLayout = chatroomLayout;
    }

    /**
     * Bind function which binds the information received from the mparameters.
     * @param name of the user
     * @param date the string date of the message
     * @param message the text of the message
     */
    void bind(String name, String date, String message)
    {
        if(!message.equals(""))
        {
            chatroomLayout.setParams(name, date, message);
        }
        else
        {
            chatroomLayout.setParams(name, date);
        }
    }

    /**
     * Binds the bitmap to the ImageView in the UI
     * @param avatar avatar to be bound to the ImageView for each message
     */
    void bindAvatar(Bitmap avatar)
    {
        chatroomLayout.setAvatar(avatar);
    }

    void bindImage(Bitmap image)
    {
        chatroomLayout.setMessageImage(image);
    }
}
