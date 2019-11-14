package p.vikpo.chatapp.comms.chatroomList;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter class for displaying the different chatrooms.
 */
public class ChatroomListAdapter extends RecyclerView.Adapter<ChatroomListViewHolder>
{
    /**
     *
     */
    public interface OnItemClickListener
    {
        /**
         * OnItemClickListener for the different chatrooms in the RecyclerView
         * @param item the ChatroomListLayout item that is clicked
         */
        void onItemClick(ChatroomListLayout item);
    }

    private List<String> data;
    private OnItemClickListener listener;

    /**
     * Construtor for contructing the RecyvlerView.Adapter
     * @param data the data to be inputted into the list - currently Strings later on ChatroomWrapperObjects
     * @param listener the OnClickListener for the adapter
     */
    public ChatroomListAdapter(List<String> data, OnItemClickListener listener)
    {
        this.data = data;
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
        ChatroomListLayout chatroomListLayout = new ChatroomListLayout(parent.getContext());
        chatroomListLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ChatroomListViewHolder(chatroomListLayout);
    }

    /**
     * When the ViewHolder is bound fill the ChatroomListViewHolder-class with information for displaying.
     * @param holder the ViewHolder whos responsibility it is to show the information
     * @param position the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ChatroomListViewHolder holder, int position)
    {
        holder.bind(data.get(position), listener);
    }

    /**
     * Gets the number of items in the Recycler
     * @return number of RecyclerItems
     */
    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
