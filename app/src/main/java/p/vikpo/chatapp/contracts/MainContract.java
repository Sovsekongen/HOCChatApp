package p.vikpo.chatapp.contracts;

/**
 * Contract for making sure the elements concerning the main-activity dosent leak memory and has the
 * right functions.
 */
public interface MainContract
{
    interface Presentor extends Contract.Presenter
    {

    }

    interface Interactor extends Contract.Interactor
    {

    }

    interface InteractorOutput
    {
        void isLoggedIn();
    }

    interface Router extends Contract.Router
    {
        void startLogin();
        void startChat();
    }
}
