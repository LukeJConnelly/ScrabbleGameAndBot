import java.util.ArrayList;

public class Test {
    public static Board board = new Board();
    public static void main(String[] args) {
        String frame = "AOFZYG_";
        System.out.println(frame.contains("Z")||frame.contains("Q"));
    }

    public static String toString(Board board)
    {
        String s="";
        for(int i=0; i<board.BOARD_SIZE; i++)
        {
            for(int j=0; j<board.BOARD_SIZE; j++)
            {
                s+=board.getSquareCopy(i,j).isOccupied() ? board.getSquareCopy(i,j).getTile().getLetter()+" " : "? ";
            }
            s+="\n";
        }
        return s;
    }

    public static ArrayList<Word> getAllWords(Word mainWord) {
        ArrayList<Word> words = new ArrayList<>();
        words.add(mainWord);
        int r = mainWord.getFirstRow();
        int c = mainWord.getFirstColumn();
        for (int i=0; i<mainWord.length(); i++) {
            if (!board.getSquareCopy(r,c).isOccupied()) {
                if (isAdditionalWord(r, c, mainWord.isHorizontal())) {
                    words.add(getAdditionalWord(r, c, mainWord.isHorizontal(), mainWord.getDesignatedLetter(i)));
                }
            }
            if (mainWord.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }
        return words;
    }

    private static boolean isAdditionalWord(int r, int c, boolean isHorizontal) {
        if ((isHorizontal &&
                ((r>0 && board.getSquareCopy(r-1,c).isOccupied()) || (r< Board.BOARD_SIZE-1 && board.getSquareCopy(r+1,c).isOccupied()))) ||
                (!isHorizontal &&
                        ((c>0 && board.getSquareCopy(r,c-1).isOccupied()) || (c< Board.BOARD_SIZE-1 && board.getSquareCopy(r,c+1).isOccupied()))) ) {
            return true;
        }
        return false;
    }

    private static Word getAdditionalWord(int mainWordRow, int mainWordCol, boolean mainWordIsHorizontal, char letter) {
        int firstRow = mainWordRow;
        int firstCol = mainWordCol;
        // search up or left for the first letter
        while (firstRow >= 0 && firstCol >= 0 && (board.getSquareCopy(firstRow,firstCol).isOccupied()||(firstRow==mainWordRow&&firstCol==mainWordCol))) {
            if (mainWordIsHorizontal) {
                firstRow--;
            } else {
                firstCol--;
            }
        }
        // went too far
        if (mainWordIsHorizontal) {
            firstRow++;
        } else {
            firstCol++;
        }
        // collect the letters by moving down or right
        String letters = "";
        int r = firstRow;
        int c = firstCol;
        while (r< Board.BOARD_SIZE && c< Board.BOARD_SIZE && (board.getSquareCopy(r,c).isOccupied()||(r==mainWordRow&&c==mainWordCol))) {
            if(r==mainWordRow&&c==mainWordCol)
            {
                letters = letters + letter;
            }
            else{
                letters = letters + board.getSquareCopy(r,c).getTile().getLetter();
            }
            if (mainWordIsHorizontal) {
                r++;
            } else {
                c++;
            }
        }
        return new Word (firstRow, firstCol, !mainWordIsHorizontal, letters);
    }
}
