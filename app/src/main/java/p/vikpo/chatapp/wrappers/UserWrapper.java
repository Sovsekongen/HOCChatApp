package p.vikpo.chatapp.wrappers;

import java.util.Map;

public class UserWrapper
{
    private String mUrl, mUid;
    private Map<String, Boolean> mHasPermission;
    private Map<String, Boolean> mNeedPermission;

    public UserWrapper(String mName, String mUid)
    {
        this.mUrl = mName;
        this.mUid = mUid;
    }

    public UserWrapper(String mUrl, String mUid, Map<String, Boolean> mHasPermission, Map<String, Boolean> mNeedPermission)
    {
        this.mUrl = mUrl;
        this.mUid = mUid;
        this.mHasPermission = mHasPermission;
        this.mNeedPermission = mNeedPermission;
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

    public Map<String, Boolean> getmNeedPermission()
    {
        return mNeedPermission;
    }

    public void setmNeedPermission(Map<String, Boolean> mNeedPermission)
    {
        this.mNeedPermission = mNeedPermission;
    }
}
