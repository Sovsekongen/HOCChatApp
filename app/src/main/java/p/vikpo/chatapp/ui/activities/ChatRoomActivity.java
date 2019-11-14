package p.vikpo.chatapp.ui.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.ui.fragments.ChatroomListFragment;

/**
 * This class handles the ChatroomActivity and contains an UI-container for fragments,
 * namely the ChatroomListFragment and the ChatroomFragment.
 */
public class ChatRoomActivity extends AppCompatActivity
{
    private static final String TAG = "ChatApp - Chatroom Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Log.e(TAG, "Launched Chatroom Activity");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.ChatRoomFragmentContainer, ChatroomListFragment.newInstance());
        fragmentTransaction.commit();
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.ChatRoomFragmentContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
    }*/
}
