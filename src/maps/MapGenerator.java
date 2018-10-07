package maps;

import attribs.Location;
import core.BlockRefs;
import core.Direction;
import entities.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static core.BlockRefs.mapDir;
import static core.Direction.*;

public final class MapGenerator {

    public static StandardMap genMap(Location loc) {

        StandardMap nMap = genBlankMap(loc);

        File f;

        f = new File(mapDir + StandardMap.getFileName(loc.getX(), loc.getY() + 1));
        if (f.exists()) {
            nMap.addExit(N, getExit(StandardMap.getFileName(loc.getX(), loc.getY() + 1), S));
        } else if (Math.random() < 0.5) {
            nMap.addExit(N, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX(), loc.getY() - 1));
        if (f.exists()) {
            nMap.addExit(S, getExit(StandardMap.getFileName(loc.getX(), loc.getY() - 1), N));
        } else if (Math.random() < 0.5) {
            nMap.addExit(S, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX() + 1, loc.getY()));
        if (f.exists()) {
            nMap.addExit(E, getExit(StandardMap.getFileName(loc.getX() + 1, loc.getY()), W));
        } else if (Math.random() < 0.5) {
            nMap.addExit(E, (BlockRefs.ySize / 4) + (int) (Math.random() * (BlockRefs.ySize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX() - 1, loc.getY()));
        if (f.exists()) {
            nMap.addExit(W, getExit(StandardMap.getFileName(loc.getX() - 1, loc.getY()), E));
        } else if (Math.random() < 0.5) {
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
                    System.out.println("File deleted successfully");
                } else {
                    System.out.println("Failed to delete the file");
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
        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - (-bounds[2] * 2) + 1][ - bounds[0] * 2 + 1] = 's';
        worldMap[((Math.abs(bounds[2]) + bounds[3]) * 2) - ((Player.loc.getY() - bounds[2]) * 2) + 1][(Player.loc.getX() - bounds[0]) * 2 + 1] = 'p';

        return worldMap;

    }

    public static int[] getBounds(){

        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();

        int[] bounds = {0,0,0,0};
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
}
