package p.vikpo.chatapp.presenters.chatroom;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.interactors.FirebaseUserInteractor;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;
import p.vikpo.chatapp.routers.ChatroomRouter;

public class ChatroomActivityPresenter implements ChatroomContract.PresenterActivity
{
    private FirebaseUserInteractor mUserHandler;
    private ChatroomRouter router;
    private AvatarViewModel avatarViewModel;

    /**
     * Instansiates the presenter for ChatroomActivity with the relevant classes.
     * @param activity the activity passed along as the parent.
     */
    public ChatroomActivityPresenter(AppCompatActivity activity)
    {
        avatarViewModel = ViewModelProviders.of(activity).get(AvatarViewModel.class);
        mUserHandler = new FirebaseUserInteractor(avatarViewModel);
        router = new ChatroomRouter(activity);
    }

    /**
     * Contacts the router to call the next fragment to the fragment container in the ChatroomActivity.
     * @param launchIntent Intent carrying the needed information for launching the next room.
     */
    public void startFragment(Intent launchIntent)
    {
        mUserHandler.addUserToDB();
        router.loadFragment(launchIntent);
        mUserHandler.loadAllAvatars();
    }

    /**
     * Function for destroying the given resources for stopping possible memory leak.
     */
    @Override
    public void onDestroy()
    {
        router.unregister();
        router = null;
        mUserHandler = null;
        avatarViewModel = null;
    }
}
