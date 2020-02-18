
public class Player {
    private String name;
    private int score;
    private Frame myFrame;

    public Player(String n) {                      //Constructor for player which takes a name as input and sets the input as the name and the score to zero
        validateName(n);
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
        System.out.print(this.name);
    }

    public void setName(String n){                 //setter method for variable "name"
        this.name = n;
    }

    public Frame getMyFrame(){                     //getter method for variable "frame"
        return myFrame;
    }

    @Override
    public String toString() {                     //toString method to check the variables stored in a particular player object
        return "Player Name: '" + name + '\'' + ", Score: " + score + ", Frame contains: " + myFrame.toString();
    }

    public boolean validateName(String n) {     //Method to validate the name of the player
        if (n.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
            // System.out.println("Name cannot be empty");
//            return false;
        } else if(n.contains(" ") ){
            throw new IllegalArgumentException("Name cannot have more than one word");
            //System.out.println("Name cannot contain more than 1 word");
//            return false;
        }
        return true;
    }
}
