package it.polimi.ingsw.utils.files;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Class ClientLogger saves log data for the client.
 */
public class ClientLogger {
    
    private static final FileChannel messageLog;
    
    private static final FileChannel errorLog;
    
    static {
        try {
            messageLog = ResourcesManager.openFileWrite(ResourcesManager.clientLoggerDir, "log.txt");
            errorLog = ResourcesManager.openFileWrite(ResourcesManager.serverLoggerDir, "error_log.txt");
        }
        catch( IOException e ) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Unused private constructor to appease Javadoc.
     */
    private ClientLogger(){}
    
    /**
     * Write a log message to the log file
     * @param s Message to be logged
     * @param channel Channel to write the message to (either messagelog or errorLog)
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
     * Log a message being sent to the server.
     * @param message Message to be logged
     */
    public static void messageLog(ModelMessage<?> message) {
        var payload = message.getPayload().toString();
        String logString = Timestamp.from(Instant.now()) + " - " + "\n" + payload + "\n";
        writeLog(logString, messageLog);
    }
    
    /**
     * Log an exception
     * @param e Exception to be logged
     * @param additionalContext Additional context to be logged
     */
    public static void errorLog(Exception e, String additionalContext) {
        String s = "Client encountered exception of type : " + e.getClass() + "\n";
        String s1 = "Message : " + e.getMessage() + "\n";
        String s2 = "Cause : " + e.getCause();
        
        if( additionalContext != null ) {
            writeLog(s + s1 + s2 + "\n" + additionalContext, errorLog);
        }else {
            writeLog(s + s1 + s2, errorLog);
        }
    }
    
}
