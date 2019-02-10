package entities;

import attribs.Location;
import core.BlockRefs;
import core.Direction;
import core.RoomType;
import maps.Map;
import maps.MapGenerator;
import paths.PathFinding;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static core.Direction.*;
import static java.lang.System.exit;
import static maps.Map.getSymbolFromType;

public final class Player {
    private static Location pos;
    public static Location loc = new Location(0, 0);

    public Player(int x, int y) {
        pos = new Location(x, y);
    }


    public static Location getPos() {
        return pos;
    }

    public static void checkDeath(Map map) {
        if (map.getBlock(pos.getX(), pos.getY()) == 'e') {
            System.out.println("You got caught by a monster!!!");
            exit(0);
        }
    }

    public Map move(Map map, Direction move) {


        if (pos.getY() == 0 && move == N) {
            pos.setY(BlockRefs.size - 1);
            return map.getAdj(S);
        }
        if (pos.getY() == BlockRefs.size - 1 && move == S) {
            pos.setY(0);
            return map.getAdj(N);
        }
        if (pos.getX() == BlockRefs.size - 1 && move == E) {
            pos.setX(0);
            return map.getAdj(W);
        }
        if (pos.getX() == 0 && move == W) {
            pos.setX(BlockRefs.size - 1);
            return map.getAdj(E);
        }


        if (!BlockRefs.worldBlocks.contains(String.valueOf(map.getBlock(pos, move)))) {
            if (map.getBlock(pos, move) == 'c') {
                map.removeBlock(pos, move);
                map.removeType(RoomType.Treasure);
                map.writeMap(MapGenerator.getFileName(map.getLocX(), map.getLocY()));

            }
            pos.move(move);
        }

        return map;
    }


}