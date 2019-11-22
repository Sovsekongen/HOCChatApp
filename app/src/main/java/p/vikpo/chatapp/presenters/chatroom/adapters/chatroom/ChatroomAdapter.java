package p.vikpo.chatapp.presenters.chatroom.adapters.chatroom;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;

import p.vikpo.chatapp.entities.MessageImageWrapper;
import p.vikpo.chatapp.entities.MessageWrapper;
import p.vikpo.chatapp.interactors.FirebaseUserInteractor;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;


/**
 * Adapter class for displaying messages when opening a chatroom.
 */
public class ChatroomAdapter extends FirestoreRecyclerAdapter<MessageImageWrapper, ChatroomViewHolder>
{
    private String userId;
    private FirebaseUserInteractor mUserHandler;
    private Fragment parentFragment;
    private Query query;
    private final String TAG = "ChatApp - Chatroom Adapter";

    /**
     * Constructor taking a Firestore Query containing a query for the right document and the ID of
     * the user.
     * @param query firestore query referencing the document to be shown in the chat
     * @param userId userid of the current user
     */
    public ChatroomAdapter(Query query, String userId, AvatarViewModel avatarViewModel, Fragment parentFragment)
    {
        super(new FirestoreRecyclerOptions
                .Builder<MessageImageWrapper>()
                .setQuery(query, MessageImageWrapper.class)
                .build());

        this.userId = userId;
        this.mUserHandler = new FirebaseUserInteractor(avatarViewModel);
        this.parentFragment = parentFragment;
        this.query = query;
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
     * Furthermore it looks the avatar up in a ViewModel and if it doesn't exist it is downloaded.
     * @param holder the ViewHolder who's responsibility it is to show the information
     * @param position the position in the list
     * @param model the MessageWrapper containing the information to be displayed by the ViewHolder.
     */
    @Override
    protected void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position, @NonNull MessageImageWrapper model)
    {
        setAvatar(model, holder);
        holder.bind(model.getMessageUser(), new Date(model.getMessageTimer()).toString(),
                model.getMessageText(), model.getMessageBitmapUrl());
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
        if(getItem(position).getMessageBitmapUrl() != null)
        {
            if(getItem(position).getMessageUserId().equals(userId))
            {
                return 4;
            }
            return 3;
        }
        else
        {
            if(getItem(position).getMessageUserId().equals(userId))
            {
                return 2;
            }
            return 1;
        }
    }

    /**
     * For discovering potential errors.
     * @param e the FirebaseFirestoreException to be printed.
     */
    @Override
    public void onError(@NonNull FirebaseFirestoreException e)
    {
        Log.e(TAG, "Encountered Error", e);
    }

    /**
     * Sets the avatar for the current message.
     * @param model the model which has access to the url
     * @param holder the holder for accessing the UI
     */
    private void setAvatar(MessageWrapper model, ChatroomViewHolder holder)
    {
        LiveData<HashMap<String, Bitmap>> userAvatarMap = mUserHandler.getAvatarMap(
                model.getMessageUserId());

        userAvatarMap.observe(parentFragment, stringBitmapHashMap ->
                holder.bindAvatar(stringBitmapHashMap.get(model.getMessageUserId())));
    }

    public void updateQuery(Query query)
    {
        Log.e(TAG, "Updating options with the new query.");
        updateOptions(new FirestoreRecyclerOptions
                .Builder<MessageImageWrapper>()
                .setQuery(query, MessageImageWrapper.class)
                .build());
    }

    public Query getQuery()
    {
        return query;
    }
}
