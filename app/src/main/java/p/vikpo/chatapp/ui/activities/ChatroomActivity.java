package p.vikpo.chatapp.ui.activities;

import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.ui.fragments.ChatroomListFragment;

/**
 * This class handles the ChatroomActivity and contains an UI-container for fragments,
 * namely the ChatroomListFragment and the ChatroomFragment.
 */
public class ChatroomActivity extends AppCompatActivity
{
    private static final String TAG = "ChatApp - Chatroom Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTransition();
        setContentView(R.layout.activity_chat_room);

        Log.e(TAG, "Launched Chatroom Activity");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.ChatRoomFragmentContainer, ChatroomListFragment.newInstance());
        fragmentTransaction.commit();
    }

    private void setTransition()
    {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade(Fade.IN));
        getWindow().setAllowReturnTransitionOverlap(true);
    }
}
