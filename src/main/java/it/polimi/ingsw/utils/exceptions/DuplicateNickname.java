package it.polimi.ingsw.utils.exceptions;

public class DuplicateNickname extends CommonException {
    
    private final String nickname;
    public DuplicateNickname(String nickname){
        super("Found duplicate nickname : " + nickname);
        this.nickname = nickname;
    }
    
    public String getNickname() {
        return nickname;
    }
}
