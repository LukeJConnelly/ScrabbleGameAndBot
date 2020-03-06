package sample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        char letter = 'A';
//        Image image = new Image("File:/Scrabble Tiles/"+letter+".png");
//        gridpane.getChildren().add(new ImageView(image));
        primaryStage.setTitle("Other Scrabbled Eggs Project");
        GridPane gridpane = new GridPane();
        Button title_text = new Button("SCRABBLE");
        title_text.setMinWidth(450);
        title_text.setMinHeight(50);
        title_text.setStyle("-fx-background-color: #003300; " +
                        "-fx-border-width: 0;" +
                        "-fx-font-size: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: \"Arial\";" +
                        "-fx-font-weight: bold");
        HBox title = new HBox(title_text);
        title.setAlignment(Pos.CENTER);

        FileInputStream input = new FileInputStream("src\\sample\\Scrabble Tiles\\S.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        gridpane.setMinSize(450, 450);
        gridpane.setVgap(0);
        gridpane.setHgap(0);
        int i, j;
        for (i=0;i<15;i++)
        {
            for(j=0;j<15;j++)
            {
                if(i+j==14||i-j==0) {
                    Button blank = new Button("DW");
                    blank.setStyle("-fx-background-color: #660466; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                    blank.setPadding(Insets.EMPTY);
                    blank.setMinWidth(30);
                    blank.setMinHeight(30);
                    gridpane.add(blank, i, j);
                }
                else
                {
                    Button blank = new Button("");
                    blank.setStyle("-fx-background-color: #006600; " +
                            "-fx-border-width: 0;");
                    blank.setPadding(Insets.EMPTY);
                    blank.setMinWidth(30);
                    blank.setMinHeight(30);
                    gridpane.add(blank, i, j);
                }
            }
        }

        gridpane.add(imageView, 3, 2);
        gridpane.setStyle("-fx-background-color: #000000;");
        gridpane.setAlignment(Pos.CENTER);

        BorderPane border = new BorderPane();
        border.setTop(title);
        border.setCenter(gridpane);
        Scene scene = new Scene(border, 450,500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
