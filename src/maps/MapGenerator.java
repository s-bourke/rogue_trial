package maps;

import attribs.Location;
import core.BlockRefs;
import core.Direction;
import entities.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static core.BlockRefs.mapDir;
import static core.Direction.*;
import static java.lang.System.exit;

public final class MapGenerator {

    public static StandardMap genMap(Location loc) {

        StandardMap nMap = genBlankMap(loc);
        File f;

        Direction[] dirs = {N, E, S, W};
        int[] dirOffset = {0, 1, 0, -1};
        int yIndex;
        for (int i = 0; i < 4; i++) {
            yIndex = (i + 1) % 4;
            f = new File(mapDir + getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]));
            if (f.exists()) {
                nMap.addExit(dirs[i], getExit(getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]), dirs[(i + 2) % 4]));
            } else if (Math.random() < 0.65) {
                nMap.addExit(dirs[i], (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
            }
        }

        for (int i = 2; i < BlockRefs.ySize - 2; i++) {
            for (int j = 2; j < BlockRefs.xSize - 2; j++) {
                if (Math.random() < 0.3) {
                    nMap.addBlock(i, j, '@');
                }
            }

        }

        nMap.writeMap(getFileName(loc.getX(), loc.getY()));
        return nMap;
    }

    public static StandardMap genBlockedMap(Location loc, Direction arrivalDir) {

        StandardMap nMap = genBlankMap(loc);
        File[] files = new File[4];
        Direction[] dirs = {N, E, S, W};
        int[] isAdj = {0, 0, 0, 0};
        int[] isAdjExit = {0, 0, 0, 0};
        int[] dirOffset = {0, 1, 0, -1};
        int yIndex;

        // Check if surrounding rooms exist.
        System.out.println(" ");
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
            return genMap(loc);
        }

        // Make sure there are at least two exits.
        while (IntStream.of(isAdj).sum() == 1) {
            for (int i = 0; i < 4; i++) {
                if (Math.random() < 0.25) {
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
                    nMap.addExit(dirs[i], getExit(getFileName(loc.getX() + dirOffset[i], loc.getY() + dirOffset[yIndex]), dirs[(i + 2) % 4]));
                } else {
                    offset[i] = (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2));
                    nMap.addExit(dirs[i], offset[i]);
                }
            }
        }

        int dir;
        do {
            dir = (int) (Math.random() * 4);
        } while (isAdj[dir] != 1 || dirs[dir] == arrivalDir);

        switch (dir) {
            case 0:
                nMap.addBlock(offset[0] - 2, 1, '-');
                nMap.addBlock(offset[0] - 1, 2, '-');
                nMap.addBlock(offset[0], 2, '-');
                nMap.addBlock(offset[0] + 1, 2, '-');
                nMap.addBlock(offset[0] + 2, 1, '-');
                nMap.addBlock(offset[0], 1, 'c');

                break;
            case 1:
                nMap.addBlock(BlockRefs.xSize - 2, offset[1] - 2, '-');
                nMap.addBlock(BlockRefs.xSize - 3, offset[1] - 1, '-');
                nMap.addBlock(BlockRefs.xSize - 3, offset[1], '-');
                nMap.addBlock(BlockRefs.xSize - 3, offset[1] + 1, '-');
                nMap.addBlock(BlockRefs.xSize - 2, offset[1] + 2, '-');
                nMap.addBlock(BlockRefs.xSize - 2, offset[1], 'c');
                break;
            case 2:
                nMap.addBlock(offset[2] - 2, BlockRefs.ySize - 2, '-');
                nMap.addBlock(offset[2] - 1, BlockRefs.ySize - 3, '-');
                nMap.addBlock(offset[2], BlockRefs.ySize - 3, '-');
                nMap.addBlock(offset[2] + 1, BlockRefs.ySize - 3, '-');
                nMap.addBlock(offset[2] + 2, BlockRefs.ySize - 2, '-');
                nMap.addBlock(offset[2], BlockRefs.ySize - 2, 'c');

                break;
            case 3:
                nMap.addBlock(1, offset[3] - 2, '-');
                nMap.addBlock(2, offset[3] - 1, '-');
                nMap.addBlock(2, offset[3], '-');
                nMap.addBlock(2, offset[3] + 1, '-');
                nMap.addBlock(1, offset[3] + 2, '-');
                nMap.addBlock(1, offset[3], 'c');
                break;

        }

        nMap.writeMap(getFileName(loc.getX(), loc.getY()));
        return nMap;
    }

    private static int getExit(String f, Direction dir) {
        return new StandardMap(f).getOffset(dir);
    }

    public static StandardMap genStartingMap() {
        StandardMap nMap = genBlankMap(new Location(0, 0));

        nMap.addExit(N, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        nMap.addExit(S, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        nMap.addExit(E, (BlockRefs.ySize / 4) + (int) (Math.random() * (BlockRefs.ySize / 2)));
        nMap.addExit(W, (BlockRefs.ySize / 4) + (int) (Math.random() * (BlockRefs.ySize / 2)));

        nMap.writeMap(getFileName(0, 0));

        return nMap;
    }

    private static StandardMap genBlankMap(Location loc) {

        ArrayList<String> mapLines = new ArrayList<String>();
        StringBuilder hBar = new StringBuilder();


        hBar.append("/");
        for (int i = 0; i < BlockRefs.xSize - 2; i++) {
            hBar.append("-");
        }
        hBar.append("\\");
        mapLines.add(hBar.toString());

        for (int i = 0; i < BlockRefs.ySize - 2; i++) {
            hBar = new StringBuilder();
            hBar.append("|");
            for (int j = 0; j < BlockRefs.xSize - 2; j++) {
                hBar.append(" ");
            }
            hBar.append("|");
            mapLines.add(hBar.toString());
        }
        hBar = new StringBuilder();


        hBar.append("\\");
        for (int i = 0; i < BlockRefs.xSize - 2; i++) {
            hBar.append("-");
        }
        hBar.append("/");
        mapLines.add(hBar.toString());


        return new StandardMap(mapLines, loc);

    }

    public static void wipeMaps() {

        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.delete()) {
                    System.out.println("File '" + child + "' deleted successfully.");
                } else {
                    System.out.println("Failed to delete the file.");
                }
            }
        }
    }

    public static char[][] displayWorldMap() {
        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();
        String[] parts;

        int[] bounds = getBounds();

        char[][] worldMap = new char[(Math.abs(bounds[2]) + bounds[3]) * 2 + 3][(Math.abs(bounds[0]) + bounds[1]) * 2 + 3];
        for (char[] chars : worldMap) {
            Arrays.fill(chars, ' ');
        }
        StandardMap edgeCheck;

        if (directoryListing != null) {
            for (File aDirectoryListing : directoryListing) {
                parts = aDirectoryListing.toString().substring(mapDir.length()).split("_");
                worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2) + 1][(Integer.parseInt(parts[0]) - bounds[0]) * 2 + 1] = 'x';
                edgeCheck = new StandardMap(aDirectoryListing.toString().substring(mapDir.length()));
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
        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - (-bounds[2] * 2) + 1][-bounds[0] * 2 + 1] = 's';
        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Player.loc.getY() - bounds[2]) * 2) + 1][(Player.loc.getX() - bounds[0]) * 2 + 1] = 'p';

        return worldMap;

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
