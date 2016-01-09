package com.bigdata.DataClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfData {
	public long allNodesWaitTime;
	public int arrayLen;
	
	public ConfData() {}
	
	public static ConfData readProperties() {
		Properties prop = new Properties();
		ConfData cd = new ConfData();
		try {
			prop.load(new FileInputStream("src/sync.properties"));
			cd.allNodesWaitTime = Long.valueOf(prop.getProperty("allNodesWaitTime")).longValue();
			cd.arrayLen = Integer.parseInt(prop.getProperty("arrayLen"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cd;
	}
}
