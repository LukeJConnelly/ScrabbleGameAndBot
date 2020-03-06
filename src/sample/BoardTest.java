package sample;

public class BoardTest {

	private static Board board = new Board();
	private static Frame frame = new Frame();

	public static void main(String[] args) {

		// display blank
		System.out.println("BLANK BOARD");
		board.display();
		
		// Test checkWord first play
		frame.setTiles("HELLOXY");
		Word newWord = new Word(1,1,true,"HELLO");
		doTest(1, newWord, false, Board.WORD_INCORRECT_FIRST_PLAY);

		newWord = new Word(Board.BOARD_SIZE,Board.BOARD_SIZE-6,true,"HELLO");
		doTest(2, newWord, false, Board.WORD_INCORRECT_FIRST_PLAY);

		newWord = new Word(Board.BOARD_SIZE,Board.BOARD_SIZE+1,true,"HELLO");
		doTest(3, newWord, false, Board.WORD_INCORRECT_FIRST_PLAY);

		newWord = new Word(Board.BOARD_SIZE-6,Board.BOARD_SIZE,true,"HELLO");
		doTest(4, newWord, false, Board.WORD_INCORRECT_FIRST_PLAY);

		newWord = new Word(Board.BOARD_SIZE,Board.BOARD_SIZE+1,true,"HELLO");
		doTest(5, newWord, false, Board.WORD_INCORRECT_FIRST_PLAY);

		newWord = new Word(Board.BOARD_SIZE,Board.BOARD_SIZE+1,true,"HELLO");
		doTest(6, newWord, false, Board.WORD_INCORRECT_FIRST_PLAY);

		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE,true,"HELLO");
		doTest(7, newWord, true, 0);

		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE,true,"HELLO");
		doTest(8, newWord, true, 0);

		board.place(frame, newWord);
		frame.setTiles("HELLO");

		newWord = new Word(-1,Board.BOARD_SIZE,true,"HELLO");
		doTest(9, newWord, false, Board.WORD_OUT_OF_BOUNDS);

		newWord = new Word(Board.BOARD_SIZE,12,true,"HELLO");
		doTest(10, newWord, false,Board.WORD_OUT_OF_BOUNDS);

		newWord = new Word(Board.BOARD_SIZE,-1,false,"HELLO");
		doTest(11, newWord, false, Board.WORD_OUT_OF_BOUNDS);

		newWord = new Word(12,Board.BOARD_SIZE,false,"HELLO");
		doTest(12, newWord, false, Board.WORD_OUT_OF_BOUNDS);

		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE,false,"HELLO");
		doTest(13, newWord, true, 0);
		
		// Test not in frame
		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE,false,"HALLO");
		doTest(14, newWord, false, Board.WORD_LETTER_NOT_IN_FRAME);
		
		// Test clash
		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE,false,"XELLO");
		doTest(15, newWord, false, Board.WORD_LETTER_CLASH);
		
		// Test frame not used
		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE,true,"HE");
		doTest(16, newWord, false, Board.WORD_NO_LETTER_PLACED);
		
		// Test no connection to existing words
		System.out.println("CONNECTION TESTS FOLLOW");
		board.display();

		newWord = new Word(Board.BOARD_CENTRE-2,Board.BOARD_CENTRE,true,"HELLO");
		doTest(17, newWord, false, Board.WORD_NO_CONNECTION);

		newWord = new Word(Board.BOARD_CENTRE+2,Board.BOARD_CENTRE,true,"HELLO");
		doTest(18, newWord, false, Board.WORD_NO_CONNECTION);

		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE-6,true,"HELLO");
		doTest(19, newWord, false, Board.WORD_NO_CONNECTION);

		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE-5,true,"HELLO");
		doTest(20, newWord, true, 0);

		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE+6,true,"H");
		doTest(21, newWord, false, Board.WORD_NO_CONNECTION);

		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE+5,true,"H");
		doTest(22, newWord, true, 0);

		newWord = new Word(Board.BOARD_CENTRE-6,Board.BOARD_CENTRE,false,"HELLO");
		doTest(23, newWord, false, Board.WORD_NO_CONNECTION);

		newWord = new Word(Board.BOARD_CENTRE-5,Board.BOARD_CENTRE,false,"HELLO");
		doTest(24, newWord, true, 0);

		newWord = new Word(Board.BOARD_CENTRE+1,Board.BOARD_CENTRE,false,"HELLO");
		doTest(25, newWord, true, 0);

		newWord = new Word(Board.BOARD_CENTRE+2,Board.BOARD_CENTRE,false,"HELLO");
		doTest(26, newWord, false, Board.WORD_NO_CONNECTION);
		
		
		// Test setWord & display
		System.out.println("HELLO");
		board.display();
		newWord = new Word(Board.BOARD_CENTRE,Board.BOARD_CENTRE,false,"HELLO");
		board.place(frame,newWord);
		System.out.println("HELLOx2");
		board.display();

		System.out.println("Test Complete");
	}

	 public static void doTest(int number, Word word, boolean expectedIsLegal, int expectedCode) {
		 boolean isLegal = board.isLegal(frame, word);
		 System.out.printf("%d: isLegal %b : expected isLegal %b", number, isLegal, expectedIsLegal);
		 if (isLegal != expectedIsLegal) {
			 System.out.println(" ERROR ");
		 } else {
			 System.out.println();
		 }
		 if (!isLegal) {
			 int checkCode = board.getCheckCode();
			 System.out.printf("%d: checkCode %d : expected checkCode %d", number, checkCode, expectedCode);
			 if (checkCode != expectedCode) {
				 System.out.println(" ERROR ");
			 } else {
				 System.out.println();
			 }
		 }
	 }
}