package it.polimi.ingsw.view.messages;

public abstract class ViewMsg<Payload> {
    
    private final Payload payload;
    private final String nickname;
    
    private final int clientId;
    public ViewMsg(Payload payload, String playerNick, int clientId){
        this.payload = payload;
        this.nickname = playerNick;
        this.clientId = clientId;
    }
    public Payload getPayload(){
        return this.payload;
    };
    public abstract Class<?> getMessageType();
    public String getPlayerNickname(){
        return this.nickname;
    };
    public int getClientId(){
        return this.clientId;
    }
}
