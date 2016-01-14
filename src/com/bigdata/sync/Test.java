package com.bigdata.sync;

import java.util.Date;
import java.util.Random;

import com.cetc.remote.*;
import com.bigdata.DataClass.*;

@SuppressWarnings("unused")
public class Test {

	public static void main(String[] args) throws InterruptedException {
		// start server1-n first, and then start control
//		control();
//		server("radar.slave1");
//		server("radar.slave2");
//		server("radar.slave3");
//		server("radar.slave4");
//		server("radar.slave5");
//		server("radar.slave6");
//		server("radar.slave7");
	}

	private static void control() throws InterruptedException {
		BDPSystem.init(new BDPSystem.SystemConfig("radar.master"));
		MasterAgent.INSTANCE.local().data();
		SlaveServer.INSTANCE.remote("radar.slave1").data().test();
		SlaveServer.INSTANCE.remote("radar.slave2").data().test();
		SlaveServer.INSTANCE.remote("radar.slave3").data().test();
		SlaveServer.INSTANCE.remote("radar.slave4").data().test();
//		SlaveServer.INSTANCE.remote("radar.slave5").data().test();
//		SlaveServer.INSTANCE.remote("radar.slave6").data().test();
//		SlaveServer.INSTANCE.remote("radar.slave7").data().test();
	}

	private static Slave server(String key) throws InterruptedException {
		BDPSystem.init(new BDPSystem.SystemConfig(key));
		return SlaveAgent.INSTANCE.local().data();
	}

}
