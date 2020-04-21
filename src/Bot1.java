import java.util.ArrayList;
import java.util.HashSet;

public class Bot1 implements BotAPI {
    //Other-Scrabbled-Eggs
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
//        else if (!me.getFrameAsString().contains("_")&&!board.isFirstPlay()) {
//            command = "X "+ me.getFrameAsString().replaceAll("[^A-Z_]", "");
//        }
        else if (board.isFirstPlay()) {
            command = makeFirstWord(me.getFrameAsString());
        }else if(shouldChallenge()){
            command = "Challenge";
        }
        else {
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
            command = Character.toString((char) (bestWord.getFirstColumn()+'A')) + Integer.toString(bestWord.getFirstRow()+1);
            command += bestWord.isHorizontal() ? " A ":" D ";
            command += bestWord.toString(); //creates command for the best word
            command += blanks;
        }
        return command;
    }

    public String makeWord(String myFrame)
    {
        myFrame = myFrame.replaceAll("[^A-Z_]", "");//turning frame into string of 7 letters
        String command = "pass";//preparing a pass command if we find no words
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
            generateGaddags(i.row, i.col, gaddags); //finds shapes for words where ? represents empty squares
        }
        Frame frame = new Frame();
        ArrayList<Tile> alt = new ArrayList<>();
        for(int i=0; i<me.getFrameAsString().replaceAll("[^A-Z_]", "").length();i++)
        {
            alt.add(new Tile(me.getFrameAsString().replaceAll("[^A-Z_]", "").charAt(i)));
        }
        frame.addTiles(alt);
        for(GADDAG g : gaddags)
        {
            int sr=g.start.row, sc=g.start.col;
            if(g.isHorizontal) {sc-=g.prefix.length();}
            else{sr-=g.prefix.length();}
            HashSet<String> GADDAGcombos = new HashSet<>();
            getGADDAGFrameCombinations(g, combinations,GADDAGcombos);
            for(String s : GADDAGcombos)
            {
                if(s.contains("_"))
                {
                    for(int i=0; i<26; i++)
                    {
                        if(s.length()-s.replaceAll("_","").length()==2) {
                            for (int j=0; j<26;j++) {
                                Word temp = new Word(sr, sc, g.isHorizontal, s, Character.toString((char) ((char) i+'A'))+Character.toString((char) ((char) j+'A')));
                                ArrayList<Word> tempwords = new ArrayList<>();
                                tempwords.add(temp);
                                if(board.isLegalPlay(frame, temp)) {
                                    if (dictionary.areWords(tempwords)) {
                                        tempwords.remove(temp);
                                        for (Word w : getAllWords(temp)) {
                                            tempwords.add(w);
                                        }
                                        if (dictionary.areWords(tempwords)) {
                                            int score = 0;
                                            score = getAllPoints(tempwords, g);
                                            if (score > maxScore) {
                                                bestWord = temp;
                                                maxScore = score;
                                                blanks = " "+Character.toString((char) ((char) i + 'A')) + Character.toString((char) ((char) j + 'A'));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Word temp = new Word(sr, sc, g.isHorizontal, s, Character.toString((char) ((char) i+'A')));
                            ArrayList<Word> tempwords = new ArrayList<>();
                            tempwords.add(temp);
                            if(board.isLegalPlay(frame, temp)) {
                                if (dictionary.areWords(tempwords)) {
                                    tempwords.remove(temp);
                                    for (Word w : getAllWords(temp)) {
                                        tempwords.add(w);
                                    }
                                    if (dictionary.areWords(tempwords)) {
                                        int score = 0;
                                        score = getAllPoints(tempwords, g);
                                        if (score > maxScore) {
                                            bestWord = temp;
                                            maxScore = score;
                                            blanks = " "+Character.toString((char) ((char) i + 'A'));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    Word temp = new Word(sr, sc, g.isHorizontal, s);
                    ArrayList<Word> tempwords = new ArrayList<>();
                    tempwords.add(temp);
                    if(board.isLegalPlay(frame, temp)) {
                        if (dictionary.areWords(tempwords)) {
                            tempwords.remove(temp);
                            for (Word w : getAllWords(temp)) {
                                tempwords.add(w);
                            }
                            if (dictionary.areWords(tempwords)) {
                                int score = 0;
                                score = getAllPoints(tempwords, g);
                                if (score > maxScore) {
                                    bestWord = temp;
                                    maxScore = score;
                                    blanks = "";
                                }
                            }
                        }
                    }
                }
            }
        }
        if(maxScore != 0)
        {
            command = Character.toString((char) (bestWord.getFirstColumn()+'A')) + Integer.toString(bestWord.getFirstRow()+1);
            command += bestWord.isHorizontal() ? " A ":" D ";
            command += bestWord.toString(); //creates command for the best word
            command += blanks;
        }
        //else if(pool.size>6){
        // decide exchange(pool.size, frame)
        // }
        System.out.println(command);
        return command;
    }

    public ArrayList<Word> getAllWords(Word mainWord) {
        ArrayList<Word> words = new ArrayList<>();
        words.add(mainWord);
        int r = mainWord.getFirstRow();
        int c = mainWord.getFirstColumn();
        for (int i=0; i<mainWord.length(); i++) {
            if (!board.getSquareCopy(r,c).isOccupied()) {
                if (isAdditionalWord(r, c, mainWord.isHorizontal())) {
                    words.add(getAdditionalWord(r, c, mainWord.isHorizontal(), mainWord.getDesignatedLetter(i)));
                }
            }
            if (mainWord.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }
        return words;
    }

    private boolean isAdditionalWord(int r, int c, boolean isHorizontal) {
        if ((isHorizontal &&
                ((r>0 && board.getSquareCopy(r-1,c).isOccupied()) || (r< Board.BOARD_SIZE-1 && board.getSquareCopy(r+1,c).isOccupied()))) ||
                (!isHorizontal &&
                        ((c>0 && board.getSquareCopy(r,c-1).isOccupied()) || (c< Board.BOARD_SIZE-1 && board.getSquareCopy(r,c+1).isOccupied()))) ) {
            return true;
        }
        return false;
    }

    private Word getAdditionalWord(int mainWordRow, int mainWordCol, boolean mainWordIsHorizontal, char letter) {
        int firstRow = mainWordRow;
        int firstCol = mainWordCol;
        // search up or left for the first letter
        while (firstRow >= 0 && firstCol >= 0 && (board.getSquareCopy(firstRow,firstCol).isOccupied()||(firstRow==mainWordRow&&firstCol==mainWordCol))) {
            if (mainWordIsHorizontal) {
                firstRow--;
            } else {
                firstCol--;
            }
        }
        // went too far
        if (mainWordIsHorizontal) {
            firstRow++;
        } else {
            firstCol++;
        }
        // collect the letters by moving down or right
        String letters = "";
        int r = firstRow;
        int c = firstCol;
        while (r< Board.BOARD_SIZE && c< Board.BOARD_SIZE && (board.getSquareCopy(r,c).isOccupied()||(r==mainWordRow&&c==mainWordCol))) {
            if(r==mainWordRow&&c==mainWordCol)
            {
                letters = letters + letter;
            }
            else{
                letters = letters + board.getSquareCopy(r,c).getTile().getLetter();
            }
            if (mainWordIsHorizontal) {
                r++;
            } else {
                c++;
            }
        }
        return new Word (firstRow, firstCol, !mainWordIsHorizontal, letters);
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
            for(int j=al.get(0).length(); j>1; j--)
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
                    s = s.substring(0, i) + Character.toString((char) ('A' + j)) + s.substring(i + 1);
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
        if(!s.isOccupied())
        {
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

    public void generateGaddags(int r, int c, HashSet<GADDAG> hs){
        GADDAG acrossMaster = new GADDAG(board, r, c, true);
        GADDAG downMaster = new GADDAG(board, r, c, false);
        ArrayList<String> suffixes = new ArrayList<>();
        ArrayList<String> prefixes = new ArrayList<>();
        String suf = ""+acrossMaster.suffix.charAt(0);
        int i=0;
        while(suf.length()-suf.replaceAll("[?]", "").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()&&acrossMaster.start.col+i<14)
        {
            if(acrossMaster.suffix.charAt(i+1)=='?'&&suf.length()>1)
            {
                suffixes.add(suf);
            }
            i++;
            suf+=acrossMaster.suffix.charAt(i);
        }
        suffixes.add(suf);
        i=0;
        String pre=acrossMaster.prefix.length()==0 ? "" : ""+acrossMaster.prefix.charAt(0);
        while(pre.length()-pre.replaceAll("[?]", "").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()&&acrossMaster.start.col-i-1>0)
        {
            if(acrossMaster.prefix.charAt(i+1)=='?')
            {
                prefixes.add(pre);
            }
            i++;
            pre+=acrossMaster.prefix.charAt(i);
        }
        prefixes.add(pre);
        for(String s : suffixes){
            if(acrossMaster.prefix.length()!=0) {
                if(acrossMaster.prefix.charAt(0)=='?') {
                    hs.add(new GADDAG("", s, acrossMaster.start.row, acrossMaster.start.col, true));
                }
            }
            for(String p : prefixes)
            {
                if ((s+p).length()-(s+p).replaceAll("[?]","").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()) {
                    hs.add(new GADDAG(p, s, acrossMaster.start.row, acrossMaster.start.col, true));
                }
            }
        }
        for(String p : prefixes)
        {
            hs.add(new GADDAG(p, ""+acrossMaster.suffix.charAt(0), acrossMaster.start.row, acrossMaster.start.col, true));
        }

        //repeated process for down
        suffixes = new ArrayList<>();
        prefixes = new ArrayList<>();
        suf = ""+downMaster.suffix.charAt(0);
        i=0;
        while(suf.length()-suf.replaceAll("[?]", "").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()&&downMaster.start.row+i<14)
        {
            if(downMaster.suffix.charAt(i+1)=='?'&&suf.length()>1)
            {
                suffixes.add(suf);
            }
            i++;
            suf+=downMaster.suffix.charAt(i);
        }
        suffixes.add(suf);
        i=0;
        pre=downMaster.prefix.length()==0 ? "" : ""+downMaster.prefix.charAt(0);
        while(pre.length()-pre.replaceAll("[?]", "").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()&&downMaster.start.row-i-1>0)
        {
            if(downMaster.prefix.charAt(i+1)=='?')
            {
                prefixes.add(pre);
            }
            i++;
            pre+=downMaster.prefix.charAt(i);
        }
        prefixes.add(pre);
        for(String s : suffixes){
            if(downMaster.prefix.length()!=0) {
                if(downMaster.prefix.charAt(0)=='?') {
                    hs.add(new GADDAG("", s, downMaster.start.row, downMaster.start.col, false));
                }
            }
            for(String p : prefixes)
            {
                if ((s+p).length()-(s+p).replaceAll("[?]","").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()) {
                    hs.add(new GADDAG(p, s, downMaster.start.row, downMaster.start.col, false));
                }
            }
        }
        for(String p : prefixes)
        {
            hs.add(new GADDAG(p, ""+downMaster.suffix.charAt(0), downMaster.start.row, downMaster.start.col, false));
        }
    }

    public void getGADDAGFrameCombinations(GADDAG g, ArrayList<String> combinations, HashSet<String> GADDAGcombos){
        for(String s : combinations)
        {
            String c = g.toString();
            if(s.length()==c.length()-c.replaceAll("[?]","").length())
            {
                int j=0;
                for (int i=0;i<c.length();i++)
                {
                    if(c.charAt(i)=='?')
                    {
                        c=c.substring(0, i)+s.charAt(j)+c.substring(i+1);
                        j++;
                    }
                }
                GADDAGcombos.add(c);
            }
        }
    }

    private int getWordPoints(Word word) {
        int wordValue = 0;
        int wordMultipler = 1;
        int r = word.getFirstRow();
        int c = word.getFirstColumn();
        for (int i = 0; i < word.length(); i++) {
            int letterValue = board.getSquareCopy(r, c).isOccupied()? board.getSquareCopy(r, c).getTile().getValue() : new Tile(word.getLetters().charAt(i)).getValue();
            if (!board.getSquareCopy(r, c).isOccupied()) {
                wordValue = wordValue + letterValue * board.getSquareCopy(r, c).getLetterMuliplier();
                wordMultipler = wordMultipler * board.getSquareCopy(r, c).getWordMultiplier();
            } else {
                wordValue = wordValue + letterValue;
            }
            if (word.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }
        return wordValue * wordMultipler;
    }

    public int getAllPoints(ArrayList<Word> words, GADDAG g) {
        int points = 0;
        for (Word word : words) {
            points = points + getWordPoints(word);
        }
        if (g.toString().length()-g.toString().replaceAll("[?]","").length() == Frame.MAX_TILES) {
            points = points + 50;
        }
        return points;
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
            this.prefix = pre;
            this.suffix = suf;
            this.start = new IntPair(r,c);
            this.isHorizontal = b;
        }
        GADDAG(BoardAPI board, int row, int col, boolean isHorizontal){
            //produces a gaddag starting at a tile on that line where ? represents empty squares - max seven ?'s in suffix or prefix
            this.prefix = "";
            this.suffix = "";
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
                        if(suffix.length()-suffix.replace("?", "").length()>=me.getFrameAsString().replaceAll("[^A-Z_]", "").length())
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
                        if(prefix.length()-prefix.replace("?", "").length()>=me.getFrameAsString().replaceAll("[^A-Z_]", "").length())
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
                        if(suffix.length()-suffix.replace("?", "").length()>=me.getFrameAsString().replaceAll("[^A-Z_]", "").length())
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
                        if(prefix.length()-prefix.replace("?", "").length()>=me.getFrameAsString().replaceAll("[^A-Z_]", "").length())
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

    private class IntPair {
        public int row;
        public int col;
        IntPair(int r, int c){
            row = r;
            col = c;
        }
    }


//    private boolean shouldChallenge() {
////        String playerMove = info.getLatestInfo();//getLatestInfo doesn't seem to return anything
//        String playerMove = info.getAllInfo();
//        String[] playerMoveSplit = new String[0];
//
//        playerMoveSplit = playerMove.split(" ");
//
//        Word word = new Word(row, col, isHori, word);
//        ArrayList<Word> words = new ArrayList<>();
//        words.add(word);

//
//        //checking the last turn was correct - if not, challenge it
//        if (!(getDictionary().areWords(word))) {
//            String command = "C";
//
//        }
//        return dictionary.areWords(arraylist);
//    }

    // (int) ‘H’ - ‘A’

    boolean shouldChallenge() {
        //trim.getAllInfo();
        trimInfo();//shouldn't have to pass in the variables as they're stored globally
        System.out.println(latestInfo);//checking what has actually been returned from that function
        if (latestInfo.contains("[0 - 9]"))
        {//possibly change this check to latestInfo rather than get all info which we don't change inplace
            return false;
        }
        String playerMove = latestInfo;
        //assumes latestInfo returns just the last played move
        String[] move = playerMove.split(" ");
        String coord = move[0];
        String [] splitCoOrd = coord.split("");
        int coordCol = Integer.parseInt(splitCoOrd[1]);
        boolean isHor;
        if(move[1] == "True"){
            isHor = true;
        }else{
            isHor = false;
        }
        Word word = new Word((int) splitCoOrd[0].toCharArray()[0] - 'A', coordCol, isHor, move[2]);
        //need to parse this from whats returned from trimInfo
        //H into an int and 7 into an int and A into the isHorizontal boolean
        ArrayList<Word> words = new ArrayList<>();
        words.add(word);
        // get words (not usable yet)
        // add(gotten words)
        return dictionary.areWords(words);
    }

    public String allInfo = info.getAllInfo(); //and update it to be getAllInfo() every turn
    public String lastAllInfo; // info from the last turn
    //then parse from that to find the last word that was played
    String latestInfo;
    // public String latestInfo = allInfo - lastAllInfo;// the info of what was played in the turn immediately preceeding this one
    //this is a string so will need to be trimmed down differently than just taking away what was there from the last turn

    private void trimInfo() {
        String move = null; //is this latest info?
        latestInfo = allInfo;
        lastAllInfo = allInfo.substring(lastAllInfo.length());
        int i = -1;
        while (i < latestInfo.length()) {
            if (latestInfo.charAt(i) == '\n')  //.matches("^[A-O][1-9] [AD] [A-Z_]{2,}$|^[A-O]1[0-5] [AD] [A-Z_]{2,}$")) {
                move = String.valueOf(latestInfo.charAt(i));
        }
        i += move.length();
    }


    private String getLine(int i, String latestInfo) {
        String s = "";
        while (latestInfo.charAt(i) != '\n') {
            s += latestInfo.charAt(i);
            i++;
        }
        return s;
    }
}
