package p.vikpo.chatapp.contracts;

import android.content.Intent;

/**
 * Contract for making sure the elements concerning the chatroom-activity dosent leak memory and has the
 * right functions.
 */
public interface ChatroomContract
{
    interface PresenterActivity
    {
        void onDestroy();
    }

    interface RouterActivity
    {
        void unregister();
        void loadFragment(Intent launchIntent);
    }
}
