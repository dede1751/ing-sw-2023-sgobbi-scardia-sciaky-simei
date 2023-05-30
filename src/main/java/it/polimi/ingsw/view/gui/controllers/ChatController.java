package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.view.LocalModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.*;

public class ChatController {
    
    @FXML
    public ScrollPane readChat;
    @FXML
    public TextField writeMessage;
   
    @FXML
    public VBox chatBox;
    public ChoiceBox recipientChoiceBox;
    @FXML
    private TextFlow writeChat;
    
    private TextArea messageArea;
    String recipientName ="Everyone";
    
    
    @FXML
    public void  setRecipientName(){
       
            
                recipientChoiceBox.getItems().add("Everyone");
                for( int i = 0; i < LocalModel.getInstance().getPlayersNicknames().size(); i++ ) {
                    recipientChoiceBox.getItems().add(LocalModel.getInstance().getPlayersNicknames().get(i));
        
                }
                recipientChoiceBox.setOnAction(
                        (event -> recipientName = (String) recipientChoiceBox.getSelectionModel().getSelectedItem()));
    
   
    }
    public void handleEnterPressed(ActionEvent event) {
        String enteredText = writeMessage.getText();
        
        if(LocalModel.getInstance().getPlayersNicknames().contains(recipientName)){
            AppClient.getViewInstance().notifyChatMessage(enteredText,recipientName);
            writeMessage.clear();
        }else {
            AppClient.getViewInstance().notifyChatMessage(enteredText);
            writeMessage.clear();
        }
        
        
    }
    
 
    
    public void writeChatMessage(String message){
        chatBox.getChildren().add(new Text(" " +message+"\n"));
    }
    

}
