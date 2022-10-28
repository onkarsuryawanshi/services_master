package com.data.loader.simulator;

import java.io.IOException;
import java.net.URISyntaxException;

import com.data.loader.simulator.loader.DeviceDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.data.loader.simulator.loader.DataLoader;

@SpringBootApplication
public class SimulatorApplication {

	@Autowired
	private DataLoader dataLoader;
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		SpringApplication.run(SimulatorApplication.class, args);
		System.out.println("Hello");
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.data.loader.simulator");
		context.refresh();
		
		SimulatorApplication simulatorApplication = context.getBean(SimulatorApplication.class);
		simulatorApplication.run();
		
		context.close();
	}
	
	public void run() throws URISyntaxException, IOException {
		dataLoader.run();
	}
	
}
