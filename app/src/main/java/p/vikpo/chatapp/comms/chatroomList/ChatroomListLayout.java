package p.vikpo.chatapp.comms.chatroomList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import p.vikpo.chatapp.R;

/**
 * ChatroomListLayout is a WrapperClass for storing the different UI-elements for each chatroom and handles
 * inflating the layout.xml file.
 */
public class ChatroomListLayout extends ConstraintLayout
{
    private TextView title;

    /**
     * Construtor for inflating the view and initialising the UI-elemntes.
     * @param context current context
     */
    public ChatroomListLayout(Context context)
    {
        super(context);
        inflate(context, R.layout.chatroom_list_item, this);
        title = findViewById(R.id.chatroom_list_item_title);
    }

    public ChatroomListLayout(Context context, AttributeSet attrs, int defStyleAttr)
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
