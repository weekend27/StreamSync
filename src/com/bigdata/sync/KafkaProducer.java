package com.bigdata.sync;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import com.bigdata.DataClass.JNData;

public class KafkaProducer {
	
	public static String brokerlist = "127.0.0.1:9092";
	public static String topic = "p";
	
	public static void producer(String key, JNData inData) {
		
		System.out.println("producer key===>" + key);
		System.out.println("producer default1===>" + inData.getDefault1());
		
		
		Properties props = new Properties();
        props.put("metadata.broker.list", brokerlist);
        props.put( "serializer.class", "com.bigdata.kafka.DataKafkaKryoEncoder");			// need to update
        props.put("partitioner.class", "com.bigdata.kafka.SimplePartitioner");				// need to update
        props.put("request.required.acks", "1");
        
        ProducerConfig config = new ProducerConfig(props);
        
        Producer<String, JNData> producer = new Producer<String, JNData>(config);
        
        producer.send(new KeyedMessage<String, JNData>(topic, key, inData));
        producer.close();
        
	}

}
