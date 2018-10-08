package attribs;

import core.Direction;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(Direction move) {
        switch(move){
            case N:
                y-=1;
                break;
            case S:
                y+=1;
                break;
            case E:
                x+=1;
                break;
            case W:
                x-=1;
                break;
        }
    }

    @Override
    public String toString() {
        return "Location{" + x + ", " + y + '}';
    }
}
