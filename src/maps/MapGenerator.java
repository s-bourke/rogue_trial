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

public final class MapGenerator {

    public static StandardMap genMap(Location loc) {

        StandardMap nMap = genBlankMap(loc);

        File f;

        f = new File(mapDir + StandardMap.getFileName(loc.getX(), loc.getY() + 1));
        if (f.exists()) {
            nMap.addExit(N, getExit(StandardMap.getFileName(loc.getX(), loc.getY() + 1), S));
        } else if (Math.random() < 0.65) {
            nMap.addExit(N, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX(), loc.getY() - 1));
        if (f.exists()) {
            nMap.addExit(S, getExit(StandardMap.getFileName(loc.getX(), loc.getY() - 1), N));
        } else if (Math.random() < 0.65) {
            nMap.addExit(S, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX() + 1, loc.getY()));
        if (f.exists()) {
            nMap.addExit(E, getExit(StandardMap.getFileName(loc.getX() + 1, loc.getY()), W));
        } else if (Math.random() < 0.65) {
            nMap.addExit(E, (BlockRefs.ySize / 4) + (int) (Math.random() * (BlockRefs.ySize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX() - 1, loc.getY()));
        if (f.exists()) {
            nMap.addExit(W, getExit(StandardMap.getFileName(loc.getX() - 1, loc.getY()), E));
        } else if (Math.random() < 0.65) {
            nMap.addExit(W, (BlockRefs.ySize / 4) + (int) (Math.random() * (BlockRefs.ySize / 2)));
        }

        for (int i = 2; i < BlockRefs.ySize - 2; i++) {
            for (int j = 2; j < BlockRefs.xSize - 2; j++) {
                if (Math.random() < 0.3) {
                    nMap.addBlock(i, j, '@');
                }
            }

        }

        nMap.writeMap(StandardMap.getFileName(loc.getX(), loc.getY()));
        return nMap;
    }

    public static StandardMap genBlockedMap(Location loc) {

        File[] files = new File[4];
        files[0] = new File(mapDir + StandardMap.getFileName(loc.getX(), loc.getY() + 1));
        files[1] = new File(mapDir + StandardMap.getFileName(loc.getX(), loc.getY() - 1));
        files[2] = new File(mapDir + StandardMap.getFileName(loc.getX() + 1, loc.getY()));
        files[3] = new File(mapDir + StandardMap.getFileName(loc.getX() - 1, loc.getY()));


        int[] isAdj = new int[4];

        for (int i = 0; i < 4; i++) {
            if (files[i].exists()) {
                isAdj[i] = 1;
            }
        }
        if (IntStream.of(isAdj).sum() != 1) {
            return genMap(loc);
        }

        while (IntStream.of(isAdj).sum() == 1) {
            for (int i = 0; i < 4; i++) {
                if (Math.random() < 0.25) {
                    isAdj[i] = 1;
                }
            }
        }

        StandardMap nMap;
        nMap = genBlankMap(loc);

        int[] offset = new int[4];
        if (isAdj[0] == 1) {
            if (files[0].exists()) {
                offset[0] = getExit(StandardMap.getFileName(loc.getX(), loc.getY() + 1), S);
                nMap.addExit(N, offset[0]);
            } else {
                offset[0] = (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2));
                nMap.addExit(N, offset[0]);
            }
        }
        if (isAdj[1] == 1) {
            if (files[1].exists()) {
                offset[1] = getExit(StandardMap.getFileName(loc.getX(), loc.getY() - 1), N);
                nMap.addExit(S, offset[1]);
            }else{
                offset[1] = (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2));

                nMap.addExit(S, offset[1]);
            }
        }
        if (isAdj[2] == 1) {
            if (files[2].exists()) {
                offset[2] = getExit(StandardMap.getFileName(loc.getX() + 1, loc.getY()), W);
                nMap.addExit(E, offset[2]);
            }else{
                offset[2] = (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2));

                nMap.addExit(E, offset[2]);
            }
        }
        if (isAdj[3] == 1) {
            if (files[3].exists()) {
                offset[3] = getExit(StandardMap.getFileName(loc.getX() - 1, loc.getY()), E);
                nMap.addExit(W, offset[3]);
            }else{
                offset[3] = (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2));
                nMap.addExit(W, offset[3]);
            }
        }

        int dir;
        do {
            dir = (int) (Math.random() * 4);
        } while (isAdj[dir] != 1);
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
                nMap.addBlock(offset[1] - 2, BlockRefs.ySize - 2, '-');
                nMap.addBlock(offset[1] - 1, BlockRefs.ySize - 3, '-');
                nMap.addBlock(offset[1], BlockRefs.ySize - 3, '-');
                nMap.addBlock(offset[1] + 1, BlockRefs.ySize - 3, '-');
                nMap.addBlock(offset[1] + 2, BlockRefs.ySize - 2, '-');
                nMap.addBlock(offset[1], BlockRefs.ySize - 2, 'c');

                break;
            case 2:
                nMap.addBlock(BlockRefs.xSize - 2, offset[2] - 2, '-');
                nMap.addBlock(BlockRefs.xSize - 3, offset[2] - 1, '-');
                nMap.addBlock(BlockRefs.xSize - 3, offset[2], '-');
                nMap.addBlock(BlockRefs.xSize - 3, offset[2] + 1, '-');
                nMap.addBlock(BlockRefs.xSize - 2, offset[2] + 2, '-');
                nMap.addBlock(BlockRefs.xSize - 2, offset[2], 'c');
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

        nMap.writeMap(StandardMap.getFileName(loc.getX(), loc.getY()));
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

        nMap.writeMap(StandardMap.getFileName(0, 0));

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
                parts = aDirectoryListing.toString().substring(mapDir.length(), aDirectoryListing.toString().length()).split("_");
                worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Integer.parseInt(parts[1]) - bounds[2]) * 2) + 1][(Integer.parseInt(parts[0]) - bounds[0]) * 2 + 1] = 'x';
                edgeCheck = new StandardMap(aDirectoryListing.toString().substring(mapDir.length(), aDirectoryListing.toString().length()));
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

    public static int[] getBounds() {

        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();

        int[] bounds = {0, 0, 0, 0};
        String[] parts;

        if (directoryListing != null) {
            for (File aDirectoryListing : directoryListing) {
                parts = aDirectoryListing.toString().substring(mapDir.length(), aDirectoryListing.toString().length()).split("_");
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
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
    }
}
