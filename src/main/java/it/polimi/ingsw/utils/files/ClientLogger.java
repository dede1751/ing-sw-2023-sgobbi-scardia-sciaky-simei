package it.polimi.ingsw.utils.files;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.Instant;

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
    
    public static void messageLog(ModelMessage<?> message) {
        var payload = message.getPayload().toString();
        String logString = Timestamp.from(Instant.now()) + " - " + "\n" + payload + "\n";
        writeLog(logString, messageLog);
    }
    
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
    
    public static void errorLog(Exception e) {
        errorLog(e, null);
    }
    
}
