import java.util.ArrayList;

public class PlayerTest {
    public static void main(String args[]) {


        //pool tests
        Pool myPoolTest = new Pool();
        int i = 0;
        for (i = 0; i < 27; i++) {
            System.out.print(myPoolTest.findValue(i) + " ");
        }    // checking we can find each value correctly
        System.out.println();
        if (!myPoolTest.isEmpty()) {
            System.out.println("Pool is not empty");
        }    // checking isEmpty() returns false when pool has tiles remaining
        for (i = 0; i < 100; i++) {
            System.out.println("Your Tile: " + myPoolTest.randomTile());
            System.out.println("Tiles Remaining: " + myPoolTest.tilesRemaining());
        }    // removing one tile at a time, printing the selected tile and the number remaining at that point.
        // this checks actually random tiles are removed and all tiles are removed eventually.
        if (myPoolTest.isEmpty()) {
            System.out.println("Pool is now empty!");
        }    // checking isEmpty() returns true when pool no longer has tiles
        myPoolTest.poolReset();        // checking poolReset() runs
        System.out.println("Pool has been reset!");
        if (!myPoolTest.isEmpty()) {
            System.out.println("Pool is not empty");
        }    // checking poolReset() refills the pool with tiles
        System.out.println("Tiles Remaining: " + myPoolTest.tilesRemaining());    // checking it refills the pool with the correct amount of tiles
        for (i = 0; i < 50; i++) {
            myPoolTest.randomTile();
        }
        System.out.println("Tiles Remaining: " + myPoolTest.tilesRemaining());    // just checking tiles are removed and still counted correctly after poolReset()
        for (i = 50; i < 100; i++) {
            myPoolTest.randomTile();
        }
        System.out.println("Tiles Remaining: " + myPoolTest.tilesRemaining());
        if (myPoolTest.isEmpty()) {
            System.out.println("Pool is now empty!");
        }    // checking isEmpty() still works after poolReset()
        //test for the so far unused numToLetter() function
        for (i = 0; i < 27; i++) {
            System.out.print(myPoolTest.numToLetter(i));
        }
        System.out.println();
        char c = 42;
        System.out.print(myPoolTest.letterToNum(c) + " ");
        for (c = 65; c < 91; c++) {
            System.out.print(myPoolTest.letterToNum(c) + " ");
        }
        //Frame myFrameTest = new Frame();
        myPoolTest.poolReset();
        //myFrameTest.fillFrame(myPoolTest);



        //Frame tests
    Frame myFrame = new Frame();
    //myFrame.fillFrame(myPoolTest);
        myFrame.getLetters().

    add('A');

        System.out.println(myFrame.toString());

        myFrame.removeLetter('A');      //this removes specific letter only if it is present in the frame
        System.out.println(myFrame.toString());


    ArrayList<Character> chars = new ArrayList<>();
        chars.add('S');
        chars.add('T');
        chars.add('U');

        myFrame.getLetters().

    addAll(chars);
        System.out.println(myFrame.toString());

        System.out.println("does frame contain S? "+myFrame.checkLetter('S'));
    //test to check if a letter is present- should be true

        System.out.println("does frame contain S,T,U?"+myFrame.checkLetters(chars));
    //test to check if multiple letters are present- should be true


        System.out.println("does frame contain V? "+myFrame.checkLetter('V'));
    //test to check if a letter is present- should be false

        System.out.println("frame is empty? "+myFrame.checkFrameEmpty());
    //test checking if frame is empty-should be false

        myFrame.removeLetters(chars);   //test to remove multiple letters
        System.out.println(myFrame.toString());

        System.out.println("frame is empty? "+myFrame.checkFrameEmpty());
    //second test checking if frame is empty-should be true




        myFrame.fillFrame(myPoolTest);  //testing frame can be filled from pool
        System.out.println(myFrame.toString());

}
}