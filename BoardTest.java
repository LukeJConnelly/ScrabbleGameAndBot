import java.util.ArrayList;

public class BoardTest {
	public static void main(String args[]){
		Board myBoardTest = new Board();
		System.out.println(myBoardTest.toString());
		myBoardTest.addTile('T', 1, 2);
		myBoardTest.addTile('E', 2, 2);
		myBoardTest.addTile('S', 3, 2);
		myBoardTest.addTile('T', 4, 2);
		System.out.println(myBoardTest.toString());
		System.out.println(myBoardTest.containsTile(4,2));
		System.out.println(myBoardTest.containsTile(4,3));
		myBoardTest.addTile('W', 4, 3);
		myBoardTest.addTile('O', 4, 4);
		System.out.println(myBoardTest.toString());
		myBoardTest.removeTile(1, 2);
		myBoardTest.removeTile(2, 2);
		myBoardTest.removeTile(3, 2);
		System.out.println(myBoardTest.toString());
		myBoardTest.boardReset();
		
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
		System.out.println(myBoardTest.getBoardTile(7,7)+" "+myBoardTest.getBoardTile(7,8));
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
		wantedRow = 7;
		wantedCol = 10;
		direction = false;
		System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
		myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
		wantedRow = 6;
		wantedCol = 8;
		direction = false;
		wantedWord="ME";
		System.out.println(myBoardTest.legalPlacement(wantedCol, wantedRow, wantedWord, direction, myFrame));
		myBoardTest.placeWord(wantedCol, wantedRow, wantedWord, direction, myFrame);
		System.out.println(myBoardTest.toString());
	}
}