package com.daa.optimizeRideshare.controller;


import com.daa.optimizeRideshare.data.EdgeDTO;
import com.daa.optimizeRideshare.graph.BayWheelsNode;
import com.daa.optimizeRideshare.graph.CreateGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RideshareController {


    @Autowired
    CreateGraph createGraph;

    Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph;
    List<GraphPath<BayWheelsNode, DefaultWeightedEdge>> kShortestPaths;

    @Cacheable("EdgeListCache")
    @GetMapping("/graph-data")
    public ResponseEntity<List<EdgeDTO>> getGraphEdges() {
        Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsData = createGraph.getBayWheelsRideGraph();

        List<EdgeDTO> edgeDTOS = bayWheelsData.edgeSet().stream().map(edge -> convertEdgeToDTO(bayWheelsData, edge)).toList();
        return ResponseEntity.ok(edgeDTOS);
    }

    @GetMapping("/get10Shortest")
    public ResponseEntity<List<List<EdgeDTO>>> getKShortestPathEdges(@RequestParam(value = "startId") String s, @RequestParam(value = "endId") String d) {
        BayWheelsNode source = new BayWheelsNode(s);
        BayWheelsNode destination = new BayWheelsNode(d);
        Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsData = createGraph.getBayWheelsRideGraph();
        List<List<EdgeDTO>> response = new ArrayList<>();
//        Set<GraphPath<BayWheelsNode, DefaultWeightedEdge>> kShortestPaths = createGraph.getKShortestPathsBetween(source, destination, 10);
        kShortestPaths = createGraph.getKShortestPaths(source, destination, 10);
        kShortestPaths.forEach(path -> {
            response.add(path.getEdgeList().stream().map(edge -> convertEdgeToDTO(bayWheelsData, edge)).toList());
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOverallShortest")
    public ResponseEntity<List<EdgeDTO>> OverallShortestPath(@RequestParam(value = "startId", required = false) String s, @RequestParam(value = "endId", required = false) String d) {
        BayWheelsNode source = new BayWheelsNode(s);
        BayWheelsNode destination = new BayWheelsNode(d);
        Graph<BayWheelsNode, DefaultWeightedEdge> kShortestPathsGraph = createGraph.createGraphFromPaths(kShortestPaths);
        GraphPath<BayWheelsNode, DefaultWeightedEdge> overallShortestPath = createGraph.getOverallShortestPath(kShortestPathsGraph, source, destination);
        List<EdgeDTO> response = overallShortestPath.getEdgeList().stream().map(defaultWeightedEdge -> convertEdgeToDTO(kShortestPathsGraph, defaultWeightedEdge)).toList();
        return ResponseEntity.ok(response);
    }

    private EdgeDTO convertEdgeToDTO(Graph<BayWheelsNode, DefaultWeightedEdge> graph, DefaultWeightedEdge edge) {
        String source = graph.getEdgeSource(edge).getStation_id();
        String target = graph.getEdgeTarget(edge).getStation_id();
        double weight = graph.getEdgeWeight(edge);

        return new EdgeDTO(source, target, weight);
    }

}
