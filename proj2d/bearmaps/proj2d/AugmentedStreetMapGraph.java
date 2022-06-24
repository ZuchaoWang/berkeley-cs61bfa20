package bearmaps.proj2d;

import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;

import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    PointSet nodePosSet;
    HashMap<Point,Long> nodePosToId;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        
        List<Point> nodePosList = new ArrayList<Point>();
        nodePosToId = new HashMap<Point,Long>();
        for (Node node: this.getNodes()) {
            if (neighbors(node.id()).size() > 0) {
                Point pos = lonLatToPos(node.lon(), node.lat());
                nodePosList.add(pos);
                nodePosToId.put(pos, node.id());
            }
        }
        nodePosSet = new KDTree(nodePosList);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point targetPos = lonLatToPos(lon, lat),
            nearestPos = nodePosSet.nearest(targetPos.getX(), targetPos.getY());
        return nodePosToId.get(nearestPos);
    }

    private Point lonLatToPos(double lon, double lat) {
        // why this is wrong? maybe not accurate enough when assuming uniform scaling
        // double xPos = lon * Math.cos(lat * Math.PI / 180);
        // double yPos = lat;
        // should use Web Mercator projection: https://en.wikipedia.org/wiki/Web_Mercator_projection
        double xPos = Math.toRadians(lon) + Math.PI;
        double yPos = Math.PI - Math.log(Math.tan(Math.PI/4 + Math.toRadians(lat)/2));
        return new Point(xPos, yPos);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return new LinkedList<>();
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        return new LinkedList<>();
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
