package com.bigdata.sync;

import java.io.*;
import java.net.*;

import com.cetc.remote.*;
import com.typesafe.config.*;
import dataClass.ConfData;

public class Test {

	public static void main(String[] args) throws InterruptedException, UnknownHostException {
		ConfData conf = ConfData.readProperties();
		String myip = InetAddress.getLocalHost().getHostAddress();
		// 根据IP确定身份
		if (myip.equals(conf.master))
			control();
		else
			server();
	}

	private static void control() throws InterruptedException {
		BDPSystem.init(new BDPSystem.Configuration(getConfig()));
		MasterAgent.INSTANCE.local().data();
	}

	private static void server() throws InterruptedException {
		BDPSystem.init(new BDPSystem.Configuration(getConfig()));
		SlaveAgent.INSTANCE.local().data();
		HandleDataAgent.INSTANCE.local().data();
	}

	private static Config getConfig() {
		System.out.println(ConfData.readProperties().akkaPath);
		// 解析config/akka.conf文件
		return ConfigFactory.parseFile(new File(ConfData.readProperties().akkaPath));
	}
}
