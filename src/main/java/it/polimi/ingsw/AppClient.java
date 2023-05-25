package it.polimi.ingsw;

import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.network.Server;
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
    
    
    private static View view;
    
    public static View getViewInstance(){
        return view;
    }
    
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Choose the type of user interface: [GUI/TUI]");
        while( true ) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if( input.equals("GUI") ) {
                view = new GUI();
                break;
            }else if( input.equals("TUI") ) {
                view = new TUI();
                break;
            }
        }
        
        System.out.println("Choose the type of network protocol: [RMI/SOCKET]");
        // noinspection InfiniteLoopStatement
        while( true ) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if( input.equals("RMI") ) {
                runRMI(view);
            }else if( input.equals("SOCKET") ) {
                runSocket(view);
            }
        }
    }
    
    private static void runRMI(View view) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        Server server = (Server) registry.lookup("myshelfie_server");
        
        view.setClient(new LocalClient(server, view));
        view.run();
    }
    
    private static void runSocket(View view) throws RemoteException {
        ServerStub serverStub = new ServerStub("localhost", 1234);
        LocalClient client = new LocalClient(serverStub, view);
        view.setClient(client);
        serverStub.setClient(client);
        
        new Thread(() -> {
            while( true ) {
                try {
                    serverStub.receive();
                }
                catch( RemoteException e ) {
                    e.printStackTrace(System.err);
                    System.err.println(e.getMessage() + "\n" + "Cannot receive from server. Stopping...");
                    try {
                        serverStub.close();
                    }
                    catch( RemoteException ex ) {
                        System.err.println("Cannot close connection with server. Halting...");
                    }
                    System.exit(1);
                }
            }
        }).start();
        view.run();
    }
}
