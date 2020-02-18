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
				boardTiles[j][i]='\0';
			}
		}
	}
	public String toString()
	{
		String s="";
		int i,j;
		for (i=0;i<15;i++)
		{
			for(j=0;j<15;j++)
			{
				s+="|";
				if (boardTiles[j][i]!='\0')
				{
					s+=boardTiles[j][i];
				}
				else
				{
					switch(this.boardValues[j][i])
					{
						case 0:
						{
							s+="_";
							break;
						}
						case 1:
						{
							s+="d";
							break;
						}
						case 2:
						{
							s+="t";
							break;
						}
						case 3:
						{
							s+="2";
							break;
						}
						case 4:
						{
							s+="3";
							break;
						}
					}
				}
			}
			s+="|\n-------------------------------\n";
		}
		return s;
	}
	public void addTile(char c, int x, int y)
	{
		boardTiles[x][y]=c;
	}
	public void removeTile(int x, int y)
	{
		boardTiles[x][y]='\0';
	}
	public boolean containsTile(int x, int y)
	{
		return !(boardTiles[x][y]=='\0');
	}

	public boolean necessaryLetters(String word, Frame myFrame){			//only works for uppercase - throw .toUpperCase on the string taken in from the scanner
		ArrayList<Character> brokenWord = new ArrayList<Character>();

		for(char c : word.toCharArray()){
			brokenWord.add(c);
		}

		boolean flag = false;
		for(int i = 0; i<brokenWord.size(); i++) {
			flag = myFrame.checkLetter(brokenWord.get(i));

			if (flag) { //take out the letters from the frame as going along to confirm that there is only one
				myFrame.removeLetter(brokenWord.get(i)); //if the letter is there
			} else return false;
		}
		return flag;
	}
}