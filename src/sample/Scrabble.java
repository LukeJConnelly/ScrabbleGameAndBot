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
    public void setup(Player player1, Player player2, Pool gamePool, Pool decisionPool, boolean player){
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
        if(randPlayer1.get(0).getLetter()>randPlayer2.get(0).getLetter())
        {
            player=true;
        }
        if (!player) {
            player1.getFrame().refill(gamePool);
        }
        player2.getFrame().refill(gamePool);
        if (player) {
            player1.getFrame().refill(gamePool);
        }
    }
    public void exchange(InputPopUp playerInput, boolean player, Player player1, Player player2, Pool gamePool, int turns)
    {
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
    }
    public void move(Board myBoard, Word currWord, Player currPlayer) {
        myBoard.place(currPlayer.getFrame(), currWord);
        currPlayer.addScore(calculateScore(myBoard, currPlayer, currWord));
    }
    public int calculateScore(Board board, Player currentPlayer, Word word) {  //input from the user?
        int tally = 0;
        int r = word.getFirstRow(), c = word.getFirstColumn();
        if (word.isHorizontal()) {
            for (int i = 0; i < word.getLength(); i++) {
                if(board.squares[r][c+i].getLetterMuliplier()==2||board.squares[r][c+i].getLetterMuliplier()==3) {
                    tally += board.squares[r][c + i].getLetterMuliplier() * board.squares[r][c + i].getTile().getValue();
                }
                else{
                    tally += board.squares[r][c + i].getTile().getValue();
                }
            }
            for (int i = 0; i < word.getLength(); i++) {
                if(board.squares[r][c+i].getWordMultiplier()==2||board.squares[r][c+i].getWordMultiplier()==3) {
                    tally *= board.squares[r][c + i].getWordMultiplier();
                }
            }
        }
        else {
            for (int i = 0; i < word.getLength(); i++) {
                if(board.squares[r+i][c].getLetterMuliplier()==2||board.squares[r+i][c].getLetterMuliplier()==3) {
                    tally += board.squares[r+i][c].getLetterMuliplier() * board.squares[r+i][c].getTile().getValue();
                }
                else{
                    tally += board.squares[r+i][c].getTile().getValue();
                }
            }
            for (int i = 0; i < word.getLength(); i++) {
                if(board.squares[r+i][c].getWordMultiplier()==2||board.squares[r+i][c].getWordMultiplier()==3) {
                    tally *= board.squares[r+i][c].getWordMultiplier();
                }
            }
        }
        return tally;
    }
    public boolean end(Player player1, Player player2, int turnsWOMove)
    {
        if (player1.getFrame().isEmpty()||player2.getFrame().isEmpty()||turnsWOMove>5)
        {
            System.out.println("\nThe game has finished!\n");
            return true;
        }
        return false;
    }
    public void getWinner(Player player1, Player player2)
    {
//        Scanner newScanner = new Scanner(System.in);
//        int n;
//        System.out.println("Please enter the penalties for "+player1.getName()+":");
//        n = -1*newScanner.nextInt();
//        player1.addScore(n);
//        System.out.println("Please enter the penalties for "+player2.getName()+":");
//        n = -1*newScanner.nextInt();
//        player2.addScore(n);
        System.out.println("The scores are:\n"+player1.getName()+" "+player1.getScore()+" - "+player2.getScore()+" "+player2.getName()+"\n");
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
