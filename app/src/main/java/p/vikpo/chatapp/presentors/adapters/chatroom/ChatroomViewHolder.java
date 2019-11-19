package p.vikpo.chatapp.presentors.adapters.chatroom;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import p.vikpo.chatapp.interactors.FirebaseImageStorage;

/**
 * ChatroomViewHolder for storing the ChatroomLayout view and handling binding of information.
 */
public class ChatroomViewHolder extends RecyclerView.ViewHolder
{
    private ChatroomLayout chatroomLayout;
    private static final String IMAGE_LOCATION = "images/";

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
     * Bind function which binds the information received from the parameters.
     * @param name of the user
     * @param date the string date of the message
     * @param message the text of the message
     * @param imageUrl the location of the image to be bound in firestore
     */
    void bind(String name, String date, String message, String imageUrl)
    {
        if(!message.equals(""))
        {
            chatroomLayout.setParams(name, date, message);
        }
        else
        {
            chatroomLayout.setParams(name, date);
            bindImage(imageUrl);
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

    private void bindImage(String bitmapUrl)
    {
        FirebaseImageStorage imageStorage = new FirebaseImageStorage();
        imageStorage.getImage(IMAGE_LOCATION + bitmapUrl, chatroomLayout::setMessageImage);
    }
}
