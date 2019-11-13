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

public class ChatroomAdapter extends FirestoreRecyclerAdapter<MessageWrapper, ChatroomViewHolder>
{
    private String userId;
    private final int MESSAGE_IN_VIEW_TYPE  = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;
    private final String TAG = "ChatApp - Chatroom Adapter";

    public ChatroomAdapter(Query query, String userId)
    {
        super(new FirestoreRecyclerOptions
                .Builder<MessageWrapper>()
                .setQuery(query, MessageWrapper.class)
                .build());
        Log.e(TAG, userId);
        this.userId = userId;
    }

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

    @Override
    protected void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position, @NonNull MessageWrapper model)
    {
        holder.bind(model.getMessageUser(),
                new Date(model.getMessageTimer()).toString(),
                model.getMessageText());

        ImageDownloader imageDownloader = new ImageDownloader(holder::bindAvatar);
        imageDownloader.execute(model.getMessageAvatarUrl());
    }

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

    @Override
    public void onError(FirebaseFirestoreException e)
    {
        Log.e(TAG, "Encountered Error", e);
    }
}
