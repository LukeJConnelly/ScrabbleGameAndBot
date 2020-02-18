import java.util.concurrent.ThreadLocalRandom;

public class Pool {
    private static final int tileValues[] = {0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    /* values for each tile:
    (0 points)- Blank Tiles
    (1 point)- A, E, I, O, U, L, N, S, T, R.
    (2 points)- D, G.
    (3 points)- B, C, M, P.
    (4 points)- F, H, V, W, Y.
    (5 points)- K.
    (8 points)- J, X.
    (10 points)- Q, Z.*/
    private static final int defaultTileAmounts[] = {2, 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
    // this will be used to restore the default pool in the event poolReset() is called
	/* amounts of each tile:
	Blanks-2, A-9, B-2, C-2, D-4, E-12, F-2, G-3, H-2, I-9, J-1, K-1, L-4, M-2,
	N-6, O-8, P-2, Q-1, R-6, S-4, T-6, U-4, V-2, W-2, X-1, Y-2, Z-1*/
    private int[] tileAmounts = Pool.defaultTileAmounts.clone();

    // this is what the game will use
    public void poolReset() {
        this.tileAmounts = Pool.defaultTileAmounts.clone();
    }

    public int tilesRemaining() {
        int sum = 0, i = 0;
        for (i = 0; i < 27; i++) {
            sum += this.tileAmounts[i];
            // total each array value from tileAmounts[] to find the total amount of tiles still in the pool.
        }
        return sum;
    }

    public boolean isEmpty() {
        return this.tilesRemaining() == 0;
    }

//
//    public char randomTile() {
//        int rand = (int) Math.round((Math.random() * 25));
//        while (this.findValue(rand) == 0) // continue choosing a random index until we arrive at an index which is non-empty
//            rand = (int) Math.round((Math.random() * 25));
//        this.tileAmounts[rand]--; // decrement the count of the tile we have chosen
//        return (char) ('A' + (char) rand); // return the tile we've selected
//    }



    public char randomTile()
    {
        int rand = ThreadLocalRandom.current().nextInt(0, this.tilesRemaining());		// random number between 0 and number of tiles remaining
        int i=0, sum=0;
        for (i=0;i<27;i++)
        {
            sum+=this.tileAmounts[i];
            if(sum>rand)							// we then go the random number's value into the array, and take that tile
            {
                break;
            }
        }
        this.tileAmounts[i]--;				// decrement the amount of the tile we've selected from the array.
        return numToLetter(i);							// return the tile we've selected
    }



    public void addTile(int i) {
        this.tileAmounts[i]++;                // increment the amount of the tile we've been passed within the array.
    }

    public int findValue(int n) {
        return Pool.tileValues[n];            // could be altered to take in a char if wanted elsewhere in the program
    }

    //	This is a method which isn't asked for, but could be needed in future for placing tiles on the board.
    public char numToLetter(int n) {
        char v = 42;        // Blank is represented by ASCII char 42, '*'
        if (n != 0) {
            v += 22 + n;    // We then add 22, and the position of the letter we want in the array of letters
        }
        return v;
    }

    public int letterToNum(char c) {
        char d = 65;        // A in ASCII, the first letter barring the * denoting a blank
        int n = 0;
        if (!(c == 42))    // If it's blank, we want to return 0, so skip the following loop and return n=0
        {
            while (true)    // we can now loop until we reach the wanted character (assumes input is correct)
            {
                n++;            // increment the number we will return
                if (c == d)        // break if the passed character matches d
                {
                    break;
                }
                d++;            // increment d
            }
        }
        return n;
    }
}



