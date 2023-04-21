package it.polimi.ingsw.Server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.CreateLobbyMessage;
import it.polimi.ingsw.view.messages.DebugMessage;
import it.polimi.ingsw.view.messages.LobbyInformation;
import it.polimi.ingsw.view.tui.TUI;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {
    
    @Nested
    public class ServerDebugTest{
        
        @Test
        public void Test1() {
            try {
                View tui = new TUI();
                Server server = new LocalServer();
                Client client = new LocalClient(server, tui);
                
                server.register(client);
                GameController.Response t = server.update(new CreateLobbyMessage(new LobbyInformation(1, "Paolo"), "Roberto", tui.getClientID()));
                System.err.println(t.msg());
                GameController.Response r = server.update(new DebugMessage("Paolo", "Roberto", tui.getClientID()));
                System.err.println(r.msg());
                assertEquals(0, r.status());
            }
            catch( RemoteException e ) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
