package p.vikpo.chatapp.entities;

import java.util.Map;

public class UserWrapper
{
    private String mUrl, mUid;
    private Map<String, Boolean> mHasPermission;

    public UserWrapper(String mName, String mUid)
    {
        this.mUrl = mName;
        this.mUid = mUid;
    }

    public UserWrapper(String mUrl, String mUid, Map<String, Boolean> mHasPermissionn)
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

    public Map<String, Boolean> getmHasPermission()
    {
        return mHasPermission;
    }

    public void setmHasPermission(Map<String, Boolean> mHasPermission)
    {
        this.mHasPermission = mHasPermission;
    }
}