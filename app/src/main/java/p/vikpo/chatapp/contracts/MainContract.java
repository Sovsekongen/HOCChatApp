package p.vikpo.chatapp.contracts;

import android.content.Intent;

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
        void isLoggedIn(Intent intent);
    }

    interface Router extends Contract.Router
    {
        void startLogin(String chatroom);
        void startChat(String chatroom);
    }
}
