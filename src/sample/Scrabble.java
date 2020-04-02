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

    //this method starts the game for us
    //we scan in two player names, then decide who goes first by drawing tiles
    public void setup(Player player1, Player player2, Pool gamePool, Pool decisionPool) {
        System.out.println("Player 1, Please enter your name: ");
        Scanner newScanner = new Scanner(System.in);
        String n;
        n = newScanner.nextLine();  //scanning player1's name
        player1.setName(n); //setting player1's name
        System.out.println("Thank you " + player1.getName());

        System.out.println("Player 2, Please enter your name: ");
        n = newScanner.nextLine();  //scanning player2's name
        player2.setName(n);//setting player2's name
        System.out.println("Thank you " + player2.getName());
        System.out.println("\nTo decide who goes first a random tile will be pulled for each player.");

        //assignment of tiles to each player
        ArrayList<Tile> randPlayer1 = decisionPool.drawTiles(1);
        ArrayList<Tile> randPlayer2 = decisionPool.drawTiles(1);

        System.out.println(player1.getName() + ", your random tile is: " + randPlayer1);
        System.out.println(player2.getName() + ", your random tile is: " + randPlayer2);

        //logic for case players pull same tile
        while (randPlayer1.get(0).getLetter() == randPlayer2.get(0).getLetter()) {
            randPlayer1 = decisionPool.drawTiles(1);
            randPlayer2 = decisionPool.drawTiles(1);
            System.out.println(player1.getName() + ", your new random tile is: " + randPlayer1);
            System.out.println(player2.getName() + ", your new random tile is: " + randPlayer2);
        }
        if (randPlayer1.get(0).getValue() == 0) { //null beats any letter, and we already know that they do not have matching tiles
            player1.getFrame().refill(gamePool);    //filling of player frames from pool
            player2.getFrame().refill(gamePool);
            System.out.println(player1.getName() + " will go first.");
        } else if (randPlayer2.get(0).getValue() == 0) {
            //logic to swap player 1 and 2
            Player temp = new Player();
            temp.setName(player1.getName());
            player1.setName(player2.getName());
            player2.setName(temp.getName());
            player1.getFrame().refill(gamePool);    //filling player frames from pool
            player2.getFrame().refill(gamePool);
            System.out.println(player1.getName() + " will go first.");

            // swap here as if p1 > p2, that means its letter is further down in the alphabet
        } else if (randPlayer1.get(0).getLetter() > randPlayer2.get(0).getLetter()) {
            //swapping player 1 and 2
            Player temp = new Player();
            temp.setName(player1.getName());
            player1.setName(player2.getName());
            player2.setName(temp.getName());
            player1.getFrame().refill(gamePool);    //filling player frames from pool
            player2.getFrame().refill(gamePool);
            System.out.println(player1.getName() + " will go first.");
        } else {
            player1.getFrame().refill(gamePool);    //filling player frames from pool
            player2.getFrame().refill(gamePool);
            System.out.println(player1.getName() + " will go first.");
        }
        System.out.println("\nThe game has begun! Please see the board and player input pop ups. Type HELP in the Player Input window for more input info!");
    }

    //this method is for when a player wishes to change any or all of his tiles
    public void exchange(InputPopUp playerInput, boolean player, Player player1, Player player2, Pool gamePool, int turns) {
        if (player){
            if (player2.getFrame().isAvailable(playerInput.playerInput.trim().replaceAll("EXCHANGE ", ""))) {
                //removal of unwanted tiles
                player2.getFrame().remove(playerInput.playerInput.trim().replaceAll("EXCHANGE ", ""));
                //filling pool back to capacity with random tiles from pool
                player2.getFrame().refill(gamePool);
                System.out.println(player2.getName() + " your frame is now:\n" + player2.getFrame().toString());
                turns = 0;
            } else {
                //rejecting of exchange attempt
                System.out.println("You do not have the letters required for this exchange!");
                player = !player;
            }
        } else {
            if (player1.getFrame().isAvailable(playerInput.playerInput.trim().replaceAll("EXCHANGE ", ""))) {
                //removal of unwanted tiles
                player1.getFrame().remove(playerInput.playerInput.trim().replaceAll("EXCHANGE ", ""));
                //filling pool back to capacity with random tiles from pool
                player1.getFrame().refill(gamePool);
                System.out.println(player1.getName() + " your frame is now:\n" + player1.getFrame().toString());
                turns = 0;
            } else {
                //rejecting of exchange attempt
                System.out.println("You do not have the letters required for this exchange!");
                player = !player;
            }
        }
    }

    //method for a player(currPlayer) to place a word(currWord) on the board(myBoard)
    public void move(Board myBoard, Word currWord, Player currPlayer) {
        myBoard.place(currPlayer.getFrame(), currWord);
        currPlayer.addScore(calculateScore(myBoard, currWord, 0));//increasing score for player
        for (Word word : findPeripheral(currWord, 0))
        {
            currPlayer.addScore(calculateScore(myBoard, word, 0));
        }
    }

    public void unmove(Board myBoard, Word currWord, Player currPlayer) {
        currPlayer.addScore(-1*calculateScore(myBoard, currWord, 1));//decreasing score for player
        for (Word word : findPeripheral(currWord, 1))
        {
            currPlayer.addScore(-1*calculateScore(myBoard, word, 1));
        }
        myBoard.unplace(currWord);
    }

    //method to calculate how much a placed word should be worth
    public int calculateScore(Board board, Word word, int turns) {  //input from the user?
        int tally = 0;
        int r = word.getFirstRow(), c = word.getFirstColumn();
        if (word.isHorizontal()) {
            for (int i = 0; i < word.getLength(); i++) {
                if ((board.squares[r][c + i].getLetterMuliplier() == 2 || board.squares[r][c + i].getLetterMuliplier() == 3)&&board.squares[r][c+i].getTile().turnsOnBoard==turns) {
                    //letter value multiplied by square value is added to tally
                    tally += board.squares[r][c + i].getLetterMuliplier() * board.squares[r][c + i].getTile().getValue();
                } else {
                    tally += board.squares[r][c + i].getTile().getValue();
                }
            }
            for (int i = 0; i < word.getLength(); i++) {
                if ((board.squares[r][c + i].getWordMultiplier() == 2 || board.squares[r][c + i].getWordMultiplier() == 3)&&board.squares[r][c+i].getTile().turnsOnBoard==turns) {
                    tally *= board.squares[r][c + i].getWordMultiplier();
                }
            }
        } else { //if vertical
            for (int i = 0; i < word.getLength(); i++) {
                if ((board.squares[r + i][c].getLetterMuliplier() == 2 || board.squares[r + i][c].getLetterMuliplier() == 3)&&board.squares[r+i][c].getTile().turnsOnBoard==turns) {
                    tally += board.squares[r + i][c].getLetterMuliplier() * board.squares[r + i][c].getTile().getValue();
                } else {
                    tally += board.squares[r + i][c].getTile().getValue();
                }
            }
            for (int i = 0; i < word.getLength(); i++) {
                if ((board.squares[r + i][c].getWordMultiplier() == 2 || board.squares[r + i][c].getWordMultiplier() == 3)&&board.squares[r+i][c].getTile().turnsOnBoard==turns) {
                    tally *= board.squares[r + i][c].getWordMultiplier();
                }
            }
        }
        return tally;
    }

    //method to declare the game is over
    public boolean end(Player player1, Player player2, int turnsWOMove) {
        if (player1.getFrame().isEmpty() || player2.getFrame().isEmpty() || turnsWOMove > 5) {//no more moves can be made
            System.out.println("\nThe game has finished!\n");
            return true;
        }
        return false;
    }
    //method to establish and declare the winning player
    public void getWinner(Player player1, Player player2) throws Exception {
        String s = "";
        //declaration of scoreline
        s += "Final scores:\n" + player1.getName() + " " + player1.getScore() + " - " + player2.getScore() + " " + player2.getName() + "\n";

        //logic to congratulate winner, or find out if there isn't one
        if (player1.getScore() > player2.getScore()) {
            s += "Congratulations " + player1.getName() + "!\n";
        }
        else if (player2.getScore() > player1.getScore()) {
            s += "Congratulations " + player2.getName() + "!\n";
        }
        else {
            s += "It's a draw! Everyone's a loser!\n";
        }
        s+="Thanks for Playing!";
        WinnerPopUp endScreen = new WinnerPopUp();
        endScreen.displayText = s;  //displaying scoreline and winner
        Stage finalStage = new Stage();
        endScreen.start(finalStage);    //game is over
    }

    //function which returns peripheral words
    public ArrayList<Word> findPeripheral (Word word, int turns)
    {
        ArrayList<Word> wordList = new ArrayList<Word>();
        for(int i=0;i<word.getLength();i++)
        {
            if(word.isHorizontal()) {
                if(Main.board.squares[word.getFirstRow()][word.getFirstColumn()+i].getTile().turnsOnBoard==turns){
                    String s=Character.toString(Main.board.squares[word.getFirstRow()][word.getFirstColumn()+i].getTile().getLetter());
                    int j=1, newFirstRow=-1;
                    while(true) {
                        if (!(word.getFirstRow() + j > 14 || word.getFirstRow() - j < 0)) {
                            if (Main.board.squares[word.getFirstRow() + j][word.getFirstColumn() + i].isOccupied() && Main.board.squares[word.getFirstRow() - j][word.getFirstColumn() + i].isOccupied()) {
                                s = Character.toString(Main.board.squares[word.getFirstRow() - j][word.getFirstColumn() + i].getTile().getLetter())
                                        + s +
                                        Character.toString(Main.board.squares[word.getFirstRow() + j][word.getFirstColumn() + i].getTile().getLetter());
                                newFirstRow=word.getFirstRow() - j;
                            }
                            else if (Main.board.squares[word.getFirstRow() + j][word.getFirstColumn() + i].isOccupied()) {
                                s = s + Character.toString(Main.board.squares[word.getFirstRow() + j][word.getFirstColumn() + i].getTile().getLetter());
                                if(newFirstRow==-1)
                                {newFirstRow=word.getFirstRow();}
                            }
                            else if (Main.board.squares[word.getFirstRow() - j][word.getFirstColumn() + i].isOccupied()) {
                                s = Character.toString(Main.board.squares[word.getFirstRow() - j][word.getFirstColumn() + i].getTile().getLetter()) + s;
                                newFirstRow=word.getFirstRow() - j;
                            }
                            else{
                                break;
                            }
                        }
                        else if (!(word.getFirstRow() + j > 14)) {
                            if (Main.board.squares[word.getFirstRow() + j][word.getFirstColumn() + i].isOccupied()) {
                                s = s + Character.toString(Main.board.squares[word.getFirstRow() + j][word.getFirstColumn() + i].getTile().getLetter());
                                if(newFirstRow==-1)
                                {newFirstRow=word.getFirstRow();}
                            }
                            else{
                                break;
                            }
                        }
                        else if (!(word.getFirstRow() - j < 0)) {
                            if (Main.board.squares[word.getFirstRow() - j][word.getFirstColumn() + i].isOccupied()) {
                                if (Main.board.squares[word.getFirstRow() - j][word.getFirstColumn() + i].isOccupied()) {
                                    s = Character.toString(Main.board.squares[word.getFirstRow() - j][word.getFirstColumn() + i].getTile().getLetter()) + s;
                                    newFirstRow=word.getFirstRow() - j;
                                }
                            }
                            else{
                                break;
                            }
                        }
                        else{
                            break;
                        }
                        j++;
                    }
                    if(newFirstRow!=-1)
                    {
                        Word foundWord = new Word(newFirstRow,word.getFirstColumn() + i, false, s);
                        wordList.add(foundWord);
                    }
                }
            }
            else {
                if(Main.board.squares[word.getFirstRow()+i][word.getFirstColumn()].getTile().turnsOnBoard==turns){
                    String s=Character.toString(Main.board.squares[word.getFirstRow()+i][word.getFirstColumn()].getTile().getLetter());
                    int j=1, newFirstCol=-1;
                    while(true) {
                        if (!(word.getFirstColumn() + j > 14 || word.getFirstColumn() - j < 0)) {
                            if (Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() + j].isOccupied() && Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() - j].isOccupied()) {
                                s = Character.toString(Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() - j].getTile().getLetter())
                                        + s +
                                        Character.toString(Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() + j].getTile().getLetter());
                                newFirstCol=word.getFirstColumn() - j;
                            }
                            else if (Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() + j].isOccupied()) {
                                s = s + Character.toString(Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() + j].getTile().getLetter());
                                if(newFirstCol==-1)
                                {newFirstCol=word.getFirstColumn();}
                            }
                            else if (Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() - j].isOccupied()) {
                                s = Character.toString(Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() - j].getTile().getLetter()) + s;
                                newFirstCol=word.getFirstColumn() - j;
                            }
                            else{
                                break;
                            }
                        }
                        else if (!(word.getFirstColumn() + j > 14)) {
                            if (Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() + j].isOccupied()) {
                                s = s + Character.toString(Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() + j].getTile().getLetter());
                                if(newFirstCol==-1)
                                {newFirstCol=word.getFirstColumn();}
                            }
                            else{
                                break;
                            }
                        }
                        else if (!(word.getFirstColumn() - j < 0)) {
                                if (Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() - j].isOccupied()) {
                                    s = Character.toString(Main.board.squares[word.getFirstRow() + i][word.getFirstColumn() - j].getTile().getLetter()) + s;
                                    newFirstCol=word.getFirstColumn() - j;
                                }
                            else{
                                break;
                            }
                        }
                        else{
                            break;
                        }
                        j++;
                    }
                    if(newFirstCol!=-1)
                    {
                        Word foundWord = new Word(word.getFirstRow() + i, newFirstCol, true, s);
                        wordList.add(foundWord);
                    }
                }
            }
        }
        return wordList;
    }
}


