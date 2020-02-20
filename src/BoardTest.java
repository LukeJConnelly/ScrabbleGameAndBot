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
		System.out.println(myBoardTest.toString());







		Board test = new Board();
		Frame myFrame = new Frame();
		Frame frame2 = new Frame();
		Pool testPool = new Pool();

		String word = "SU";
		ArrayList<Character> chars = new ArrayList<>();
		chars.add('S');
		chars.add('T');
		chars.add('U');
		myFrame.getLetters().addAll(chars);
		System.out.println(myFrame);
//		String word1 = "HI";
//		ArrayList<Character> asdf = new ArrayList<>();
//		asdf.add('H');
//		asdf.add('I');
//		myFrame.getLetters().addAll(chars);
//		System.out.println(myFrame.toString());


//
//		System.out.println(test.necessaryLetters(word, myFrame));
//		System.out.println(myFrame);
//


//		test.boardReset();
//		test.legalPlacement(1, 2, "TUS", 'D', myFrame);
//
////		test.legalPlacement(1, 2, "HI", 'R', frame2);
//		System.out.println(test.toString());


		System.out.println("Does the word contain at least one letter from the rack? " + test.oneFromRack(word, myFrame));
	}
}