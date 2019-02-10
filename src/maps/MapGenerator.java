package maps;

import attribs.Location;
import core.BlockRefs;
import core.Direction;
import core.RoomType;
import entities.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static core.BlockRefs.mapDir;
import static core.BlockRefs.size;
import static core.Direction.*;
import static java.lang.System.exit;

public final class MapGenerator {

    public static Map genMap(Location loc) {

        System.out.println("Generating standard map at " + loc);

        Map nMap = genBlankMap(loc);
        File f;

        Direction[] dirs = {N, E, S, W};
        int[] dirOffset = {0, 1, 0, -1};
        int yIndex;
        for (int i = 0; i < 4; i++) {
            yIndex = (i + 1) % 4;
            f = new File(mapDir + getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]));
            if (f.exists()) {
                nMap.addExit(dirs[i], getExit(getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]), dirs[(i + 2) % 4]));
            } else if (Math.random() < 0.6) {
                nMap.addExit(dirs[i], (BlockRefs.size / 4) + (int) (Math.random() * (BlockRefs.size / 2)));
            }
        }

        for (int i = 2; i < BlockRefs.size - 2; i++) {
            for (int j = 2; j < BlockRefs.size - 2; j++) {
                if (Math.random() < 0.3) {
                    nMap.addBlock(i, j, '@');
                }
            }
        }

        if (Math.random() < 0.3) {
            int x = (int) (Math.random() * (size - 6)) + 3;
            int y = (int) (Math.random() * (size - 6)) + 3;
            System.out.println(x + " " + y);
            nMap.addBlock(x, y, 'e');
        }

        nMap.writeMap(getFileName(loc.getX(), loc.getY()));
        return nMap;
    }

    private static Map genHighAccessMap(Location loc) {

        System.out.println("Generating high access map at " + loc);

        Map nMap = genBlankMap(loc);
        File f;

        Direction[] dirs = {N, E, S, W};
        int[] dirOffset = {0, 1, 0, -1};
        int yIndex;
        int offset;
        for (int i = 0; i < 4; i++) {
            yIndex = (i + 1) % 4;
            f = new File(mapDir + getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]));
            if (f.exists()) {
                nMap.addExit(dirs[i], getExit(getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]), dirs[(i + 2) % 4]));
            } else {
                offset = (BlockRefs.size / 4) + (int) (Math.random() * (BlockRefs.size / 2));
                nMap.addExit(dirs[i], offset);
            }
        }

        for (int i = 2; i < BlockRefs.size - 2; i++) {
            for (int j = 2; j < BlockRefs.size - 2; j++) {
                if (Math.random() < 0.3) {
                    nMap.addBlock(i, j, '@');
                }
            }
        }

        nMap.writeMap(getFileName(loc.getX(), loc.getY()));
        return nMap;
    }


    public static Map genBlockedMap(Location loc, Direction arrivalDir) {

        System.out.println("Generating blocked map at " + loc);

        Map nMap = genBlankMap(loc);
        File[] files = new File[4];
        Direction[] dirs = {N, E, S, W};
        int[] isAdj = {0, 0, 0, 0};
        int[] isAdjExit = {0, 0, 0, 0};
        int[] dirOffset = {0, 1, 0, -1};
        int yIndex;

        // Check if surrounding rooms exist.
        for (int i = 0; i < 4; i++) {
            yIndex = (i + 1) % 4;
            files[i] = new File(mapDir + getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]));
            if (files[i].exists()) {
                isAdj[i] = 1;
                if (getExit(getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]), dirs[(i + 2) % 4]) != -1) {
                    isAdjExit[i] = 1;
                }
            }
        }

        if (IntStream.of(isAdj).sum() != IntStream.of(isAdjExit).sum()) {
            System.out.println("Failed to generate blocked map");
            return genMap(loc);
        }

        // Make sure there are at least two exits.
        while (IntStream.of(isAdj).sum() == 1) {
            for (int i = 0; i < 4; i++) {
                if (Math.random() < 0.7) {
                    isAdj[i] = 1;
                }
            }
        }

        // Add exits.
        int[] offset = new int[4];

        for (int i = 0; i < 4; i++) {
            yIndex = (i + 1) % 4;
            if (isAdj[i] == 1) {
                if (files[i].exists()) {
                    offset[i] = getExit(getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]), dirs[(i + 2) % 4]);
                    nMap.addExit(dirs[i], offset[i]);
                } else {
                    offset[i] = (BlockRefs.size / 4) + (int) (Math.random() * (BlockRefs.size / 2));
                    nMap.addExit(dirs[i], offset[i]);
                }
            }
        }

        int dir;
        do {
            dir = (int) (Math.random() * 4);
        } while (isAdj[dir] != 1 || dirs[dir] == arrivalDir);
        Map hidden = null;
        nMap.addType(RoomType.Treasure);
        nMap.writeMap(getFileName(loc.getX(), loc.getY()));
        switch (dir) {
            case 0:
                nMap.addBlock(offset[dir] - 2, 1, '-');
                nMap.addBlock(offset[dir] - 1, 2, '-');
                nMap.addBlock(offset[dir], 2, '-');
                nMap.addBlock(offset[dir] + 1, 2, '-');
                nMap.addBlock(offset[dir] + 2, 1, '-');
                nMap.addBlock(offset[dir], 1, 'c');
                hidden = genHighAccessMap(loc.adjLocation(N));

                break;
            case 1:
                nMap.addBlock(BlockRefs.size - 2, offset[dir] - 2, '-');
                nMap.addBlock(BlockRefs.size - 3, offset[dir] - 1, '-');
                nMap.addBlock(BlockRefs.size - 3, offset[dir], '-');
                nMap.addBlock(BlockRefs.size - 3, offset[dir] + 1, '-');
                nMap.addBlock(BlockRefs.size - 2, offset[dir] + 2, '-');
                nMap.addBlock(BlockRefs.size - 2, offset[dir], 'c');
                hidden = genHighAccessMap(loc.adjLocation(E));
                break;
            case 2:
                nMap.addBlock(offset[dir] - 2, BlockRefs.size - 2, '-');
                nMap.addBlock(offset[dir] - 1, BlockRefs.size - 3, '-');
                nMap.addBlock(offset[dir], BlockRefs.size - 3, '-');
                nMap.addBlock(offset[dir] + 1, BlockRefs.size - 3, '-');
                nMap.addBlock(offset[dir] + 2, BlockRefs.size - 2, '-');
                nMap.addBlock(offset[dir], BlockRefs.size - 2, 'c');
                hidden = genHighAccessMap(loc.adjLocation(S));
                break;
            case 3:
                nMap.addBlock(1, offset[dir] - 2, '-');
                nMap.addBlock(2, offset[dir] - 1, '-');
                nMap.addBlock(2, offset[dir], '-');
                nMap.addBlock(2, offset[dir] + 1, '-');
                nMap.addBlock(1, offset[dir] + 2, '-');
                nMap.addBlock(1, offset[dir], 'c');
                hidden = genHighAccessMap(loc.adjLocation(W));
                break;

        }
        hidden.addType(RoomType.Hidden);
        nMap.writeMap(getFileName(loc.getX(), loc.getY()));

        return nMap;
    }

    private static int getExit(String f, Direction dir) {
        return new Map(f).getOffset(dir);
    }

    public static Map genStartingMap() {
        System.out.println("Generating starting map");
        Map nMap = genBlankMap(new Location(0, 0));
        int offset = (BlockRefs.size / 4) + (int) (Math.random() * (BlockRefs.size / 2));
        nMap.addExit(N, offset);
        offset = (BlockRefs.size / 4) + (int) (Math.random() * (BlockRefs.size / 2));
        nMap.addExit(S, offset);
        offset = (BlockRefs.size / 4) + (int) (Math.random() * (BlockRefs.size / 2));
        nMap.addExit(E, offset);
        offset = (BlockRefs.size / 4) + (int) (Math.random() * (BlockRefs.size / 2));
        nMap.addExit(W, offset);
        nMap.addBlock(3, 3, 'e');
        nMap.addType(RoomType.Start);
        nMap.writeMap(getFileName(0, 0));

        return nMap;
    }

    private static Map genBlankMap(Location loc) {

        char[][] map = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = ' ';
            }
        }

        for (int i = 0; i < size; i++) {
            map[i][0] = '-';
            map[i][size - 1] = '-';
            map[0][i] = '-';
            map[size - 1][i] = '-';
        }

        return new Map(map, loc);

    }

    public static void wipeMaps() {

        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.delete()) {
                    System.out.println("File '" + child + "' deleted successfully");
                } else {
                    System.out.println("Failed to delete the file: " + child);
                }
            }
        }
    }

    public static char[][] buildWorldMap() {
        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();
        String[] parts;

        int[] bounds = getBounds();

        char[][] worldMap = new char[(Math.abs(bounds[2]) + bounds[3]) * 2 + 3][(Math.abs(bounds[0]) + bounds[1]) * 2 + 3];
        for (char[] chars : worldMap) {
            Arrays.fill(chars, ' ');
        }
        Map edgeCheck;

        if (directoryListing != null) {
            for (File aDirectoryListing : directoryListing) {
                parts = aDirectoryListing.toString().substring(mapDir.length()).split("_");
                worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2) + 1][(Integer.parseInt(parts[0]) - bounds[0]) * 2 + 1] = getRoomCode(aDirectoryListing);
                if (worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2) + 1][(Integer.parseInt(parts[0]) - bounds[0]) * 2 + 1] != 'h') {
                    edgeCheck = new Map(aDirectoryListing.toString().substring(mapDir.length()));
                    if (edgeCheck.getOffset(N) != -1) {
                        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2)][(Integer.parseInt(parts[0]) - bounds[0]) * 2 + 1] = '|';
                    }
                    if (edgeCheck.getOffset(S) != -1) {
                        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2) + 2][(Integer.parseInt(parts[0]) - bounds[0]) * 2 + 1] = '|';
                    }
                    if (edgeCheck.getOffset(E) != -1) {
                        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2) + 1][(Integer.parseInt(parts[0]) - bounds[0]) * 2 + 2] = '-';
                    }
                    if (edgeCheck.getOffset(W) != -1) {
                        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2) + 1][(Integer.parseInt(parts[0]) - bounds[0]) * 2] = '-';
                    }
                }
            }
        }
        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - (-bounds[2] * 2) + 1][-bounds[0] * 2 + 1] = 's';
        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Player.loc.getY() - bounds[2]) * 2) + 1][(Player.loc.getX() - bounds[0]) * 2 + 1] = 'p';

        return worldMap;

    }

    private static char getRoomCode(File aDirectoryListing) {

        ArrayList<RoomType> types = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(aDirectoryListing))) {

            for (char s : br.readLine().toCharArray()) {
                types.add(Map.getTypeFromSymbol(s));
            }

        } catch (IOException e) {
            System.err.println("Missing map file: " + aDirectoryListing);
            exit(2);
        }

        if (types.contains(RoomType.Hidden)) {
            return 'h';
        }
        if (types.contains(RoomType.Start)) {
            return 's';
        }
        if (types.contains(RoomType.Treasure)) {
            return 't';
        }

        return 'x';
    }

    private static int[] getBounds() {

        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();

        int[] bounds = {0, 0, 0, 0};
        String[] parts;

        if (directoryListing != null) {
            for (File aDirectoryListing : directoryListing) {
                parts = aDirectoryListing.toString().substring(mapDir.length()).split("_");
                bounds[0] = Math.min(bounds[0], Integer.parseInt(parts[0]));
                bounds[1] = Math.max(bounds[1], Integer.parseInt(parts[0]));
                bounds[2] = Math.min(bounds[2], Integer.parseInt(parts[1]));
                bounds[3] = Math.max(bounds[3], Integer.parseInt(parts[1]));
            }
        }

        return bounds;
    }

    public static void makeMapDir() {
        File directory = new File(mapDir);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                System.err.println("Could not create " + mapDir + "directory.");
                exit(5);
            }
        }
    }

    public static String getFileName(int x, int y) {
        return x + "_" + y;
    }
}
