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
        View view = askViewType();
        
        if ( askUseRMI() ) {
            runRMI(view);
        } else {
            runSocket(view);
        }
    }
    
    private static View askViewType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the type of user interface: [GUI/TUI]");
        
        while (true) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if ( input.equals("GUI") ) {
                return new GUI();
            } else if ( input.equals("TUI") ) {
                return new TUI();
            }
        }
    }
    
    private static boolean askUseRMI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the type of network protocol: [RMI/SOCKET]");
        
        while (true) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if ( input.equals("RMI") ) {
                return true;
            } else if ( input.equals("SOCKET") ) {
                return false;
            }
        }
    }
    
    public static void runRMI(View view) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        Server server = (Server) registry.lookup("server");
        
        LocalClient client = new LocalClient(server, view);
        client.connectServer();
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
    }
    
}
