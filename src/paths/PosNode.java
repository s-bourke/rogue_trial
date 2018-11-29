package paths;

import attribs.Location;

import java.util.ArrayList;

public class PosNode {

    char type;
    boolean checked;
    int dist;
    int x;
    int y;
    ArrayList<PosNode> edges;
    PosNode path;

    public PosNode(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.checked = false;
        this.dist = 9999;
        this.edges = new ArrayList<>();
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ArrayList<PosNode> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<PosNode> edges) {
        this.edges = edges;
    }

    public PosNode getPath() {
        return path;
    }

    public void setPath(PosNode path) {
        this.path = path;
    }

    public void addEdge(PosNode node) {
        edges.add(node);
    }

    public Location getPos() {
        return new Location(x,y);
    }
    public void printEdges(){
        for (PosNode edge : edges) {
            System.out.println(edge.getPos());
        }
    }
}