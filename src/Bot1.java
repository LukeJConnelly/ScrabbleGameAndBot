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
//        else if (!me.getFrameAsString().contains("_")&&!board.isFirstPlay()) {
//            command = "X "+ me.getFrameAsString().replaceAll("[^A-Z_]", "");
//        }
        else if (board.isFirstPlay()) {
            command = makeFirstWord(me.getFrameAsString());
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
                                Word temp = new Word(sr, sc, g.isHorizontal, s, Character.toString((char) i+'A')+Character.toString((char) j+'A'));
                                ArrayList<Word> tempwords = new ArrayList<>();
                                tempwords.add(temp);
                                if(board.isLegalPlay(frame, temp)) {
                                    if (dictionary.areWords(tempwords)) {
                                        tempwords.remove(temp);
                                        for (Word w : getAllWordsWithBlanks(temp)) {
                                            tempwords.add(w);
                                        }
                                        if (dictionary.areWords(tempwords)) {
                                            int score = 0;
                                            for (Word w : getAllWords(temp)) {
                                                score += getWordPoints(w);
                                            }
                                            if (score > maxScore) {
                                                bestWord = temp;
                                                maxScore = score;
                                                blanks = " "+Character.toString((char) i + 'A') + Character.toString((char) j + 'A');
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Word temp = new Word(sr, sc, g.isHorizontal, s, Character.toString((char) i+'A'));
                            ArrayList<Word> tempwords = new ArrayList<>();
                            tempwords.add(temp);
                            if(board.isLegalPlay(frame, temp)) {
                                if (dictionary.areWords(tempwords)) {
                                    tempwords.remove(temp);
                                    for (Word w : getAllWordsWithBlanks(temp)) {
                                        tempwords.add(w);
                                    }
                                    if (dictionary.areWords(tempwords)) {
                                        int score = 0;
                                        for (Word w : getAllWords(temp)) {
                                            score += getWordPoints(w);
                                        }
                                        if (score > maxScore) {
                                            bestWord = temp;
                                            maxScore = score;
                                            blanks = " "+Character.toString((char) i + 'A');
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
                                for (Word w : getAllWords(temp)) {
                                    score += getWordPoints(w);
                                }
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
            command = Character.toString(bestWord.getFirstColumn()+'A') + Integer.toString(bestWord.getFirstRow()+1);
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

    public ArrayList<Word> getAllWords(Word word) {
        ArrayList<Word> wordList = new ArrayList<Word>();   //the list of words we find that will be returned
        for(int i=0;i<word.getLetters().length();i++) //for each letter in the word we check above and below or left and right for connecting words
        {
            if(word.isHorizontal()) {
                String s=Character.toString(word.getLetter(i));
                int j=1, newFirstRow=-1;    //new first row used for a peripheral word found
                while(true) {   //we'll use breaks to get out of this
                    if (!(word.getFirstRow() + j > 14 || word.getFirstRow() - j < 0)) {//ensuring we dont throw an arrayindex out of bounds exception
                        if (board.getSquareCopy(word.getFirstRow() + j,word.getFirstColumn() + i).isOccupied() && board.getSquareCopy(word.getFirstRow() - j,word.getFirstColumn() + i).isOccupied()) {
                            //if both above and below the letter are occupied we want to add both the char above and below to the string
                            s = Character.toString(board.getSquareCopy(word.getFirstRow() - j,word.getFirstColumn() + i).getTile().getLetter())
                                    + s +
                                    Character.toString(board.getSquareCopy(word.getFirstRow() + j,word.getFirstColumn() + i).getTile().getLetter());
                            newFirstRow=word.getFirstRow() - j; //we've found one above, so the first row must be changed
                        }
                        else if (board.getSquareCopy(word.getFirstRow() + j,word.getFirstColumn() + i).isOccupied()) {
                            //if below the letter is occupied we want to addthe char below to the string
                            s = s + Character.toString(board.getSquareCopy(word.getFirstRow() + j,word.getFirstColumn() + i).getTile().getLetter());
                            if(newFirstRow==-1)//making sure newFirstRow is updated for words that run below the letter
                            {newFirstRow=word.getFirstRow();}
                        }
                        else if (board.getSquareCopy(word.getFirstRow() - j,word.getFirstColumn() + i).isOccupied()) {
                            //if above the letter is occupied we want to add the char above to the string
                            s = Character.toString(board.getSquareCopy(word.getFirstRow() - j,word.getFirstColumn() + i).getTile().getLetter()) + s;
                            newFirstRow=word.getFirstRow() - j;//update first row
                        }
                        else{
                            //if we find nothing the word is over/never began
                            break;
                        }
                    }
                    else if (!(word.getFirstRow() + j > 14)) {  //the same checks but altered for if the word is on the final row
                        if (board.getSquareCopy(word.getFirstRow() + j,word.getFirstColumn() + i).isOccupied()) {
                            s = s + Character.toString(board.getSquareCopy(word.getFirstRow() + j,word.getFirstColumn() + i).getTile().getLetter());
                            if(newFirstRow==-1)
                            {newFirstRow=word.getFirstRow();}
                        }
                        else{
                            break;
                        }
                    }
                    else if (!(word.getFirstRow() - j < 0)) {   //same checks but altered for if the word is on the first row
                        if (board.getSquareCopy(word.getFirstRow() - j,word.getFirstColumn() + i).isOccupied()) {
                            if (board.getSquareCopy(word.getFirstRow() - j,word.getFirstColumn() + i).isOccupied()) {
                                s = Character.toString(board.getSquareCopy(word.getFirstRow() - j,word.getFirstColumn() + i).getTile().getLetter()) + s;
                                newFirstRow=word.getFirstRow() - j;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    else{
                        break;
                    }
                    j++;
                }
                if(newFirstRow!=-1) //if newFirstRow has been changed - ie. letters were found above or below the letter
                {
                    Word foundWord = new Word(newFirstRow,word.getFirstColumn() + i, false, s);
                    wordList.add(foundWord);    //construct a new word and add it to the list
                }
            }
            else {      //exact same checks performed but flipped for vertical words
                String s=Character.toString(word.getLetter(i));
                int j=1, newFirstCol=-1;
                while(true) {
                    if (!(word.getFirstColumn() + j > 14 || word.getFirstColumn() - j < 0)) {
                        if (board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() + j).isOccupied() && board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() - j).isOccupied()) {
                            s = Character.toString(board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() - j).getTile().getLetter())
                                    + s +
                                    Character.toString(board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() + j).getTile().getLetter());
                            newFirstCol=word.getFirstColumn() - j;
                        }
                        else if (board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() + j).isOccupied()) {
                            s = s + Character.toString(board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() + j).getTile().getLetter());
                            if(newFirstCol==-1)
                            {newFirstCol=word.getFirstColumn();}
                        }
                        else if (board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() - j).isOccupied()) {
                            s = Character.toString(board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() - j).getTile().getLetter()) + s;
                            newFirstCol=word.getFirstColumn() - j;
                        }
                        else{
                            break;
                        }
                    }
                    else if (!(word.getFirstColumn() + j > 14)) {
                        if (board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() + j).isOccupied()) {
                            s = s + Character.toString(board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() + j).getTile().getLetter());
                            if(newFirstCol==-1)
                            {newFirstCol=word.getFirstColumn();}
                        }
                        else{
                            break;
                        }
                    }
                    else if (!(word.getFirstColumn() - j < 0)) {
                        if (board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() - j).isOccupied()) {
                            s = Character.toString(board.getSquareCopy(word.getFirstRow() + i,word.getFirstColumn() - j).getTile().getLetter()) + s;
                            newFirstCol=word.getFirstColumn() - j;
                        }
                        else{
                            break;
                        }
                    }
                    else{
                        break;
                    }
                    j++;
                }
                if(newFirstCol!=-1)
                {
                    Word foundWord = new Word(word.getFirstRow() + i, newFirstCol, true, s);
                    wordList.add(foundWord);
                }
            }
        }
        return wordList;    //return all the words we've found
    }

    public ArrayList<Word> getAllWordsWithBlanks(Word word) {
        return getAllWords(new Word(word.getFirstRow(), word.getFirstColumn(), word.isHorizontal(), word.getDesignatedLetters()));
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
}
