package sample;

public class Board {

    public static final int BOARD_SIZE = 15;
    public static final int BOARD_CENTRE = 7;

    private static final int[][] LETTER_MULTIPLIER =
            { {1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1},
                    {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
                    {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
                    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                    {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
                    {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
                    {1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} };
    private static final int[][] WORD_MULTIPLIER =
            { {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3},
                    {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                    {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
                    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                    {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                    {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
                    {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                    {3, 1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1, 3} };

    public static final int WORD_INCORRECT_FIRST_PLAY = 0;
    public static final int WORD_OUT_OF_BOUNDS = 1;
    public static final int WORD_LETTER_NOT_IN_FRAME = 2;
    public static final int WORD_LETTER_CLASH = 3;
    public static final int WORD_NO_LETTER_PLACED = 4;
    public static final int WORD_NO_CONNECTION = 5;

    public Square[][] squares;
    private int checkCode;
    public int numPlays;

    Board() {
        squares = new Square[BOARD_SIZE][BOARD_SIZE];
        for (int r=0; r<BOARD_SIZE; r++)  {
            for (int c=0; c<BOARD_SIZE; c++)   {
                squares[r][c] = new Square(LETTER_MULTIPLIER[r][c],WORD_MULTIPLIER[r][c]);
            }
        }
        numPlays = 0;
    }

    public void display () {
        System.out.print("    ");
        for (int c = 0; c< BOARD_SIZE; c++) {
            System.out.printf("%c ",(char) ((int) 'A' + c));
        }
        System.out.println();
        for (int r = 0; r< BOARD_SIZE; r++) {
            System.out.printf("%2d  ", r+1);
            for (int c = 0; c< BOARD_SIZE; c++) {
                if (squares[r][c].isOccupied()) {
                    System.out.printf("%c ",squares[r][c].getTile().getLetter());
                }
                else {
                    if (squares[r][c].isDoubleLetter()) {
                        System.out.print("dl");
                    } else if (squares[r][c].isTripleLetter()) {
                        System.out.print("tl");
                    } else if (squares[r][c].isDoubleWord()) {
                        System.out.print("dw");
                    } else if (squares[r][c].isTripleWord()) {
                        System.out.print("tw");
                    } else {
                        System.out.print("  ");
                    }
                }
            }
            System.out.printf("  %2d\n", r+1);
        }
    }

    public boolean isLegal(Frame frame, Word word) {
        boolean isLegal = true;
        //check for invalid first play
        if (numPlays == 0 &&
                ((word.isHorizontal() && (word.getRow()!=BOARD_CENTRE || word.getFirstColumn()>BOARD_CENTRE ||
                        word.getLastColumn()<BOARD_CENTRE)) ||
                        (word.isVertical() && (word.getColumn()!=BOARD_CENTRE || word.getFirstRow()>BOARD_CENTRE ||
                                word.getLastRow()<BOARD_CENTRE)))) {
            isLegal = false;
            checkCode = WORD_INCORRECT_FIRST_PLAY;
        }
        // check for word out of bounds
        if (isLegal && ((word.isHorizontal() && word.getLastColumn()>= BOARD_SIZE) ||
                (word.isVertical() && word.getLastRow()>= BOARD_SIZE))) {
            isLegal = false;
            checkCode = WORD_OUT_OF_BOUNDS;
        }
        // check that letters in the word do not clash with those on the board
        String lettersPlaced = "";
        if (isLegal) {
            int r = word.getFirstRow();
            int c = word.getFirstColumn();
            for (int i = 0; i < word.getLength() && isLegal; i++) {
                if (squares[r][c].isOccupied() && squares[r][c].getTile().getLetter() != word.getLetter(i)) {
                    isLegal = false;
                    checkCode = WORD_LETTER_CLASH;
                } else if (!squares[r][c].isOccupied()) {
                    lettersPlaced = lettersPlaced + word.getLetter(i);
                }
                if (word.isHorizontal()) {
                    c++;
                } else {
                    r++;
                }
            }
        }
        // check that more than one letter is placed
        if (isLegal && lettersPlaced.length() == 0) {
            isLegal = false;
            checkCode = WORD_NO_LETTER_PLACED;
        }
        // check that the letters placed are in the frame
        if (isLegal && !frame.isAvailable(lettersPlaced)) {
            isLegal = false;
            checkCode = WORD_LETTER_NOT_IN_FRAME;
        }
        // check that the letters placed connect with the letters on the board
        if (isLegal && numPlays!=0) {
            int boxTop = Math.max(word.getFirstRow()-1,0);
            int boxBottom = Math.min(word.getLastRow()+1, BOARD_SIZE-1);
            int boxLeft = Math.max(word.getFirstColumn()-1,0);
            int boxRight = Math.min(word.getLastColumn()+1, BOARD_SIZE-1);
            boolean foundConnection = false;
            for (int r=boxTop; r<=boxBottom && !foundConnection; r++) {
                for (int c=boxLeft; c<=boxRight && !foundConnection; c++) {
                    if (squares[r][c].isOccupied()) {
                        foundConnection = true;
                    }
                }
            }
            if (!foundConnection) {
                isLegal = false;
                checkCode = WORD_NO_CONNECTION;
            }
        }
        return isLegal;
    }

    // getCheckCode precondition: isLegal is false
    public int getCheckCode() {
        return checkCode;
    };

    // place precondition: isLegal is true
    public void place(Frame frame, Word word) {
        int r = word.getFirstRow();
        int c = word.getFirstColumn();
        for (int i=0; i<word.getLength(); i++) {
            if (!squares[r][c].isOccupied()) {
                char letter = word.getLetter(i);
                Tile tile = frame.getTile(letter);
                squares[r][c].add(tile);
                frame.remove(tile);
            }
            if (word.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }
        numPlays++;
    }

    public void unplace(Word word) {
        int r = word.getFirstRow();
        int c = word.getFirstColumn();
        for (int i=0; i<word.getLength(); i++) {
            if(squares[r][c].getTile().turnsOnBoard==1) {
                squares[r][c].remove();
            }
            if (word.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }
        numPlays--;
    }

}
