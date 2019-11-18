package p.vikpo.chatapp.wrappers;

public class ChatroomWrapper
{

    private String name, description;
    private long lastMessage;
    private boolean newMessage;

    public ChatroomWrapper(String name, String description, long lastMessage, boolean newMessage)
    {
        this.name = name;
        this.description = description;
        this.lastMessage = lastMessage;
        this.newMessage = newMessage;
    }

    public ChatroomWrapper()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getLastMessage()
    {
        return lastMessage;
    }

    public void setLastMessage(long lastMessage)
    {
        this.lastMessage = lastMessage;
    }

    public boolean isNewMessage()
    {
        return newMessage;
    }

    public void setNewMessage(boolean newMessage)
    {
        this.newMessage = newMessage;
    }
}
