package core;

import java.util.Scanner;

import static java.lang.System.exit;

public final class UserInput {


    private static Scanner in = new Scanner(System.in);
    private static int[] move = {0,0};

    public static int[] getMove(){
        String s = in.nextLine();
        switch (s.toUpperCase()) {
            case "W":
                move[0] = -1;
                move[1] = 0;
                break;
            case "E":
                move[0] = 1;
                move[1] = 0;
                break;
            case "S":
                move[0] = 0;
                move[1] = 1;
                break;
            case "N":
                move[0] = 0;
                move[1] = -1;
                break;
            case "NW":
                move[0] = -1;
                move[1] = -1;
                break;
            case "NE":
                move[0] = 1;
                move[1] = -1;
                break;
            case "SE":
                move[0] = 1;
                move[1] = 1;
                break;
            case "SW":
                move[0] = -1;
                move[1] = 1;
                break;
            case "Q":
                exit(0);
        }
        return move;
    }

}
