package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.view.LocalModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.script.ScriptContext;
import java.awt.*;

public class ChatController {
    
    @FXML
    public ScrollPane readChat;
    @FXML
    public TextField writeMessage;
    @FXML
    public TextField recipient;
    @FXML
    public VBox chatBox;
    @FXML
    private TextFlow writeChat;
    
    private TextArea messageArea;
    
    
    
    
    public void handleEnterPressed(ActionEvent event) {
        
        String enteredText = writeMessage.getText();
        String recipientName=recipient.getText();
        if(LocalModel.getInstance().getPlayersNicknames().contains(recipientName)){
            AppClient.getViewInstance().notifyChatMessage(enteredText,recipientName);
            writeMessage.clear();
        }else if(enteredText.equals("everyone")  ) {
            AppClient.getViewInstance().notifyChatMessage(enteredText);
            writeMessage.clear();
        }
        
        
    }
    public void writeChatMessage(String message){
        
        chatBox.getChildren().add(new Text(" " +message+"\n"));
    }
}
