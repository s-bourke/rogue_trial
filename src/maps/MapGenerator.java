package maps;

import attribs.Location;
import core.BlockRefs;
import core.Direction;

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
        System.out.println("Checking: " + N);
        if (f.exists()) {
            nMap.addExit(N, getExit(StandardMap.getFileName(loc.getX(), loc.getY() + 1), S));
        } else if (Math.random() < 0.5) {
            nMap.addExit(N, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX(), loc.getY() - 1));
        System.out.println("Checking: " + S);
        if (f.exists()) {
            nMap.addExit(S, getExit(StandardMap.getFileName(loc.getX(), loc.getY() - 1), N));
        } else if (Math.random() < 0.5) {
            nMap.addExit(S, (BlockRefs.xSize / 4) + (int) (Math.random() * (BlockRefs.xSize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX() + 1, loc.getY()));
        System.out.println("Checking: " + E);
        if (f.exists()) {
            nMap.addExit(E, getExit(StandardMap.getFileName(loc.getX() + 1, loc.getY()), W));
        } else if (Math.random() < 0.5) {
            nMap.addExit(E, (BlockRefs.ySize / 4) + (int) (Math.random() * (BlockRefs.ySize / 2)));
        }

        f = new File(mapDir + StandardMap.getFileName(loc.getX() - 1, loc.getY()));
        System.out.println("Checking: " + W);
        if (f.exists()) {
            nMap.addExit(W, getExit(StandardMap.getFileName(loc.getX() - 1, loc.getY()), E));
        } else if (Math.random() < 0.5) {
            nMap.addExit(W, (BlockRefs.ySize / 4) + (int) (Math.random() * (BlockRefs.ySize / 2)));
        }

        nMap.writeMap(StandardMap.getFileName(loc.getX(), loc.getY()));
        return nMap;
    }

    private static int getExit(String f, Direction dir) {
        System.out.println(new StandardMap(f).getOffset(dir));
        System.out.println(dir);
        System.out.println(f);
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
        StringBuilder horizBar = new StringBuilder();


        horizBar.append("/");
        for (int i = 0; i < BlockRefs.xSize - 2; i++) {
            horizBar.append("-");
        }
        horizBar.append("\\");
        mapLines.add(horizBar.toString());

        for (int i = 0; i < BlockRefs.ySize - 2; i++) {
            horizBar = new StringBuilder();
            horizBar.append("|");
            for (int j = 0; j < BlockRefs.xSize - 2; j++) {
                horizBar.append(" ");
            }
            horizBar.append("|");
            mapLines.add(horizBar.toString());
        }
        horizBar = new StringBuilder();


        horizBar.append("\\");
        for (int i = 0; i < BlockRefs.xSize - 2; i++) {
            horizBar.append("-");
        }
        horizBar.append("/");
        mapLines.add(horizBar.toString());


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

    public static void displayWorldMap() {
        File dir = new File(mapDir);
        File[] directoryListing = dir.listFiles();
        int xMin = 0;
        int xMax = 0;
        int yMin = 0;
        int yMax = 0;
        String[] parts;

        if (directoryListing != null) {
            for (File aDirectoryListing : directoryListing) {
                parts = aDirectoryListing.toString().substring(mapDir.length(), aDirectoryListing.toString().length()).split("_");
                xMin = Math.min(xMin, Integer.parseInt(parts[0]));
                xMax = Math.max(xMax, Integer.parseInt(parts[0]));
                yMin = Math.min(yMin, Integer.parseInt(parts[1]));
                yMax = Math.max(yMax, Integer.parseInt(parts[1]));
            }
        }


        char[][] worldMap = new char[(Math.abs(yMin) + yMax) * 2 + 3][(Math.abs(xMin) + xMax) * 2 + 3];
        for (char[] chars : worldMap) {
            Arrays.fill(chars,' ');
        }

    StandardMap edgeCheck;

        if (directoryListing != null) {
            for (File aDirectoryListing : directoryListing) {
                parts = aDirectoryListing.toString().substring(mapDir.length(), aDirectoryListing.toString().length()).split("_");
                worldMap[((Math.abs(yMin) + yMax) * 2) - ((Integer.parseInt(parts[1])-yMin) * 2) + 1][(Integer.parseInt(parts[0]) - xMin) * 2 + 1] = 'x';
                edgeCheck = new StandardMap(aDirectoryListing.toString().substring(mapDir.length(), aDirectoryListing.toString().length()));
                if (edgeCheck.getOffset(N) != -1){
                    worldMap[((Math.abs(yMin) + yMax) * 2) - ((Integer.parseInt(parts[1])-yMin) * 2)][(Integer.parseInt(parts[0]) - xMin) * 2 + 1] = '|';
                }
                if (edgeCheck.getOffset(S) != -1){
                    worldMap[((Math.abs(yMin) + yMax) * 2) - ((Integer.parseInt(parts[1])-yMin) * 2)+2][(Integer.parseInt(parts[0]) - xMin) * 2 + 1] = '|';
                }
                if (edgeCheck.getOffset(E) != -1){
                    worldMap[((Math.abs(yMin) + yMax) * 2) - ((Integer.parseInt(parts[1])-yMin) * 2)+1][(Integer.parseInt(parts[0]) - xMin) * 2 + 2] = '-';
                }
                if (edgeCheck.getOffset(W) != -1){
                    worldMap[((Math.abs(yMin) + yMax) * 2) - ((Integer.parseInt(parts[1])-yMin) * 2)+1][(Integer.parseInt(parts[0]) - xMin) * 2] = '-';
                }
            }
        }
        StringBuilder line;
        for (char[] chars : worldMap) {
            line = new StringBuilder();
            for (char aChar : chars) {
                line.append(aChar);
            }
            System.out.println(line);
        }
    }
}
