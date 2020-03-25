package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application {
    static GridPane gridpane = new GridPane();
    static Board board;

    public Main() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Board myBoard = new Board();
        Main.board = myBoard;
        primaryStage.setTitle("Other Scrabbled Eggs Project");
        gridpane.setMinSize(480, 480);
        gridpane.setVgap(0);
        gridpane.setHgap(0);
        int r, c;
        for (r = 0; r < 16; r++) {
            for (c = 0; c < 16; c++) {
                Button blank;
                if (r<15&&c<15) {
                    if (Main.board.squares[r][c].isDoubleLetter()) {
                        blank = new Button("DL");
                        blank.setStyle("-fx-background-color: #6666FF; " +      //these are the things to swap into css jev
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
                    } else if (Main.board.squares[r][c].isTripleWord() || r == 14 && c == 7) {
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
                }
                else if (r==15&&c==15) {
                    blank = new Button("");
                    blank.setStyle("-fx-background-color: #002200; " +
                            "-fx-border-width: 0;");
                } else if (r==15){
                    blank = new Button(Integer.toString(c+1));
                    blank.setStyle("-fx-background-color: #002200; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 18;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else{
                    blank = new Button(Character.toString((char) ('A'+r)));
                    blank.setStyle("-fx-background-color: #002200; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 18;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
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
        imageView1.setFitWidth(480);
        imageView1.setFitHeight(90);
        HBox hbox = new HBox(imageView1);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(Insets.EMPTY);
        border.setTop(hbox);
        FileInputStream input2 = new FileInputStream("src\\sample\\Scrabble Tiles\\footer.jpg");
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(480);
        imageView2.setFitHeight(60);
        HBox hbox1 = new HBox(imageView2);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPadding(Insets.EMPTY);
        border.setBottom(hbox1);
        border.setCenter(gridpane);
        border.setStyle("-fx-background-color: #000000;");
        Scene scene = new Scene(border, 480, 630);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Scrabble game = new Scrabble();
        System.out.println("Welcome to Scrabble!");
        Player player1 = new Player();
        Player player2 = new Player();
        Pool gamePool = new Pool();
        Pool decisionPool = new Pool();
        boolean quit = false;
        boolean player = false;   //Player 1 goes first if false, player 2 goes first if true
        game.setup(player1, player2, gamePool, decisionPool);
        int turns = 0;
        while (!game.end(player1, player2, turns) && !quit) {
            System.out.println();
            if (player) {
                System.out.println(player2.getName() + " your frame is:\n" + player2.getFrame().toString());
            } else {
                System.out.println(player1.getName() + " your frame is:\n" + player1.getFrame().toString());
            }
            Stage s = new Stage();
            InputPopUp playerInput = new InputPopUp();
            playerInput.start(s);
            while (playerInput.playerInput == "") {
                System.out.println("\nPlease make a move. If you do not wish to make a move, type PASS");
                playerInput.start(s);
            }
            if (playerInput.playerInput == "Q") {
                primaryStage.close();
                quit = true;
            } else if (playerInput.playerInput == "P") {
                turns++;
            } else if (playerInput.playerInput.matches("^EXCHANGE [A-Z]{1,7}$")) {
                game.exchange(playerInput, player, player1, player2, gamePool, turns);
            } else {
                String[] currMoveInput = playerInput.playerInput.toUpperCase().trim().split(" ");
                boolean isHorizontal=true;
                if (currMoveInput[1].toUpperCase().charAt(0)=='D'){isHorizontal=false;}
                int moveRow=0;
                if (playerInput.playerInput.matches("^[A-O][1-9] [AD] [A-Z_]{2,}$")) {
                    moveRow = Character.getNumericValue(currMoveInput[0].charAt(1)) - 1;
                }
                else{
                    moveRow = ((Character.getNumericValue(currMoveInput[0].charAt(1))*10)+Character.getNumericValue(currMoveInput[0].charAt(2))) - 1;
                }
                int moveCol = currMoveInput[0].charAt(0);
                moveCol-=65;
                Word currWord = new Word(moveRow, moveCol, isHorizontal, currMoveInput[2]);
                if (player) {
                    if (myBoard.isLegal(player2.getFrame(), currWord)) {
                        game.move(myBoard, currWord, player2);
                        turns = 0;
                    }
                    else{
                        player=!player;
                        System.out.println("\nSorry, the word you want to place cannot be legally placed. Relevant error code: " + myBoard.getCheckCode());
                    }
                } else {
                    if (myBoard.isLegal(player1.getFrame(), currWord)) {
                        game.move(myBoard, currWord, player1);
                        turns = 0;
                    }
                    else{
                        player=!player;
                        System.out.println("\nSorry, the word you want to place cannot be legally placed. Relevant error code: " + myBoard.getCheckCode());
                    }
                }
            }
            if (player) {
                player2.getFrame().refill(gamePool);
            } else {
                player1.getFrame().refill(gamePool);
            }
            player = !player;
            System.out.println("The scores are:\n"+player1.getName()+" "+player1.getScore()+" - "+player2.getScore()+" "+player2.getName()+"\n");
            Main.run();
        }
        game.getWinner(player1, player2);
    }

    public static void run() throws FileNotFoundException {
        int r, c;
        gridpane.getChildren().clear();
        for (r = 0; r < 16; r++) {
            for (c = 0; c < 16; c++) {
                Button blank;
                if (r<15&&c<15) {
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
                    } else if (Main.board.squares[r][c].isTripleWord() || r == 14 && c == 7) {
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
                }
                else if (r==15&&c==15) {
                    blank = new Button("");
                    blank.setStyle("-fx-background-color: #002200; " +
                            "-fx-border-width: 0;");
                } else if (r==15){
                    blank = new Button(Integer.toString(c+1));
                    blank.setStyle("-fx-background-color: #002200; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 18;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                } else{
                    blank = new Button(Character.toString((char) ('A'+r)));
                    blank.setStyle("-fx-background-color: #002200; " +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 18;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: \"Arial\";" +
                            "-fx-font-weight: bold");
                }
                blank.setPadding(Insets.EMPTY);
                blank.setMinWidth(30);
                blank.setMinHeight(30);
                gridpane.add(blank, r, c);
            }
        }
        for (r = 0; r < 15; r++) {
            for (c = 0; c < 15; c++) {
                if (Main.board.squares[r][c].isOccupied()) {
                    addLetter(r, c, Main.board);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void addLetter(int r, int c, Board myBoard) throws FileNotFoundException {
        FileInputStream input;
        if (myBoard.squares[r][c].getTile().isBlank()) {
            input = new FileInputStream("src\\sample\\Scrabble Tiles\\0.png");
        } else {
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