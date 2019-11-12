package p.vikpo.chatapp.session;

import android.graphics.Bitmap;

import com.facebook.AccessToken;

import java.util.Date;

public class User
{
    private String email, id;
    private Bitmap avatar;
    private AccessToken accessToken;

    public User()
    {

    }

    public User(String email, String id, Bitmap avatar, AccessToken accessToken)
    {
        this.email = email;
        this.id = id;
        this.avatar = avatar;
        this.accessToken = accessToken;
    }

    public User(User copyUser)
    {
        this.email = copyUser.email;
        this.id = copyUser.id;
        this.avatar = copyUser.avatar;
        this.accessToken = copyUser.accessToken;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Bitmap getAvatar()
    {
        return avatar;
    }

    public void setAvatar(Bitmap avatar)
    {
        this.avatar = avatar;
    }

    public AccessToken getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken)
    {
        this.accessToken = accessToken;
    }

    @Override
    public String toString()
    {
        return this.email + ": Id= " + this.id;
    }
}
