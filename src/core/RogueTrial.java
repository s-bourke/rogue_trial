package core;

import attribs.Position;
import maps.StandardMap;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class RogueTrial {


    public static void main(String[] args) throws InterruptedException {

        String mapDir = "preGenMaps";
        String mapLoc = mapDir + "/Test1.map";
        StandardMap testMap = null;
        
        try {
            testMap = new StandardMap(mapLoc);
        } catch (FileNotFoundException e) {
            System.err.println("Missing map file: " + mapLoc);
            exit(1);
        }

        testMap.displayMap(new Position(2,3));
        TimeUnit.SECONDS.sleep(1);
        testMap.displayMap(new Position(2,4));
        TimeUnit.SECONDS.sleep(1);
        testMap.displayMap(new Position(2,5));
        TimeUnit.SECONDS.sleep(1);

        exit(0);

    }
}
