package p.vikpo.chatapp.presenters.chatroom.adapters.chatroomList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import p.vikpo.chatapp.R;

/**
 * ChatroomListLayout is a WrapperClass for storing the different UI-elements for each chatroom and handles
 * inflating the layout.xml file.
 */
public class ChatroomListLayout extends ConstraintLayout
{
    private TextView title, description;
    private ImageView chevron;
    /**
     * Construtor for inflating the view and initialising the UI-elemntes.
     * @param context current context
     */
    public ChatroomListLayout(Context context, int viewType)
    {
        super(context);
        inflate(context, R.layout.chatroom_list_item, this);

        title = findViewById(R.id.chatroom_list_item_title);
        description = findViewById(R.id.chatroom_list_item_description);
        chevron = findViewById(R.id.chatroom_list_item_chevron);

        if(viewType == 1)
        {
            chevron.setColorFilter(Color.CYAN);
        }
    }

    public ChatroomListLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.chatroom_list_item, this);
        title = findViewById(R.id.chatroom_list_item_title);
        description = findViewById(R.id.chatroom_list_item_description);
        chevron = findViewById(R.id.chatroom_list_item_chevron);
    }

    public TextView getTitle()
    {
        return title;
    }

    public TextView getDescription()
    {
        return description;
    }

    public void setParams(String title, String description)
    {
        this.title.setText(title);
        this.description.setText(description);
    }
}
