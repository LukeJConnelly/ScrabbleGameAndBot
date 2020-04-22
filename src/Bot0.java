import java.util.ArrayList;
import java.util.HashSet;

public class Bot0 implements BotAPI {


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
    private int pool = 0;
    private boolean hasPool = false;
    private String AllInfo="";

    Bot0(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
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
            command = "NAME Scrabbled";
        }
//        else if (!me.getFrameAsString().contains("_")&&!board.isFirstPlay()) {
//            command = "X "+ me.getFrameAsString().replaceAll("[^A-Z_]", "");
//        }
        else if (board.isFirstPlay()) {
            command = makeFirstWord(me.getFrameAsString());
        }
        else if(!hasPool)
        {
            if(shouldChallenge())
            {
                command="CHALLENGE";
            }
            else {
                command = "POOL";
                hasPool = true;
            }
        }
        else {
            pool=getPool();
            System.out.println(pool);
            command = makeWord(me.getFrameAsString());
            hasPool=false;
        }
        System.out.println("scrabbled: " + me.getScore());
        System.out.println("Bot0: " + opponent.getScore());
        turnCount++;
        return command;
    }

    public boolean shouldChallenge() {
        String currInfo = info.getAllInfo().substring(AllInfo.length());
        String[] InfoArray = currInfo.split("\n");
        int i=InfoArray.length-1;
        while(true) {
            if(InfoArray[i].contains(">"))
            {
                if(!InfoArray[i].contains("NAME"))
                {
                    break;
                }
            }
            i--;
            if (i == -1) {
                return false; //some error - dont challenge
            }
        }
        if(InfoArray[i].toUpperCase().equals("> CHALLENGE") || InfoArray[i].toUpperCase().equals("> PASS") || InfoArray[i].toUpperCase().equals("> P") || InfoArray[i].toUpperCase().matches("[>][ ]EXCHANGE( )+([A-Z_]){1,7}") || InfoArray[i].toUpperCase().matches("[>][ ]X( )+([A-Z_]){1,7}"))
        {
            return false; // not a move
        }
        String[] parts = InfoArray[i].toUpperCase().substring(1).trim().split("( )+");
        String gridText = parts[0];
        int column = ((int) gridText.charAt(0)) - ((int) 'A');
        String rowText = parts[0].substring(1);
        int row = Integer.parseInt(rowText)-1;
        String directionText = parts[1];
        boolean isHorizontal = directionText.equals("A");
        String letters = parts[2];
        Word word;
        if (parts.length == 3) {
            word = new Word(row, column, isHorizontal, letters);
        } else {
            String designatedBlanks = parts[3];
            word = new Word(row, column, isHorizontal, letters, designatedBlanks);
        }
        ArrayList<Word> words = new ArrayList<>();
        for(Word w : getAllWordsChallenge(word))
        {
            words.add(w);
        }
        AllInfo=info.getAllInfo();
        return !dictionary.areWords(words);
    }

    public int getPool()
    {
        String currInfo = info.getAllInfo().substring(AllInfo.length());
        String[] InfoArray = currInfo.split("\n");
        int i=InfoArray.length-1;
        while(!InfoArray[i].contains("Pool has ")) {
            i--;
            if (i == -1) {
                return 100; //some error - assume happens at start
            }
        }
        AllInfo=info.getAllInfo();
        return Integer.parseInt(InfoArray[i].replaceAll("\\D+",""));
    }

    public String exchange(String frame){
        if(board.isFirstPlay())
        {
            return "X "+frame.replaceAll("[ZXQJ_S]","");
        }
        if (pool<7)
        {
            return "pass";
        } if (pool<30) {        //TODO test with greater than or less than 15
            System.out.println("EXCHANGE");
            return "X "+frame.replaceAll("[^ZXQJVWGKBFHCD]", "");
        }
        return "X "+frame.replaceAll("[ZXQJ_S]","");
    }

    public String makeFirstWord(String myFrame)
    {
        myFrame = myFrame.replaceAll("[^A-Z_]", "");//turning frame into string of 7 letters
        String command = exchange(myFrame);//preparing an exchange command if we find no words
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
                                if(getFirstWordPoints(word)>35) {
                                    bestWord = wordWithBlanks;
                                    maxScore = getFirstWordPoints(word);
                                    blanks = " ";
                                    for (int o = 0; o < combination.length(); o++) {
                                        if (combination.charAt(o) == '_') {
                                            blanks += combinationWithoutBlanks.charAt(o);
                                        }
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
                            if(word.toString().length() - word.toString().replaceAll("[ZQXJ]","").length() >0)
                            {
                                if(getFirstWordPoints(word)>30)
                                {
                                    bestWord = word;
                                    maxScore = getFirstWordPoints(word);
                                    blanks = "";
                                }
                            }
                            else {
                                bestWord = word;
                                maxScore = getFirstWordPoints(word);
                                blanks = "";
                            }
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
        String command = exchange(myFrame);//preparing a pass command if we find no words
        Word bestWord = new Word(0,0,false,"neverever");//is never used, just a placeholder
        int maxScore=0;
        String blanks="";
        ArrayList<String> combinations = getCombinations(myFrame);    //find every combination of the letters
        HashSet<Bot0.IntPair> hooks = new HashSet<Bot0.IntPair> ();
        for (int r=0; r<15; r++)  {
            for (int c=0; c<15; c++)   {
                if (isHook(r, c))
                {
                    hooks.add(new Bot0.IntPair(r,c));
                }
            }
        }
        HashSet<Bot0.GADDAG> gaddags = new HashSet<Bot0.GADDAG>();
        for(Bot0.IntPair i : hooks)
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
        for(Bot0.GADDAG g : gaddags)
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
                                            if (usedLetters(temp).length() - usedLetters(temp).replaceAll("[ZQX]","").length() >0){
                                                if(pool>20)     //TODO experiment with these numbers
                                                {
                                                    if(score>maxScore&&score>60){
                                                        bestWord = temp;
                                                        maxScore = score;
                                                        blanks = " "+Character.toString((char) ((char) i + 'A')) + Character.toString((char) ((char) j + 'A'));
                                                    }
                                                }
                                                else if(score>maxScore)
                                                {
                                                    bestWord = temp;
                                                    maxScore = score;
                                                    blanks = " "+Character.toString((char) ((char) i + 'A')) + Character.toString((char) ((char) j + 'A'));
                                                }
                                            }
                                            else if(pool>20)
                                            {
                                                if(score>maxScore&&score>50){
                                                    bestWord = temp;
                                                    maxScore = score;
                                                    blanks = " "+Character.toString((char) ((char) i + 'A')) + Character.toString((char) ((char) j + 'A'));
                                                }
                                            }
                                            else if (score > maxScore) {
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
                                        if (usedLetters(temp).length() - usedLetters(temp).replaceAll("[ZQX]","").length() >0){
                                            if(pool>20) //TODO experiment with these numbers
                                            {
                                                if(score>maxScore&&score>50){
                                                    bestWord = temp;
                                                    maxScore = score;
                                                    blanks = " "+Character.toString((char) ((char) i + 'A'));
                                                }
                                            }
                                            else if(score>maxScore)
                                            {
                                                bestWord = temp;
                                                maxScore = score;
                                                blanks = " "+Character.toString((char) ((char) i + 'A'));
                                            }
                                        }
                                        else if(pool>20)
                                        {
                                            if(score>maxScore&&score>40){
                                                bestWord = temp;
                                                maxScore = score;
                                                blanks = " "+Character.toString((char) ((char) i + 'A'));
                                            }
                                        }
                                        else if (score > maxScore) {
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
                                if (usedLetters(temp).length() - usedLetters(temp).replaceAll("[ZQX]","").length() >0){
                                    if(pool>20) //TODO experiment with these numbers
                                    {
                                        if(score>maxScore&&score>25){
                                            bestWord = temp;
                                            maxScore = score;
                                            blanks = "";
                                        }
                                    }
                                    else if(score>maxScore)
                                    {
                                        bestWord = temp;
                                        maxScore = score;
                                        blanks = "";
                                    }
                                }
                                else if (score > maxScore) {
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
        if((maxScore != 0&&pool<1)||maxScore>5)//TODO check what size pool and min score works best here
        {
            command = "    "+Character.toString((char) (bestWord.getFirstColumn()+'A')) + Integer.toString(bestWord.getFirstRow()+1)+"   ";
            command += bestWord.isHorizontal() ? " A ":" D ";
            command += "   "+bestWord.toString(); //creates command for the best word
            command += blanks;  //the spaces break other peoples code (hopefully)
        }
        System.out.println(command);
        return command;
    }



    public String usedLetters(Word word){
        String s="";
        for (int i=0; i<word.toString().length(); i++){
            if(word.isHorizontal()){
                if(!board.getSquareCopy(word.getFirstRow(), word.getFirstColumn()+i).isOccupied()){
                    s+=Character.toString(word.getLetter(i));
                }
            }
            else{
                if(!board.getSquareCopy(word.getFirstRow()+i, word.getFirstColumn()).isOccupied()){
                    s+=Character.toString(word.getLetter(i));
                }
            }
        }
        return s;
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

    public ArrayList<Word> getAllWordsChallenge(Word mainWord) {
        ArrayList<Word> words = new ArrayList<>();
        words.add(mainWord);
        int r = mainWord.getFirstRow();
        int c = mainWord.getFirstColumn();
        for (int i=0; i<mainWord.length(); i++) {
            if (board.getSquareCopy(r,c).isOccupied()) {
                if (isAdditionalWord(r, c, mainWord.isHorizontal())) {
                    words.add(getAdditionalWordChallenge(r, c, mainWord.isHorizontal()));
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

    private Word getAdditionalWordChallenge(int mainWordRow, int mainWordCol, boolean mainWordIsHorizontal) {
        int firstRow = mainWordRow;
        int firstCol = mainWordCol;
        // search up or left for the first letter
        while (firstRow >= 0 && firstCol >= 0 && board.getSquareCopy(firstRow,firstCol).isOccupied()) {
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
        while (r< Board.BOARD_SIZE && c< Board.BOARD_SIZE && board.getSquareCopy(r,c).isOccupied()) {
            letters = letters + board.getSquareCopy(r, c).getTile().getLetter();
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

    public void generateGaddags(int r, int c, HashSet<Bot0.GADDAG> hs){
        Bot0.GADDAG acrossMaster = new Bot0.GADDAG(board, r, c, true);
        Bot0.GADDAG downMaster = new Bot0.GADDAG(board, r, c, false);
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
                    hs.add(new Bot0.GADDAG("", s, acrossMaster.start.row, acrossMaster.start.col, true));
                }
            }
            for(String p : prefixes)
            {
                if ((s+p).length()-(s+p).replaceAll("[?]","").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()) {
                    hs.add(new Bot0.GADDAG(p, s, acrossMaster.start.row, acrossMaster.start.col, true));
                }
            }
        }
        for(String p : prefixes)
        {
            hs.add(new Bot0.GADDAG(p, ""+acrossMaster.suffix.charAt(0), acrossMaster.start.row, acrossMaster.start.col, true));
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
                    hs.add(new Bot0.GADDAG("", s, downMaster.start.row, downMaster.start.col, false));
                }
            }
            for(String p : prefixes)
            {
                if ((s+p).length()-(s+p).replaceAll("[?]","").length()<me.getFrameAsString().replaceAll("[^A-Z_]","").length()) {
                    hs.add(new Bot0.GADDAG(p, s, downMaster.start.row, downMaster.start.col, false));
                }
            }
        }
        for(String p : prefixes)
        {
            hs.add(new Bot0.GADDAG(p, ""+downMaster.suffix.charAt(0), downMaster.start.row, downMaster.start.col, false));
        }
    }

    public void getGADDAGFrameCombinations(Bot0.GADDAG g, ArrayList<String> combinations, HashSet<String> GADDAGcombos){
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

    public int getAllPoints(ArrayList<Word> words, Bot0.GADDAG g) {
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
        public Bot0.IntPair start;
        public boolean isHorizontal;
        GADDAG(String pre, String suf, int r, int c, boolean b){
            this.prefix = pre;
            this.suffix = suf;
            this.start = new Bot0.IntPair(r,c);
            this.isHorizontal = b;
        }
        GADDAG(BoardAPI board, int row, int col, boolean isHorizontal){
            //produces a gaddag starting at a tile on that line where ? represents empty squares - max seven ?'s in suffix or prefix
            this.prefix = "";
            this.suffix = "";
            this.start = new Bot0.IntPair(row,col);
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