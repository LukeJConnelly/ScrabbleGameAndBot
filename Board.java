import java.util.ArrayList;

public class Board {
    private int[][] boardValues = {     //assignment of board values for placement of special tiles later, such as
                                        //double and triple letter or word scores
            {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4},
            {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
            {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
            {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
            {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
            {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
            {4, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 4},
            {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
            {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
            {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
            {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
            {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
            {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
            {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4}
    };
    private char[][] boardTiles = new char[15][15];

    public void boardReset() {      //method to reset the board, will insert null character in all squares
        int i, j;
        for (i = 0; i < 15; i++) {
            for (j = 0; j < 15; j++) {
                boardTiles[i][j] = '\0';
            }
        }
    }

    public String toString() {
        String s = "";

        //pritning of column letters
        System.out.println("  A   B   C   D   E   F   G   H   I   J   K   L   M   N   O");

        s += "| ----------------------------------------------------------- |\n";

        int i, j;
        for (i = 0; i < 15; i++) {
            for (j = 0; j < 15; j++) {  //nested loop used to print each square, as well as a character in it

                s += "| ";
                if (boardTiles[i][j] != '\0') {
                    s += boardTiles[i][j] + " ";
                } else {
                    switch (this.boardValues[i][j]) {
                        case 0: {
                            s += "_ ";
                            break;
                        }
                        case 1: {
                            s += "d ";
                            break;
                        }
                        case 2: {
                            s += "t ";
                            break;
                        }
                        case 3: {
                            s += "2 ";
                            break;
                        }
                        case 4: {
                            s += "3 ";
                            break;
                        }
                    }
                }
            }

            s += "| | " + (i + 1);  //prints the row number at the end of the row

            s += "\n| ----------------------------------------------------------- |\n";
        }
        return s;
    }

    public void addTile(char c, int x, int y)   //method to add single tile to board
    {
        boardTiles[y][x] = c;
    }

    public void removeTile(int x, int y)    //method to remove single tile to board
    {
        boardTiles[y][x] = '\0';
    }

    public boolean containsTile(int x, int y)   //method checks if a square on the board is empty or not, returns a bool
    {
        return !(boardTiles[y][x] == '\0');
    }

    public char getBoardTile(int x, int y)  //method returns value of given board tile
    {

        return this.boardTiles[y][x];
    }

    public boolean legalPlacement(int row, int col, String word, boolean direction, Frame myFrame) {
        //method that checks if a move is legal
        //checks letters being placed are somehow adjacent to others already on the board, and don't go off the board

        //not first word placed	// bool direction 1=right, 0=down
        boolean flagEmptySquare = false, flagFullSquare = false, flagTouchTile = false;
        //1 - touch at least one empty square and 2 - touch at least one full square and 3 - at least one peripheral square has a letter
        int k;
        int right = direction ? 1 : 0;
        int down = !direction ? 1 : 0;
        ArrayList<Character> arrWord = convertWordToArrayList(word);
        ArrayList<Character> charsToBeReturned = new ArrayList<Character>();
        if (row < 0 || row + (right * word.length()) > 14 || col < 0 || col + (down * word.length()) > 14) {
            System.out.println("Your placement is not within the bounds of the board.");
            return false;
        } else {
            for (k = 0; k < arrWord.size(); k++) {
                if (!containsTile(row + k * right, col + k * down)) {
                    if (myFrame.checkLetter(arrWord.get(k))) {
                        myFrame.removeLetter(arrWord.get(k));
                        charsToBeReturned.add(arrWord.get(k));
                        flagEmptySquare = true;

                        if(row+k*right < 14){//dont check to right
                            if (containsTile(row - 1, col) || containsTile(row + 1, col) || containsTile(row, col - 1)) {
                                flagTouchTile = true;
                            }
                        }
                            if(row+k*right > 1){//dont check to left
                            if (containsTile(row - 1, col) || containsTile(row + 1, col) || containsTile(row, col + 1)) {
                                flagTouchTile = true;
                            }
                        }
                            if(col+k*down < 14){//dont check below
                            if (containsTile(row - 1, col) || containsTile(row, col - 1) || containsTile(row, col + 1)) {
                                flagTouchTile = true;
                            }
                        }
                            if(col+k*down > 1){//dont check above
                            if (containsTile(row + 1, col) || containsTile(row, col - 1) || containsTile(row, col + 1)) {
                                flagTouchTile = true;
                            }
                        } else {//check all sides
                            if (containsTile(row - 1, col) || containsTile(row + 1, col) || containsTile(row, col - 1) || containsTile(row, col + 1)) {
                                flagTouchTile = true;
                            }
                        }
                    } else {
                        myFrame.letters.addAll(charsToBeReturned);
                        System.out.println("You do not have the required Tiles to make this move.");
                        return false;
                    }
                } else {
                    flagFullSquare = true;
                    if (getBoardTile(row + k * right, col + k * down) != arrWord.get(k)) {
                        System.out.println("The word you want to place conflicts with another letter on the board");
                        myFrame.letters.addAll(charsToBeReturned);
                        return false;
                    }
                }
            }
        }

        myFrame.letters.addAll(charsToBeReturned);
        if(!(flagEmptySquare&&(flagFullSquare||flagTouchTile)))
        {
            System.out.println("You have not passed all of the tests, therefore you cannot place this word.");
            System.out.println("Either your attempt is not connecting to an existing word or you have not used at least one empty square.");
            return false;
        }
        else
        {
            System.out.println("Congratulations, your word is able to be placed on the board!");
            return true;
        }
    }

    public ArrayList<Character> convertWordToArrayList(String word) {   //method that turns string into ArrayList
        ArrayList<Character> brokenWord = new ArrayList<Character>();
        for (char c : word.toCharArray()) {
            brokenWord.add(c);
        }
        return brokenWord;
    }

    public void placeWord(int wantedRow, int wantedCol, String wantedWord, boolean direction, Frame myFrame) {
        //method to place a whole word (multiple letters) on the board
        int k;
        int right = direction ? 1 : 0;
        int down = !direction ? 1 : 0;
        ArrayList<Character> arrWord = convertWordToArrayList(wantedWord);
        for (k = 0; k < arrWord.size(); k++) {
            if (!containsTile(wantedRow + k * right, wantedCol + k * down)) {
                myFrame.removeLetter(arrWord.get(k));
                addTile(arrWord.get(k), wantedRow + k * right, wantedCol + k * down);
            }
        }
    }


    public boolean firstTurn( int x, int y, String word, boolean direction, Frame myFrame) {
        //method to check the first player places a tile on the centre with first move

        int k;
        boolean flagCentre = false;
        int right = direction ? 1 : 0;
        int down = !direction ? 1 : 0;
        ArrayList<Character> arrWord = convertWordToArrayList(word);
        for (k = 0; k < arrWord.size(); k++) {
            if (!myFrame.checkLetter(arrWord.get(k))) {
                return false;
            }
            if ((x + k * right == 7) && (y + k * down == 7)) {
                flagCentre = true;
            }
        }
        return flagCentre;

        }
    }








