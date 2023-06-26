package it.polimi.ingsw;

import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.socket.ServerStub;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Main entry point for the client application.
 */
public class AppClient {
    
    private static View view;
    
    // Regex for IPv4 address, 4 repeats of '(?:\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])' which matches 0-255
    private static final String byteRegex = "(?:\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])";
    private static final Pattern ipv4Pattern =
            Pattern.compile(String.format("%s\\.%s\\.%s\\.%s", byteRegex, byteRegex, byteRegex, byteRegex));
    
    /**
     * Unused private constructor to appease Javadoc.
     */
    private AppClient() {
    }
    
    /**
     * Client's entry point
     *
     * @param args unused
     *
     * @throws RemoteException   Should an error occur in RMI comunication, the program will stop.
     * @throws NotBoundException If the "server" object is not found in the RMI register.
     */
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Choose the type of user interface: [GUI/TUI]");
        while( true ) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim().toUpperCase();
            
            if( input.equals("GUI") ) {
                view = new GUI();
                break;
            }else if( input.equals("TUI") ) {
                view = new TUI();
                break;
            }
        }
        
        System.out.print("Insert the server's IPV4 address:");
        String ip;
        while( true ) {
            System.out.print("\n>>  ");
            ip = scanner.next().trim().toUpperCase();
            if( ip.equals("LOCALHOST") ) {
                try {
                    ip = Inet4Address.getLocalHost().getHostAddress();
                }
                catch( UnknownHostException e ) {
                    throw new RuntimeException(e);
                }
                break;
            }
            Matcher matcher = ipv4Pattern.matcher(ip);
            
            if( matcher.matches() ) {
                break;
            }
        }
        
        System.out.print("Choose the type of network protocol: [RMI/SOCKET]");
        // noinspection InfiniteLoopStatement
        while( true ) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim().toUpperCase();
            
            if( input.equals("RMI") ) {
                runRMI(ip);
            }else if( input.equals("SOCKET") ) {
                runSocket(ip);
            }
        }
    }
    
    /**
     * Run the client application using the RMI protocol. <br>
     * Looks for a registry on default port 1099 of the server.
     *
     * @param ip The server's IP address.
     *
     * @throws RemoteException   In case of communication error.
     * @throws NotBoundException If the "server" object is not found in the RMI registry
     */
    private static void runRMI(String ip) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ip); // use default port 1099
        Server server = (Server) registry.lookup("myshelfie_server");
        
        view.setClient(new LocalClient(server));
        view.run();
    }
    
    /**
     * Run the client application using the socket protocol. <br>
     * Communication happens on default port 23456.
     *
     * @param ip The server's IP address.
     *
     * @throws RemoteException In case of communication error.
     */
    private static void runSocket(String ip) throws RemoteException {
        ServerStub serverStub = new ServerStub(ip, 23456);
        LocalClient client = new LocalClient(serverStub);
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
    
    /**
     * Get the singleton instance of the view running in the client application.
     *
     * @return The view instance.
     */
    public static View getViewInstance() {
        return view;
    }
}
