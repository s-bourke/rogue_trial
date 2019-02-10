package paths;

import attribs.Location;
import core.BlockRefs;
import maps.Map;

import java.util.ArrayList;

public class PathGraph {
    private ArrayList<PosNode> nodeList;
    private ArrayList<PosNode> nextList;
    private ArrayList<PosNode> futureList;


    public PathGraph() {
        this.nodeList = new ArrayList<PosNode>();
        this.nextList = new ArrayList<PosNode>();
        this.futureList = new ArrayList<PosNode>();
    }

    public void poulateNodes(Map map) {
        for (int i = 0; i < BlockRefs.size; i++) {
            for (int j = 0; j < BlockRefs.size; j++) {
                nodeList.add(new PosNode(i, j, map.getBlock(i, j)));
            }
        }
    }

    public void populateEdges() {
        for (PosNode node : nodeList) {
            if (getNode(node.getX(), node.getY() + 1) != null && !BlockRefs.worldBlocks.contains(String.valueOf(getNode(node.getX(), node.getY() + 1).getType()))) {
                node.addEdge(getNode(node.getX(), node.getY() + 1));
            }
            if (getNode(node.getX(), node.getY() - 1) != null && !BlockRefs.worldBlocks.contains(String.valueOf(getNode(node.getX(), node.getY() - 1).getType()))) {
                node.addEdge(getNode(node.getX(), node.getY() - 1));
            }
            if (getNode(node.getX() + 1, node.getY()) != null && !BlockRefs.worldBlocks.contains(String.valueOf(getNode(node.getX() + 1, node.getY()).getType()))) {
                node.addEdge(getNode(node.getX() + 1, node.getY()));
            }
            if (getNode(node.getX() - 1, node.getY()) != null && !BlockRefs.worldBlocks.contains(String.valueOf(getNode(node.getX() - 1, node.getY()).getType()))) {
                node.addEdge(getNode(node.getX() - 1, node.getY()));
            }
        }
    }


    private PosNode getNode(int x, int y) {
        for (PosNode node : nodeList) {
            if (node.getX() == x && node.getY() == y) {
                return node;
            }
        }
        return null;
    }

    private PosNode getNode(Location loc) {
        int x = loc.getX();
        int y = loc.getY();
        for (PosNode node : nodeList) {
            if (node.getX() == x && node.getY() == y) {
                return node;
            }
        }
        return null;
    }


    public Location findPath(Location start, Location end) {

        for (PosNode node : nodeList) {

            if (node.getPos().equals(start)) {
                node.setDist(0);
                nextList.add(node);
                findNextPath(node, end);
                break;
            }
        }

        ArrayList<Location> path = new ArrayList<Location>();
        PosNode currentNode = getNode(end);
        while (!currentNode.getPos().equals(start)) {
            path.add(currentNode.getPos());
            currentNode = currentNode.getPath();
        }
        return path.get(path.size() - 1);
    }

    private void findNextPath(PosNode currentNode, Location end) {

        currentNode.setChecked(true);


        if (currentNode.getPos().equals(end)) {
            return;
        }

        nextList.remove(currentNode);

        for (PosNode node : currentNode.getEdges()) {
            if (!node.isChecked()) {
                if (node.getDist() > currentNode.getDist() + 1) {
                    node.setDist(currentNode.getDist() + 1);
                    node.setPath(currentNode);
                    futureList.add(node);
                }
            }
        }

        if (nextList.size() == 0) {
            nextList = futureList;
            futureList = new ArrayList<PosNode>();
        }

        findNextPath(nextList.get((int) (Math.random() * nextList.size())), end);

    }

}

