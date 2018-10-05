package core;

import attribs.Position;
import entities.Player;
import maps.StandardMap;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;


public class RogueTrial {

    private static final int[] XMOVE = {-1,1,0,0};
    private static final int[] YMOVE = {0,0,1,-1};

    public static void main(String[] args) throws InterruptedException {

        String mapDir = "preGenMaps";
        String mapLoc = mapDir + "/Test1.map";
        Player player = new Player(5,5,50);

        StandardMap testMap;

        testMap = new StandardMap(mapLoc);

        int[] move = {0,0};
        int moveRand;
        for (int i = 0; i < 30; i++) {
            testMap.displayMap(player.getPos());
            TimeUnit.SECONDS.sleep(1);
            moveRand = (int)(Math.random()*3);
            move[0] = XMOVE[moveRand];
            move[1] = YMOVE[moveRand];
            player.move(move[0],move[1], testMap.getBlock(player.getPos(),move));
        }

        exit(0);

    }
}
