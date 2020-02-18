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
	}
}