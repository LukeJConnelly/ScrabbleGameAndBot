package sample;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
//Two player game of scrabble
public class Scrabble {
    public static void main(String[] args) {
    }
    public void move(Board myBoard, Player currPlayer){

    }
}
//    public int numTurns = 0;
//
//    public static void main(String[] args) {
//        Scrabble game = new Scrabble();
//        System.out.println("Welcome to Scrabble!");
//        Player player1 = new Player();
//        Player player2 = new Player();
//        Pool gamePool = new Pool();
//        Pool decisionPool = new Pool();
//
//        System.out.println("Player 1, Please enter your name: ");
//        Scanner newScanner = new Scanner(System.in);
//        String n;
//        n = newScanner.nextLine();
//        player1.setName(n);
//        System.out.println("Thank you " + player1.getName());
//
//        System.out.println("Player 2, Please enter your name: ");
//        n = newScanner.nextLine();
//        player2.setName(n);
//        System.out.println("Thank you " + player2.getName());
//
//        System.out.println("\nTo decide who goes first a random tile will be pulled for each player.");
//        ArrayList<Tile> randPlayer1 = decisionPool.drawTiles(1);
//        ArrayList<Tile> randPlayer2 = decisionPool.drawTiles(1);
//        System.out.println(player1.getName() + ", your random tile is: " + randPlayer1);
//        System.out.println(player2.getName() + ", your random tile is: " + randPlayer2);
//
//        //how to get tile on own - .get(0)?        //what if its a blank tile
////        if(randPlayer1.get(0).comparesTo(randPlayer2.get(0))){
////
////        }
//    }
//
//    public void turn(Player currentPlayer) {
//        //pull code for scanning testing in here
//        System.out.println("It is currently " + currentPlayer.getName() + "'s turn.");
//        System.out.println(currentPlayer.getFrame());
//        System.out.println(currentPlayer.getScore());
//        numTurns++;                                 //isnt tied to player is just global to the game - see in board? - used for legal placement
//
//        int scoreFromThisTurn = calculateScore(); // possibly make global
//
//        store until next turn;
//        if (score < 100 or other criteria to end i.e pool empty ){
//            if (numTurns % 2 == 0 probs wrong need to check ){
//                turn(playerOne) else turn(Player 2)
//            } else endGame()
//        }
//    }
//
//    public int calculateScore(input from the user?){
//        getModifiers and getValues of word
//        return score * modifier;
//    }
//
//    public Player decideWinner(Player one, Player two){
//        Player winner = null;
//        if(one.getScore() > two.getScore()){
//            winner = one;
//        }else if(two.getScore() > one.getScore()){
//            winner = two;
//        }else {
//            System.out.println("This game has resulted in a draw.");
//        }
//        return winner;
//    }
//
//}