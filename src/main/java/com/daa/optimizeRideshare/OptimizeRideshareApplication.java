package com.daa.optimizeRideshare;


import com.daa.optimizeRideshare.application.ExecuteApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.daa.optimizeRideshare.repository", "com.daa.optimizeRideshare.application", "com.daa.optimizeRideshare.graph", "com.daa.optimizeRideshare.controller"})
public class OptimizeRideshareApplication {

    @Autowired
    ExecuteApp executeApp;

    public static void main(String[] args) {
//		System.setProperty("org.graphstream.ui", "swing");
//		SpringApplication app = new SpringApplication(OptimizeRideshareApplication.class);
//		SpringApplication app = new SpringApplication(OptimizeRideshareApplication.class);
//		app.setWebApplicationType(WebApplicationType.NONE);
//		app.setHeadless(true);
//		app.run(args);
        SpringApplication.run(OptimizeRideshareApplication.class, args);
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
