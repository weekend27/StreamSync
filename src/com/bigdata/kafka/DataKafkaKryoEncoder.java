package com.bigdata.kafka;

import kafka.utils.VerifiableProperties;

import dataClass.JNData;

public class DataKafkaKryoEncoder extends KafkaKryoEncoder<JNData> {

	public DataKafkaKryoEncoder(VerifiableProperties props) {
		super(JNData.class);
	}

}
