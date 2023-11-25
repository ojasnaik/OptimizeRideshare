package com.daa.optimizeRideShare;


import com.daa.optimizeRideShare.application.ExecuteApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.daa.optimizeRideShare.repository", "com.daa.optimizeRideShare.application", "com.daa.optimizeRideShare.graph", "com.daa.optimizeRideShare.controller"})
public class OptimizeRideShareApplication {

    @Autowired
    ExecuteApp executeApp;

    public static void main(String[] args) {
        SpringApplication.run(OptimizeRideShareApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ExecuteApp yourService) {
        return args -> {
            yourService.getBayWheelsDataAndCreateGraph();
            System.out.println("Found");
        };
    }

}
