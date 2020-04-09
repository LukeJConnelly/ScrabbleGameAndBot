import java.util.ArrayList;

public class Bot1 implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

    private PlayerAPI me;
    private OpponentAPI opponent;
    private BoardAPI board;
    private UserInterfaceAPI info;
    private DictionaryAPI dictionary;
    private int turnCount = 0;
    private static final int[][] LETTER_MULTIPLIER =
            { 	{1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1},
                    {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
                    {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
                    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                    {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
                    {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
                    {1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} };
    private static final int[][] WORD_MULTIPLIER =
            {   {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3},
                    {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                    {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
                    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                    {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                    {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
                    {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                    {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3} };

    Bot1(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.info = ui;
        this.dictionary = dictionary;
    }

    public String getCommand() {
        // Add your code here to input your commands
        String command = "";
        if (turnCount==0) {
            command = "NAME Bot1";
        }
        //useful for testing blank related code
//        else if (!me.getFrameAsString().contains("_")) {
//            command = "X "+ me.getFrameAsString().replaceAll("[^A-Z_]", "");
//        }
        else if (board.isFirstPlay()) {
            command = makeFirstWord(me.getFrameAsString());
        } else {
            for (int i=0;i<15;i++)
            {
                for (int j=0;j<15;j++)
                {
//                    Square currSquare = board.getSquareCopy(i,j);
//                    if(currSquare.isOccupied())
//                    {
//                        //try to make word from that square
//                    }
//                    else if(hasPeripherals(currSquare,i,j))
//                    {
//                        //find max word next to peripheral squares
//                    }
                }
            }
        }
        turnCount++;
        return command;
    }

    public String makeFirstWord(String myFrame)
    {
        myFrame = myFrame.replaceAll("[^A-Z_]", "");//turning frame into string of 7 letters
        String command = "X "+myFrame;//preparing an exchange command if we find no words
        Word bestWord = new Word(0,0,false,"neverever");//is never used, just a placeholder
        int maxScore=0;
        String blanks="";
        ArrayList<String> combinations = getCombinations(myFrame);    //find every combination of the letters
        ArrayList<Word> found = new ArrayList<Word>();
        for (int i=1; i<8; i++) {
            for (String combination : combinations) {
                if(combination.contains("_"))
                {
                    ArrayList<String> combinationsWithoutBlanks = new ArrayList<String>();
                    addStringsWithoutBlanks(combination, combinationsWithoutBlanks);
                    for (String combinationWithoutBlanks : combinationsWithoutBlanks) {
                        Word word = new Word(7,i,true,combinationWithoutBlanks);
                        found.add(word);
                        if (dictionary.areWords(found))
                        {
                            Word wordWithBlanks = new Word(7,i,true,combination);
                            if((getFirstWordPoints(wordWithBlanks)>maxScore||(getFirstWordPoints(wordWithBlanks)==maxScore&&wordWithBlanks.length()<bestWord.length()))&&i+wordWithBlanks.length()>6)
//if word is a word, beats current best score, reaches double word we have a new best word
                            {
                                bestWord = wordWithBlanks;
                                maxScore = getFirstWordPoints(word);
                                blanks=" ";
                                for (int o=0;o<combination.length();o++)
                                {
                                    if(combination.charAt(o)=='_') {
                                        blanks+=combinationWithoutBlanks.charAt(o);
                                    }
                                }
                            }
                        }
                        found.remove(word);
                    }
                }
                else{
                    Word word = new Word(7,i,true,combination);
                    found.add(word);
                    if (dictionary.areWords(found))
                    {
                        if((getFirstWordPoints(word)>maxScore||(getFirstWordPoints(word)==maxScore&&word.length()<bestWord.length()))&&i+word.length()>6)
//if word is a word, beats current best score, reaches double word  we have a new best word
                        {
                            bestWord = word;
                            maxScore = getFirstWordPoints(word);
                            blanks="";
                        }
                    }
                    found.remove(word);
                }
            }
        }
        if(maxScore != 0)
        {
            command = Character.toString(bestWord.getFirstColumn()+'A') + Integer.toString(bestWord.getFirstRow()+1);
            command += bestWord.isHorizontal() ? " A ":" D ";
            command += bestWord.toString(); //creates command for the best word
            command += blanks;
        }
        return command;
    }

    public boolean hasPeripherals(Square square, int row, int col)
    {
        // add check to see if peripheral connection to square possible
        return true;
    }

    public ArrayList<String> getCombinations(String s)
    {
        ArrayList<String> allCombinations = new ArrayList<String>();
        combo("", s, allCombinations);
        return allCombinations;
    }

    static void combo(String prefix, String s, ArrayList<String> allCombinations)
    {
        int N = s.length();

        permute("", s, allCombinations);

        for (int i = 0 ; i < N ; i++) {
            combo(prefix + s.charAt(i), s.substring(i + 1), allCombinations);
        }
    }

    static void permute(String prefix, String s, ArrayList<String> allCombinations) {
        int N = s.length();

        if (N == 0 && prefix.length() > 1)
            allCombinations.add(prefix);

        for (int i = 0; i < N; i++) {
            permute(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, N), allCombinations);
            combo(prefix + s.charAt(i), s.substring(0, i) + s.substring(i+1, N), allCombinations);
        }
    }

    private int getFirstWordPoints(Word word) {
        int wordValue = 0;
        int wordMultipler = 1;
        int r = word.getFirstRow();
        int c = word.getFirstColumn();
        for (int i = 0; i<word.length(); i++) {
            Tile tile = new Tile(word.getLetter(i));
            int letterValue = tile.getValue();
            //error in getSquareCopy, cant use this
            //wordValue = wordValue + letterValue * board.getSquareCopy(r, c).getLetterMuliplier();
            //wordMultipler = wordMultipler * board.getSquareCopy(r, c).getWordMultiplier();
            wordValue = wordValue + letterValue * LETTER_MULTIPLIER[r][c];
            wordMultipler = wordMultipler * WORD_MULTIPLIER[r][c];
            if (word.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }
        if(word.length()==7)
        {
            wordValue+=50;
        }
        return wordValue * wordMultipler;
    }

    private void addStringsWithoutBlanks(String s, ArrayList<String> al)
    {
        for(int i=0;i<s.length();i++) {
            if (s.charAt(i) == '_') {
                for (int j = 0; j < 26; j++) {
                    s = s.substring(0, i) + Character.toString('A' + j) + s.substring(i + 1);
                    if (s.substring(i + 1).contains("_")) {
                        addStringsWithoutBlanks(s, al);
                    } else {
                        al.add(s);
                    }
                }
            }
        }
    }

    //might be an idea to write a method that determines the best thing to exchange
    //for example AEILNRST are considered to be the most useful letters
    //and q's and z's will score lots of points
}
