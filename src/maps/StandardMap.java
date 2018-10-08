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

    private final ArrayList<String> map;
    private final Location loc;

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
        }
        return " ";
    }

    public void addExit(Direction dir, int offset) {
        if (offset != -1) {
            switch (dir) {
                case N:
                    map.set(0, (map.get(0).substring(0, offset) + ' ' + map.get(0).substring(offset + 1)));
                    break;
                case S:
                    map.set(BlockRefs.ySize - 1, (map.get(BlockRefs.ySize - 1).substring(0, offset) + ' ' + map.get(BlockRefs.ySize - 1).substring(offset + 1)));
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
            return new StandardMap(filename);
        }

        int dir = (int) (Math.random() * (20));
        switch (dir) {
            case 0:
                return MapGenerator.genBlockedMap(newLoc);
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

    public void addBlock(int x, int y, char c) {
        map.set(y, (map.get(y).substring(0, x) + c + map.get(y).substring(x + 1, BlockRefs.xSize)));
    }

    public ArrayList<String> getArray() {
        return map;
    }
}

