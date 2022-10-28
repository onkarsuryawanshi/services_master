package com.microservice_b;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiverRoute extends RouteBuilder {
    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;
    @Autowired
    private SimpleLoggingProcessor loggingProcessor;

    @Override
    public void configure() throws Exception {

        //transform
        /* can be done using either transform.constant()
        or bean with return type function
        * */

        //process

        /* you can process by using either process or bean as shown below
         * */


        /*
        * application service-3 is deployed on ec-2 instance of aws
        * you can access is by replacing uri
        * http:localhost:9090/get ==> ec2-43-205-128-85.ap-south-1.compute.amazonaws.com:8080/get
        * just make sure that ec-2 instance is up and running it will work similar just like local host:8080/get
        * */
        log.info("Initialising Camel Routes ");
        from("kafka:device-topic-3")
                .pollEnrich("http:localhost:9090/get",
                        new AggregationStrategy() {
                            @Override
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                if (newExchange == null) {
                                    return oldExchange;
                                }
                                String old = oldExchange.getIn()
                                        .getBody(String.class);
                                String service3 = newExchange.getIn()
                                        .getBody(String.class);
                                int lengthOfOld = old.length();
                                int lengthOfService = service3.length();
                                String transformedOld = old.substring(0, lengthOfOld - 1);
                                String transformedService = service3.substring(1, lengthOfService - 1);
                                String body = transformedOld + "," + transformedService + "}";
                                oldExchange.getIn().setBody(body);
                                return oldExchange;
                            }
                        })
                .to("log:result?showAll=true")
                .to("direct:index");


//        from("direct:index")
//                .to("log:${body}")
//                .to("elasticsearch-rest:local?operation=Index&hostAddresses=localhost:9200&indexName=test_device_topic_three")
//                .to("log:result?showAll=true");


        log.info("injecting the data in elastic search ");
        from("direct:index")
                .to("elasticsearch-rest:local?operation=Index&hostAddresses=localhost:9200&indexName=test_device_topic_three")
                .to("log:result?showAll=true");


        log.info("camel...... ");

    }

}

@Component
class SimpleLoggingProcessingComponent {
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    public void process(String message) {
        logger.info("simpleLoggingProcessingComponent ${}", message);
    }
}

@Component
class SimpleLoggingProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("simpleLoggingProcessor {}", exchange.getMessage().getBody());
    }
}
