package com.service_3.controller;

import com.service_3.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServiceController {

    @Autowired
    private Query query;


    @GetMapping("/get")
    public Map<String, Object> getDetails(){
        Map<String, Object> res = query.getDetails();
        return res;
    }
}
