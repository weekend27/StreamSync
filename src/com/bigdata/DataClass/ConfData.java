package com.bigdata.DataClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfData {
	public long allNodesWaitTime;
	public int arrayLen;
	public String brokerlist;
	public String topic;
	public String[] servers;
	public String server;
	public int serverNum;
	
	public ConfData() {}
	
	public static void main(String[] args) {
		ConfData.readProperties();
	}
	
	public static ConfData readProperties() {
		Properties prop = new Properties();
		ConfData cd = new ConfData();
		try {
			prop.load(new FileInputStream("src/sync.properties"));
			cd.allNodesWaitTime = Long.valueOf(prop.getProperty("allNodesWaitTime")).longValue();
			cd.arrayLen = Integer.parseInt(prop.getProperty("arrayLen"));
			cd.brokerlist = prop.getProperty("brokerlist");
			cd.topic = prop.getProperty("topic");
			cd.server = prop.getProperty("server");
			cd.serverNum = Integer.parseInt(prop.getProperty("serverNum"));
			cd.servers = new String[cd.serverNum];
			for (int i = 1; i <= cd.serverNum; i++) {
				cd.servers[i-1] = cd.server + ("" + i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cd;
	}
}
