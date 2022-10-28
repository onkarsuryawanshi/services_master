package com.data.loader.simulator.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class ApplicationProperties {
	
	private int noOfThreads;
	
	private int noOfDevices;
	
	private int noOfRecords;
	
	private int batchSize;
	
	private int timeInterval;
}
