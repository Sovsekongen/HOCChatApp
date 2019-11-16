package p.vikpo.chatapp.session;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import p.vikpo.chatapp.comms.chatroom.ChatroomAdapter;
import p.vikpo.chatapp.comms.chatroom.MessageWrapper;
import p.vikpo.chatapp.comms.chatroomList.ChatroomListAdapter;

public class FirebaseChatroom
{
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mDatabase;
    private String document;
    private static final String COLLECTION_CHATROOM= "chatrooms";
    private static final String DOCUMENT_SUFFIX = "Messages";
    private static final String DOCUMENT_FIELD_LAST = "lastMessage";
    private static final String DOCUMENT_FIELD_NEW = "newMessage";
    private static final String DOCUMENT_FIELD_TIMER = "messageTimer";
    private static final String TAG = "ChatApp - Firebase Connection";

    public FirebaseChatroom()
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public FirebaseChatroom(String document)
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        this.document = document;
    }

    public void addMessage(MessageWrapper message)
    {
        mDatabase.collection(document + DOCUMENT_SUFFIX).add(message);
    }

    public void updateChatroomNew()
    {
        DocumentReference docReference = mDatabase.collection(COLLECTION_CHATROOM)
                .document(translateTitle(document));

        docReference.update(DOCUMENT_FIELD_LAST, System.currentTimeMillis());
        docReference.update(DOCUMENT_FIELD_NEW, true);
    }

    public void updateChatroomSeen()
    {
        DocumentReference docReference = mDatabase.collection(COLLECTION_CHATROOM)
                .document(translateTitle(document));

        docReference.update(DOCUMENT_FIELD_NEW, false);
    }

    public ChatroomAdapter getChatroomMessageAdapter()
    {
        Query query = mDatabase.collection(document + DOCUMENT_SUFFIX)
                .orderBy(DOCUMENT_FIELD_TIMER)
                .limit(50);

        query.addSnapshotListener((queryDocumentSnapshots, e) ->
        {
            if(e != null)
            {
                Log.e(TAG, "Encountered Query Failure", e);
            }

            if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty())
            {
                Log.e(TAG, "Updated query");
            }
        });

        return new ChatroomAdapter(query, mUser.getUid());
    }

    public ChatroomListAdapter getChatroomListAdapter(ChatroomListAdapter.OnItemClickListener listener)
    {
        Query query = mDatabase.collection(COLLECTION_CHATROOM)
                .orderBy(DOCUMENT_FIELD_LAST, Query.Direction.DESCENDING);
        query.addSnapshotListener((queryDocumentSnapshots, e) ->
        {
            if(e != null)
            {
                Log.e(TAG, "Encountered Query Failure", e);
            }

            if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty())
            {
                Log.e(TAG, "Updated query - " + COLLECTION_CHATROOM);
                Log.e(TAG, queryDocumentSnapshots.getDocuments().toString());
            }
        });

        return new ChatroomListAdapter(query, listener);
    }

    private String translateTitle(String document)
    {
        switch(document)
        {
            case "Music":
                return "music";
            case "Spare Time":
                return "sparetime";
            case "Photography":
                return "photo";
            case "Series":
                return "series";
            default:
                return document;
        }
    }

}
