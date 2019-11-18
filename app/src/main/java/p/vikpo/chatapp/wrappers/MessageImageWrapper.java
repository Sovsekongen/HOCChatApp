package p.vikpo.chatapp.wrappers;

import p.vikpo.chatapp.wrappers.MessageWrapper;

public class MessageImageWrapper extends MessageWrapper
{
   private String messageBitmapUrl;

   public MessageImageWrapper()
   {

   }

    public MessageImageWrapper(String messageUser, String messageText, String messageUserId,
                               long messageTimer, String messageAvatarUrl, String messageBitmapUrl)
    {
        super(messageUser, messageText, messageUserId, messageTimer, messageAvatarUrl);
        this.messageBitmapUrl = messageBitmapUrl;
    }

    public String getMessageBitmapUrl()
    {
        return messageBitmapUrl;
    }

    public void setMessageBitmapUrl(String messageBitmapUrl)
    {
        this.messageBitmapUrl = messageBitmapUrl;
    }
}
