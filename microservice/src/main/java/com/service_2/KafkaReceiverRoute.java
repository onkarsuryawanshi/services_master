package com.service_2;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiverRoute extends RouteBuilder {
    @Autowired
    private RestClientService3 restClientService3;
    @Override
    public void configure() throws Exception {


        log.info("Initialising Camel Routes ");

        from("kafka:device-topic-main")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                    String firstMess = (String) exchange.getMessage().getBody();
                    String secondMess = restClientService3.getRequest();

                    int lenFirstMess = firstMess.length();
                    int lenSecondMess = secondMess.length();
                    String transformedFirstMess = firstMess.substring(0,lenFirstMess-1);
                    String transformedSecondMess = secondMess.substring(1,lenSecondMess-1);
                    String body = transformedFirstMess + "," + transformedSecondMess + "}";
                        exchange.getIn().setBody(body);
                        }

                })

                .to("log:result?showAll=true")
                .to("direct:index");

        log.info("injecting the data in elastic search ");
        from("direct:index")
                .to("elasticsearch-rest:local?operation=Index&hostAddresses=localhost:9200&indexName=device_index_main")
                .to("log:result?showAll=true");


        log.info("camel...... ");

    }

}