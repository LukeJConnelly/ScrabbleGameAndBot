import java.util.ArrayList;
import java.util.HashSet;

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
            command = makeWord(me.getFrameAsString());
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

    public String makeWord(String myFrame)
    {
        myFrame = myFrame.replaceAll("[^A-Z_]", "");//turning frame into string of 7 letters
        String command = "X "+myFrame;//preparing an exchange command if we find no words
        Word bestWord = new Word(0,0,false,"neverever");//is never used, just a placeholder
        int maxScore=0;
        String blanks="";
        ArrayList<String> combinations = getCombinations(myFrame);    //find every combination of the letters
        HashSet<IntPair> hooks = new HashSet<IntPair> ();
        for (int r=0; r<15; r++)  {
            for (int c=0; c<15; c++)   {
                if (isHook(r, c))
                {
                    hooks.add(new IntPair(r,c));
                }
            }
        }
        HashSet<GADDAG> gaddags = new HashSet<GADDAG>();
        for(IntPair i : hooks)
        {
            generateGaddags(i.row, i.col, gaddags); //currently only finds across
        }
        for(GADDAG g : gaddags)
        {
            //permute with frame
            //check is word (and check peripherals)
            //score words (and check peripherals)
            //check if is Best
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
        permute(s, 0, s.length()-1, allCombinations);
        addShortened(allCombinations);
        return allCombinations;
    }

    private static void permute(String str, int l, int r, ArrayList<String> al)
    {
        if (l == r)
            al.add(str);
        else
        {
            for (int i = l; i <= r; i++)
            {
                str = swap(str,l,i);
                permute(str, l+1, r, al);
                str = swap(str,l,i);
            }
        }
    }

    public static String swap(String a, int i, int j)
    {
        char temp;
        char[] charArray = a.toCharArray();
        temp = charArray[i] ;
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    public static void addShortened(ArrayList<String> al){
        int size = al.size();
        HashSet<String> h = new HashSet<>();
        for(int i=0; i<size; i++)
        {
            for(int j=al.get(0).length(); j>2; j--)
            {
                h.add(al.get(i).substring(0, j - 1));
            }
        }
        for(String s : h)
        {
            al.add(s);
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
            wordValue = wordValue + letterValue * board.getSquareCopy(r, c).getLetterMuliplier();
            wordMultipler = wordMultipler * board.getSquareCopy(r, c).getWordMultiplier();
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


    public boolean isHook(int r, int c){
        Square s = board.getSquareCopy(r,c);
        boolean isHook=false;
        if(s.isOccupied())
        {
            //check if surrounded
            if(r>=1){
                   if(!board.getSquareCopy(r-1,c).isOccupied())
                   {
                       isHook=true;
                   }
            }
            if(c>=1){
                if(!board.getSquareCopy(r,c-1).isOccupied())
                {
                    isHook=true;
                }
            }
            if(r<=13){
                if(!board.getSquareCopy(r+1,c).isOccupied())
                {
                    isHook=true;
                }
            }
            if(c<=13){
                if(!board.getSquareCopy(r,c+1).isOccupied())
                {
                    isHook=true;
                }
            }
        }
        else{
            //check if has next door tile
            if(r>=1){
                if(board.getSquareCopy(r-1,c).isOccupied())
                {
                    isHook=true;
                }
            }
            if(c>=1){
                if(board.getSquareCopy(r,c-1).isOccupied())
                {
                    isHook=true;
                }
            }
            if(r<=13){
                if(board.getSquareCopy(r+1,c).isOccupied())
                {
                    isHook=true;
                }
            }
            if(c<=13){
                if(board.getSquareCopy(r,c+1).isOccupied())
                {
                    isHook=true;
                }
            }
        }
        return isHook;
    }

    public void generateGaddags(int r, int c, HashSet<GADDAG> al){
        GADDAG acrossMaster = new GADDAG(board, r, c, true);
        GADDAG downMaster = new GADDAG(board, r, c, false);

        //across gaddags
        int numEmptyPrefix = acrossMaster.prefix.length() - acrossMaster.prefix.replace("?","").length();
        int numEmptySuffix = acrossMaster.suffix.length() - acrossMaster.suffix.replace("?","").length();
        //generate just suffix'
        for(int i = numEmptySuffix; i>0; i--)
        {
            String pre="", suf="";
            int j=0;
            while(suf.length()-suf.replace("?", "").length()<numEmptySuffix)
            {
                suf+=acrossMaster.suffix.charAt(j);
            }
            if(!(suf.length()==1))
            {
                al.add(new GADDAG(pre, suf, acrossMaster.start.row, acrossMaster.start.col, true));
            }
        }
        //generate just prefix
        for(int i = numEmptyPrefix; i>0; i--)
        {
            String pre="", suf=board.getSquareCopy(acrossMaster.start.row, acrossMaster.start.col).isOccupied() ? ""+ board.getSquareCopy(acrossMaster.start.row, acrossMaster.start.col).getTile().getLetter() : "?";
            int j=0;
            while(pre.length()-pre.replace("?", "").length()<numEmptyPrefix)
            {
                pre+=acrossMaster.prefix.charAt(acrossMaster.prefix.length()-1-j);
            }
            al.add(new GADDAG(pre, suf, acrossMaster.start.row, acrossMaster.start.col, true));
        }
        //generate a mix of both
        for(int i = numEmptySuffix; i>=0; i--)
        {
            String pre="", suf="";
            int k=0;
            while(suf.length()-suf.replace("?", "").length()<i)
            {
                suf+=acrossMaster.suffix.charAt(k);
                k++;
            }
            for (int j = i; j<numEmptyPrefix; j++)
            {
                int l=0;
                while(pre.length()-pre.replace("?", "").length()<j-i)
                {
                    pre+=acrossMaster.prefix.charAt(acrossMaster.prefix.length()-1-l);
                    l++;
                }
            }
            al.add(new GADDAG(pre, suf, acrossMaster.start.row, acrossMaster.start.col, true));
        }
    }

    //might be an idea to write a method that determines the best thing to exchange
    //for example AEILNRST are considered to be the most useful letters
    //and q's and z's will score lots of points

    private class GADDAG {
        public String prefix;
        public String suffix;
        public IntPair start;
        public boolean isHorizontal;
        GADDAG(String pre, String suf, int r, int c, boolean b){
            this.prefix = reverse(pre);
            this.suffix = suf;
            this.start = new IntPair(r,c);
            this.isHorizontal = b;
        }
        GADDAG(BoardAPI board, int row, int col, boolean isHorizontal){
            //produces a gaddag starting at a tile on that line where ? represents empty squares - max seven ?'s in suffix or prefix
            this.prefix = "";
            this.suffix = board.getSquareCopy(row, col).isOccupied() ? ""+board.getSquareCopy(row, col).getTile().getLetter():"?";
            this.start = new IntPair(row,col);
            this.isHorizontal = isHorizontal;
            int ctemp=col, rtemp=row;
            if(isHorizontal){
                while (ctemp<15) {
                    Square temp = board.getSquareCopy(row, ctemp);
                    if(temp.isOccupied()){
                        suffix=suffix+temp.getTile().getLetter();
                    }
                    else{
                        suffix+="?";
                        if(suffix.length()-suffix.replace("?", "").length()>me.getFrameAsString().replaceAll("[^A-Z_]", "").length())
                        {
                            break;
                        }
                    }
                    ctemp++;
                }
                ctemp=col-1;
                while (ctemp>-1) {
                    Square temp = board.getSquareCopy(row, ctemp);
                    if(temp.isOccupied()){
                        prefix=prefix+temp.getTile().getLetter();
                    }
                    else{
                        prefix+="?";
                        if(prefix.length()-prefix.replace("?", "").length()>me.getFrameAsString().replaceAll("[^A-Z_]", "").length())
                        {
                            break;
                        }
                    }
                    ctemp--;
                }
            }
            else
            {
                while (rtemp<15) {
                    Square temp = board.getSquareCopy(rtemp, col);
                    if(temp.isOccupied()){
                        suffix=suffix+temp.getTile().getLetter();
                    }
                    else{
                        suffix+="?";
                        if(suffix.length()-suffix.replace("?", "").length()>7)
                        {
                            break;
                        }
                    }
                    rtemp++;
                }
                rtemp=row-1;
                while (rtemp>-1) {
                    Square temp = board.getSquareCopy(rtemp, col);
                    if(temp.isOccupied()){
                        prefix=prefix+temp.getTile().getLetter();
                    }
                    else{
                        prefix+="?";
                        if(prefix.length()-prefix.replace("?", "").length()>7)
                        {
                            break;
                        }
                    }
                    rtemp--;
                }
            }
        }

        public String reverse(String s) {
            String c = "";
            for(int i=s.length()-1; i>=0; i--)
            {
                c+=s.charAt(i);
            }
            return c;
        }

        public String toString(){
            return reverse(prefix)+suffix;
        }
    }

    public class IntPair {
        public int row;
        public int col;
        IntPair(int r, int c){
            row = r;
            col = c;
        }
    }
}
