package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
public class WinnerPopUp extends Application {
    public String displayText="";   // passed into this class by getWinner() in Scrabble.java
    public void start(Stage s) throws Exception
    {
        s.setTitle("Game Finished!");
        // create a label with final message about game to players
        Label b = new Label(displayText);
        b.setTextAlignment(TextAlignment.CENTER);
        b.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-font-family: \"Arial\"; -fx-font-weight: bold;");
        StackPane sp = new StackPane(b);        // initially used this so an image could be placed behind the text, but decided against it
        sp.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 90%, #006600, #003300);");    //nice green gradient background
        Scene sc = new Scene(sp, 500, 225);
        s.setScene(sc);
        s.show();       // show final screen
    }
    public static void main(String args[]){launch(args);}
} 