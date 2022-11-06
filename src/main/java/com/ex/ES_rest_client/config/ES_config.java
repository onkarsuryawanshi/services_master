package com.ex.ES_rest_client.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ES_config {
    public final static String indexName = "test_device_topic_three";
    public final static String elasticSearchHost = "localhost";
    public final static Integer elasticSearchPort = 9200;
    public final static String elasticSearchScheme = "http";


}
