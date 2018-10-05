package entities;

import attribs.Health;
import attribs.Position;
import core.worldRefs;

public class Player {
    private Position pos;
    private Health hp;
    public Player(Position pos, int hp) {
        this.pos = pos;
        this.hp = new Health(hp);
    }

    public Player(int x, int y, int hp) {
        this.hp = new Health(hp);
        this.pos = new Position(x, y);
    }

    public Position getPos() {
        return pos;
    }

    public void move(int x, int y, String target){
        System.err.println(target);
        if (!worldRefs.getWorldBlocks().contains(target)) {
            pos.move(x,y);
        }
    }
}
