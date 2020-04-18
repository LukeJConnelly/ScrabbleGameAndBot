import java.util.ArrayList;
import java.util.HashSet;

public class Test {
    public static Board board = new Board();
    public static void main(String[] args) {
        Tile tile = new Tile('A');
        board.getSquare(7,7).add(tile);
        board.getSquare(7,14).add(tile);
        board.getSquare(7,0).add(tile);
        HashSet<GADDAG> hs = new HashSet<>();
        GADDAG acrossMaster = new GADDAG(board, 7, 7, true);
        ArrayList<String> suffixes = new ArrayList<>();
        ArrayList<String> prefixes = new ArrayList<>();
        String suf = ""+acrossMaster.suffix.charAt(0);
        int i=0;
        while(suf.length()-suf.replaceAll("[?]", "").length()<7&&acrossMaster.start.col+i<14)
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
        String pre=""+acrossMaster.prefix.charAt(0);
        while(pre.length()-pre.replaceAll("[?]", "").length()<7&&acrossMaster.start.col-i-1>0)
        {
            if(acrossMaster.prefix.charAt(i+1)=='?')
            {
                prefixes.add(pre);
            }
            i++;
            pre+=acrossMaster.prefix.charAt(i);
        }
        prefixes.add(pre);
        System.out.println(suffixes);
        System.out.println(prefixes);
        for(String s : suffixes){
            hs.add(new GADDAG("", s, 7, 7, true));
            for(String p : prefixes)
            {
                if ((s+p).length()-(s+p).replaceAll("[?]","").length()<7) {
                    hs.add(new GADDAG(p, s, 7, 7, true));
                }
            }
        }
        for(String p : prefixes)
        {
            hs.add(new GADDAG(p, ""+acrossMaster.suffix.charAt(0), 7, 7, true));
        }
        System.out.println(hs);
    }
    public static void generateGaddags(int r, int c, HashSet<GADDAG> hs){
        GADDAG acrossMaster = new GADDAG(board, r, c, true);
        GADDAG downMaster = new GADDAG(board, r, c, false);
        acrossMaster.getSlaves(7, hs);
    }
    private static class GADDAG {
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
                        if(suffix.length()-suffix.replace("?", "").length()>=7)
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
                        if(prefix.length()-prefix.replace("?", "").length()>=7)
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
                        if(suffix.length()-suffix.replace("?", "").length()>=7)
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
                        if(prefix.length()-prefix.replace("?", "").length()>=7)
                        {
                            break;
                        }
                    }
                    rtemp--;
                }
            }
        }
        public void getSlaves(int u, HashSet<GADDAG> hs){
            for(int i=0;i<u;i++)
            {
                String pre="", suf="";
                suf=board.getSquareCopy(start.row, start.col).isOccupied() ? ""+board.getSquareCopy(start.row, start.col).getTile().getLetter() : "?";
                if(suf=="?"){}
//                hs.add(pre, suf, start.row, start.col, isHorizontal);
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

    private static class IntPair {
        public int row;
        public int col;
        IntPair(int r, int c){
            row = r;
            col = c;
        }
    }
}
