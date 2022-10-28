package com.ex.query;


import com.ex.dataGenertor.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class Query {
    @Autowired
    private DataGenerator generator;
    public Map<String, Object> getDetails() {
        Map<String, Object> request = new HashMap<>();
        request.put("averageSpeed",generator.getAverageSpeedOfDevice());
        request.put("DataUsed",generator.getDataUsed());
        request.put("date",generator.getDate());
        request.put("time",generator.getTime());
        return request;

    }
}
