package com.service_2;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component()
public class RestClientService3 {
    private final static Logger LOGGER = Logger.getLogger(RestClientService3.class.getName());

    public String getRequest() throws IOException {
        //Adding rest client
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:9090/get");
        HttpResponse response = client.execute(request);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException ex) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("The method is down." + ex.getMessage());
            }
        }
        String line = "";
        while (true) {
            if (((line = reader.readLine()) != null))
                return line;
        }
    }
}
