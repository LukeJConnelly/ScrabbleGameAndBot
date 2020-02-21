import java.util.ArrayList;

public class Board {
	private int[][] boardValues = {
		{4,0,0,1,0,0,0,4,0,0,0,1,0,0,4},
		{0,3,0,0,0,2,0,0,0,2,0,0,0,3,0},
		{0,0,3,0,0,0,1,0,1,0,0,0,3,0,0},
		{1,0,0,3,0,0,0,1,0,0,0,3,0,0,1},
		{0,0,0,0,3,0,0,0,0,0,3,0,0,0,0},
		{0,2,0,0,0,2,0,0,0,2,0,0,0,2,0},
		{0,0,1,0,0,0,1,0,1,0,0,0,1,0,0},
		{4,0,0,1,0,0,0,3,0,0,0,1,0,0,4},
		{0,0,1,0,0,0,1,0,1,0,0,0,1,0,0},
		{0,2,0,0,0,2,0,0,0,2,0,0,0,2,0},
		{0,0,0,0,3,0,0,0,0,0,3,0,0,0,0},
		{1,0,0,3,0,0,0,1,0,0,0,3,0,0,1},
		{0,0,3,0,0,0,1,0,1,0,0,0,3,0,0},
		{0,3,0,0,0,2,0,0,0,2,0,0,0,3,0},
		{4,0,0,1,0,0,0,4,0,0,0,1,0,0,4}
	};
	private char[][] boardTiles = new char[15][15];
	public void boardReset()
	{
		int i,j;
		for (i=0;i<15;i++)
		{
			for (j=0;j<15;j++)
			{
				boardTiles[i][j]='\0';
			}
		}
	}
	 public String toString()
	    {
	        String s="";

	        //pritning of column letters
	        System.out.println("  A   B   C   D   E   F   G   H   I   J   K   L   M   N   O");

	        s+="| ----------------------------------------------------------- |\n";

	        int i,j;
	        for (i=0;i<15;i++)
	        {
	            for(j=0;j<15;j++)
	            {

	                s+="| ";
	                if (boardTiles[i][j]!='\0')
	                {
	                    s+=boardTiles[i][j] + " ";
	                }
	                else
	                {
	                    switch(this.boardValues[i][j])
	                    {
	                        case 0:
	                        {
	                            s+="_ ";
	                            break;
	                        }
	                        case 1:
	                        {
	                            s+="d ";
	                            break;
	                        }
	                        case 2:
	                        {
	                            s+="t ";
	                            break;
	                        }
	                        case 3:
	                        {
	                            s+="2 ";
	                            break;
	                        }
	                        case 4:
	                        {
	                            s+="3 ";
	                            break;
	                        }
	                    }
	                }
	            }

	            s+= "| | " +(i+1);  //prints the row number

	            s+="\n| ----------------------------------------------------------- |\n";
	        }
	        return s;
	    }
	public void addTile(char c, int x, int y)
	{
		boardTiles[y][x]=c;
	}
	public void removeTile(int x, int y)
	{
		boardTiles[y][x]='\0';
	}
	public boolean containsTile(int x, int y)
	{
		return !(boardTiles[y][x]=='\0');
	}
	public char getBoardTile(int x, int y)
	{
		return this.boardTiles[y][x];
	}
	public boolean legalPlacement(int row, int col, String word, boolean direction, Frame myFrame) { //not first word placed	// bool direction 1=right, 0=down
		boolean flagEmptySquare = false, flagFullSquare = false; //1 - touch at least one empty square and 2 - touch at least one full square
		int k;
		int right = direction ? 1 : 0;
		int down = !direction ? 1 : 0;
		ArrayList<Character> arrWord = convertWordToArrayList(word);
		ArrayList<Character> charsToBeReturned = new ArrayList<Character>();
		if (row < 0 || row + (right * word.length()) > 14 || col < 0 || col + (down * word.length()) > 14)
		{
			return false;
		}
		for (k = 0; k < arrWord.size(); k++) {
			if (!containsTile(row + k * right, col + k * down)) {
				if(myFrame.checkLetter(arrWord.get(k))) {
					myFrame.removeLetter(arrWord.get(k));
					charsToBeReturned.add(arrWord.get(k));
					flagEmptySquare = true;
				}
				else
				{
					myFrame.letters.addAll(charsToBeReturned);
					return false;
				}
			}
			else {
				flagFullSquare = true;
				if(getBoardTile(row + k * right, col + k * down)!=arrWord.get(k))
				{
					myFrame.letters.addAll(charsToBeReturned);
					return false;
				}
			}
		}
		myFrame.letters.addAll(charsToBeReturned);
		if(!(flagEmptySquare&&flagFullSquare))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public ArrayList<Character> convertWordToArrayList(String word){
		ArrayList<Character> brokenWord = new ArrayList<Character>();
		for(char c : word.toCharArray()){
			brokenWord.add(c);
		}
		return brokenWord;
	}
	public void placeWord(int wantedRow, int wantedCol, String wantedWord, boolean direction, Frame myFrame)
	{
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
}