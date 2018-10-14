package entities;

import attribs.Location;
import attribs.Position;
import core.BlockRefs;
import core.Direction;
import core.RoomType;
import maps.Map;

import static core.Direction.*;
import static maps.Map.getSymbolFromType;

public final class Player {
    private static Position pos;
    public static Location loc = new Location(0, 0);

    public Player(int x, int y) {
        pos = new Position(x, y);
    }


    public static Position getPos() {
        return pos;
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
            if (map.getBlock(pos, move) == getSymbolFromType(RoomType.Treasure)) {
                map.removeBlock(pos, move);
            }
            pos.move(move);
        }
        return map;
    }


}