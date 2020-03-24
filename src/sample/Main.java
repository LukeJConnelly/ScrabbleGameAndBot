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
import java.util.ArrayList;

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
        gridpane.setMinSize(450, 450);
        gridpane.setVgap(0);
        gridpane.setHgap(0);
        int r, c;
        for (r = 0; r < 15; r++) {
            for (c = 0; c < 15; c++) {
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
        Scene scene = new Scene(border, 450, 578);
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
        game.setup(player1, player2, gamePool, decisionPool, player);
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
                if (player) {
                    //player 2 moves
                    //game.move(myBoard, player2);
                    turns = 0;
                } else {
                    //game.move(myBoard, player1);
                    turns = 0;
                }
            }
            if (player) {
                player2.getFrame().refill(gamePool);
            } else {
                player1.getFrame().refill(gamePool);
            }
            player = !player;
            Main.run();
        }
        game.getWinner(player1, player2);
    }

    public static void run() throws FileNotFoundException {
        int r, c;
        gridpane.getChildren().clear();
        for (r = 0; r < 15; r++) {
            for (c = 0; c < 15; c++) {
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


    public void move(Player currentPlayer, InputPopUp playerInput) {

//        System.out.println("It is currently " + currentPlayer.getName() + "'s turn.");
//        System.out.println(currentPlayer.getFrame());
//        System.out.println("Your score is currently: "+ currentPlayer.getScore());
//        System.out.println("Your opponent "+ otherPlayer.getName()+"'s score is currently: "+ otherPlayer.getScore());
//
//        int scoreFromThisTurn = calculateScore(); // possibly make global
//        //do we even need this fucking variable??
//        //what am i even using this for - should it just be popped straight into the players score variable
//        //possibly need to hold until the next turn to check that the other player doesn't challenge it and then add it on?
//
//        //store until next turn;
//        if (currentPlayer.getScore() < 100 && otherPlayer.getScore() < 100 && !pool.isEmpty()){
//            turn(otherPlayer, currentPlayer, pool);
//        } else {
//            endgame(currentPlayer, otherPlayer, );
//        }


        /*TODO
        check if word legal
        place
        score*/
        String[] parsedInput = playerInput.getPlayerInput().split(" ");
        //need to get coordinates
        boolean direction = false;
        if (parsedInput[1] == "A") {
            direction = true;
        } else {
            direction = false;
        }
        Word word = new Word(parsedInput[0], parsedInput[0], direction, parsedInput[2]);
        if (board.isLegal(currentPlayer.getFrame(), word)) {
            //go ahead with placement
            board.place(currentPlayer.getFrame(), word);
            calculateScore(currentPlayer, word);
        } else {
            System.out.println("Sorry, the word you want to place cannot be legally placed. Relevant error code: " + board.getCheckCode());
        }
    }

    public void calculateScore(Player currentPlayer, Word word) {  //input from the user?
//        getModifiers and getValues of word
//        tally for all the letter mods, then check for word mod, then add to total.

        int tally = 0;
        int r = word.getFirstRow(), c = word.getLastColumn();
        String wordInput = word.getLetters();//need to break into chars to send into tile constructor
        char[] parsedWord = new char[word.getLength()]; //need to make all these Tiles.
        for (int i = 0; i < wordInput.length(); i++) {
            parsedWord[i] = wordInput.charAt(i);
        }
        ArrayList<Tile> wordTiles = new ArrayList<>();
        for (int i = 0; i < parsedWord.length; i++) {
            wordTiles.add(new Tile(parsedWord[i]));
        }

        //ALSO NEED TO LOOP THROUGH [R][C] ? - not sure if i can increment like done below
        //tile values and letter multipliers
        for (int i = 0; i < wordTiles.size(); i++, r++, c++) {
            tally += board.squares[r][c].getLetterMuliplier() * wordTiles.get(i).getValue();
        }
        for (int i = 0; i < wordTiles.size(); i++, r++, c++) {
            tally *= board.squares[r][c].getWordMultiplier();
        }

        currentPlayer.addScore(tally);
    }
}
