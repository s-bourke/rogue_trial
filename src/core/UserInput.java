package core;

import java.util.Scanner;
import core.Direction;
import maps.MapGenerator;

import static core.Direction.*;
import static java.lang.System.exit;

public final class UserInput {


    private static Scanner in = new Scanner(System.in);
    private static int[] move = {0,0};

    public static Direction getMove(){
        String s = in.nextLine();
        switch (s.toUpperCase()) {
            case "W":
                return W;
            case "E":
                return E;
            case "N":
                return N;
            case "S":
                return S;
            case "NE":
                return NE;
            case "SE":
                return SE;
            case "NW":
                return NW;
            case "SW":
                return SW;
            case "M":
                MapGenerator.displayWorldMap();
                break;
            case "Q":
                exit(0);
        }
        return null;
    }

}
