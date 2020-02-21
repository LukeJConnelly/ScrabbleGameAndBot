import java.util.ArrayList;

public class Frame {
    public ArrayList<Character> letters;        //stores the letters in a frame


    public Frame (){

        this.letters = new ArrayList<>();

    }


public void removeLetter(Character c){          //function to remove a single character from the frame
    this.letters.remove(c);
}

public void removeLetters(ArrayList<Character> chars){      //function to remove multiple letters
        this.letters.removeAll(chars);                      //removes an Arraylist of characters from the frame
}


public boolean checkLetter(Character c){        //boolean function that checks if a specific letter is in the frame
       return this.letters.contains(c);
}

/*
public boolean checkLetters(ArrayList<Character> chars){        //boolean function that checks if multiple letters are in the frame
        for(Character c : chars){
            if(!this.letters.contains(c)) return false;
        }
        return true;
}   */

    public boolean checkLetters(ArrayList<Character> chars) {        //boolean function that checks if multiple letters are in the frame
        ArrayList<Character> charsToBeReturned = new ArrayList<Character>();
        for (Character c : chars) {
            if (this.letters.contains(c)) {
                this.letters.remove(c);
                charsToBeReturned.add(c);
            }
            else{
                this.letters.addAll(charsToBeReturned);
                return false;
            }
        }
        this.letters.addAll(charsToBeReturned);
        return true;
    }


public boolean checkFrameEmpty(){     //boolean function that checks if frame is empty
                                                                //identifies as true if frame is empty
        if (this.letters.isEmpty()){                                   //false otherwise
            return true;
        }
        else{
            return false;
        }

}


public ArrayList<Character> getLetters(){                //accessor for letters in frame
        return this.letters;
}



public String toString(){                                   //toString function that displays letters in frame

       String w= "Frame contains:" + this.letters.toString();
       return w;
}


    public void fillFrame(Pool myPool) {            //function that fills the frame with letters from the Pool
        while(this.letters.size()<7 && !(myPool.isEmpty()) ) {
            this.letters.add(myPool.randomTile());
        }
    }

}
