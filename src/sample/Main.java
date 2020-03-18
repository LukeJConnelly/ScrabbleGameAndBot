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
        Board myBoard = new Board();
        primaryStage.setTitle("Other Scrabbled Eggs Project");
        GridPane gridpane = new GridPane();
//        Button title_text = new Button("SCRABBLE");
//        title_text.setMinWidth(450);
//        title_text.setMinHeight(50);
//        title_text.setStyle("-fx-background-color: #003300; " +
//                        "-fx-border-width: 0;" +
//                        "-fx-font-size: 30;" +
//                        "-fx-text-fill: white;" +
//                        "-fx-font-family: \"Arial\";" +
//                        "-fx-font-weight: bold");
//        HBox title = new HBox(title_text);
//        title.setAlignment(Pos.CENTER);

        gridpane.setMinSize(450, 450);
        gridpane.setVgap(0);
        gridpane.setHgap(0);
        int r, c;
        for (r=0;r<15;r++)
        {
            for(c=0;c<15;c++)
            {
                if (myBoard.squares[r][c].isOccupied())
                {
                    if (myBoard.squares[r][c].getTile().isBlank())
                    {
                        FileInputStream input = new FileInputStream("src\\sample\\Scrabble Tiles\\0.png");
                        Image image = new Image(input);
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(29);
                        imageView.setFitHeight(29);
                        gridpane.add(imageView, r, c);
                    }
                    else
                    {
                        FileInputStream input = new FileInputStream("src\\sample\\Scrabble Tiles\\"+myBoard.squares[r][c].getTile().getLetter()+".png");
                        Image image = new Image(input);
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(28);
                        imageView.setFitHeight(28);
                        imageView.setStyle("-fx-border-width:1;");
                        gridpane.add(imageView, r, c);
                    }
                }
                else if (myBoard.squares[r][c].isDoubleLetter()) {
                    Button blank = new Button("DL");
                    blank.setStyle("-fx-background-color: #6666FF; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                    blank.setPadding(Insets.EMPTY);
                    blank.setMinWidth(30);
                    blank.setMinHeight(30);
                    gridpane.add(blank, r, c);
                } else if (myBoard.squares[r][c].isTripleLetter()) {
                    Button blank = new Button("TL");
                    blank.setStyle("-fx-background-color: #0133FF; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                    blank.setPadding(Insets.EMPTY);
                    blank.setMinWidth(30);
                    blank.setMinHeight(30);
                    gridpane.add(blank, r, c);
                } else if (myBoard.squares[r][c].isDoubleWord()) {
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
                    gridpane.add(blank, r, c);
                } else if (myBoard.squares[r][c].isTripleWord()) {
                    Button blank = new Button("TW");
                    blank.setStyle("-fx-background-color: #880101; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                    blank.setPadding(Insets.EMPTY);
                    blank.setMinWidth(30);
                    blank.setMinHeight(30);
                    gridpane.add(blank, r, c);
                } else {
                    Button blank = new Button("");
                    blank.setStyle("-fx-background-color: #006600; " +
                            "-fx-border-width: 0;");
                    blank.setPadding(Insets.EMPTY);
                    blank.setMinWidth(30);
                    blank.setMinHeight(30);
                    gridpane.add(blank, r, c);
                }
            }
        }
        for (r=0;r<5;r++) {
            FileInputStream input = new FileInputStream("src\\sample\\Scrabble Tiles\\"+(char)(r+65)+".png");
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(28);
            imageView.setFitHeight(28);
            imageView.setStyle("-fx-border-width:1;");
            gridpane.add(imageView, r+3, 3);
        }
        gridpane.setStyle("-fx-background-color: #000000;");
        gridpane.setAlignment(Pos.CENTER);

        BorderPane border = new BorderPane();
        FileInputStream input1 = new FileInputStream("src\\sample\\Scrabble Tiles\\header.jpg");
        Image image1 = new Image(input1);
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(450);
        imageView1.setFitHeight(78);
        border.setTop(imageView1);
        FileInputStream input2 = new FileInputStream("src\\sample\\Scrabble Tiles\\footer.jpg");
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(450);
        imageView2.setFitHeight(50);
        border.setBottom(imageView2);
        border.setCenter(gridpane);
        border.setStyle("-fx-background-color: #000000;");
        Scene scene = new Scene(border, 450,578);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
