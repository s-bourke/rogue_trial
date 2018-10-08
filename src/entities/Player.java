package entities;

import attribs.Location;
import attribs.Position;
import core.BlockRefs;
import core.Direction;
import maps.StandardMap;

import static core.Direction.*;

public final class Player {
    private static Position pos;
    public static Location loc = new Location(0,0);

    public Player(int x, int y) {
        pos = new Position(x, y);
    }


    public static Position getPos() {
        return pos;
    }

    public StandardMap move(StandardMap map, Direction move) {


        if (pos.getY() == 0){
            if (move == N){
                pos.setY(BlockRefs.ySize-1);
                return map.getAdj(S);
            }
        }
        else if (pos.getY() == BlockRefs.ySize-1){
            if (move == S){
                pos.setY(0);
                return map.getAdj(N);
            }
        }
        else if (pos.getX() == 0){
            if (move == W){
                pos.setX(BlockRefs.xSize-1);
                return map.getAdj(E);
            }
        }
        else if (pos.getX() == BlockRefs.xSize-1){
            if (move == E){
                pos.setX(0);
                return map.getAdj(W);
            }
        }
        if (!BlockRefs.worldBlocks.contains(map.getBlock(pos, move))) {
            pos.move(move);
        }
        return map;
    }


}