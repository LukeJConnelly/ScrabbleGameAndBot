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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main extends Application {
    static GridPane gridpane = new GridPane();      // global as it is required for use in both start() and run()
    static Board board;                             // global as it is required for use in both Main and Scrabble
    public Main() {}

    @Override
    public void start(Stage primaryStage) throws Exception {
        Board myBoard = new Board();
        Main.board = myBoard;
        primaryStage.setTitle("Other Scrabbled Eggs Project");
        gridpane.setMinSize(480, 480);      // square 30px for each tile on the 16 pane grid for board
        gridpane.setVgap(0);
        gridpane.setHgap(0);
        Main.run();                                 // initializing the board with no letters
        gridpane.setStyle("-fx-background-color: #000000;");
        gridpane.setAlignment(Pos.CENTER);

        BorderPane border = new BorderPane();
        FileInputStream input1 = new FileInputStream("/Scrabble Tiles/header.jpg");    //now taking in a header image file and converting to an image view
        Image image1 = new Image(input1);
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(480);
        imageView1.setFitHeight(90);
        HBox hbox = new HBox(imageView1);
        hbox.setAlignment(Pos.CENTER);      //centering image using hbox - it looked ugly on laptops with different aspect ratios otherwise
        hbox.setPadding(Insets.EMPTY);
        border.setTop(hbox);
        FileInputStream input2 = new FileInputStream("/Scrabble Tiles/footer.jpg");    // repeating the process with a footer image
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(480);
        imageView2.setFitHeight(60);
        HBox hbox1 = new HBox(imageView2);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPadding(Insets.EMPTY);
        border.setBottom(hbox1);        //setting areas of the borderpane
        border.setCenter(gridpane);
        border.setStyle("-fx-background-color: #000000;");
        Scene scene = new Scene(border, 480, 630);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);       //done so as to preserve a neat presentation
        primaryStage.show();
        //end of window set up
        Scrabble game = new Scrabble();             // now introducing the actual gameplay
        System.out.println("Welcome to Scrabble!");
        Player player1 = new Player();
        Player player2 = new Player();
        Pool gamePool = new Pool();
        Pool decisionPool = new Pool();   // declaring everything we need to play, so it can be passed if needed
        boolean quit = false;
        boolean player = false;   //Player 1 goes first if false, player 2 goes first if true
        game.setup(player1, player2, gamePool, decisionPool);           // takes in names and decides player order
        int turns = 0;
        Word prevWord=null;
        while (!game.end(player1, player2, turns) && !quit) {           // if the game isnt over and the player hasnt quit we play
            System.out.println();
            if (player) {       //we use a boolean player and if statements to differentiate between turns
                System.out.println(player2.getName() + " your frame is:\n" + player2.getFrame().toString());//first giving the player their frame
            } else {
                System.out.println(player1.getName() + " your frame is:\n" + player1.getFrame().toString());
            }
            Stage s = new Stage();
            InputPopUp playerInput = new InputPopUp();      //this takes in the players input as scanner cant run in conjunction with an application
            playerInput.start(s);
            while (playerInput.playerInput == "") {         // if player closes input window we open it again for them
                System.out.println("\nPlease make a move. If you do not wish to make a move, type PASS");
                playerInput.start(s);
            }
            if (playerInput.playerInput == "Q") {       //QUIT
                primaryStage.close();
                quit = true;            // window closes and loop will end
            } else if (playerInput.playerInput == "P") {        //PASS
                turns++;    //just let the turn go, but keep a count of how many turns in a row have been passed - 6 ends game
            } else if (playerInput.playerInput == "C") {        //CHALLENGE
                if(prevWord==null)
                {
                    System.out.println("There has not been a word played yet! Please make another move");
                    player=!player;
                }
                // check peripheral words for legality
                else if(isWord(prevWord.getLetters()))
                {
                    System.out.println("Challenge failed, "+prevWord.getLetters()+" is in the dictionary");
                }
                else
                {
                    System.out.println("Challenge success, "+prevWord.getLetters()+" is not in the dictionary");
                    player=!player;
                    game.unmove(myBoard, prevWord, player? player2:player1);
                    Main.run();     //update board display
                }
            } else if (playerInput.playerInput.matches("^EXCHANGE [A-Z]{1,7}$")) {
                game.exchange(playerInput, player, player1, player2, gamePool, turns);  //exchange is handled in scrabble
                Main.run();     //update board display
            } else {    //player makes valid move
                String[] currMoveInput = playerInput.playerInput.toUpperCase().trim().split(" ");   //split input by the spaces to understand
                boolean isHorizontal=true;
                if (currMoveInput[1].toUpperCase().charAt(0)=='D'){isHorizontal=false;} // set isHorizontal to false if they typed d instead of a
                int moveRow=0;
                if (playerInput.playerInput.matches("^[A-O][1-9] [AD] [A-Z_]{2,}$")) {  // this just discerns between rows that are one or two digits
                    moveRow = Character.getNumericValue(currMoveInput[0].charAt(1)) - 1;
                } else{
                    moveRow = ((Character.getNumericValue(currMoveInput[0].charAt(1))*10)+Character.getNumericValue(currMoveInput[0].charAt(2))) - 1;
                }
                int moveCol = currMoveInput[0].charAt(0);   // making handy use of how chars cast to ints
                moveCol-=65;
                Word currWord = new Word(moveRow, moveCol, isHorizontal, currMoveInput[2]); // now we can use the word constructor on the pllayers inputs
                if (player) {
                    if (myBoard.isLegal(player2.getFrame(), currWord)) {
                        game.move(myBoard, currWord, player2);  //moves are handled in scrabb;e
                        turns = 0;
                        prevWord = currWord;
                        Main.run();     //update board display
                    }
                    else{       // they have to take their turn again
                        player=!player;     //negates the flipping of the turns
                        System.out.println("\nSorry, the word you want to place cannot be legally placed. Relevant error code: " + myBoard.getCheckCode());
                    }
                } else {        //same process goes for player 1's move
                    if (myBoard.isLegal(player1.getFrame(), currWord)) {
                        game.move(myBoard, currWord, player1);
                        turns = 0;
                        prevWord = currWord;
                        Main.run();     //update board display
                    }
                    else{
                        player=!player;
                        System.out.println("\nSorry, the word you want to place cannot be legally placed. Relevant error code: " + myBoard.getCheckCode());
                    }
                }
            }
            if (player) {   //refill relevant players frame
                player2.getFrame().refill(gamePool);
            } else {
                player1.getFrame().refill(gamePool);
            }
            player = !player;   // flip turns
            System.out.println("The scores are:\n"+player1.getName()+" "+player1.getScore()+" - "+player2.getScore()+" "+player2.getName()+"\n");
        }
        game.getWinner(player1, player2);   // game decides a winner
    }

    public static void run() throws FileNotFoundException {
        int r, c;
        gridpane.getChildren().clear();     //get rid of whatevers there, in future letters could be removed by CHALLENGE
        for (r = 0; r < 16; r++) {
            for (c = 0; c < 16; c++) {
                Button blank;   // a button which can be used in the following if statements
                if (r<15&&c<15) {   //playable board area
                    if (Main.board.squares[r][c].isDoubleLetter()) {    //each square has specific text/styling
                        blank = new Button("DL");
                        blank.setStyle("-fx-background-color: #6666FF; -fx-border-width: 0; -fx-font-size: 14;" +
                                "-fx-text-fill: white; -fx-font-family: \"Arial\"; -fx-font-weight: bold");
                    } else if (Main.board.squares[r][c].isTripleLetter()) {
                        blank = new Button("TL");
                        blank.setStyle("-fx-background-color: #0133FF; -fx-border-width: 0; -fx-font-size: 14;" +
                                "-fx-text-fill: white; -fx-font-family: \"Arial\"; -fx-font-weight: bold");
                    } else if (Main.board.squares[r][c].isDoubleWord()) {
                        blank = new Button("DW");
                        blank.setStyle("-fx-background-color: #660466; -fx-border-width: 0; -fx-font-size: 14;" +
                                "-fx-text-fill: white; -fx-font-family: \"Arial\"; -fx-font-weight: bold");
                    } else if (Main.board.squares[r][c].isTripleWord() || r == 14 && c == 7) {
                        blank = new Button("TW");
                        blank.setStyle("-fx-background-color: #880101; -fx-border-width: 0; -fx-font-size: 14;" +
                                "-fx-text-fill: white; -fx-font-family: \"Arial\"; -fx-font-weight: bold");
                    } else {
                        blank = new Button("");
                        blank.setStyle("-fx-background-color: #006600; -fx-border-width: 0;");
                    }
                }
                else if (r==15&&c==15) {    //corner of row labels
                    blank = new Button("");
                    blank.setStyle("-fx-background-color: #002200; -fx-border-width: 0;");
                } else if (r==15){      //numbers
                    blank = new Button(Integer.toString(c+1));  //number to string
                    blank.setStyle("-fx-background-color: #002200; -fx-border-width: 0; -fx-font-size: 18;" +
                            "-fx-text-fill: white; -fx-font-family: \"Arial\"; -fx-font-weight: bold");
                } else{         //letters
                    blank = new Button(Character.toString((char) ('A'+r))); //number to char to string
                    blank.setStyle("-fx-background-color: #002200; -fx-border-width: 0; -fx-font-size: 18;" +
                            "-fx-text-fill: white; -fx-font-family: \"Arial\"; -fx-font-weight: bold");
                }
                blank.setPadding(Insets.EMPTY);
                blank.setMinWidth(30);
                blank.setMinHeight(30);
                gridpane.add(blank, r, c);  // add our new buttons
            }
        }
        for (r = 0; r < 15; r++) {
            for (c = 0; c < 15; c++) {
                if (Main.board.squares[r][c].isOccupied()) {    //loop over again and if we
                    addLetter(r, c, Main.board);
                    Main.board.squares[r][c].getTile().turnsOnBoard++;
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void addLetter(int r, int c, Board myBoard) throws FileNotFoundException {
        FileInputStream input;
        if (myBoard.squares[r][c].getTile().isBlank()) {        //blank has a specific image
            input = new FileInputStream("/Scrabble Tiles/0.png");
        } else {        // just find the character.png - we made these ourselves in ms paint :)
            input = new FileInputStream("/Scrabble Tiles/" + myBoard.squares[r][c].getTile().getLetter() + ".png");
        }
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(28);
        imageView.setFitHeight(28);     //slightly smaller than squares - i like how it seems like it sits on the board
        imageView.setStyle("-fx-border-width:1;");
        gridpane.add(imageView, c, r);      // finally add our image
    }

    public static boolean isWord(String word) throws FileNotFoundException {
        Scanner dictionary = new Scanner(new File("/Scrabble Tiles/dictionary.txt"));
        while (dictionary.hasNextLine() != false) {
            if (dictionary.nextLine() == word) {
                return true;
            }
        }
        return false;
    }
}