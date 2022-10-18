package com.microservice_b;

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
        from("kafka:microservice")
                .bean(loggingComponent)
//                .bean(loggingProcessor)
                .process(new SimpleLoggingProcessor())
                .to("log:${body}");
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
class SimpleLoggingProcessor implements Processor{
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("simpleLoggingProcessor {}" ,exchange.getMessage().getBody());
    }
}
