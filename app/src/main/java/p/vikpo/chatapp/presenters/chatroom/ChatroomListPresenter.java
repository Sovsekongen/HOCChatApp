package p.vikpo.chatapp.presenters.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.interactors.FirebaseChatroomInteractor;
import p.vikpo.chatapp.presenters.chatroom.adapters.chatroomList.ChatroomListAdapter;
import p.vikpo.chatapp.routers.ChatroomRouter;

public class ChatroomListPresenter implements ChatroomContract.PresenterList
{
    private FirebaseChatroomInteractor firebaseChatroomInteractor;
    private ChatroomRouter router;

    public ChatroomListPresenter(AppCompatActivity activity)
    {
        firebaseChatroomInteractor = new FirebaseChatroomInteractor();
        router = new ChatroomRouter(activity);
    }

    public ChatroomListAdapter getAdapter()
    {
        return firebaseChatroomInteractor.getChatroomListAdapter(item ->
                {
                    firebaseChatroomInteractor.updateChatroomSeen();
                    router.loadFragment(item.getTitle().getText().toString());
                });

    }

    @Override
    public void onDestroy()
    {

    }
}
