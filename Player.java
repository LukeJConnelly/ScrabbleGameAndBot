
public class Player {
    private String name;
    private int score;
    private Frame myFrame;

    public Player(String n) {                      //Constructor for player which takes a name as input and sets the input as the name and the score to zero
        this.name = n;
        this.score = 0;
        myFrame = new Frame();
    }

    public void resetPlayerData() {                //method to reset the data of a particular player
        name = null;
        score = 0;
        myFrame = new Frame();
    }

    public void increaseScore(int scoreTally) {     //method to increase the players score
        this.score += scoreTally;
    }

    public int accessScore() {                     //method to access the score of a given player
        return this.score;
    }

    public Frame accessFrame() {                   //method to access the frame of a given player
        return myFrame;
    }

    public void displayName() {                    //method to display the name of the player
        System.out.println(this.name);
    }

    public void setName(String n) {                 //setter method for variable "name"
        this.name = n;
    }

    public Frame getMyFrame() {
        return myFrame;
    }

    @Override
    public String toString() {                     //toString method to check the variables stored in a particular player object
        return "Player Name: '" + name + '\'' + ", Score: " + score + ", Frame contains: " + myFrame.toString();
    }

}
