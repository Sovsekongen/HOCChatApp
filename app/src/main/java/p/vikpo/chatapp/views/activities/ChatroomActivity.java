package p.vikpo.chatapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.presenters.chatroom.ChatroomActivityPresenter;

/**
 * This class handles the ChatroomActivity and contains an UI-container for fragments,
 * namely the ChatroomListFragment and the ChatroomFragment.
 */
public class ChatroomActivity extends AppCompatActivity
{
    private static final String TAG = "ChatApp - Chatroom Activity";
    private ChatroomActivityPresenter presenter;

    /**
     * Implement start loading avatars. Remove the ViewModel or make it work better - is not currently
     * working as intented.
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTransition();
        setContentView(R.layout.activity_chat_room);

        presenter = new ChatroomActivityPresenter(this);
        presenter.startFragment(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Adds the transition between the last activity and possibly this.
     */
    private void setTransition()
    {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade(Fade.IN));
        getWindow().setAllowReturnTransitionOverlap(true);
    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy();
        super.onDestroy();
    }
}
