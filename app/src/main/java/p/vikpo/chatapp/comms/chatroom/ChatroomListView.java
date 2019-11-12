package p.vikpo.chatapp.comms.chatroom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import p.vikpo.chatapp.R;

public class ChatroomListView extends ConstraintLayout
{
    private TextView title;

    public ChatroomListView(Context context)
    {
        super(context);
        inflate(context, R.layout.chatroom_list_item, this);
        title = findViewById(R.id.chatroom_list_item_title);
    }

    public ChatroomListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.chatroom_list_item, this);
        title = findViewById(R.id.chatroom_list_item_title);
    }

    public TextView getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title.setText(title);
    }
}
