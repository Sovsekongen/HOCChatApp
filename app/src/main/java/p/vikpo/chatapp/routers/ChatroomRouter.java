package p.vikpo.chatapp.routers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.interactors.FirebaseChatroomInteractor;
import p.vikpo.chatapp.views.fragments.ChatroomFragment;
import p.vikpo.chatapp.views.fragments.ChatroomListFragment;

public class ChatroomRouter implements ChatroomContract.RouterActivity
{
    private AppCompatActivity activity;

    public ChatroomRouter(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    @Override
    public void unregister()
    {
        activity = null;
    }

    @Override
    public void loadFragment(Intent launchIntent)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

        if(launchIntent.getStringExtra("chatroomName") != null)
        {
            Fragment chatFragment = ChatroomFragment.newInstance();
            Bundle chatroomBundle = new Bundle();

            chatroomBundle.putString("chatroomName", launchIntent.getStringExtra("chatroomName"));
            chatroomBundle.putParcelable("messageImage", launchIntent.getParcelableExtra("messageImage"));

            chatFragment.setArguments(chatroomBundle);

            fragmentTransaction.replace(R.id.ChatRoomFragmentContainer, chatFragment).commit();
        }
        else
        {
            fragmentTransaction.add(R.id.ChatRoomFragmentContainer, ChatroomListFragment.newInstance()).commit();
        }
    }

    /**
     * Opens the chosen chatroom fragment - or at this point just the ChatroomFragment.
     * @param title the title of the chatroom - currently what initializes the list based on a set of strings.
     */
    @Override
    public void loadFragment(String title)
    {
        /*
         * Initializes variables needed for handling the transaction to the given chatroom.
         */
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        FirebaseChatroomInteractor firebaseChatroomInteractor = new FirebaseChatroomInteractor(title);
        Fragment chatFragment = ChatroomFragment.newInstance();
        Bundle chatroomBundle = new Bundle();

        chatroomBundle.putString("chatroomName", title);
        firebaseChatroomInteractor.updateChatroomSeen();
        chatFragment.setArguments(chatroomBundle);

        fragmentTransaction.replace(R.id.ChatRoomFragmentContainer, chatFragment)
                .addToBackStack("listFragment")
                .commit();
    }

    @Override
    public void startCameraIntent()
    {

    }
}
