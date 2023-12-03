package com.daa.optimizeRideShare.graph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service Class to implement Yens Algorithm
 */
@Service
public class YensAlgorithm {

    /**
     * Service Method to get K-shortest Paths using Yens Algorithm. Uses a library implementation of Dijkstra's algorithm internally.
     * @param graph Input Graph
     * @param source Source Node
     * @param sink Destination Node
     * @param k Number of shortest paths required
     * @return a Set of K unique shortest paths
     */
    public Set<GraphPath<BayWheelsNode, DefaultWeightedEdge>> getKShortestPaths(Graph<BayWheelsNode, DefaultWeightedEdge> graph, BayWheelsNode source, BayWheelsNode sink, int k) {
        Set<GraphPath<BayWheelsNode, DefaultWeightedEdge>> paths = new HashSet<>();
        Set<GraphPath<BayWheelsNode, DefaultWeightedEdge>> candidatePaths = new HashSet<>();

        // Step 1: Get the shortest path and add it to the list of paths
        GraphPath<BayWheelsNode, DefaultWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(graph, source, sink);
        if (shortestPath != null) {
            paths.add(shortestPath);
        } else {
            return paths; // No path exists
        }

        GraphPath<BayWheelsNode, DefaultWeightedEdge> nextShortestPath = shortestPath;
        // Step 2: Iteratively find the next shortest paths
        for (int i = 1; i < k; i++) {
            // For each edge in the path, try removing it and find a new path
            for (int j = 0; j < nextShortestPath.getEdgeList().size(); j++) {
                // Temporary graph clone to manipulate
                Graph<BayWheelsNode, DefaultWeightedEdge> tempGraph = new AsSubgraph<>(graph);

                // Remove edge and find new path
                DefaultWeightedEdge removedEdge = nextShortestPath.getEdgeList().get(j);
                tempGraph.removeEdge(removedEdge);
                GraphPath<BayWheelsNode, DefaultWeightedEdge> newPath = DijkstraShortestPath.findPathBetween(tempGraph, source, sink);

                if (newPath != null) {
                    candidatePaths.add(newPath);
                }

            }

            // Find the shortest path among candidates
            nextShortestPath = getShortestPath(candidatePaths, paths);

            if (nextShortestPath != null) {
                paths.add(nextShortestPath);
                candidatePaths.remove(nextShortestPath);
            } else {
                break; // No more unique paths
            }
        }

        return paths;
    }


    private static GraphPath<BayWheelsNode, DefaultWeightedEdge> getShortestPath(Set<GraphPath<BayWheelsNode, DefaultWeightedEdge>> candidatePaths, Set<GraphPath<BayWheelsNode, DefaultWeightedEdge>> paths) {

        double minWeight = Double.MAX_VALUE;
        GraphPath<BayWheelsNode, DefaultWeightedEdge> minPath = null;
        for(GraphPath<BayWheelsNode, DefaultWeightedEdge> graphPath :candidatePaths){
            if(graphPath.getWeight()< minWeight && !paths.contains(graphPath)){
                minWeight = graphPath.getWeight();
                minPath = graphPath;
            }
        }
        return minPath;
    }
}

