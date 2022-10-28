package com.data.loader.simulator.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "elastic-search")
@Getter
@Setter
public class ElasticSearchProperties {
	
	private String hostname;
	
	private int port;
	
	private String protocol;
}
