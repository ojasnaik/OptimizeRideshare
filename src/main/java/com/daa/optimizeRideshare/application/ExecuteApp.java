package com.daa.optimizeRideshare.application;

import com.daa.optimizeRideshare.data.BayWheels;
import com.daa.optimizeRideshare.data.BayWheelsClean;
import com.daa.optimizeRideshare.graph.BayWheelsNode;
import com.daa.optimizeRideshare.graph.CreateGraph;
import com.daa.optimizeRideshare.repository.BayWheelsCleanRepository;
import com.daa.optimizeRideshare.repository.BaywheelsRepository;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecuteApp {

    public List<BayWheels> baywheelsRepositoryAll;

    public List<BayWheelsClean> bayWheelsCleanDataList;

    @Autowired
    BaywheelsRepository baywheelsRepository;

    @Autowired
    BayWheelsCleanRepository bayWheelsCleanRepository;

    @Autowired
    CreateGraph createGraph;

//    @Autowired
//    DisplayGraph displayGraph;
    public Graph<BayWheelsNode, DefaultWeightedEdge> getBayWheelsDataAndCreateGraph() {
//        baywheelsRepositoryAll = baywheelsRepository.findAll();
        bayWheelsCleanDataList = bayWheelsCleanRepository.findAll();

        Graph<BayWheelsNode, DefaultWeightedEdge> bayWheelsGraph = createGraph.createGraphFromData(bayWheelsCleanDataList);
//        displayGraph.graphStreamFromJGraphT(bayWheelsGraph);
//        createGraph.display();
//        System.out.println("Found");
        return bayWheelsGraph;
    }
}
