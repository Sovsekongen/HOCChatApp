package p.vikpo.chatapp.routers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.contracts.ChatroomContract;
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
}
