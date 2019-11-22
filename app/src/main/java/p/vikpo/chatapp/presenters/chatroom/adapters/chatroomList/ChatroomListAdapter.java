package p.vikpo.chatapp.presenters.chatroom.adapters.chatroomList;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import p.vikpo.chatapp.entities.ChatroomWrapper;

/**
 * Adapter class for displaying the different chatrooms.
 */
public class ChatroomListAdapter extends FirestoreRecyclerAdapter<ChatroomWrapper, ChatroomListViewHolder>
{
    public interface OnItemClickListener
    {
        /**
         * OnItemClickListener for the different chatrooms in the RecyclerView
         * @param item the ChatroomListLayout item that is clicked
         */
        void onItemClick(ChatroomListLayout item);
    }
    private OnItemClickListener listener;
    private static final int MESSAGE_NEW_VIEW_TYPE = 1;
    private final String TAG = "ChatApp - Chatroom List Adapter";

    /**
     *
     * @param query the query that the Adapter should be initialized with.
     * @param listener the OnClickListener for the adapter
     */
    public ChatroomListAdapter(Query query, OnItemClickListener listener)
    {
        super(new FirestoreRecyclerOptions
                .Builder<ChatroomWrapper>()
                .setQuery(query, ChatroomWrapper.class)
                .build());
        this.listener = listener;
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
    public ChatroomListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ChatroomListLayout chatroomListLayout = new ChatroomListLayout(parent.getContext(), viewType);
        chatroomListLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ChatroomListViewHolder(chatroomListLayout);
    }

    /**
     * When the ViewHolder is bound fill the ChatroomListViewHolder-class with information for displaying.
     * @param holder the ViewHolder whos responsibility it is to show the information
     * @param position the position in the list
     * @param model the model that the adapter gets the data from.
     */
    @Override
    protected void onBindViewHolder(@NonNull ChatroomListViewHolder holder, int position, @NonNull ChatroomWrapper model)
    {
        holder.bind(model.getName(), model.getDescription(), listener);
    }

    @Override
    public int getItemViewType(int position)
    {
        if(getItem(position).isNewMessage())
        {
            return MESSAGE_NEW_VIEW_TYPE;
        }

        return 0;
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
}
