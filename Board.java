public class Board {

    public static void main(String[] args) {


    }
    private int[][] boardValues = {             //assignment of a score value to each square on the board
            {4,0,0,1,0,0,0,4,0,0,0,1,0,0,4},
            {0,3,0,0,0,2,0,0,0,2,0,0,0,3,0},
            {0,0,3,0,0,0,1,0,1,0,0,0,3,0,0},
            {1,0,0,3,0,0,0,1,0,0,0,3,0,0,1},
            {0,0,0,0,3,0,0,0,0,0,3,0,0,0,0},
            {0,2,0,0,0,2,0,0,0,2,0,0,0,2,0},
            {0,0,1,0,0,0,1,0,1,0,0,0,1,0,0},
            {4,0,0,1,0,0,0,3,0,0,0,1,0,0,4},
            {0,0,1,0,0,0,1,0,1,0,0,0,1,0,0},
            {0,2,0,0,0,2,0,0,0,2,0,0,0,2,0},
            {0,0,0,0,3,0,0,0,0,0,3,0,0,0,0},
            {1,0,0,3,0,0,0,1,0,0,0,3,0,0,1},
            {0,0,3,0,0,0,1,0,1,0,0,0,3,0,0},
            {0,3,0,0,0,2,0,0,0,2,0,0,0,3,0},
            {4,0,0,1,0,0,0,4,0,0,0,1,0,0,4}
    };

    private char[][] boardTiles = new char[15][15];

    public void boardReset()
    {
        int i,j;
        for (i=0;i<15;i++)
        {
            for (j=0;j<15;j++)
            {
                boardTiles[j][i]='\0';   //method resets board by inserting null character into each square
            }
        }
    }
    public String toString()
    {
        String s="";

        //pritning of column letters
        System.out.println("  A   B   C   D   E   F   G   H   I   J   K   L   M   N   O");

        s+="| ----------------------------------------------------------- |\n";

        int i,j;
        for (i=0;i<15;i++)
        {
            for(j=0;j<15;j++)
            {

                s+="| ";
                if (boardTiles[j][i]!='\0')
                {
                    s+=boardTiles[j][i] + " ";
                }
                else
                {
                    switch(this.boardValues[j][i])
                    {
                        case 0:
                        {
                            s+="_ ";
                            break;
                        }
                        case 1:
                        {
                            s+="d ";
                            break;
                        }
                        case 2:
                        {
                            s+="t ";
                            break;
                        }
                        case 3:
                        {
                            s+="2 ";
                            break;
                        }
                        case 4:
                        {
                            s+="3 ";
                            break;
                        }
                    }
                }
            }

            s+= "| | " +(i+1);  //prints the row number

            s+="\n| ----------------------------------------------------------- |\n";
        }
        return s;
    }
    public void addTile(char c, int x, int y)   //method to add tile to particular square
    {
        boardTiles[x][y]=c;
    }
    public void removeTile(int x, int y)        //method to remove a given tile
    {
        boardTiles[x][y]='\0';
    }
    public boolean containsTile(int x, int y)   //boolean method to test presence of tile
    {
        return !(boardTiles[x][y]=='\0');
    }
}

