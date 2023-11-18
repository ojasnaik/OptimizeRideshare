package com.daa.optimizeRideshare.application;

import com.daa.optimizeRideshare.data.BayWheels;
import com.daa.optimizeRideshare.repository.BaywheelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecuteApp {

    @Autowired
    BaywheelsRepository baywheelsRepository;

    @Cacheable
    public List<BayWheels> getBayWheelsData(){
        List<BayWheels> baywheelsRepositoryAll = baywheelsRepository.findAll();
        System.out.println("Found");
        return baywheelsRepositoryAll;
    }
}
