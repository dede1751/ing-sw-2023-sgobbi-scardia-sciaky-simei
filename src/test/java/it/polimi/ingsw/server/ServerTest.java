package it.polimi.ingsw.server;

import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.view.messages.CreateLobbyMessage;
import it.polimi.ingsw.view.messages.DebugMessage;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.tui.TUI;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

public class ServerTest {
    
    @Nested
    public class ServerDebugTest {
        
        @Test
        public void Test1() {
            try {
                LocalServer server = new LocalServer();
                
                LocalClient client1 = new LocalClient(server, new TUI());
                server.update(client1, new CreateLobbyMessage(2, "Roberto"));
                
                LocalClient client2 = new LocalClient(server, new TUI());
                server.update(client2, new JoinLobbyMessage(0, "Luca"));
                
                server.update(client1, new DebugMessage("Paolo", "Roberto"));
            }
            catch( RemoteException e ) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
