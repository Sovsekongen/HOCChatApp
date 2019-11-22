package p.vikpo.chatapp.interactors.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import p.vikpo.chatapp.contracts.LoginContract;

public class GoogleInteractor implements LoginContract.Interactor
{
    private static final String TAG ="ChatApp - GoogleInteractor";

    private FirebaseAuth mAuth;
    private LoginContract.InteractorOutput output;

    public GoogleInteractor(LoginContract.InteractorOutput output)
    {
        mAuth = FirebaseAuth.getInstance();
        this.output = output;
    }

    @Override
    public void unregister()
    {
        output = null;
    }

    /**
     * This functions registers the google user with firebase.
     * The method is called from onActivityResult when the google signInActivity returns a corresponding
     * GoogleSignInAccount it is registered with firebase.
     * @param acct the GoogleSignInAccount to be authenticated with firebase.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, Activity activity)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, task ->
        {
            if (task.isSuccessful())
            {
                Log.e(TAG, "Login Success being called");
                output.onLoginSuccess();
            }
            else
            {
                output.onLoginError(task.getException().toString());
            }
        });
    }

    /**
     * Method being passed to the MainActivity class that handles what happends when the google
     * sign-in intent gives a result.
     * @param data the intent returned from the Google sign-in intent
     * @param activity parent activity
     */
    public void onActivityResult(Intent data, Activity activity)
    {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try
        {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if(account != null)
            {
                firebaseAuthWithGoogle(account, activity);
            }
        }
        catch (ApiException e)
        {
            Log.w(TAG, "Google sign in failed", e);
        }
    }
}
