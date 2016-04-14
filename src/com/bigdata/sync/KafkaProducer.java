package com.bigdata.sync;

import java.util.Properties;

import dataClass.ConfData;
import dataClass.JNData;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {
	
	public static String brokerlist = ConfData.readProperties().brokerlist;
	public static String topic = ConfData.readProperties().topic;
	
	public static void producer(String key, JNData inData) {
		
		//System.out.println("**************Kafka receivess data START**************");
		System.out.println("[RECV] Kafka receives: Key===>" + key);
		System.out.println("[RECV] Kafka receives: Default1===>" + inData.Default1);
		System.out.println("[RECV] Kafka receives: Default2===>" + inData.Default2);
		System.out.println("[RECV] Kafka receives: Rid===>" + inData.Rid);
		System.out.println("[RECV] Kafka receives: Tid===>" + inData.Tid);
		System.out.println("[RECV] Kafka receives: FrameNo===>" + inData.FrameNo);
		System.out.println("[RECV] Kafka receives: TBeamID===>" + inData.TBeamID);
		System.out.println("[RECV] Kafka receives: PackageNO===>" + inData.PackageNO);
		System.out.println("[RECV] Kafka receives: totalSize1===>" + inData.totalSize1);
		System.out.println("[RECV] Kafka receives: totalSize2===>" + inData.totalSize2);
		System.out.println("[RECV] Kafka receives: waveData1.length===>" + inData.waveData1.length);
		System.out.println("[RECV] Kafka receives: waveData2.length===>" + inData.waveData2.length);
		//System.out.println("**************Kafka receives data END**************");
		
		
		Properties props = new Properties();
        props.put("metadata.broker.list", brokerlist);
        props.put("serializer.class", "com.bigdata.kafka.DataKafkaKryoEncoder");			// need to update
        props.put("partitioner.class", "com.bigdata.kafka.SimplePartitioner");				// need to update
//        props.put("send.buffer.bytes", "100000000");
        props.put("request.required.acks", "1");
        
        ProducerConfig config = new ProducerConfig(props);
        
        Producer<String, JNData> producer = new Producer<String, JNData>(config);
        
        producer.send(new KeyedMessage<String, JNData>(topic, key, inData));
        producer.close();
        
	}

}
