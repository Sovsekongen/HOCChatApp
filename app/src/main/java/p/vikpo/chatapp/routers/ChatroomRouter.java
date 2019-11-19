package p.vikpo.chatapp.routers;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.ChatroomContract;
import p.vikpo.chatapp.views.activities.CameraActivity;
import p.vikpo.chatapp.views.fragments.ChatroomFragment;
import p.vikpo.chatapp.views.fragments.ChatroomListFragment;

public class ChatroomRouter implements ChatroomContract.RouterActivity
{
    private static final int REQUEST_RETURN_IMAGE = 2010;
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
        Fragment chatFragment = ChatroomFragment.newInstance();
        Bundle chatroomBundle = new Bundle();

        chatroomBundle.putString("chatroomName", title);
        chatFragment.setArguments(chatroomBundle);

        startFragment(chatFragment);
    }

    @Override
    public void startCameraIntent(Fragment parent)
    {
        Intent cameraIntent = new Intent(activity, CameraActivity.class);
        parent.startActivityForResult(cameraIntent, REQUEST_RETURN_IMAGE);
    }

    /**
     * Function for instantiating the function that handles the proper reaction when the back button
     * is pressed in each chatroom.
     */
    @Override
    public void onBackCallback(Fragment fragment)
    {
        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                startFragment(ChatroomListFragment.newInstance());
            }
        };

        activity.getOnBackPressedDispatcher().addCallback(fragment, callback);
    }

    private void startFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ChatRoomFragmentContainer, fragment)
                .addToBackStack("listFragment")
                .commit();
    }
}
