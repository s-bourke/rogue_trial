package paths;

import attribs.Location;
import maps.Map;

import java.util.ArrayList;

public final class PathFinding {

    public static Location getPath(Map map, Location start, Location end) {

        PathGraph nodeList = new PathGraph();

        nodeList.poulateNodes(map);
        nodeList.populateEdges();

        return nodeList.findPath(start, end);

    }

}
