//package com.bigdata.kafka;
//
//import
// kafka.javaapi.producer.Producer;
//import kafka.producer.KeyedMessage;
//import kafka.producer.ProducerConfig;
//
//import storm.DataClass.JNData;
//import storm.DataClass.JamSignalData;
//import storm.DataClass.NonJamRTSData;
//
//import java.util.Properties;
//
///**
// * Created by cloud on 16-1-7.
// */
//public class TestProducer {
//    public static String brokerlist = "localhost:9092";// ip1:port1,ip2:port2,...
////    public static String brokerlist = "ubuntu2:9092";
//    public static String topic = "p";
//
//    public static void main(String[] args) throws InterruptedException {
//        //BasicConfigurator.configure();
//
//        Properties props = new Properties();
//        props.put("metadata.broker.list", brokerlist);
//        //props.put("serializer.class","kafka.serializer.StringEncoder");
//        //props.put( "serializer.class", "kafka.serializer.DefaultEncoder");							/*The kafka.serializer.DefaultEncoder is not actually an encoder: it just takes a byte[] and returns it. */
//        props.put( "serializer.class", "com.bigdata.kafka.DataKafkaKryoEncoder");
//        props.put("partitioner.class", "com.bigdata.kafka.SimplePartitioner");
//        props.put("request.required.acks", "1");
//        
//        ProducerConfig config = new ProducerConfig(props);
//
//        producedata(config, topic);
//        System.out.println("Kafka producer is closed");
//        //System.out.println(new Date().getTime());
//    }
//    public static void producedata(ProducerConfig config, String topic) throws InterruptedException {
//        Producer<String, JNData> producer = new Producer<String, JNData>(config);
//        short default1 = 0;
//        int packno = 0;
//        while (true) {
//            Thread.sleep(200);
//            default1++;
//            for(int Tid = 1; Tid < 11; Tid++){
//                for (int i=1; i<=7; i++ ) {
//                    JNData jnData = produce(default1,Tid,i);
//                   // JNData jnData2 = produce2(default1,Tid,i);
//                    packno++;
//                    producer.send(new KeyedMessage<String, JNData>(topic, String.valueOf(packno), jnData));
////                    packno++;
////                    producer.send(new KeyedMessage<String, JNData>(topic, String.valueOf(packno), jnData2));
//                }
//            }
//            System.out.println("Default1--->" + default1);
//        }
////        producer.close();
//    }
//
//    public static JNData produce(short default1,int Tid, int Rid) {
//        JNData jnData = new JNData();
//        JamSignalData jam = new JamSignalData();
//        NonJamRTSData nonJam = new NonJamRTSData();
//        jnData.hasJam = true;
//        jnData.hasNonJam = true;
//        jam.Default1 = default1;
//        jam.Rid = Rid;
//        jam.Tid = Tid;
//        jam.TBeamID = 1;
//        jnData.jamData = jam;
//        
//        nonJam.Default1 = default1;
//        nonJam.Rid = Rid;
//        nonJam.Tid = Tid;
//        nonJam.TBeamID = 1;
//        jnData.nonJamData = nonJam;
//        return jnData;
//    }
//
//    public static JNData produce2(short default1,int Tid, int Rid) {
//        JNData jnData = new JNData();
//        NonJamRTSData nonJam = new NonJamRTSData();
////        JamSignalData jam = new JamSignalData();
//        jnData.hasJam = false;
//        jnData.hasNonJam = true;
//        nonJam.Default1 = default1;
//        nonJam.Rid = Rid;
//        nonJam.Tid = Tid;
//        nonJam.TBeamID = 1;
//        jnData.nonJamData = nonJam;
//        return jnData;
//    }
//}
