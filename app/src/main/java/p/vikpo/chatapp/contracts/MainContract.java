package p.vikpo.chatapp.contracts;

/**
 * Contract for making sure the elements concerning the main-activity dosent leak memory and has the
 * right functions.
 */
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
