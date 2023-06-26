package it.polimi.ingsw.utils.files;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Class ServerLogger saves log data for the server.
 */
public class ServerLogger {
    
    private static final FileChannel log;
    
    private static final FileChannel errorLog;
    
    static {
        try {
            log = ResourcesManager.openFileWrite(ResourcesManager.serverLoggerDir, "log.txt");
            errorLog = ResourcesManager.openFileWrite(ResourcesManager.serverLoggerDir, "error_log.txt");
        }
        catch( IOException e ) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Write a log message to the log file
     * @param s Message to be logged
     * @param channel Channel to write the message to (either log or errorLog)
     */
    private static void writeLog(String s, FileChannel channel) {
        String logString = Timestamp.from(Instant.now()) + " - " + s + "\n";
        
        try {
            if( channel != null ) {
                channel.write(ByteBuffer.wrap(logString.getBytes(Charset.defaultCharset())));
            }
        }
        catch( IOException e ) {
            System.err.println("Unable to log message : " + s);
            e.printStackTrace(System.err);
        }
    }
    
    /**
     * Write a log message to the log file
     * @param s Message to be logged
     */
    public static void log(String s) {
        ServerLogger.writeLog(s, log);
    }
    
    /**
     * Log a message being sent to a client.
     *
     * @param clientContext String describing the client
     * @param message Message being sent
     */
    public static void messageLog(String clientContext, ModelMessage<?> message) {
        String s = "Updated Client : " + clientContext + " with message type : " + message.getClass().getSimpleName() +
                   "\n";
        ServerLogger.writeLog(s, log);
    }
    
    /**
     * Log an error message
     * @param e Exception being logged
     * @param additionalContext Additional context to be logged
     */
    public static void errorLog(Exception e, String additionalContext) {
        String s = "Server encountered exception of type : " + e.getClass() + "\n";
        String s1 = "Message : " + e.getMessage() + "\n";
        String s2 = "Cause : " + e.getCause();
        
        if( additionalContext != null ) {
            writeLog(s + s1 + s2 + "\n" + additionalContext, errorLog);
        }else {
            writeLog(s + s1 + s2, errorLog);
        }
    }
    
    /**
     * Log a simple error message
     * @param e Exception being logged
     */
    public static void errorLog(Exception e) {
        ServerLogger.errorLog(e, null);
    }
}
