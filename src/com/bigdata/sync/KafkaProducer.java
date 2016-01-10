package com.bigdata.sync;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.bigdata.DataClass.ConfData;
import com.bigdata.DataClass.JNData;

public class KafkaProducer {
	
	public static String brokerlist = ConfData.readProperties().brokerlist;
	public static String topic = ConfData.readProperties().topic;
	
	public static void producer(String key, JNData inData) {
		
		System.out.println("@@@@KAFKA PRODUCER KEY===>" + key);
		System.out.println("@@@@KAFKA PRODUCER DEFAULT1===>" + inData.getDefault1());
		
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
