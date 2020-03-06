package sample;

import java.util.Scanner;
public class ScanningTesting {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String input = "";
        boolean endGame=false;
        while(!endGame)
        {
            input = sc.nextLine().toUpperCase();
            while(!input.matches("^[A-O][1-9] [AD] [A-Z]{2,}$|^[A-O]1[0-5] [AD] [A-Z]{2,}$|^QUIT$|^PASS$|^HELP$|^EXCHANGE [A-Z]{1,7}$"))
            {
                System.out.println("Input currently not recognized, please re-enter");
                input = sc.nextLine().toUpperCase();
            }
            if (input.matches("^QUIT$"))
            {
                System.out.print("Game Quit");
                endGame=true;
            }
            else if (input.matches("^PASS$"))
            {
                System.out.print("Turn Passed");
            }
            else if (input.matches("^HELP$"))
            {
                System.out.println("Help Guide:");
                System.out.println("'quit' to exit game");
                System.out.println("'pass' to skip your turn");
                System.out.println("'exchange <list of letters>' to swap letters (be sure to enter a space between exchange and letters)");
                System.out.println("'<co-ordinate> <A/D for Across/Down> <Word>' to take your turn (be sure to enter a space after co-ordinate and A/D)");
                System.out.print("Enjoy!");
            }
            else if (input.matches("^EXCHANGE [A-Z]{1,7}$"))
            {
                System.out.print("Exchanging Letters");
            }
            else if (input.matches("^[A-O][1-9] [AD] [A-Z]{2,}$|^[A-O]1[0-5] [AD] [A-Z]{2,}$"))
            {
                System.out.print("Following move will be made: "+input);
            }
            System.out.println();
        }
        sc.close();
    }
}