package com.daa.optimizeRideshare;

import com.daa.optimizeRideshare.application.ExecuteApp;
import com.daa.optimizeRideshare.data.BayWheels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@ComponentScan({"com.daa.optimizeRideshare.repository", "com.daa.optimizeRideshare.application"})
public class OptimizeRideshareApplication {

	@Autowired
	ExecuteApp executeApp;
	public static void main(String[] args) {
		SpringApplication.run(OptimizeRideshareApplication.class, args);
	}
	@Bean
	CommandLineRunner run(ExecuteApp yourService) {
		return args -> {
			// Call methods of your service here
			List<BayWheels> bayWheelsData = yourService.getBayWheelsData();
			System.out.println("Found");
		};
	}

}
