package p.vikpo.chatapp.entities;

import java.util.HashMap;
import java.util.Map;

public class UserWrapper
{
    private String mUrl, mUid, mMessageToken;
    private HashMap<String, Boolean> mHasPermission;

    public UserWrapper()
    {

    }

    public UserWrapper(String mName, String mUid)
    {
        this.mUrl = mName;
        this.mUid = mUid;
    }

    public UserWrapper(String mUrl, String mUid, HashMap<String, Boolean> mHasPermission, String mMessageToken)
    {
        this.mUrl = mUrl;
        this.mUid = mUid;
        this.mHasPermission = mHasPermission;
        this.mMessageToken = mMessageToken;
    }

    public UserWrapper(String mUrl, String mUid, HashMap<String, Boolean> mHasPermission)
    {
        this.mUrl = mUrl;
        this.mUid = mUid;
        this.mHasPermission = mHasPermission;
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

    public HashMap<String, Boolean> getmHasPermission()
    {
        return mHasPermission;
    }

    public void setmHasPermission(HashMap<String, Boolean> mHasPermission)
    {
        this.mHasPermission = mHasPermission;
    }

    public String getmMessageToken()
    {
        return mMessageToken;
    }

    public void setmMessageToken(String mMessageToken)
    {
        this.mMessageToken = mMessageToken;
    }
}
