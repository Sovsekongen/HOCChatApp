package p.vikpo.chatapp.contracts;

public interface Contract
{
    interface Router
    {
        void unregister();
    }

    interface Presenter
    {
        void onDestroy();
    }

    interface Interactor
    {
        void unregister();
    }
}
