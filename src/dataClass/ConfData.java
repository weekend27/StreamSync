package dataClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfData {
	public static ConfData instance = null;
	public long allNodesWaitTime;
	public int arrayLen;
	public String brokerlist;
	public String topic;
	public String[] servers;
	public String master;
	public String libpreprocessPath;
	public String akkaPath;
	
	public ConfData() {}
	
	public static void main(String[] args) {
		ConfData.readProperties();
	}
	
	public static ConfData readProperties() {
		if (instance != null) {
			return instance;
		}
		Properties prop = new Properties();
		ConfData cd = new ConfData();
		try {
			prop.load(new FileInputStream(System.getProperty("user.home")  + "/RadarSync/sync.properties"));
			cd.libpreprocessPath = System.getProperty("user.home")  + prop.getProperty("libppPath");
			cd.akkaPath = System.getProperty("user.home")  + prop.getProperty("akkaPath");
			cd.allNodesWaitTime = Long.valueOf(prop.getProperty("allNodesWaitTime")).longValue();
			cd.arrayLen = Integer.parseInt(prop.getProperty("arrayLen"));
			cd.brokerlist = prop.getProperty("brokerlist");
			cd.topic = prop.getProperty("topic");
			cd.master = prop.getProperty("master");
			String serverStr = prop.getProperty("serverslist");
			if (serverStr != null) {
				cd.servers = serverStr.replaceAll("\\s", "").split(",");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instance = cd;
	}
}
