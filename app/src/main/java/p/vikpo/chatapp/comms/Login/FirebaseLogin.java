package p.vikpo.chatapp.comms.Login;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseLogin
{
    /**
     * Check to see if the user is logged in - if the user is logged in, start the chatroom activity.
     * If the user isn't logged in continue.
     */
    public static boolean isUserLoggedIn()
    {

        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
