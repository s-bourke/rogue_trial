package maps;

import attribs.Location;
import attribs.Position;
import core.Direction;
import core.RoomType;
import entities.Player;

import java.io.*;
import java.util.ArrayList;

import static core.BlockRefs.mapDir;
import static core.BlockRefs.size;
import static core.RoomType.Enemy;
import static core.RoomType.Start;
import static core.RoomType.Treasure;
import static java.lang.System.exit;

public class Map {

    private final char[][] map;
    private final ArrayList<RoomType> types;
    private final Location loc;

    public Map(String filepath) {


        String[] parts = filepath.split("_");
        int X = Integer.parseInt(parts[0]);
        int Y = Integer.parseInt(parts[1]);
        types = new ArrayList<>();

        this.loc = new Location(X, Y);
        map = new char[size][size];
        try (BufferedReader br = new BufferedReader(new FileReader(mapDir + filepath))) {

            String currentLine;
            currentLine = br.readLine();

            for (char s : currentLine.toCharArray()) {
                types.add(getTypeFromSymbol(s));
            }

            char[] line;
            for (int i = 0; i < size; i++) {
                line = br.readLine().toCharArray();
                System.arraycopy(line, 0, map[i], 0, size);
            }


        } catch (IOException e) {
            System.err.println("Missing map file: " + filepath);
            exit(2);
        }

    }

    public void addType(RoomType type) {
        if (!types.contains(type)) {
            types.add(type);
        }
    }

    public void removeType(RoomType type) {
        types.remove(type);
    }

    public static RoomType getTypeFromSymbol(char s) {
        switch (s) {
            case 'E':
                return Enemy;
            case 'S':
                return Start;
            case 'T':
                return Treasure;
            default:
                return null;
        }
    }

    public static char getSymbolFromType(RoomType type) {
        switch (type) {
            case Enemy:
                return 'E';
            case Start:
                return 'S';
            case Treasure:
                return 'T';
            default:
                return ' ';
        }
    }

    public Map(char[][] map, Location loc) {
        this.map = map;
        this.loc = loc;
        types = new ArrayList<>();
    }

    public char getBlock(Position pos, Direction move) {
        switch (move) {
            case N:
                return map[pos.getY() - 1][pos.getX()];
            case S:
                return map[pos.getY() + 1][pos.getX()];
            case E:
                return map[pos.getY()][pos.getX() + 1];
            case W:
                return map[pos.getY()][pos.getX() - 1];
        }
        return ' ';
    }

    public void removeBlock(Position pos, Direction move) {
        switch (move) {
            case N:
                map[pos.getY() - 1][pos.getX()] = ' ';
                break;
            case S:
                map[pos.getY() + 1][pos.getX()] = ' ';
                break;
            case E:
                map[pos.getY()][pos.getX() + 1] = ' ';
                break;
            case W:
                map[pos.getY()][pos.getX() - 1] = ' ';
                break;
        }
    }

    public void addExit(Direction dir, int offset) {
        if (offset != -1) {
            switch (dir) {
                case N:
                    map[0][offset] = ' ';
                    break;
                case S:
                    map[size - 1][offset] = ' ';
                    break;
                case E:
                    map[offset][size - 1] = ' ';
                    break;
                case W:
                    map[offset][0] = ' ';
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

        StringBuilder builder = new StringBuilder();
        for (RoomType type : types) {
            builder.append(getSymbolFromType(type));
        }
        writer.println(builder);

        for (char[] chars : map) {
            builder = new StringBuilder();
            for (char aChar : chars) {
                builder.append(aChar);
            }
            writer.println(builder);
        }

        writer.close();
    }

    public Map getAdj(Direction arrivalDir) {
        String filename = null;
        Location newLoc = null;
        switch (arrivalDir) {
            case S:
                filename = MapGenerator.getFileName(loc.getX(), loc.getY() + 1);
                newLoc = new Location(loc.getX(), loc.getY() + 1);
                break;
            case N:
                filename = MapGenerator.getFileName(loc.getX(), loc.getY() - 1);
                newLoc = new Location(loc.getX(), loc.getY() - 1);
                break;
            case W:
                filename = MapGenerator.getFileName(loc.getX() + 1, loc.getY());
                newLoc = new Location(loc.getX() + 1, loc.getY());
                break;
            case E:
                filename = MapGenerator.getFileName(loc.getX() - 1, loc.getY());
                newLoc = new Location(loc.getX() - 1, loc.getY());
                break;
        }
        Player.loc = newLoc;
        File f = new File(mapDir + filename);
        if (f.exists()) {
            return new Map(filename);
        }

        int dir = (int) (Math.random() * (20));
        switch (dir) {
            case 0:
                return MapGenerator.genBlockedMap(newLoc, arrivalDir);
        }
        return MapGenerator.genMap(newLoc);
    }


    public int getOffset(Direction dir) {
        switch (dir) {
            case N:
                return new String(map[0]).indexOf(" ");
            case S:
                return new String(map[size - 1]).indexOf(" ");
            case E:
                for (int i = 0; i < size; i++) {
                    if (map[i][size - 1] == ' ') {
                        return i;
                    }
                }
                break;
            case W:
                for (int i = 0; i < size; i++) {
                    if (map[i][0] == ' ') {
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    public void addBlock(int x, int y, char c) {
        map[x][y] = c;
    }

    public char[][] getArray() {
        return map;
    }

    public void printMap() {
        StringBuilder builder;
        for (char[] chars : map) {
            builder = new StringBuilder();
            for (char aChar : chars) {
                builder.append(aChar);
            }
            System.out.println(builder);
        }

    }

}

