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
		ArrayList<Character> brokenWord = convertWordToArrayList(word);
//need a second arraylist to store the removed tiles and then add them back in - so as not to affect the actual frame when just checking - need to remove the letter from the frame when its actually being placed tho!
		Frame spare = new Frame();
		spare.letters.addAll(myFrame.letters);
		boolean flag = false;
		for(int i = 0; i<brokenWord.size(); i++) {
			flag = spare.checkLetter(brokenWord.get(i));//check that it has the letter
			if (flag == true) { //take out the letters from the frame as going along to confirm that there is only one
				spare.removeLetter(brokenWord.get(i)); //if the letter is there
			} else return false;//if it doesn't we return false to the user
		}
		return flag;
	}

	public ArrayList<Character> convertWordToArrayList(String word){
		ArrayList<Character> brokenWord = new ArrayList<Character>();

		for(char c : word.toCharArray()){
			brokenWord.add(c);
		}

		return brokenWord;
	}

	//definitely not working
	public boolean oneFromRack(String word, Frame frame){ //check if this is actually working
		ArrayList<Character> brokenWord = convertWordToArrayList(word);

		if(brokenWord.size() == 1){
			necessaryLetters(word, frame);
		}
		else if(brokenWord.size() > 1){
			necessaryLetters(word, frame);
		}
		return false;
	}


	//x and y - coordinates on the board, direction either D or R
	public boolean legalPlacement(int row, int col, String word, char direction, Frame myFrame) { //not first word placed
		boolean flagEmptySquare = false, flagFullSquare = false; //1 - touch at least one empty square and 2 - touch at least one full square
		char curr = 0;

		if (row > 0 && row <= 15 + word.length() && col > 0 && col <= 15 + word.length()) { //checking the word is placed within the bounds of the board
			//for(int i = 0; i < word.length(); i++){//should we be looping here like?
			ArrayList<Character> arrWord = convertWordToArrayList(word);
			if (direction == 'D') {
				directionD(row, col, arrWord, curr, flagEmptySquare, flagFullSquare);
			} else if (direction == 'R') {
				//					row++;//why do we increment here and in the check?
				//					if(containsTile(row+1, col)) { //square to right is empty
				//						necessaryLetters(word, myFrame);
				//						//place tile;
				//						addTile(curr, row, col);
				//						flagEmptySquare = true;
				//					}else{
				//						flagFullSquare = true;
				//					}ArrayList<Character> arrWord = convertWordToArrayList(word);
				for (int k = 0; k < arrWord.size(); k++, row++) {
					curr = arrWord.get(k);
					if (!containsTile(row + 1, col)) { // square beneath is empty
						if(necessaryLetters(word, myFrame)) {
							//place tile
							addTile(curr, row, col); //need to loop through the word at some point - possibly convert to arraylist
							flagEmptySquare = true;
						}
					} else {
						flagFullSquare = true;
					}
				}
			}

			//}
		} else return false; //outside the bounds of the board

		if (flagEmptySquare && flagFullSquare) {
			return true;
		} else return false;
	}//possible switch statement for direction and first move

//this works for placing the letters downward - haven't fully tested yet
	public boolean directionD(int row, int col, ArrayList<Character> arrWord, char curr, boolean flag1, boolean flag2 ){
		for (int k = 0; k < arrWord.size(); k++, col++) {
			curr = arrWord.get(k);
			if (!containsTile(row, col + 1)) { // square beneath is empty
				//if (necessaryLetters(word, myFrame)) {     this is removing the letters and we still need them so like shit
				//placing tile
				addTile(curr, row, col); //need to loop through the word at some point - possibly convert to arraylist
				flag1 = true;
			} else {
				flag2 = true;
			}
		}
		return false;
	}







//Not sure if this part is relevant anymore

	public boolean wordPlacement(String word){
//
//		necessaryLetters();
//		boundsOfBoard();
//		existingLetters();
//		oneFromRack(word, frame);
//		if (word = first word){
//			placeCentre();
//		}else {
//			connectsWithExisting();
//		}

		return false;
	}

}