import java.util.ArrayList;

public class BoardTest {
    public static void main(String args[]) {
        Board myBoardTest = new Board();
        System.out.println(myBoardTest.toString());
        myBoardTest.addTile('T', 1, 2);
        myBoardTest.addTile('E', 2, 2);
        myBoardTest.addTile('S', 3, 2);
        myBoardTest.addTile('T', 4, 2);
        System.out.println(myBoardTest.toString());
        System.out.println(myBoardTest.containsTile(4, 2));
        System.out.println(myBoardTest.containsTile(4, 3));
        myBoardTest.addTile('W', 4, 3);
        myBoardTest.addTile('O', 4, 4);
        System.out.println(myBoardTest.toString());
        myBoardTest.removeTile(1, 2);
        myBoardTest.removeTile(2, 2);
        myBoardTest.removeTile(3, 2);
        System.out.println(myBoardTest.toString());
        myBoardTest.boardReset();
        System.out.println(myBoardTest.toString());


        // MOCK GAME
        Frame myFrame = new Frame();
        ArrayList<Character> chars = new ArrayList<>();
        chars.add('O');
        chars.add('O');
        chars.add('L');
        myFrame.getLetters().addAll(chars);
        System.out.println(myBoardTest.toString());
        myBoardTest.addTile('T', 7, 7);
        myBoardTest.addTile('E', 8, 7);
        myBoardTest.addTile('S', 9, 7);
        myBoardTest.addTile('T', 10, 7);
        System.out.println(myBoardTest.toString());
        System.out.println(myBoardTest.getBoardTile(7, 7) + " " + myBoardTest.getBoardTile(7, 8));
        System.out.println(myFrame.toString());
        int wantedRow = 7, wantedCol = 7;
        String wantedWord = "TOO";
        boolean direction = false;
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
        System.out.println(myBoardTest.toString());
        System.out.println(myFrame.toString());
        chars = new ArrayList<>();
        chars.add('O');
        chars.add('O');
        chars.add('T');
        chars.add('M');
        myFrame.getLetters().addAll(chars);
        wantedRow = 0;
        wantedCol = 0;
        direction = true;
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        wantedRow = 15;
        wantedCol = 15;
        direction = true;
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        wantedRow = -1;
        wantedCol = -1;
        direction = true;
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        wantedRow = 7;
        // places into wanted row, but only when row is passed as col and vice versa
        wantedCol = 10;
        direction = false;
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
        wantedRow = 6;
        wantedCol = 8;
        direction = false;
        wantedWord = "ME";
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
        System.out.println(myBoardTest.toString());


        myBoardTest = new Board();
        System.out.println(myBoardTest.toString());


        myBoardTest.addTile('T', 1, 2); //simple tests to add single tiles
        myBoardTest.addTile('E', 2, 2);
        myBoardTest.addTile('S', 3, 2);
        myBoardTest.addTile('T', 4, 2);
        System.out.println(myBoardTest.toString());
        System.out.println(myBoardTest.containsTile(4, 2)); //tests of containsTile method
        System.out.println(myBoardTest.containsTile(4, 3));
        myBoardTest.addTile('W', 4, 3);
        myBoardTest.addTile('O', 4, 4);
        System.out.println(myBoardTest.toString());
        myBoardTest.removeTile(1, 2);   //test of removeTile method
        myBoardTest.removeTile(2, 2);
        myBoardTest.removeTile(3, 2);
        System.out.println(myBoardTest.toString());
        myBoardTest.boardReset();   //test of boardReset method
        System.out.println(myBoardTest.toString());


// MOCK GAME
        myFrame = new Frame();
        chars = new ArrayList<>();
        chars.add('T'); //adding letters to arraylist, then to frame
        chars.add('E');
        chars.add('S');
        chars.add('T');
        chars.add('O');
        chars.add('O');
        chars.add('L');
        myFrame.getLetters().addAll(chars);
        System.out.println(myBoardTest.toString());

        wantedRow = 9;
        wantedCol = 9;
        wantedWord = "TEST";
        direction = true;
        System.out.println(myBoardTest.firstTurn(wantedCol, wantedRow, wantedWord, direction, myFrame));
        //test for firstTurn trying to place first tiles off centre - should return false

        wantedRow = 7;
        wantedCol = 7;
        wantedWord = "HELLO";   //second test for first turn with invalid word input- should return false
        System.out.println(myBoardTest.firstTurn(wantedCol, wantedRow, wantedWord, direction, myFrame));

        wantedWord = "TEST";    //final test for firstTurn- should return true
        System.out.println(myBoardTest.firstTurn(wantedCol, wantedRow, wantedWord, direction, myFrame));
        myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
        System.out.println(myBoardTest.toString());

        System.out.println(myBoardTest.getBoardTile(7, 7) + " " + myBoardTest.getBoardTile(7, 8));
        System.out.println(myFrame.toString()); //printing of frame

        wantedWord = "TOO";
        System.out.println(myBoardTest.toString());
        System.out.println(myFrame.toString());
        chars = new ArrayList<>();
        chars.add('O');
        chars.add('O');
        chars.add('T');
        chars.add('M');
        myFrame.getLetters().addAll(chars);
        wantedRow = 0;
        wantedCol = 0;
        direction = true;
        //tests for legalPlacement method- should be false
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        wantedRow = 7;
        wantedCol = 10;
        direction = false;
        //further test of legalPlacement- should be true
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);

        wantedRow = 6;
        wantedCol = 8;
        direction = false;
        wantedWord = "ME";
        // final connecting test of legalPlacement
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
        //placement of another word
        System.out.println(myBoardTest.toString());
        System.out.println(myBoardTest.getBoardTile(7,7));
        // test for new peripheral placement
        chars = new ArrayList<>();
        chars.add('R');
        chars.add('O');
        chars.add('F');
        myFrame.getLetters().addAll(chars);
        wantedRow = 9;
        wantedCol = 11;
        direction = false;
        wantedWord = "FOR";
        //final peripheral test of legalPlacement - should be true
        System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
        myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
        System.out.println(myBoardTest.toString());
    }
}