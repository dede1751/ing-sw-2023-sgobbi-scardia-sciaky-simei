package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.view.View;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

public class GUI extends View {
    
    @Override
    public void setAvailableLobbies(List<LobbyController.LobbyView> lobbies) {
    
    }
    
    @Override
    public void update(GameModelView model, GameModel.Event evt) {
    
    }
    
    @Override
    public void update(ModelMessage<?> msg){
        //TODO
    }
    
    
    
    
    @Override
    public void run() {
    
    }
    
}
