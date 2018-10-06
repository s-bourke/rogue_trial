package core;

import entities.Player;
import maps.MapGenerator;
import maps.StandardMap;


public class RogueTrial {

    public static void main(String[] args) {

        Player player = new Player(BlockRefs.xSize/2, BlockRefs.ySize/2, 50, 0,0);

        MapGenerator.wipeMaps();

        StandardMap currentMap;
        currentMap = MapGenerator.genStartingMap();

        Direction move;

        while (true) {
            currentMap.displayMap(player.getPos());
            move = UserInput.getMove();
            if (move != null) {
                currentMap = player.move(currentMap, move);
            }
        }

    }

}

