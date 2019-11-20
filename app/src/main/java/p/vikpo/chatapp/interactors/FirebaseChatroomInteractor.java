package p.vikpo.chatapp.interactors;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import p.vikpo.chatapp.entities.MessageWrapper;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroom.ChatroomAdapter;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroomList.ChatroomListAdapter;

/**
 * Class for handling communication with FirebaseFirestore. Handles the adapters and retrieving and
 * updating information in the database.
 */
public class FirebaseChatroomInteractor
{
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mDatabase;
    private String document;
    private AvatarViewModel avatarViewModel;
    private Fragment parentFragment;

    private static final String COLLECTION_CHATROOM = "chatrooms";
    private static final String DOCUMENT_SUFFIX = "Messages";
    private static final String DOCUMENT_FIELD_LAST = "lastMessage";
    private static final String DOCUMENT_FIELD_NEW = "newMessage";
    private static final String DOCUMENT_FIELD_TIMER = "messageTimer";
    private static final String TAG = "ChatApp - Firebase Connection";
    private static final int PAGE_LIMIT = 50;

    /**
     * No-arg construtor for initializing the class.
     */
    public FirebaseChatroomInteractor()
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public FirebaseChatroomInteractor(String document, AvatarViewModel avatarViewModel, Fragment parentFragment)
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        this.document = document;
        this.avatarViewModel = avatarViewModel;
        this.parentFragment = parentFragment;
    }

    /**
     * Constructor for initializing the class for a given document. Used for handling specific chatroom
     * messages.
     * @param document the MessageDocument for which the class is initialized.
     */
    public FirebaseChatroomInteractor(String document)
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        this.document = document;
    }

    /**
     * Adds a message to the given document.
     * @param message the message to be added to the database.
     */
    public void addMessage(MessageWrapper message)
    {
        mDatabase.collection(document + DOCUMENT_SUFFIX).add(message);
        updateChatroomNew();
    }

    /**
     * When a new message is sent from the chatroom set the status of the chatroom to new and
     * update when the last message was received. Currently works really bad due to it not being for
     * each individual user - looking into adding a user specific DB-entry.
     */
    private void updateChatroomNew()
    {
        DocumentReference docReference = mDatabase.collection(COLLECTION_CHATROOM)
                .document(translateTitle(document));

        docReference.update(DOCUMENT_FIELD_LAST, System.currentTimeMillis());
        docReference.update(DOCUMENT_FIELD_NEW, true);
    }

    /**
     * When the chatroom has been opened make it appear as seen. Same critiques as above.
     */
    public void updateChatroomSeen()
    {
        DocumentReference docReference = mDatabase.collection(COLLECTION_CHATROOM)
                .document(translateTitle(document));

        docReference.update(DOCUMENT_FIELD_NEW, false);
    }

    /**
     * Handles creating the adapter for handling the messages. It is ordered by the time the messages
     * are received and limited to loading 50 on first try.
     * @return a new Chatroom adapter for the given messages.
     */
    public ChatroomAdapter getChatroomMessageAdapter()
    {
        ChatroomAdapter adapter;
        Query query = mDatabase.collection(document + DOCUMENT_SUFFIX)
                .orderBy(DOCUMENT_FIELD_TIMER, Query.Direction.DESCENDING)
                .limit(PAGE_LIMIT);

        adapter = new ChatroomAdapter(query, mUser.getUid(), avatarViewModel, parentFragment);

        return adapter;
    }

    /**
     * Updates the query when the limit of the recyclerview is reached. The new query will load PAGE_LIMIT
     * extra messages each time the function is called.
     * @param adapter the adapter that needs to be updated.
     */
    public void updateChatroomQuery(ChatroomAdapter adapter)
    {
        adapter.getQuery().get().addOnSuccessListener(documentSnapshots ->
        {
            Query next = mDatabase.collection(document + DOCUMENT_SUFFIX)
                    .orderBy(DOCUMENT_FIELD_TIMER, Query.Direction.DESCENDING)
                    .limit(PAGE_LIMIT * ((documentSnapshots.size() / PAGE_LIMIT) + 1));
            adapter.updateQuery(next);
        });
    }

    /**
     * Handles creating the adapter for handling loading the chatrooms for the recycler list.
     * @param listener the on click listener for handling the actions which happends onclick.
     * @return a new ChatroomList adapter for the chatrooms.
     */
    public ChatroomListAdapter getChatroomListAdapter(ChatroomListAdapter.OnItemClickListener listener)
    {
        Query query = mDatabase.collection(COLLECTION_CHATROOM)
                .orderBy(DOCUMENT_FIELD_LAST, Query.Direction.DESCENDING);

        return new ChatroomListAdapter(query, listener);
    }

    /**
     * Methods for translating the titles of the chatrooms to the document id's in the db.
     * @param document the title of the chatroom in the app
     * @return the document name in the database
     */
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
