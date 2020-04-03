package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class BlankPopUp extends Application {
        public char blankInput;       // stored here so as to allow access from the main game
        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setTitle("Blank Decision Time ");
            FileInputStream fav = new FileInputStream("Scrabble Tiles/S.png");
            Image favicon = new Image(fav);
            primaryStage.getIcons().add(favicon);
            TextField textField = new TextField();
            Button button = new Button("Change Blank");

            button.setOnAction(action -> {          // when player clicks change blank:
                char input;
                input = textField.getText().toUpperCase().trim().toCharArray()[0];       // we take their input and check it matches something acceptable
                if (!Character.toString(input).matches("[A-Z?]"))   // check that the input is one of the letters of the alphabet
                {
                    System.out.println("Input currently not recognized, please re-enter");
                }else{                  //pass new letter value back to the main game and close the pop up
                    System.out.println("The blank will be converted to: "+input);
                    blankInput=input;
                    primaryStage.close();
                }
            });
            button.setStyle("-fx-background-color: #006600;" +
                    "-fx-border-width: 0;" +
                    "-fx-font-size: 14;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: \"Arial\";" +
                    "-fx-font-weight: bold");
            textField.setStyle("-fx-background-color: #006600;" +
                    "-fx-border-width: 0;" +
                    "-fx-font-size: 14;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: \"Arial\";" +
                    "-fx-font-weight: bold");

            // now aligning and setting sizes for each element and adding them to scene
            textField.setPrefSize(250, 50);
            button.setPrefSize(150, 50);
            textField.setAlignment(Pos.CENTER);
            button.setAlignment(Pos.CENTER);
            HBox hbox = new HBox(textField, button);
            hbox.setAlignment(Pos.CENTER);
            BorderPane bp = new BorderPane();
            bp.setCenter(hbox);
            bp.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 90%, #006600, #003300);");
            Scene scene = new Scene(bp, 425, 100);
            primaryStage.setScene(scene);

            primaryStage.showAndWait(); //game will not progress until this window closes
        }
        public static void main(String[] args) {
            Application.launch(args);
        }
    }