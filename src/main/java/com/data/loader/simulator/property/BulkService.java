package com.data.loader.simulator.property;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class BulkService {
	
	public void save_data(List<Map<String, Object>> deviceList, RestHighLevelClient restHighLevelClient) throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		
		for (int i = 0; i < deviceList.size(); i++) {
			IndexRequest indexRequest = new IndexRequest((String) deviceList.get(i).get("index")).source(deviceList.get(i),
			    XContentType.JSON);
			bulkRequest.add(indexRequest);
		}
		
		BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);// use bulk_async
		System.out.println(bulkResponse.status());
	}
}
