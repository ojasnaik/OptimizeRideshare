package com.daa.optimizeRideshare.graph;

import com.daa.optimizeRideshare.data.BayWheelsClean;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateGraph {

    Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsRideMap;
    public Graph<BayWheelsNode, DefaultWeightedEdge> createGraphFromData(List<BayWheelsClean> data){

            bayWheelsRideMap = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

            for(BayWheelsClean entry : data){
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
                if(edge != null){
                    bayWheelsRideMap.setEdgeWeight(edge, entry.getTotal_time());
                }

            }



            return bayWheelsRideMap;
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
