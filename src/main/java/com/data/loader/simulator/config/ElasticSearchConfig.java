package com.data.loader.simulator.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.data.loader.simulator.JsonToObject;
import com.data.loader.simulator.property.ElasticSearchProperties;

@Configuration
public class ElasticSearchConfig {
	
	private RestHighLevelClient restHighLevelClient;
	
	@Autowired
	private ElasticSearchProperties elasticSearchProperties;
	
	@Bean
	public RestHighLevelClient getRestHighLevelClient() {
		try {
			restHighLevelClient = new RestHighLevelClient(
			        RestClient.builder(new HttpHost(elasticSearchProperties.getHostname(), elasticSearchProperties.getPort(),
			                elasticSearchProperties.getProtocol())));
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return restHighLevelClient;
	}
	
	@Bean
	public JsonToObject getObject() {
		JsonToObject jsonToObject = new JsonToObject();
		return jsonToObject;
	}
}
