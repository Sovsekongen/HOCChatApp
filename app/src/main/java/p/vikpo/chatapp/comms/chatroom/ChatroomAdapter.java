package p.vikpo.chatapp.comms.chatroom;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Date;

import p.vikpo.chatapp.session.ImageDownloader;
import p.vikpo.chatapp.session.message.MessageWrapper;


/**
 * Adapter class for displaying messages when opening a chatroom.
 */
public class ChatroomAdapter extends FirestoreRecyclerAdapter<MessageWrapper, ChatroomViewHolder>
{
    private String userId;
    private final int MESSAGE_IN_VIEW_TYPE  = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;
    private final String TAG = "ChatApp - Chatroom Adapter";

    /**
     * Constructor taking a Firestore Query containing a query for the right document and the ID of
     * the user.
     * @param query firestore query referencing the document to be shown in the chat
     * @param userId userid of the current user
     */
    public ChatroomAdapter(Query query, String userId)
    {
        super(new FirestoreRecyclerOptions
                .Builder<MessageWrapper>()
                .setQuery(query, MessageWrapper.class)
                .build());
        Log.e(TAG, userId);
        this.userId = userId;
    }

    /**
     * Inflates the design in the RecylerView based on the ChatroomLayout wrapper containing the
     * design for the constraint layout.
     * @param parent the ViewGroup parent.
     * @param viewType the viewType for choosing how to display the messages
     * @return a new ChatroomViewHolder based on the ChatroomLayout wrapper
     */
    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ChatroomLayout chatroomLayout = new ChatroomLayout(parent.getContext(), viewType);
        chatroomLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ChatroomViewHolder(chatroomLayout);
    }

    /**
     * When the ViewHolder is bound fill the ChatroomViewHolder-class with information for displaying.
     * Furthermore it downloads the corresponding avatar-image.
     * @param holder the ViewHolder whos responsobility it is to show the information
     * @param position the position in the list
     * @param model the MessageWrapper containing the information to be displayed by the ViewHolder.
     */
    @Override
    protected void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position, @NonNull MessageWrapper model)
    {
        holder.bind(model.getMessageUser(),
                new Date(model.getMessageTimer()).toString(),
                model.getMessageText());

        ImageDownloader imageDownloader = new ImageDownloader(holder::bindAvatar);
        imageDownloader.execute(model.getMessageAvatarUrl());
    }

    /**
     * Returns the corresponding ViewType based on the owner of the message. For determening what
     * view to inflate.
     * @param position the position of the current message.
     * @return returns either a 1 if user is now owner of message and a 2 if user is owner og message
     */
    @Override
    public int getItemViewType(int position)
    {
        Log.e(TAG, getItem(position).getMessageUserId() + " " + userId);
        if(getItem(position).getMessageUserId().equals(userId))
        {
            return MESSAGE_OUT_VIEW_TYPE;
        }

        return MESSAGE_IN_VIEW_TYPE;
    }

    /**
     * For discovering potential errors.
     * @param e the FirebaseFirestoreException to be printed.
     */
    @Override
    public void onError(FirebaseFirestoreException e)
    {
        Log.e(TAG, "Encountered Error", e);
    }
}
