package attribs;

import core.Direction;

import java.util.Objects;

public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
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

    public Location adjLocation(Direction dir) {
        switch (dir) {
            case N:
                return new Location(x, y + 1);
            case S:
                return new Location(x, y - 1);
            case E:
                return new Location(x + 1, y);
            case W:
                return new Location(x - 1, y);
        }
        return null;
    }

    public void move(Direction move) {
        switch (move) {
            case N:
                y -= 1;
                break;
            case S:
                y += 1;
                break;
            case E:
                x += 1;
                break;
            case W:
                x -= 1;
                break;
        }
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x &&
                y == location.y;
    }

}
