package com.daa.optimizeRideshare.graph;

import com.daa.optimizeRideshare.data.BayWheelsClean;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreateGraph {

    @Autowired
    YensAlgorithm yensAlgorithm;

    Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsRideMap;
    List<GraphPath<BayWheelsNode, DefaultWeightedEdge>> kShortestpaths;

    //    Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph;
    GraphPath<BayWheelsNode, DefaultWeightedEdge> overallShortestPath;

    public Graph<BayWheelsNode, DefaultWeightedEdge> createGraphFromData(List<BayWheelsClean> data) {

        bayWheelsRideMap = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (BayWheelsClean entry : data) {
            if (entry.getTotal_time() < 0) continue;

            BayWheelsNode node1 = new BayWheelsNode();
            node1.setStation_id(entry.getStart_station_id());
            node1.setStation_name(entry.getStart_station_name());
            node1.setStation_latitude(entry.getStart_lat());
            node1.setStation_longitude(entry.getStart_lng());
            bayWheelsRideMap.addVertex(node1);

            BayWheelsNode node2 = new BayWheelsNode();
            node2.setStation_id(entry.getEnd_station_id());
            node2.setStation_name(entry.getEnd_station_name());
            node2.setStation_latitude(entry.getEnd_lat());
            node2.setStation_longitude(entry.getEnd_lng());
            bayWheelsRideMap.addVertex(node2);

            DefaultWeightedEdge edge = bayWheelsRideMap.addEdge(node1, node2);
            if (edge != null) {
                bayWheelsRideMap.setEdgeWeight(edge, entry.getTotal_time());
            }

        }

        return bayWheelsRideMap;
    }

    public Graph<BayWheelsNode, DefaultWeightedEdge> getBayWheelsRideGraph() {
        return bayWheelsRideMap;
    }

    public Set<GraphPath<BayWheelsNode, DefaultWeightedEdge>> getKShortestPathsBetween(BayWheelsNode source, BayWheelsNode sink, int k) {
        return yensAlgorithm.getKShortestPaths(bayWheelsRideMap, source, sink, k);
    }

    public List<GraphPath<BayWheelsNode, DefaultWeightedEdge>> getKShortestPaths(BayWheelsNode source, BayWheelsNode sink, int k) {
        YenKShortestPath<BayWheelsNode, DefaultWeightedEdge> yens = new YenKShortestPath<>(bayWheelsRideMap);
        kShortestpaths = yens.getPaths(source, sink, k);
        return kShortestpaths;
    }

    public GraphPath<BayWheelsNode, DefaultWeightedEdge> getOverallShortestPath(Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph, BayWheelsNode source, BayWheelsNode sink) {

        Set<BayWheelsNode> visited = new HashSet<>();
        Map<BayWheelsNode, GraphPath<BayWheelsNode, DefaultWeightedEdge>> cacheMap = new HashMap<>();
        visited.add(source);
        return dp(kShortestPathsGraph, source, sink, visited, cacheMap);

    }

    private GraphPath<BayWheelsNode, DefaultWeightedEdge> dp(Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph, BayWheelsNode source, BayWheelsNode sink, Set<BayWheelsNode> visited, Map<BayWheelsNode, GraphPath<BayWheelsNode, DefaultWeightedEdge>> cacheMap) {
//        if (kShortestPathsGraph.containsEdge(source, sink)) {
//            return createGraphPath(kShortestPathsGraph, source, sink);
//        }
        if(source.equals(sink)){
            return new GraphWalk<>(kShortestPathsGraph, source, sink, new ArrayList<>(), 0);
        }
        if (cacheMap.containsKey(source)) return cacheMap.get(source);
        Set<DefaultWeightedEdge> outgoingEdges = kShortestPathsGraph.outgoingEdgesOf(source);
        GraphPath<BayWheelsNode, DefaultWeightedEdge> minPath = null;
        for (DefaultWeightedEdge outgoingEdge : outgoingEdges) {
            BayWheelsNode target = kShortestPathsGraph.getEdgeTarget(outgoingEdge);
            GraphPath<BayWheelsNode, DefaultWeightedEdge> path2 = null;
            if(!visited.contains(target)){
                visited.add(target);
                path2 = dp(kShortestPathsGraph, target, sink, visited, cacheMap);
                visited.remove(target);
            }
            double totalWeight;
            if (path2 != null) {
                 totalWeight = getWeightForEdge(kShortestPathsGraph, outgoingEdge) + path2.getWeight();
                if (minPath == null || minPath.getWeight() > totalWeight) {
                    GraphPath<BayWheelsNode, DefaultWeightedEdge> path1 = createGraphPath(kShortestPathsGraph, source, target);
                    GraphPath<BayWheelsNode, DefaultWeightedEdge> combinedPath = appendPaths(kShortestPathsGraph, path1, path2, totalWeight);
                    minPath = combinedPath;
                }
            }
        }
        cacheMap.put(source, minPath);
        return minPath;
    }

    private GraphPath<BayWheelsNode, DefaultWeightedEdge> appendPaths(Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph, GraphPath<BayWheelsNode, DefaultWeightedEdge> path1, GraphPath<BayWheelsNode, DefaultWeightedEdge> path2, double totalWeight) {
        List<DefaultWeightedEdge> combinedEdges = new ArrayList<>();
        combinedEdges.addAll(path1.getEdgeList());
        combinedEdges.addAll(path2.getEdgeList());

        List<BayWheelsNode> combinedVertices = new ArrayList<>();
        combinedVertices.addAll(path1.getVertexList());

        // Avoid adding the duplicate vertex where path1 ends and path2 begins
        combinedVertices.addAll(path2.getVertexList().subList(1, path2.getVertexList().size()));
        GraphPath<BayWheelsNode, DefaultWeightedEdge> combinedPath =
                new GraphWalk<>(kShortestPathsGraph, path1.getStartVertex(), path2.getEndVertex(),
                        combinedVertices, combinedEdges, totalWeight);
        return combinedPath;

    }

    private GraphPath<BayWheelsNode, DefaultWeightedEdge> createGraphPath(Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph, BayWheelsNode source, BayWheelsNode sink) {
        List<DefaultWeightedEdge> edges = new ArrayList<>();
        DefaultWeightedEdge edge = kShortestPathsGraph.getEdge(source, sink);

        edges.add(edge);
        double weight = getWeightForEdge(kShortestPathsGraph, edge);
        return new GraphWalk<>(kShortestPathsGraph, source, sink, edges, weight);
    }

    private double getWeightForEdge(Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph, DefaultWeightedEdge edge) {
        double weight = 0;
        double edgeWeight = kShortestPathsGraph.getEdgeWeight(edge);
        if (edgeWeight > 100) {
            weight = 2 * ((edgeWeight - 100) / 60);
        }
        return weight;
    }

    public Graph<BayWheelsNode, DefaultWeightedEdge> createGraphFromPaths(List<GraphPath<BayWheelsNode, DefaultWeightedEdge>> kShortestPaths) {
        Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph =
                new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (GraphPath<BayWheelsNode, DefaultWeightedEdge> path : kShortestPaths) {
            // Add all nodes in the path
            for (BayWheelsNode node : path.getVertexList()) {
                if (!kShortestPathsGraph.containsVertex(node)) {
                    kShortestPathsGraph.addVertex(node);
                }
            }

            // Add all edges in the path
            for (DefaultWeightedEdge edge : path.getEdgeList()) {
                BayWheelsNode sourceNode = path.getGraph().getEdgeSource(edge);
                BayWheelsNode targetNode = path.getGraph().getEdgeTarget(edge);

                // Check if the edge is already added
                if (!kShortestPathsGraph.containsEdge(sourceNode, targetNode)) {
                    DefaultWeightedEdge newEdge = kShortestPathsGraph.addEdge(sourceNode, targetNode);

                    // Copying the weight of the edge from the original graph
                    double weight = path.getGraph().getEdgeWeight(edge);
                    kShortestPathsGraph.setEdgeWeight(newEdge, weight);
                }
            }
        }
        return kShortestPathsGraph;
    }

//    public void display(){
//        GraphExporter<BayWheelsNode, DefaultWeightedEdge> exporter = new DOTExporter<>();
//
//        try {
//            FileWriter writer = new FileWriter("C:\\Users\\naiko\\Downloads\\graph.dot");
//            exporter.exportGraph(bayWheelsRideMap, writer);
//        } catch (IOException | ExportException e) {
//            e.printStackTrace();
//        }
//
//    }

}
