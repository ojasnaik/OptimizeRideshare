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
//		System.setProperty("org.graphstream.ui", "swing");
//		SpringApplication app = new SpringApplication(OptimizeRideshareApplication.class);
//		SpringApplication app = new SpringApplication(OptimizeRideshareApplication.class);
//		app.setWebApplicationType(WebApplicationType.NONE);
//		app.setHeadless(true);
//		app.run(args);
        SpringApplication.run(OptimizeRideShareApplication.class, args);
    }
	@Bean
	CommandLineRunner run(ExecuteApp yourService) {
		return args -> {
			// Call methods of your service here
			yourService.getBayWheelsDataAndCreateGraph();
			System.out.println("Found");
		};
	}

}
