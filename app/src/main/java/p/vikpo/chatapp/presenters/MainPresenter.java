package p.vikpo.chatapp.presenters;

import android.app.Activity;

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

    @Override
    public void isLoggedIn()
    {
        if(interactor.isUserLoggedIn())
        {
            mainRouter.startChat();
        }
        else
        {
            mainRouter.startLogin();
        }
    }
}
