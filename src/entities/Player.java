package entities;

import attribs.Health;
import attribs.Location;
import attribs.Position;
import core.BlockRefs;
import core.Direction;
import maps.StandardMap;

import static core.Direction.*;

public final class Player {
    private Position pos;
    private Health hp;
    public static Location loc = new Location(0,0);

    public Player(int x, int y, int hp) {
        this.hp = new Health(hp);
        this.pos = new Position(x, y);
    }


    public Position getPos() {
        return pos;
    }

    public StandardMap move(StandardMap map, Direction move) {


        if (pos.getY() == 0){
            if (move == N || move == NE || move == NW){
                pos.setY(BlockRefs.ySize-1);
                return map.getAdj(S);
            }
        }
        else if (pos.getY() == BlockRefs.ySize-1){
            if (move == S || move == SE || move == SW){
                pos.setY(0);
                return map.getAdj(N);
            }
        }
        else if (pos.getX() == 0){
            if (move == W || move == SW || move == NW){
                pos.setX(BlockRefs.xSize-1);
                return map.getAdj(E);
            }
        }
        else if (pos.getX() == BlockRefs.xSize-1){
            if (move == E || move == NE || move == SE){
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