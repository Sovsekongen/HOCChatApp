package p.vikpo.chatapp.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import p.vikpo.chatapp.R;

public class SplashFragment extends Fragment
{
    private final Handler delayHandler = new Handler();

    public static SplashFragment newInstance()
    {
        return new SplashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);
        Log.e("ChatApp", "Created Splash View");

        final FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        delayHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                fragmentTransaction.replace(R.id.MainFragmentContainer, LoginFragment.newInstance());
                fragmentTransaction.commit();
            }
        }, 1000);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}
