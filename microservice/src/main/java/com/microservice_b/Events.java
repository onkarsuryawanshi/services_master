package com.microservice_b;

import org.apache.camel.main.MainSupport;

public interface Events {
    void afterStart(MainSupport main);

    void beforeStop(MainSupport main);
}
