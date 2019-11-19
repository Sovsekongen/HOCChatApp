package p.vikpo.chatapp.presenters.chatroom;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.interactors.FirebaseUserInteractor;
import p.vikpo.chatapp.routers.ChatroomRouter;

public class ChatroomActivityPresenter implements ChatroomContract.PresenterActivity
{
    private FirebaseUserInteractor mUserHandler;
    private ChatroomRouter router;

    public ChatroomActivityPresenter(AppCompatActivity activity)
    {
        mUserHandler = new FirebaseUserInteractor();
        mUserHandler.addUserToDB();
        router = new ChatroomRouter(activity);
    }

    public void startFragment(Intent launchIntent)
    {
        router.loadFragment(launchIntent);
    }

    @Override
    public void onDestroy()
    {
        router.unregister();
        router = null;
    }
}
