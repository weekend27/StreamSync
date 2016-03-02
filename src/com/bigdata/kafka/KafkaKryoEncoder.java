package com.bigdata.kafka;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import kafka.serializer.Decoder;
import kafka.serializer.Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KafkaKryoEncoder<T> implements Encoder<T>, Decoder<T> {
	private Kryo kryo;
	private Class<T> clazz;
	
	public KafkaKryoEncoder(Class<T> clazz) {
		this.kryo = new Kryo();
		this.kryo.register(clazz);
		this.clazz = clazz;
	}
	
	@Override
	public byte[] toBytes(T arg0) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Output output = new Output(stream);
		kryo.writeObject(output, arg0);
		output.close();  // Also calls output.flush()
		return stream.toByteArray(); // Serialization done, get bytes
	}
	
	@Override
	public T fromBytes(byte[] bytes) {
		return kryo.readObject(new Input(new ByteArrayInputStream(bytes)), clazz);
	}
}
