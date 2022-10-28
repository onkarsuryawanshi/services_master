package com.data.loader.simulator.loader;

import java.io.IOException;
import java.util.*;

import com.data.loader.simulator.property.BulkService;
import org.elasticsearch.client.RestHighLevelClient;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;


public class SingleDeviceDataLoader implements Runnable {

	private final BulkService bulkService;
	
	int device_id;
	
	DateTime start_time;
	
	int no_of_records;
	
	int operator_id;
	
	int batch_size;
	
	int time_interval;
	
	Map<String, Object> request;
	
	RestHighLevelClient restHighLevelClient;
	
	public SingleDeviceDataLoader(int device_id, DateTime start_time, int no_of_records,
	    RestHighLevelClient restHighLevelClient, int batch_size, int time_interval) {
		this.device_id = device_id;
		this.start_time = start_time;
		this.no_of_records = no_of_records;
		this.operator_id = generateOperatorId();
		this.restHighLevelClient = restHighLevelClient;
		this.bulkService = new BulkService();
		this.batch_size = batch_size;
		this.time_interval = time_interval;
	}
	
	public int generateOperatorId() {
		Random random = new Random();
		operator_id = random.nextInt(100);
		return operator_id;
	}
	
	public void createRequest(int duration, List<Map<String, Object>> deviceList) {
		Map<String, Object> objectMap = new HashMap<>();
		DateTime current_time = start_time.plusSeconds(duration);
		LocalDate localDate = current_time.toLocalDate();
		
		objectMap.put("device_id", device_id);
		objectMap.put("ms_time", current_time);
		objectMap.put("operator_id", operator_id);
		
		String index_name = operator_id + "_v2_metrics_" + localDate;
		objectMap.put("index", index_name);
		System.out.println(
		    "Request :" + objectMap + " thread name:" + Thread.currentThread().getName() + " Index name:" + index_name);
		deviceList.add(objectMap);
	}
	
	@Override
	public void run() {
		List<Map<String, Object>> deviceList = new ArrayList<>();
		int ms_time = 0;
		//mstime = start_time + duration
		for (int i = 0; i < no_of_records; i++) {
			createRequest(ms_time, deviceList);// deviselist.add
			
			if (deviceList.size() == batch_size) {
				
				System.out.println("Save the data into elasticsearch and clear the list");
				try {
					bulkService.save_data(deviceList, restHighLevelClient);
					deviceList.clear();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			ms_time = ms_time + time_interval;
		}
		if (!deviceList.isEmpty()) {
			System.out.println("Save the remaining data into elasticsearch and clear the list");
			System.out.println("List :" + deviceList + "thread name:" + Thread.currentThread().getName());
			try {
				bulkService.save_data(deviceList, restHighLevelClient);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			deviceList.clear();
		}
	}
}
