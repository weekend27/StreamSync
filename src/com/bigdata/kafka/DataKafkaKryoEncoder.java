package com.bigdata.kafka;

import kafka.utils.VerifiableProperties;
import com.bigdata.DataClass.*;

public class DataKafkaKryoEncoder extends KafkaKryoEncoder<JNData> {

	public DataKafkaKryoEncoder(VerifiableProperties props) {
		//super(PreProcessedData.class);
		super(JNData.class);
		// TODO Auto-generated constructor stub
	}

}
