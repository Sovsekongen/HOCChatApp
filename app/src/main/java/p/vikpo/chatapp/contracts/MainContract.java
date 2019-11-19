package p.vikpo.chatapp.contracts;

public interface MainContract
{
    interface Presentor
    {
        void onDestroy();
    }

    interface Interactor
    {
        void unregister();
    }

    interface InteractorOutput
    {
        void isLoggedIn();
    }

    interface Router
    {
        void unregister();
        void startLogin();
        void startChat();
    }
}
