package it.polimi.ingsw;

import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.network.socket.ServerStub;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class AppClient {
    
    public static void main( String[] args ) throws RemoteException, NotBoundException {
        System.out.println("\n\n$$\\      $$\\            $$$$$$\\  $$\\                 $$\\  $$$$$$\\  $$\\");
        System.out.println("$$$\\    $$$ |          $$  __$$\\ $$ |                $$ |$$  __$$\\ \\__|");
        System.out.println("$$$$\\  $$$$ |$$\\   $$\\ $$ /  \\__|$$$$$$$\\   $$$$$$\\  $$ |$$ /  \\__|$$\\  $$$$$$\\");
        System.out.println("$$\\$$\\$$ $$ |$$ |  $$ |\\$$$$$$\\  $$  __$$\\ $$  __$$\\ $$ |$$$$\\     $$ |$$  __$$\\");
        System.out.println("$$ \\$$$  $$ |$$ |  $$ | \\____$$\\ $$ |  $$ |$$$$$$$$ |$$ |$$  _|    $$ |$$$$$$$$ |");
        System.out.println("$$ |\\$  /$$ |$$ |  $$ |$$\\   $$ |$$ |  $$ |$$   ____|$$ |$$ |      $$ |$$   ____|");
        System.out.println("$$ | \\_/ $$ |\\$$$$$$$ |\\$$$$$$  |$$ |  $$ |\\$$$$$$$\\ $$ |$$ |      $$ |\\$$$$$$$\\");
        System.out.println("\\__|     \\__| \\____$$ | \\______/ \\__|  \\__| \\_______|\\__|\\__|      \\__| \\_______|");
        System.out.println("             $$\\   $$ |");
        System.out.println("             \\$$$$$$  |");
        System.out.println("              \\______/\n");
        
        Scanner scanner = new Scanner(System.in);
        View view;
        
        System.out.println("Choose the type of user interface: [GUI/TUI]");
        while (true) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if ( input.equals("GUI") ) {
                view = new GUI();
                break;
            } else if ( input.equals("TUI") ) {
                view = new TUI();
                break;
            }
        }
        
        System.out.println("Choose the type of network protocol: [RMI/SOCKET]");
        while (true) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if ( input.equals("RMI") ) {
                runRMI(view);
                break;
            } else if ( input.equals("SOCKET") ) {
                runSocket(view);
                break;
            }
        }
    }
    
    public static void runRMI(View view) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        Server server = (Server) registry.lookup("server");
        
        LocalClient client = new LocalClient(server, view);
        client.connectServer();
        view.run();
    }
    
    public static void runSocket(View view) throws RemoteException {
        ServerStub serverStub = new ServerStub("localhost", 1234);
        LocalClient client = new LocalClient(serverStub, view);
        
        client.connectServer();
        new Thread(() -> {
            while(true) {
                try {
                    serverStub.receive(client);
                } catch (RemoteException e) {
                    System.err.println("Cannot receive from server. Stopping...");
                    try {
                        serverStub.close();
                    } catch (RemoteException ex) {
                        System.err.println("Cannot close connection with server. Halting...");
                    }
                    System.exit(1);
                }
            }
        }).start();
        view.run();
    }
    
}
