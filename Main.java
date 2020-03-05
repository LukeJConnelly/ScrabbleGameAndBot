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
//        Scene scene = new Scene(gridpane, 240, 100);
//        primaryStage.setScene(scene);
//        primaryStage.show();
        primaryStage.setTitle("Other Scrabbled Eggs Project");
        GridPane gridpane = new GridPane();
        Button title_text = new Button("SCRABBLE");
        title_text.setMinWidth(450);
        title_text.setMinHeight(50);
        title_text.setStyle("-fx-background-color: #006600; " +
                        "-fx-border-width: 0;" +
                        "-fx-font-size: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: \"Arial\";"    );
        HBox title = new HBox(title_text);
        title.setAlignment(Pos.CENTER);

        FileInputStream input = new FileInputStream("C:\\Users\\Luke\\IdeaProjects\\Scrabble Game\\src\\sample\\Scrabble Tiles\\S.png");
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
                Button blank = new Button("");
                blank.setMinWidth(30);
                blank.setMinHeight(30);
                blank.setStyle("-fx-background-color: #006600; " +
                        "-fx-border-width: 0;");
                gridpane.add(blank, i, j);
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
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
