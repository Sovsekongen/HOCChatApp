package p.vikpo.chatapp.contracts;

import android.content.Intent;

import androidx.fragment.app.Fragment;

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
        void loadFragment(String title);
        void startCameraIntent(Fragment parent);
        void onBackCallback(Fragment fragment);
    }

    interface PresenterList
    {
        void onDestroy();
    }

    interface ChatroomView
    {
        void setInputBox(String text);
        String getInputBox();
    }
}
