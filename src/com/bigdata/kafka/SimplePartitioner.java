package com.bigdata.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner {
	public SimplePartitioner (VerifiableProperties props) {
		
	}
	
	public int partition(Object key, int a_numPatitions) {
		int partition = 0;
		String stringKey = (String) key;
		int offset  = stringKey.lastIndexOf('.');
		if (offset > 0) {
			partition = Integer.parseInt(stringKey.substring(offset+1)) % a_numPatitions;
		}
		return partition;
	}
}
