package it.polimi.ingsw;

import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.socket.ClientSkeleton;
import it.polimi.ingsw.utils.files.ServerLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main entry point for the server application
 */
public class AppServer {
    
    /**
     * Unused private constructor to appease Javadoc.
     */
    private AppServer() {
    }
    
    /**
     * Server entry point. Sets up both communication protocols and waits.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        LocalServer server;
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
    
    /**
     * Starts the RMI protocol, by publishing the server instance on the registry. <br>
     * Server is called "myshelfie_server" and is published on port 1099.
     *
     * @param server The server instance to publish.
     *
     * @throws RemoteException In case of errors binding to the registry.
     */
    private static void startRMI(LocalServer server) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("myshelfie_server", server);
    }
    
    /**
     * Starts the socket protocol, by listening on port 23456.
     *
     * @param server The server instance to use.
     *
     * @throws RemoteException In case of errors starting/running the socket.
     */
    private static void startSocket(LocalServer server) throws RemoteException {
        try( ExecutorService executorService = Executors.newCachedThreadPool();
             ServerSocket serverSocket = new ServerSocket(23456) ) {
            //noinspection InfiniteLoopStatement
            while( true ) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                        
                        //noinspection InfiniteLoopStatement
                        while( true ) {
                            clientSkeleton.receive(server);
                        }
                    }
                    catch( RemoteException e ) {
                        ServerLogger.errorLog(e, "Cannot receive from client. Closing this connection...");
                    }
                    finally {
                        try {
                            socket.close();
                        }
                        catch( IOException e ) {
                            ServerLogger.errorLog(e, "Cannot close socket");
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
