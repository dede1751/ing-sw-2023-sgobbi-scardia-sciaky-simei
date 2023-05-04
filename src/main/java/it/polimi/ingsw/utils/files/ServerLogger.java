package it.polimi.ingsw.utils.files;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.Instant;

public class ServerLogger {
    
    private static FileChannel log = null;
    
    static {
        try {
            log = ResourcesManager.openFileWrite(ResourcesManager.mainResourcesDir + "/server/log.txt");
        }
        catch( IOException e ) {
            throw new RuntimeException(e);
        }
    }
    
    public static void log(String s) {
        try {
            if( log != null ) {
                String s1 = s + " " + Timestamp.from(Instant.now()) + "\n";
                log.write(ByteBuffer.wrap(s.getBytes(Charset.defaultCharset())));
            }
        }
        catch( IOException e ) {
            System.err.println("Logger IOException");
            e.printStackTrace(System.err);
        }
    }
    
    public static void messageLog(String clientContext, ModelMessage<?> message) {
        String s = "Updated Client : " + clientContext + "\nwith message type : " + message.getClass().getSimpleName() +
                   "\n";
        ServerLogger.log(s);
    }
    
    public static void errorLog(Exception e, String additionalContext) {
        String s = "Server encountered exception of type : " + e.getClass() + "\n";
        String s1 = "Message : " + e.getMessage() + "\n";
        String s2 = "Cause : " + e.getCause();
        if( additionalContext != null ) {
            ServerLogger.log(s + s1 + s2 + "\n" + additionalContext);
        }else {
            ServerLogger.log(s + s1 + s2);
        }
    }
    
    public static void errorLog(Exception e) {
        ServerLogger.errorLog(e, null);
    }
}
