package com.elastiSearchClient;

import com.elastiSearchClient.Exception.InvalidInputException;
import com.elastiSearchClient.client.LocalHostClient;
import com.elastiSearchClient.config.ES_config;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

@Repository
public class ElasticSearchQuery {

    @Autowired
    LocalHostClient restHighLevelClient;
    RestHighLevelClient client = restHighLevelClient.create();

    public SearchResponse getAllDocument() {
        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return searchResponse;
    }

    public long getDocumentCount() {

        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();

        long numHits = totalHits.value;
        return numHits;
    }

    public SearchResponse getSumOfDataUsedBetweenTwoDates(String startDate, String endDate) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gt(startDate).lt(endDate);

        SumAggregationBuilder aggregationBuilders = AggregationBuilders.sum("SUM").field("DataUsed");


        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(rangeQueryBuilder);


        SumAggregationBuilder aggregation = AggregationBuilders.sum("sum").field("DataUsed");
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
//        *********************************
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Aggregations agg = searchResponse.getAggregations();
        /*
         * from here return only value
         * */


        return searchResponse;

    }

    public SearchResponse getAvgOfDataUsedBetweenTwoDates(String startDate, String endDate) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gt(startDate).lt(endDate);

        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(rangeQueryBuilder);


        AvgAggregationBuilder aggregation = AggregationBuilders.avg("avg").field("DataUsed");
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
//        *********************************
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return searchResponse;
    }


//    method will return device data for whom data usage is maximum between the 2 dates entered
    public SearchResponse getMaxOfDataUsedBetweenTwoDates(String startDate, String endDate) {
        Double maxDataUsed = null;

        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gt(startDate).lt(endDate);
        searchSourceBuilder.query(rangeQueryBuilder);

        MaxAggregationBuilder aggregation = AggregationBuilders.max("max").field("DataUsed");
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
//        *********************************
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(searchResponse);
        Map<String, Aggregation> agg = searchResponse.getAggregations().asMap();

        for (Map.Entry<String, Aggregation> curr : agg.entrySet()
        ) {
            JSONObject obj = new JSONObject(curr.getValue());
            maxDataUsed = obj.getDouble("value");
            System.out.println("value" + maxDataUsed);
        }
        //private method
        return getDevice(maxDataUsed);
    }

    private SearchResponse getDevice(Double maxDataUsed) {

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("DataUsed", maxDataUsed));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return searchResponse;
    }

    public SearchResponse getMinOfDataUsedBetweenTwoDates(String startDate, String endDate) {
        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gt(startDate).lt(endDate);
        searchSourceBuilder.query(rangeQueryBuilder);

        MinAggregationBuilder aggregation = AggregationBuilders.min("Min").field("DataUsed");
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
//        *********************************
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return searchResponse;
    }

    public SearchResponse getSumOfFieldsBetweenTwoDates(String startDate, String endDate, String userField) {

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gt(startDate).lt(endDate);


        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(rangeQueryBuilder);


        SumAggregationBuilder aggregation = AggregationBuilders.sum("sum").field(userField);
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
//        *********************************
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new InvalidInputException("error in input ...Please try with different field");
        }

        Aggregations agg = searchResponse.getAggregations();
        return searchResponse;
    }

    public SearchResponse getAvgOfFieldsBetweenTwoDates(String startDate, String endDate, String userField) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gt(startDate).lt(endDate);
        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(rangeQueryBuilder);
        SumAggregationBuilder aggregation = AggregationBuilders.sum("Avg").field(userField);
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
//        *********************************
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new InvalidInputException("error in input ...Please try with different field");
        }
        return searchResponse;
    }

    public GetResponse getById(String doc_Id) {
        GetRequest getRequest = new GetRequest(
                ES_config.indexName,
                doc_Id);

        GetResponse response = null;
        try {
            response = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    public MainResponse getInfoLog() {
        MainResponse response = null;
        try {
            response = client.info(RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public SearchResponse getDocumentByMatchQuery(String field, String textTobeSearch) {

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, textTobeSearch));
        searchRequest.source(searchSourceBuilder);


        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new InvalidInputException("error in input ...Please try with different field");
        }

        System.out.println("total number of match record are ==> " + searchResponse.getHits().getTotalHits());

        return searchResponse;

    }

    public List<String> getDocumentByRadioType() {

        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchQuery("radioType",inputRadioType));
        AggregationBuilder aggregation =
                AggregationBuilders
                        .filters("agg",
                                new FiltersAggregator.KeyedFilter("BN", QueryBuilders.matchQuery("radioType", "BN")),
                                new FiltersAggregator.KeyedFilter("RN", QueryBuilders.matchQuery("radioType", "RN")));
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new InvalidInputException("error in input ...Please try with different field");
        }

        Filters agg = searchResponse.getAggregations().get("agg");
        List<String> aggList = new ArrayList<>();
        // For each entry
        for (Filters.Bucket entry : agg.getBuckets()) {
            String key = entry.getKeyAsString();            // bucket key
            long docCount = entry.getDocCount();            // Doc count
            aggList.add("key ==> " + key + "    doc_count ==>" + docCount);
        }
        System.out.println(searchResponse);
        return aggList;

    }

//    A multi-bucket value source based aggregation where buckets are dynamically built - one per unique value.
    public Map<String, Long> getAggByOperators() {
        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation =AggregationBuilders
                .terms("operators")
                .field("operator.keyword");
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        Terms genders = searchResponse.getAggregations().get("operators");

        // For each entry
        Map<String,Long> mapAggByOperator = new HashMap<>();
        for (Terms.Bucket entry : genders.getBuckets()) {
            mapAggByOperator.put((String) entry.getKey(),entry.getDocCount());
        }
        return mapAggByOperator;
    }


    public Map<ZonedDateTime, Long> getHistogramAggregationByDate() {

        SearchRequest searchRequest = new SearchRequest(ES_config.indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation =
                AggregationBuilders
                        .dateHistogram("agg")
                        .field("date")
                        .format("yyyy-MM-dd")
                        .calendarInterval(DateHistogramInterval.MONTH);

        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        Histogram agg = searchResponse.getAggregations().get("agg");
//         For each entry

        Map<ZonedDateTime,Long> dateHistogramMap = new HashMap<>();
        for (Histogram.Bucket entry : agg.getBuckets()) {
            ZonedDateTime key = (ZonedDateTime) entry.getKey();    // Key
            long docCount = entry.getDocCount();         // Doc count
            dateHistogramMap.put(key,docCount);
        }
        return dateHistogramMap;
    }
}