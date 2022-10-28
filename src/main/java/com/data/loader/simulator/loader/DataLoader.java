package com.data.loader.simulator.loader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.data.loader.simulator.property.BulkService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.data.loader.simulator.JsonToObject;
import com.data.loader.simulator.KPI;
import com.data.loader.simulator.property.ApplicationProperties;

@Component
public class DataLoader {

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private JsonToObject jsonToObject;

	@Autowired
	private BulkService bulkService;

	public void run() throws URISyntaxException, IOException {
		System.out.println("Rest client :" + restHighLevelClient);

		Map<String, KPI> kpiMap = jsonToObject.createMapOfKpis();
		long start = System.currentTimeMillis();

		ExecutorService pool = Executors.newFixedThreadPool(applicationProperties.getNoOfThreads());
		for (int i = 1; i <= applicationProperties.getNoOfDevices(); i++) {
			String deviceId = "OnePlus" + i;
			Random random = new Random();
			int operatorId = random.nextInt(100);
			Runnable runnable = new DeviceDataLoader(applicationProperties.getNoOfRecords(),
			        applicationProperties.getBatchSize(), applicationProperties.getTimeInterval(), bulkService,
			        restHighLevelClient, kpiMap, deviceId, operatorId);
			pool.execute(runnable);

		}
		pool.shutdown();
		while (!pool.isTerminated()) {}
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		System.out.println(
		    "Finished all threads , total time in millisecond:" + elapsedTime + " in seconds:" + (elapsedTime * 1.0 / 1000));

	}
}
