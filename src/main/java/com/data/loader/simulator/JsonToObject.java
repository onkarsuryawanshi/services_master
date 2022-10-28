package com.data.loader.simulator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonToObject {
	
	public Map<String, KPI> createMapOfKpis() throws URISyntaxException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		URL res = this.getClass().getClassLoader().getResource("config.json");
		File file = Paths.get(res.toURI()).toFile();
		KPI[] kpis = objectMapper.readValue(file, KPI[].class);
		Map<String, KPI> kpiMap = new HashMap<>();
		for (KPI kpi : kpis) {
			kpiMap.put(kpi.getName(), kpi);
			//	System.out
			//	        .println("++++ name:" + kpi.getName() + "  type:" + kpi.getType() + " dimension:" + kpi.getDimension()+ "+++is negative:"+kpi.isNegative());
		}
		return kpiMap;
	}
}
