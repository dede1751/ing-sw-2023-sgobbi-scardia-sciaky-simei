package it.polimi.ingsw.utils.exceptions;

public class DuplicateListener extends CommonException {
    
    private final String name;
    
    public DuplicateListener(String name) {
        super("Found duplicate listener : " + name);
        this.name = name;
    }
    
    public String getNickname() {
        return name;
    }
}
