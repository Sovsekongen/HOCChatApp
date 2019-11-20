package p.vikpo.chatapp.presenters;

import android.app.Activity;

import p.vikpo.chatapp.contracts.NotificationContract;
import p.vikpo.chatapp.interactors.NotificationInteractor;
import p.vikpo.chatapp.routers.NotificationRouter;

public class NotificationPresenter implements NotificationContract.Presenter
{
    private NotificationRouter router;
    private NotificationInteractor interactor;
    private String document;

    public NotificationPresenter(Activity activity, String document)
    {
        initChannel();

        router = new NotificationRouter(activity);
        interactor = new NotificationInteractor(activity);

        this.document = document;
    }

    private void initChannel()
    {
        interactor.initChannel();
    }

    public void showNotification()
    {
        interactor.showNotification(router.getChatroomIntent(document), document);
    }

    @Override
    public void onDestroy()
    {
        router.unregister();
        router = null;
        interactor.unregister();
        interactor = null;
    }
}
