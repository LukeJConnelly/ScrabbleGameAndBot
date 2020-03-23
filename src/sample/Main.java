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
import java.util.ArrayList;
import java.util.Scanner;

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
        System.out.println("Welcome to Scrabble!");
        Player player1 = new Player();
        Player player2 = new Player();
        Pool gamePool = new Pool();
        Pool decisionPool = new Pool();

        System.out.println("Player 1, Please enter your name: ");
        Scanner newScanner = new Scanner(System.in);
        String n;
        n = newScanner.nextLine();
        player1.setName(n);
        System.out.println("Thank you " + player1.getName());

        System.out.println("Player 2, Please enter your name: ");
        n = newScanner.nextLine();
        player2.setName(n);
        System.out.println("Thank you " + player2.getName());

        System.out.println("\nTo decide who goes first a random tile will be pulled for each player.");
        ArrayList<Tile> randPlayer1 = decisionPool.drawTiles(1);
        ArrayList<Tile> randPlayer2 = decisionPool.drawTiles(1);
        System.out.println(player1.getName() + ", your random tile is: " + randPlayer1);
        System.out.println(player2.getName() + ", your random tile is: " + randPlayer2);
        while (randPlayer1.get(0).getLetter()==randPlayer2.get(0).getLetter())
        {
            randPlayer1 = decisionPool.drawTiles(1);
            randPlayer2 = decisionPool.drawTiles(1);
            System.out.println(player1.getName() + ", your new random tile is: " + randPlayer1);
            System.out.println(player2.getName() + ", your new random tile is: " + randPlayer2);
        }
        boolean player=false;   //Player 1 goes first if false, player 2 goes first if true
        if((randPlayer1.get(0).getLetter()>randPlayer2.get(0).getLetter()&&randPlayer1.get(0).getLetter()!='_')||randPlayer2.get(0).getLetter()=='_')
        {
            player=true;
        }
        boolean quit=false;
        if (!player) {
            player1.getFrame().refill(gamePool);
        }
        player2.getFrame().refill(gamePool);
        if (player) {
            player1.getFrame().refill(gamePool);
        }
        int turns = 0;
        while (!endgame(player1, player2, turns)&&!quit) {
            if (player) {
                System.out.println(player2.getName()+" your frame is:\n"+player2.getFrame().toString());
            }
            else {
                System.out.println(player1.getName()+" your frame is:\n"+player1.getFrame().toString());
            }
            Stage s = new Stage();
            InputPopUp playerInput = new InputPopUp();
            playerInput.start(s);
            while (playerInput.playerInput == "") {
                System.out.println("Please make a move. If you do not wish to make a move, type PASS");
                playerInput.start(s);
            }
            if (playerInput.playerInput == "Q") {
                primaryStage.close();
                quit=true;
            }
            else if (playerInput.playerInput == "P") {
                turns++;
            }
            else if (playerInput.playerInput.matches("^EXCHANGE [A-Z]{1,7}$")) {
                if (player) {
                    if (player2.getFrame().isAvailable(playerInput.playerInput.trim().replaceAll("EXCHANGE ", "")))
                    {
                        player2.getFrame().remove(playerInput.playerInput.trim().replaceAll("EXCHANGE ", ""));
                        player2.getFrame().refill(gamePool);
                        System.out.println(player2.getName()+" your frame is now:\n"+player2.getFrame().toString());
                        turns = 0;
                    }
                    else
                    {
                        System.out.println("You do not have the letters required for this exchange!");
                        player=!player;
                    }
                }
                else{
                    if (player1.getFrame().isAvailable(playerInput.playerInput.trim().replaceAll("EXCHANGE ", "")))
                    {
                        player1.getFrame().remove(playerInput.playerInput.trim().replaceAll("EXCHANGE ", ""));
                        player1.getFrame().refill(gamePool);
                        System.out.println(player1.getName()+" your frame is now:\n"+player1.getFrame().toString());
                        turns = 0;
                    }
                    else
                    {
                        System.out.println("You do not have the letters required for this exchange!");
                        player=!player;
                    }
                }
            } else {
                if (player) {
                    //player 2 moves
                    game.move(myBoard, player2);
                    turns = 0;
                }
                else {
                    game.move(myBoard, player1);
                    turns = 0;
                }
            }
            if (player) {
                player2.getFrame().refill(gamePool);
            }
            else {
                player1.getFrame().refill(gamePool);
            }
            player=!player;
            Main.run();
        }
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
        FileInputStream input;
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
    public boolean endgame(Player player1, Player player2, int turnsWOMove)
    {
        if (player1.getFrame().isEmpty()||player2.getFrame().isEmpty()||turnsWOMove>5)
        {
            System.out.println("The game has finished!");
            // get winner and congratulate
            getWinner(player1, player2);
            return true;
        }
        return false;
    }
    public void getWinner(Player player1, Player player2)
    {
        System.out.println("The scores are:\n"+player1.getName()+" "+player1.getScore()+" - "+player2.getScore()+" "+player1.getName()+"");
        if (player1.getScore()>player2.getScore())
        {
            System.out.println("Congratulations "+player1.getName()+"!");
        }
        else if (player1.getScore()>player2.getScore())
        {
            System.out.println("Congratulations "+player2.getName()+"!");
        }
        else
        {
            System.out.println("It's a draw! Everyone's a loser!");
        }
    }
}
