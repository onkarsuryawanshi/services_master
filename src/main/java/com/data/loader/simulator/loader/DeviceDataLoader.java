package com.data.loader.simulator.loader;

import com.data.loader.simulator.KPI;
import com.data.loader.simulator.config.SystemConfig;
import com.data.loader.simulator.datagenerator.DataGenerator;
import com.data.loader.simulator.property.BulkService;
import com.data.loader.simulator.util.JsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.elasticsearch.client.RestHighLevelClient;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class DeviceDataLoader implements Runnable {

    private final int no_of_records;
    private final int batch_size;
    private final int time_interval;

    private final BulkService bulkService;

    private final RestHighLevelClient restHighLevelClient;

    private final DataGenerator dataGenerator;

    private final Map<String, KPI> kpiMap;

    private final String deviceId;

    private final int operatorId;

    public DeviceDataLoader(int no_of_records, int batch_size, int time_interval, BulkService bulkService,
                            RestHighLevelClient restHighLevelClient, Map<String, KPI> kpiMap, String deviceId, int operatorId) {
        this.no_of_records = no_of_records;
        this.batch_size = batch_size;
        this.time_interval = time_interval;
        this.bulkService = bulkService;
        this.restHighLevelClient = restHighLevelClient;
        this.kpiMap = kpiMap;
        this.dataGenerator = new DataGenerator();
        this.deviceId = deviceId;
        this.operatorId = operatorId;
    }

    public Map<String, Object> createRequest(DateTime dateTime) {
        Map<String, Object> request = new HashMap<>();

        for (Map.Entry<String, KPI> kpiEntry : kpiMap.entrySet()) {
            String kpiName = kpiEntry.getKey();
            String type = kpiEntry.getValue().getType();
            Object kpiValue = null;
            if (type.equals("Integer")) {
                if (kpiEntry.getValue().getDimension().equals("vector"))
                    kpiValue = dataGenerator.generateVectorData();
                else if (kpiEntry.getValue().isNegative())
                    kpiValue = dataGenerator.generatePositiveNegativeNumericData();
                else
                    kpiValue = dataGenerator.generateNumericData();
            } else if (type.equals("Float")) {
                if (kpiEntry.getValue().getDimension().equals("vector"))
                    kpiValue = dataGenerator.generateVectorData();
                else if (kpiEntry.getValue().isNegative())
                    kpiValue = dataGenerator.generatePositiveNegativeDecimalData();
                else
                    kpiValue = dataGenerator.generateFloatData();
            } else if (type.equals("String")) {
                kpiValue = dataGenerator.generateRandomStringData();
            } else if (type.equals("IP")) {
                kpiValue = dataGenerator.generateIPAddress();
            } else if (type.equals("DateTime")) {
                kpiValue = dataGenerator.generateDateTimeData();
            } else if (type.equals("Boolean")) {
                kpiValue = dataGenerator.generateBooleanData();
            }
            request.put(kpiName, kpiValue);
        }
        LocalDate localDate = dateTime.toLocalDate();
        String index_name = operatorId + "_v2_metrics_" + localDate;
        System.out.println("+++Index  name:" + index_name);
//		request.put("index", index_name);
        request.put("operatorId", operatorId);
        request.put("deviceId", deviceId);
        request.put("msTime", dateTime);
        request.put("softwareVersion", dataGenerator.generateSoftwareVersionData());
        request.put("radioType", dataGenerator.generateRadioTypeData());
        return request;
    }

    @Override
    public void run() {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, SystemConfig.producerApplicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SystemConfig.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


        List<Map<String, Object>> deviceList = new ArrayList<>();
        DateTime ms_time = DateTime.now();
        for (int i = 0; i < no_of_records; i++) {
            Map<String, Object> deviceRequest = createRequest(ms_time);
            deviceList.add(deviceRequest);
            System.out.println(deviceRequest);

            KafkaProducer<Integer, Map<String, Object>> producer = new KafkaProducer<Integer, Map<String, Object>>(props);
            ProducerRecord<Integer, Map<String, Object>> record =
                    new ProducerRecord<>(SystemConfig.topicName, i, deviceRequest);
            try {
                RecordMetadata recordMetadata = producer.send(record).get();
                String message = String.format("sent message to topic:%s partition:%s  offset:%s",
                        recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                System.out.println(message);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
