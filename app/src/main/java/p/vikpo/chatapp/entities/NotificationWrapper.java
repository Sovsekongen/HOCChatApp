package p.vikpo.chatapp.entities;

public class NotificationWrapper
{
    private String mUserId, mMessage, mTitle, mChatroomName;

    public NotificationWrapper()
    {

    }

    public NotificationWrapper(String mUserId, String mMessage, String mTitle, String mChatroomName)
    {
        this.mUserId = mUserId;
        this.mMessage = mMessage;
        this.mTitle = mTitle;
        this.mChatroomName = mChatroomName;
    }

    public String getmUserId()
    {
        return mUserId;
    }

    public void setmUserId(String mUserId)
    {
        this.mUserId = mUserId;
    }

    public String getmMessage()
    {
        return mMessage;
    }

    public void setmMessage(String mMessage)
    {
        this.mMessage = mMessage;
    }

    public String getmTitle()
    {
        return mTitle;
    }

    public void setmTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }

    public String getmChatroomName()
    {
        return mChatroomName;
    }

    public void setmChatroomName(String mChatroomName)
    {
        this.mChatroomName = mChatroomName;
    }
}
