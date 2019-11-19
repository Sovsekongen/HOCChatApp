package p.vikpo.chatapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.interactors.FirebaseUserHandler;
import p.vikpo.chatapp.interactors.viewmodel.AvatarViewModel;
import p.vikpo.chatapp.views.fragments.ChatroomFragment;
import p.vikpo.chatapp.views.fragments.ChatroomListFragment;

/**
 * This class handles the ChatroomActivity and contains an UI-container for fragments,
 * namely the ChatroomListFragment and the ChatroomFragment.
 */
public class ChatroomActivity extends AppCompatActivity
{
    private static final String TAG = "ChatApp - Chatroom Activity";
    private FirebaseUserHandler mUserHandler;
    private AvatarViewModel avatarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTransition();
        setContentView(R.layout.activity_chat_room);

        avatarViewModel = ViewModelProviders.of(this).get(AvatarViewModel.class);
        mUserHandler = new FirebaseUserHandler(avatarViewModel);
        mUserHandler.addUserToDB();

        startFragment();
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
    public void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startFragment()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mUserHandler.loadAllAvatars();

        if(getIntent().getStringExtra("chatroomName") != null)
        {
            Fragment chatFragment = ChatroomFragment.newInstance();
            Bundle chatroomBundle = new Bundle();

            chatroomBundle.putString("chatroomName", getIntent().getStringExtra("chatroomName"));
            chatroomBundle.putParcelable("messageImage", getIntent().getParcelableExtra("messageImage"));

            chatFragment.setArguments(chatroomBundle);

            fragmentTransaction.replace(R.id.ChatRoomFragmentContainer, chatFragment).commit();
        }
        else
        {
            fragmentTransaction.add(R.id.ChatRoomFragmentContainer, ChatroomListFragment.newInstance()).commit();
        }
    }
}
