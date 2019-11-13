package p.vikpo.chatapp.session.message;

public class MessageWrapper
{
    private String messageUser, messageText, messageUserId, messageAvatarUrl;
    private long messageTimer;

    public MessageWrapper()
    {

    }

    public MessageWrapper(String messageUser, String messageText, String messageUserId, long messageTimer, String messageAvatarUrl)
    {
        this.messageUser = messageUser;
        this.messageText = messageText;
        this.messageUserId = messageUserId;
        this.messageTimer = messageTimer;
        this.messageAvatarUrl = messageAvatarUrl;
    }

    public String getMessageAvatarUrl()
    {
        return messageAvatarUrl;
    }

    public void setMessageAvatarUrl(String messageAvatarUrl)
    {
        this.messageAvatarUrl = messageAvatarUrl;
    }

    public String getMessageUser()
    {
        return messageUser;
    }

    public void setMessageUser(String messageUser)
    {
        this.messageUser = messageUser;
    }

    public String getMessageText()
    {
        return messageText;
    }

    public void setMessageText(String messageText)
    {
        this.messageText = messageText;
    }

    public String getMessageUserId()
    {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId)
    {
        this.messageUserId = messageUserId;
    }

    public long getMessageTimer()
    {
        return messageTimer;
    }

    public void setMessageTimer(long messageTimer)
    {
        this.messageTimer = messageTimer;
    }
}
