package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.view.LocalModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 
 The ChatController class is responsible for managing the chat functionality in the GUI.
 */
public class ChatController {
    /**
     * The ScrollPane used to display the chat messages.
     */
    @FXML
    public ScrollPane readChat;
    /**
     * The TextField used for writing chat messages.
     */
    @FXML
    public TextField writeMessage;
    
    /**
     * The VBox that contains the chat messages.
     */
    @FXML
    public VBox chatBox;
    
    /**
     * The recipientChoiceBox field represents a choice box for selecting the recipient of a chat message.
     */
    public ChoiceBox<String> recipientChoiceBox;
    @FXML
    private TextFlow writeChat;
    @FXML
    private TextArea messageArea;
    String recipientName = "Everyone";
    
    
    /**
     * Constructs a new ChatController.
     */
    public ChatController() {
    
    }
    
    /**
     
     Sets the recipient name and initializes the recipient choice box.
     
     Clears the write message field, populates the recipient choice box with available recipients,
     
     and sets the default recipient to "Everyone".
     */
    
    @FXML
    public void setRecipientName() {
        
        writeMessage.clear();
        recipientChoiceBox.getItems().add("Everyone");
        for( int i = 0; i < LocalModel.getInstance().getPlayersNicknames().size(); i++ ) {
            
            if( !LocalModel.getInstance().getPlayersNicknames().get(i).equals(
                    AppClient.getViewInstance().getNickname()) )
                recipientChoiceBox.getItems().add(LocalModel.getInstance().getPlayersNicknames().get(i));
        }
        recipientChoiceBox.setValue("Everyone");
        recipientChoiceBox.setOnAction(
                (event -> recipientName = recipientChoiceBox.getSelectionModel().getSelectedItem()));
    }
    /**
     
     Handles the action when the enter key is pressed in the write message field.
     Sends the entered message to the appropriate recipient (individual or everyone) and clears the write message field.
     If the entered message is empty, no action is performed.
     @param event The action event triggered by the enter key press.
     */
    public void handleEnterPressed(ActionEvent event) {
        String enteredText = writeMessage.getText().substring(0, 69);
        if( enteredText == null || enteredText.equals("") )
            return;
        if( LocalModel.getInstance().getPlayersNicknames().contains(recipientName) ) {
            AppClient.getViewInstance().notifyChatMessage(enteredText, recipientName);
            writeMessage.clear();
        }else {
            AppClient.getViewInstance().notifyChatMessage(enteredText);
            writeMessage.clear();
        }
    }
    
    /**
     
     Writes a chat message to the chat box with the specified color.
     
     @param message The message to be displayed in the chat box.
     
     @param color The color of the message.
     */
    public void writeChatMessage(String message, Color color) {
        
        Text mex = new Text(" " + message + "\n");
        
        mex.setFill(color);
        mex.setFont(new Font("Noto Sans", 15));
        chatBox.getChildren().add(mex);
    }
    
}
