package it.polimi.ingsw;

import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.socket.ClientSkeleton;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server main class
 */
public class AppServer {
    
    /**
     * Server entry point
     * @param args ignored
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new LocalServer();
        }
        catch( RemoteException e ) {
            System.err.println("Unable to start server. Shutting down...");
            return;
        }
        
        Thread rmiThread = new Thread(() -> {
            try {
                startRMI(server);
            }
            catch( RemoteException e ) {
                System.err.println("Cannot start RMI. This protocol will be disabled.");
            }
        });
        rmiThread.start();
        
        Thread socketThread = new Thread(() -> {
            try {
                startSocket(server);
            }
            catch( RemoteException e ) {
                System.err.println("Cannot start socket. This protocol will be disabled.");
            }
        });
        socketThread.start();
        
        try {
            rmiThread.join();
            socketThread.join();
        }
        catch( InterruptedException e ) {
            System.err.println("No connection protocol available. Exiting...");
        }
    }
    
    private static void startRMI(Server server) throws RemoteException {
        LocateRegistry.createRegistry(1099);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("server", server);
    }
    
    public static void startSocket(Server server) throws RemoteException {
        try( ExecutorService executorService = Executors.newCachedThreadPool();
             ServerSocket serverSocket = new ServerSocket(1234) ) {
            //noinspection InfiniteLoopStatement
            while( true ) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                        
                        server.register(clientSkeleton);
                        //noinspection InfiniteLoopStatement
                        while( true ) {
                            clientSkeleton.receive(server);
                        }
                    }
                    catch( RemoteException e ) {
                        System.err.println("Cannot receive from client. Closing this connection...");
                    }
                    finally {
                        try {
                            socket.close();
                        }
                        catch( IOException e ) {
                            System.err.println("Cannot close socket");
                        }
                    }
                });
            }
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot start socket server", e);
        }
    }
}
