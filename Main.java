package sample;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Scrabble");
        GridPane gridpane = new GridPane();
        char letter = 'A';
        Image image = new Image("File:/Scrabble Tiles/"+letter+".png");
        gridpane.getChildren().add(new ImageView(image));
        Scene scene = new Scene(gridpane, 240, 100);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
