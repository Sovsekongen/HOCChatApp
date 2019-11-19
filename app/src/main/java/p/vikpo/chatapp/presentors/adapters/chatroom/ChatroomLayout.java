package p.vikpo.chatapp.presentors.adapters.chatroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import p.vikpo.chatapp.R;

/**
 * ChatroomLayout is a WrapperClass for storing the different UI-elements for each message and handles
 * the correct layout.xml file based on the ViewType determined in the ChatroomAdapter.
 */
public class ChatroomLayout extends ConstraintLayout
{
    private TextView name, date, message;
    private ImageView avatar, messageImage;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;
    private final int MESSAGE_IN_IMAGE = 3;
    private  final int MESSAGE_OUT_IMAGE = 4;
    private static final String TAG = "ChatApp - ChatroomLayout";

    /**
     * Construtor for inflating the view and initialising the UI-elemntes.
     * @param context current context
     * @param viewType the ViewType received from the ChatroomAdapter
     */
    public ChatroomLayout(Context context, int viewType)
    {
        super(context);

        switch(viewType)
        {
            case MESSAGE_IN_IMAGE:
                inflate(context, R.layout.chatroom_message_image, this);
                initUIImage();
                break;
            case MESSAGE_OUT_IMAGE:
                inflate(context, R.layout.chatroom_message_image_mirror, this);
                initUIImage();
                break;
            case MESSAGE_OUT_VIEW_TYPE:
                inflate(context, R.layout.chatroom_message_item_mirror, this);
                initUIMessage();
                break;
            default:
                inflate(context, R.layout.chatroom_message_item, this);
                initUIMessage();
        }
    }

    /**
     * Binds the parameters data to the UI-elements
     * @param name of the user
     * @param date the string date of the message
     * @param message the text of the message
     */
    public void setParams(String name, String date, String message)
    {
        this.name.setText(name);
        this.date.setText(date);
        this.message.setText(message);
    }

    /**
     * Binds the parameters data to the UI-elements
     * @param name of the user
     * @param date the string date of the message
     */
    public void setParams(String name, String date)
    {
        this.name.setText(name);
        this.date.setText(date);
    }

    /**
     * Binds the bitmap to the ImageView in the UI
     * @param avatar avatar to be bound to the ImageView for each message
     */
    public void setAvatar(Bitmap avatar)
    {
        this.avatar.setImageBitmap(avatar);
    }

    /**
     * Binds the bitmap to the ImageView in the UI
     * @param image image to be bound to the ImageView for each ImageMessage
     */
    public void setMessageImage(Bitmap image)
    {
        this.messageImage.setImageBitmap(image);
    }

    /**
     * Method for initializing UI based on which view is selected
     */
    private void initUIMessage()
    {
        name = findViewById(R.id.chatroom_message_item_name);
        date = findViewById(R.id.chatroom_message_item_date);
        message = findViewById(R.id.chatroom_message_item_text);
        avatar = findViewById(R.id.chatroom_message_item_avatar);
    }

    /**
     * Method for initializing UI based on which view is selected
     */
    private void initUIImage()
    {
        name = findViewById(R.id.chatroom_message_image_name);
        date = findViewById(R.id.chatroom_message_image_date);
        avatar = findViewById(R.id.chatroom_message_image_avatar);
        messageImage = findViewById(R.id.chatroom_message_image_image);
    }
}
