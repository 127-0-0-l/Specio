package io.github._127_0_0_l.infra_tg_bot.models;

public class Chat {
    private final long id;
    private ChatState state;
    private Filters filters;
    private int lastMessageId;

    public Chat (long id){
        this.id = id;
    }

    public Chat (long id, ChatState state){
        this(id);
        this.state = state;
    }

    public Chat (long id, ChatState state, Filters filters){
        this(id, state);
        this.filters = filters;
    }

    public long getId (){
        return id;
    }

    public ChatState getState(){
        return state;
    }

    public Filters getFilters(){
        return filters;
    }

    public int getLastMessageId(){
        return lastMessageId;
    }

    public void setState (ChatState state){
        this.state = state;
    }

    public void setFilters (Filters filters){
        this.filters = filters;
    }

    public void setLastMessageId(int messageId){
        lastMessageId = messageId;
    }
}
