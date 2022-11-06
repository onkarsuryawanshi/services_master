package com.elastiSearchClient;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.core.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;


@RestController()
@RequestMapping("/query")
public class ElasticSearchController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;


    //    return all documents available on given index
    @GetMapping("/getAllDoc")
    public ResponseEntity<SearchResponse> searchAllDocument() {
        SearchResponse searchResponse = elasticSearchQuery.getAllDocument();
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    @GetMapping("/getDocCount")
    public ResponseEntity<String> countAllDocument() {
        long documentCount = elasticSearchQuery.getDocumentCount();
        return new ResponseEntity<>("Total Number of Documents are --> " + documentCount, HttpStatus.OK);
    }



//    returns the all records based on the match field entered
//    field = region , textTobeSearch = Vivo will return all matching records
    @GetMapping("/matchQuery")
    public ResponseEntity<SearchResponse> matchQueryByField(@RequestParam String field, @RequestParam String textTobeSearch){
        SearchResponse searchResponse = elasticSearchQuery.getDocumentByMatchQuery(field,textTobeSearch);
        return new ResponseEntity<>(searchResponse,HttpStatus.OK);
    }

    //    returns sum of dataUsed between two Entered dates
    @GetMapping("/sumOfDataUsedBetweenTwoDates")
    public ResponseEntity<SearchResponse> sumOfDataUsedBetweenTwoDates(@RequestParam String startDate, @RequestParam String endDate) {
        SearchResponse searchResponse = elasticSearchQuery.getSumOfDataUsedBetweenTwoDates(startDate, endDate);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }


    //    returns avg of dataUsed between two Entered dates
    @GetMapping("/avgOfDataUsedBetweenTwoDates")
    public ResponseEntity<SearchResponse> avgOfDataUsedBetweenTwoDates(@RequestParam String startDate, @RequestParam String endDate) {
        SearchResponse searchResponse = elasticSearchQuery.getAvgOfDataUsedBetweenTwoDates(startDate, endDate);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }


    //    returns max of dataUsed between two Entered dates
    @GetMapping("/maxOfDataUsedBetweenTwoDates")
    public ResponseEntity<SearchResponse> maxOfDataUsedBetweenTwoDates(@RequestParam String startDate, @RequestParam String endDate) {
        SearchResponse searchResponse = elasticSearchQuery.getMaxOfDataUsedBetweenTwoDates(startDate, endDate);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    //    returns min of dataUsed between two Entered dates
    @GetMapping("/minOfDataUsedBetweenTwoDates")
    public ResponseEntity<SearchResponse> minOfDataUsedBetweenTwoDates(@RequestParam String startDate, @RequestParam String endDate) {
        SearchResponse searchResponse = elasticSearchQuery.getMinOfDataUsedBetweenTwoDates(startDate, endDate);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }




    /*
     *
     * with the field from user
     *
     * */

    @GetMapping("/sumOfFieldsBetweenTwoDates")
    public ResponseEntity<Object> sumOfFields(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String field) {
        SearchResponse searchResponse = elasticSearchQuery.getSumOfFieldsBetweenTwoDates(startDate, endDate, field);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    @GetMapping("/avgOfFieldsBetweenTwoDates")
    public ResponseEntity<Object> avgOfFields(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String field) {
        SearchResponse searchResponse = elasticSearchQuery.getAvgOfFieldsBetweenTwoDates(startDate, endDate, field);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }


    //    returning the document with the id entered
    @GetMapping("/getById")
    public ResponseEntity<Object> getDocumentById(@RequestParam String doc_Id) {
        GetResponse response = elasticSearchQuery.getById(doc_Id);
        if (response.isExists()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("entered Id " + doc_Id + "doesn't exits...",HttpStatus.NOT_FOUND);
        }
    }


//    Cluster information can be retrieved using the info() method:
    @GetMapping("/infoLog")
    public MainResponse infoLog() {
        return elasticSearchQuery.getInfoLog();
    }

    @GetMapping("/radioType")
    public List<String> radioType(){
        return elasticSearchQuery.getDocumentByRadioType();
    }


    @GetMapping("/termAggByOperator")
    public ResponseEntity operators(){
        Map<String, Long> operatorsAggMap =elasticSearchQuery.getAggByOperators();
        return new ResponseEntity<>(operatorsAggMap,HttpStatus.OK);
    }


//    Histogram Aggregation
    @GetMapping("/histogramAggregationByMonths")
    public ResponseEntity<Map<ZonedDateTime, Long>> histogramAggregationBYMonth(){
        Map<ZonedDateTime, Long> dateHistogramMap = elasticSearchQuery.getHistogramAggregationByDate();
         return new ResponseEntity<>(dateHistogramMap, HttpStatus.OK);
    }
}
