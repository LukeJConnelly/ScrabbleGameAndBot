package sample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main extends Application {
    static GridPane gridpane = new GridPane();
    static Board board;
    public Main() {}
    @Override
    public void start(Stage primaryStage) throws Exception{
        Board myBoard= new Board();
        Main.board = myBoard;
        primaryStage.setTitle("Other Scrabbled Eggs Project");
        gridpane.setMinSize(450, 450);
        gridpane.setVgap(0);
        gridpane.setHgap(0);
        int r, c;
        for (r=0;r<15;r++)
        {
            for(c=0;c<15;c++)
            {
                Button blank;
                if (Main.board.squares[r][c].isDoubleLetter()) {
                    blank = new Button("DL");
                    blank.setStyle("-fx-background-color: #6666FF; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else if (Main.board.squares[r][c].isTripleLetter()) {
                    blank = new Button("TL");
                    blank.setStyle("-fx-background-color: #0133FF; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else if (Main.board.squares[r][c].isDoubleWord()) {
                    blank = new Button("DW");
                    blank.setStyle("-fx-background-color: #660466; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else if (Main.board.squares[r][c].isTripleWord()||r==14&&c==7) {
                    blank = new Button("TW");
                    blank.setStyle("-fx-background-color: #880101; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else {
                    blank = new Button("");
                    blank.setStyle("-fx-background-color: #006600; " +
                            "-fx-border-width: 0;");
                }
                blank.setPadding(Insets.EMPTY);
                blank.setMinWidth(30);
                blank.setMinHeight(30);
                gridpane.add(blank, r, c);
            }
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
        Scrabble game = new Scrabble();
        game.turn(myBoard);
        Main.run();
    }

    public static void run() throws FileNotFoundException {
        int r, c;
        gridpane.getChildren().clear();
        for (r=0;r<15;r++)
        {
            for(c=0;c<15;c++)
            {
                Button blank;
                if (Main.board.squares[r][c].isDoubleLetter()) {
                    blank = new Button("DL");
                    blank.setStyle("-fx-background-color: #6666FF; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else if (Main.board.squares[r][c].isTripleLetter()) {
                    blank = new Button("TL");
                    blank.setStyle("-fx-background-color: #0133FF; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else if (Main.board.squares[r][c].isDoubleWord()) {
                    blank = new Button("DW");
                    blank.setStyle("-fx-background-color: #660466; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else if (Main.board.squares[r][c].isTripleWord()||r==14&&c==7) {
                    blank = new Button("TW");
                    blank.setStyle("-fx-background-color: #880101; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else {
                    blank = new Button("");
                    blank.setStyle("-fx-background-color: #006600; " +
                            "-fx-border-width: 0;");
                }
                blank.setPadding(Insets.EMPTY);
                blank.setMinWidth(30);
                blank.setMinHeight(30);
                gridpane.add(blank, r, c);
            }
        }
        for (r=0;r<15;r++)
        {
            for(c=0;c<15;c++)
            {
                if (Main.board.squares[r][c].isOccupied())
                {
                    addLetter(r, c, Main.board);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void addLetter(int r, int c, Board myBoard) throws FileNotFoundException {
        InputStream input;
        if (myBoard.squares[r][c].getTile().isBlank())
        {
            input = new FileInputStream("src\\sample\\Scrabble Tiles\\0.png");
        }
        else
        {
            input = new FileInputStream("src\\sample\\Scrabble Tiles\\" + myBoard.squares[r][c].getTile().getLetter() + ".png");
        }
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(28);
        imageView.setFitHeight(28);
        imageView.setStyle("-fx-border-width:1;");
        gridpane.add(imageView, c, r);
    }
}
