package com.daa.optimizeRideShare.controller;


import com.daa.optimizeRideShare.data.EdgeDTO;
import com.daa.optimizeRideShare.graph.BayWheelsNode;
import com.daa.optimizeRideShare.graph.GraphOperations;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RideShareController {

    @Autowired
    GraphOperations graphOperations;

    List<GraphPath<BayWheelsNode, DefaultWeightedEdge>> kShortestPaths;

    /**
     * Controller method to get the graph created from the BayWheels dataset.
     * @return graph representation as a list of Edges.
     */
    @Cacheable("EdgeListCache")
    @GetMapping("/graph-data")
    public ResponseEntity<List<EdgeDTO>> getGraphEdges() {
        Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsData = graphOperations.getBayWheelsRideGraph();
        List<EdgeDTO> edgeDTOS = bayWheelsData.edgeSet().stream().map(edge -> convertEdgeToDTO(bayWheelsData, edge)).toList();
        return ResponseEntity.ok(edgeDTOS);
    }

    /**
     * Controller method to get the 10 shortest paths from a source docking station to the destination.
     * @param sourceStationId The id of the start station.
     * @param destinationStationId The id of the destination station.
     * @return A list of 10 paths, each represented as a list of edges.
     */
    @GetMapping("/get10ShortestPaths")
    public ResponseEntity<List<List<EdgeDTO>>> getKShortestPathEdges(@RequestParam(value = "sourceStationId") String sourceStationId, @RequestParam(value = "destinationStationId") String destinationStationId) {
        BayWheelsNode source = new BayWheelsNode(sourceStationId);
        BayWheelsNode destination = new BayWheelsNode(destinationStationId);
        Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsData = graphOperations.getBayWheelsRideGraph();
        List<List<EdgeDTO>> response = new ArrayList<>();
        kShortestPaths = graphOperations.getKShortestPaths(source, destination, 10);
        kShortestPaths.forEach(path -> {
            response.add(path.getEdgeList().stream().map(edge -> convertEdgeToDTO(bayWheelsData, edge)).toList());
        });

        return ResponseEntity.ok(response);
    }

    /**
     * Controller method to get the overall cheapest path based on the BayWheels time dependent pricing criteria.
     * @param sourceStationId The id of the start station.
     * @param destinationStationId The id of the destination station.
     * @return The most cost-efficient path among the 10 shortest paths .
     */
    @GetMapping("/getOverallCheapestPath")
    public ResponseEntity<List<List<EdgeDTO>>> getOverallCheapestPath(@RequestParam(value = "sourceStationId", required = false) String sourceStationId, @RequestParam(value = "destinationStationId", required = false) String destinationStationId) {
        BayWheelsNode source = new BayWheelsNode(sourceStationId);
        BayWheelsNode destination = new BayWheelsNode(destinationStationId);
        Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph = graphOperations.createGraphFromPaths(kShortestPaths);
        GraphPath<BayWheelsNode, DefaultWeightedEdge> overallCheapestPath = graphOperations.getOverallCheapestPath(kShortestPathsGraph, source, destination);
        List<EdgeDTO> response = overallCheapestPath.getEdgeList().stream().map(defaultWeightedEdge -> convertEdgeToDTO(kShortestPathsGraph, defaultWeightedEdge)).toList();
        return ResponseEntity.ok(Collections.singletonList(response));
    }

    /**
     * Controller method to get the shortest path according to Dijkstra's algorithm
     * @param sourceStationId The id of the start station.
     * @param destinationStationId The id of the destination station.
     * @return the Shortest path according to Dijkstra's algorithm
     */
    @GetMapping("/getDijkstraShortestPath")
    public ResponseEntity<List<List<EdgeDTO>>> getDijkstraShortestPath(@RequestParam(value = "sourceStationId", required = false) String sourceStationId, @RequestParam(value = "destinationStationId", required = false) String destinationStationId) {
        BayWheelsNode source = new BayWheelsNode(sourceStationId);
        BayWheelsNode destination = new BayWheelsNode(destinationStationId);
        Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph = graphOperations.createGraphFromPaths(kShortestPaths);
        GraphPath<BayWheelsNode, DefaultWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(kShortestPathsGraph, source, destination);
        List<EdgeDTO> response = shortestPath.getEdgeList().stream().map(defaultWeightedEdge -> convertEdgeToDTO(kShortestPathsGraph, defaultWeightedEdge)).toList();
        return ResponseEntity.ok(Collections.singletonList(response));
    }

    /**
     * Utility method to jGraphT edge to EdgeDTO
     * @param graph jGraphT graph
     * @param edge jGraphT edge
     * @return EdgeDTO representation
     */
    private EdgeDTO convertEdgeToDTO(Graph<BayWheelsNode, DefaultWeightedEdge> graph, DefaultWeightedEdge edge) {
        String source = graph.getEdgeSource(edge).getStation_id();
        String target = graph.getEdgeTarget(edge).getStation_id();
        double weight = graph.getEdgeWeight(edge);
        return new EdgeDTO(source, target, weight);
    }

}
