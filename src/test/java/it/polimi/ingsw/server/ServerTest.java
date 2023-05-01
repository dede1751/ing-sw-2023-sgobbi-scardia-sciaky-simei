package it.polimi.ingsw.server;

import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.*;
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
                View tui1 = new TUI();
                View tui2 = new TUI();
                LocalServer server = new LocalServer();
                LocalClient client1 = new LocalClient(server, tui1);

                client1.connectServer();

                Response t = server.update(new CreateLobbyMessage(2, "Roberto", tui1.getClientID()));
                System.out.println(t.msg());
                LocalClient client2 = new LocalClient(server, tui2);
                client2.connectServer();
                Response m = server.update(new JoinLobbyMessage( 0, "Luca",
                                                                tui2.getClientID() ));
                
                Response r = server.update(new DebugMessage("Paolo", "Roberto", tui1.getClientID()));
                System.out.println(r.msg());
                assertEquals(0, r.status());
            }
            catch( RemoteException e ) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
