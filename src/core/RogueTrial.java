package core;

import entities.Player;
import maps.StandardMap;

import java.util.concurrent.TimeUnit;


public class RogueTrial {

    public static void main(String[] args) throws InterruptedException {

        String mapDir = "preGenMaps";
        String mapLoc = mapDir + "/Test1.map";
        Player player = new Player(5, 5, 50);

        StandardMap testMap;

        testMap = new StandardMap(mapLoc);

        int[] move;

        while(true) {
            testMap.displayMap(player.getPos());
            TimeUnit.SECONDS.sleep(1);
            move = UserInput.getMove();
            player.move(move[0], move[1], testMap.getBlock(player.getPos(), move));
        }

    }
}
