package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class InputPopUp extends Application {

    public String playerInput="";
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Player Input");

        TextField textField = new TextField();

        Button button = new Button("Make Move");

        button.setOnAction(action -> {
            String input = "";
            input = textField.getText().toUpperCase();
            if(!input.matches("^[A-O][1-9] [AD] [A-Z]{2,}$|^[A-O]1[0-5] [AD] [A-Z]{2,}$|^QUIT$|^PASS$|^HELP$|^EXCHANGE [A-Z]{1,7}$"))
            {
                System.out.println("Input currently not recognized, please re-enter");
            }
            if (input.matches("^QUIT$"))
            {
                System.out.println("Game Quit");
                playerInput="Q";
                primaryStage.close();
            }
            else if (input.matches("^PASS$"))
            {
                System.out.println("Turn Passed");
                playerInput="P";
                primaryStage.close();
            }
            else if (input.matches("^HELP$"))
            {
                System.out.println("Help Guide:");
                System.out.println("'quit' to exit game");
                System.out.println("'pass' to skip your turn");
                System.out.println("'exchange <list of letters>' to swap letters (be sure to enter a space between exchange and letters)");
                System.out.println("'<co-ordinate> <A/D for Across/Down> <Word>' to take your turn (be sure to enter a space after co-ordinate and A/D)");
                System.out.println("Enjoy!");
            }
            else if (input.matches("^EXCHANGE [A-Z]{1,7}$"))
            {
                System.out.println("Exchanging Letters");
                playerInput=input;
                primaryStage.close();
            }
            else if (input.matches("^[A-O][1-9] [AD] [A-Z]{2,}$|^[A-O]1[0-5] [AD] [A-Z]{2,}$"))
            {
                System.out.println("Following move will be made: "+input);
                playerInput=input;
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
        primaryStage.showAndWait();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}