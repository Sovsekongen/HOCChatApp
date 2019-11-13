package p.vikpo.chatapp.comms.chatroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import p.vikpo.chatapp.R;

public class ChatroomLayout extends ConstraintLayout
{
    private TextView name, date, message;
    private ImageView avatar;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;

    public ChatroomLayout(Context context, int viewType)
    {
        super(context);

        if(viewType == MESSAGE_OUT_VIEW_TYPE)
        {
            inflate(context, R.layout.chatroom_message_item_mirror, this);
        }
        else
        {
            inflate(context, R.layout.chatroom_message_item, this);
        }

        name = findViewById(R.id.chatroom_message_item_name);
        date = findViewById(R.id.chatroom_message_item_date);
        message = findViewById(R.id.chatroom_message_item_text);
        avatar = findViewById(R.id.chatroom_message_item_avatar);
    }

    public void setParams(String name, String date, String message)
    {
        this.name.setText(name);
        this.date.setText(date);
        this.message.setText(message);
    }

    public void setAvatar(Bitmap avatar)
    {
        this.avatar.setImageBitmap(avatar);
    }
}
