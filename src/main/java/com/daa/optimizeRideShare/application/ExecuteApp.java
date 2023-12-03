package com.daa.optimizeRideShare.application;

import com.daa.optimizeRideShare.data.BayWheelsClean;
import com.daa.optimizeRideShare.graph.GraphOperations;
import com.daa.optimizeRideShare.repository.BayWheelsCleanRepository;
import com.daa.optimizeRideShare.repository.BaywheelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExecuteApp {
    public List<BayWheelsClean> bayWheelsCleanDataList;

    @Autowired
    BaywheelsRepository baywheelsRepository;

    @Autowired
    BayWheelsCleanRepository bayWheelsCleanRepository;

    @Autowired
    GraphOperations graphOperations;

    /**
     * This method will fetch all the clean data saved into Postgres after EDA and create a weighted and directed graph.
     */
    public void getBayWheelsDataAndCreateGraph() {
        bayWheelsCleanDataList = bayWheelsCleanRepository.findAll();
        graphOperations.createGraphFromData(bayWheelsCleanDataList);
    }
}
