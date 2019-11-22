package p.vikpo.chatapp.presenters;

import android.app.Activity;
import android.content.Intent;

import p.vikpo.chatapp.contracts.MainContract;
import p.vikpo.chatapp.interactors.FirebaseUserInteractor;
import p.vikpo.chatapp.routers.MainRouter;

public class MainPresenter implements MainContract.Presentor, MainContract.InteractorOutput
{
    private FirebaseUserInteractor interactor;
    private MainRouter mainRouter;

    public MainPresenter(Activity activity)
    {
        interactor = new FirebaseUserInteractor(this);
        mainRouter = new MainRouter(activity);
    }

    @Override
    public void onDestroy()
    {
        mainRouter.unregister();
        mainRouter = null;

        interactor.unregister();
        interactor = null;
    }

    /**
     * Handles logging in with an intent given. The only information im interested in at this point
     * is the chatroom name since the rest of the information is no longer nedded.
     * @param intent the intent that contains the chatroom string
     */
    @Override
    public void isLoggedIn(Intent intent)
    {
        String chatroom = "";
        if(intent != null)
        {
            chatroom = intent.getStringExtra("chatroom");
        }
        if(interactor.isUserLoggedIn())
        {
            mainRouter.startChat(chatroom);
        }
        else
        {
            mainRouter.startLogin(chatroom);
        }
    }
}
