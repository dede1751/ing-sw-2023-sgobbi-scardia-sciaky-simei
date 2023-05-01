package it.polimi.ingsw.utils.exceptions;

public class NoPlayerWithNickname extends CommonException{
    private final String nickname;
    public NoPlayerWithNickname(String nickname){
        super("There is no player already with name : " + nickname);
        this.nickname = nickname;
    }
    
    public String getNickname() {
        return nickname;
    }
}
