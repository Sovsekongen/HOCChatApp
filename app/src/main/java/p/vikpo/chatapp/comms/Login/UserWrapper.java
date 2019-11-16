package p.vikpo.chatapp.comms.Login;

public class UserWrapper
{
    private String mUrl, mUid;

    public UserWrapper(String mName, String mUid)
    {
        this.mUrl = mName;
        this.mUid = mUid;
    }

    public String getmUrl()
    {
        return mUrl;
    }

    public void setmUrl(String mUrl)
    {
        this.mUrl = mUrl;
    }

    public String getmUid()
    {
        return mUid;
    }

    public void setmUid(String mUid)
    {
        this.mUid = mUid;
    }
}
