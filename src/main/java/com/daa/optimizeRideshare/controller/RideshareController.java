package com.daa.optimizeRideshare.controller;


import com.daa.optimizeRideshare.application.ExecuteApp;
import com.daa.optimizeRideshare.data.EdgeDTO;
import com.daa.optimizeRideshare.graph.BayWheelsNode;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RideshareController {

    @Autowired
    ExecuteApp executeApp;

    @GetMapping("/graph-data")
    public ResponseEntity<List<EdgeDTO>> getGraphEdges() {
        Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsData = executeApp.getBayWheelsData();

        List<EdgeDTO> edgeDTOS = bayWheelsData.edgeSet().stream().map(edge -> convertEdgeToDTO(bayWheelsData, edge)).toList();
        return ResponseEntity.ok(edgeDTOS);
    }

    private EdgeDTO convertEdgeToDTO(Graph<BayWheelsNode, DefaultWeightedEdge> graph, DefaultWeightedEdge edge) {
        String source = graph.getEdgeSource(edge).getStation_id();
        String target = graph.getEdgeTarget(edge).getStation_id();
        double weight = graph.getEdgeWeight(edge);

        return new EdgeDTO(source, target, weight);
    }

}
