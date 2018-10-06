package maps;

import attribs.Location;
import attribs.Position;
import core.BlockRefs;
import core.Direction;
import entities.Player;

import java.io.*;
import java.util.ArrayList;

import static core.BlockRefs.mapDir;
import static java.lang.System.exit;

public class StandardMap {

    private ArrayList<String> map;
    private Location loc;

    public StandardMap(String filepath) {


        String[] parts = filepath.split("_");
        int X = Integer.parseInt(parts[0]);
        int Y = Integer.parseInt(parts[1]);


        this.loc = new Location(X, Y);
        map = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(mapDir + filepath))) {

            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                map.add(currentLine);
            }

        } catch (IOException e) {
            System.err.println("Missing map file: " + filepath);
            exit(2);
        }

    }

    public StandardMap(ArrayList<String> map, Location loc) {
        this.map = map;
        this.loc = loc;
    }

    public void displayMap(Position player) {

        int x = player.getX();
        int y = player.getY();

        for (int i = 0; i < 2; i++) {
            System.out.println(" ");
        }
        System.out.println(loc);
        for (int i = 0; i < map.size(); i++) {
            if (i == y) {
                System.out.println(map.get(i).substring(0, x) + 'p' + map.get(i).substring(x + 1, map.get(i).length()));
            } else {
                System.out.println(map.get(i));
            }
        }
    }

    public String getBlock(Position pos, Direction move) {
        switch (move) {
            case N:
                return map.get(pos.getY() - 1).substring(pos.getX(), pos.getX() + 1);
            case S:
                return map.get(pos.getY() + 1).substring(pos.getX(), pos.getX() + 1);
            case E:
                return map.get(pos.getY()).substring(pos.getX() + 1, pos.getX() + 2);
            case W:
                return map.get(pos.getY()).substring(pos.getX() - 1, pos.getX());
            case NE:
                return map.get(pos.getY() - 1).substring(pos.getX() + 1, pos.getX() + 2);
            case SE:
                return map.get(pos.getY() + 1).substring(pos.getX() + 1, pos.getX() + 2);
            case NW:
                return map.get(pos.getY() - 1).substring(pos.getX() - 1, pos.getX());
            case SW:
                return map.get(pos.getY() + 1).substring(pos.getX() - 1, pos.getX());
        }
        return " ";
    }

    public void addExit(Direction dir, int offset) {
        if (offset != -1) {
            switch (dir) {
                case N:
                    map.set(0, (map.get(0).substring(0, offset) + ' ' + map.get(0).substring(offset + 1, map.get(0).length())));
                    break;
                case S:
                    map.set(BlockRefs.ySize - 1, (map.get(BlockRefs.ySize - 1).substring(0, offset) + ' ' + map.get(BlockRefs.ySize - 1).substring(offset + 1, map.get(BlockRefs.ySize - 1).length())));
                    break;
                case E:
                    map.set(offset, (map.get(offset).substring(0, BlockRefs.xSize - 1) + ' '));
                    break;
                case W:
                    map.set(offset, ' ' + (map.get(offset).substring(1, BlockRefs.xSize)));
                    break;
            }
        }
    }

    public void writeMap(String filename) {

        PrintWriter writer = null;

        try {
            writer = new PrintWriter("worldMaps/" + filename, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.err.println("Could not write map " + filename + " to file");
            exit(3);
        }

        for (String s : map) {
            writer.println(s);
        }
        writer.close();
    }

    public StandardMap getAdj(Direction arrivalDir) {
        String filename = null;
        Location newLoc = null;
        switch (arrivalDir) {
            case S:
                filename = getFileName(loc.getX(), loc.getY() + 1);
                newLoc = new Location(loc.getX(), loc.getY() + 1);
                break;
            case N:
                filename = getFileName(loc.getX(), loc.getY() - 1);
                newLoc = new Location(loc.getX(), loc.getY() - 1);
                break;
            case W:
                filename = getFileName(loc.getX() + 1, loc.getY());
                newLoc = new Location(loc.getX() + 1, loc.getY());
                break;
            case E:
                filename = getFileName(loc.getX() - 1, loc.getY());
                newLoc = new Location(loc.getX() - 1, loc.getY());
                break;
        }
        Player.loc = newLoc;
        File f = new File(mapDir + filename);
        if (f.exists()) {
            if (filename != null) {
                return new StandardMap(filename);
            }
            System.err.println("Failed to generate file name for existing map.");
            exit(4);
        }
        return MapGenerator.genMap(newLoc);
    }

    public static String getFileName(int x, int y) {
        return x + "_" + y;
    }

    public int getOffset(Direction dir) {
        switch (dir) {
            case N:
                return map.get(0).indexOf(' ');
            case S:
                return map.get(BlockRefs.ySize - 1).indexOf(' ');
            case W:
                for (int i = 1; i < map.size() - 1; i++) {
                    if (map.get(i).charAt(0) == ' ') {
                        return i;
                    }
                }
                break;
            case E:
                for (int i = 1; i < map.size() - 1; i++) {
                    if (map.get(i).charAt(BlockRefs.xSize - 1) == ' ') {
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    public void addBlock(int i, int j, char c) {
        map.set(i, (map.get(i).substring(0,j) + c + map.get(i).substring(j+1,BlockRefs.xSize)));
    }
}

