package com.daa.optimizeRideshare.graph;

import org.springframework.stereotype.Service;

@Service
public class DisplayGraph {

//    public void graphStreamFromJGraphT(org.jgrapht.Graph<BayWheelsNode, DefaultWeightedEdge> jGraphT){
//        Graph graphStream = new SingleGraph("Graph");
//
//        for (DefaultWeightedEdge edge : jGraphT.edgeSet()) {
//            String sourceVertex = jGraphT.getEdgeSource(edge).toString();
//            String targetVertex = jGraphT.getEdgeTarget(edge).toString();
//            if(graphStream.getNode(sourceVertex) == null) graphStream.addNode(sourceVertex);
//            if(graphStream.getNode(targetVertex) == null) graphStream.addNode(targetVertex);
//            String edgeId = edge.toString();
//            graphStream.addEdge(edgeId, sourceVertex, targetVertex);
//
//            // Assume getEdgeWeight is a method to get the weight of the edge from your JGraphT graph
//            double weight = jGraphT.getEdgeWeight(edge);
//            graphStream.getEdge(edgeId).setAttribute("ui.label", String.valueOf(weight));
//        }
//        graphStream.setAttribute("ui.stylesheet", "edge { text-alignment: along; text-background-mode: rounded-box; text-background-color: yellow; text-color: black; }");
//        Viewer viewer = graphStream.display();
//    }


}
