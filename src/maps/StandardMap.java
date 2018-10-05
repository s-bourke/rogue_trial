package maps;

import attribs.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class StandardMap {

    private ArrayList<String> map;

    public StandardMap(String filepath){


        map = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String CurrentLine;

            while ((CurrentLine = br.readLine()) != null) {
                map.add(CurrentLine);
            }

        } catch (IOException e) {
            System.err.println("Missing map file: " + filepath);
            exit(2);
        }
    }

    public void displayMap(Position player) {

        int x = player.getX();
        int y = player.getY();

        for (int i = 0; i < 25; i++) {
            System.out.println(" ");
        }

        for (int i = 0; i < map.size(); i++) {
            if (i == y) {
                System.out.println(map.get(i).substring(0, x) + 'p' + map.get(i).substring(x + 1, map.size() + 2));
            } else {
                System.out.println(map.get(i));
            }
        }

    }

    public String getBlock(Position pos, int[] move) {
        return map.get(pos.getY() + move[1]).substring(pos.getX() + move[0], pos.getX() + move[0] + 1);
    }
}
