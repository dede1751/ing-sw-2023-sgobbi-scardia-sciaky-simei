package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class VirtualView implements PropertyChangeListener {
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public static void main(String[] args) {
        VirtualView view = new VirtualView();
        GameModel model = new GameModel(2, 0, 0);
        GameController controller = new GameController(model, view);
        
        view.addPropertyChangeListener(controller);
        view.login("Andrea");
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    private void login(String nickname) {
        this.pcs.firePropertyChange("LOGIN", "", nickname);
    }
    
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch( evt.getPropertyName() ) {
            case "ADD PLAYER" -> System.out.println("View notified about player");
            default -> System.out.println("Unsupported property change");
        }
    }
}
