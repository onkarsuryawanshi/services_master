package com.ex.ES_rest_client.services;

import com.ex.ES_rest_client.client.LocalHostClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;

import org.elasticsearch.search.aggregations.AggregationBuilders;

import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
public class indexDocument {
    @Autowired
    LocalHostClient restHighLevelClient;
    public void indexDoc(){
        RestHighLevelClient client = restHighLevelClient.create();

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gt("2020-01-05").lt("2021-04-25");

        SumAggregationBuilder aggregationBuilders = AggregationBuilders.sum("SUM").field("DataUsed");


        SearchRequest searchRequest = new SearchRequest("test_device_topic_three");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(rangeQueryBuilder);

        System.out.println(rangeQueryBuilder);
        System.out.println(aggregationBuilders);

        SumAggregationBuilder aggregation = AggregationBuilders.sum("sum").field("DataUsed");
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        System.out.println(searchSourceBuilder);
//        *********************************
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println(searchResponse);

        RestStatus status = searchResponse.status();
//        if(status==RestStatus.OK){
//            Aggregations aggregations = searchResponse.getAggregations();
//
//            Histogram dataUsedHistogram = aggregations.get("DataUsed");
//
//        }

        }
}

