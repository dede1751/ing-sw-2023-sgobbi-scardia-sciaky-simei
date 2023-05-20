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
    
    
    static {
        try {
            messageLog = ResourcesManager.openFileWrite(ResourcesManager.mainResourcesDir + "/client/messageLog.txt");
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
    public static void writeMessage(ModelMessage<?> message){
        var payload = message.getPayload().toString();
        String logString = Timestamp.from(Instant.now()) + " - " + "\n" + payload + "\n";
        writeLog(logString, messageLog);
    }
    
    
}
