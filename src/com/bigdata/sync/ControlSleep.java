package com.bigdata.sync;

import com.cetc.remote.Agent;
import com.cetc.remote.AliasServer;

public class ControlSleep implements InterfaceCS {

	private volatile Boolean flag = false;
	
	public static void main(String[] args) {}
	
	public void sleep(final short id, final long millis) {
		synchronized(this) {
			if (flag)
				return;
			flag = true;
		}
		System.out.printf("Master receives SLEEP(%d) for (%d) milliseconds.\n", id, millis);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
				} finally {
					MasterServer.INSTANCE.remote("radar.master").data().processMsgAwake(id);
					flag = false;
				}
			}
		}).start();
	}
}

class SleepAgent extends Agent<InterfaceCS, ControlSleep> {
	public static final SleepAgent INSTANCE = new SleepAgent();
	private SleepAgent() {
		super(InterfaceCS.class, ControlSleep.class);
	}
}

class SleepServer extends AliasServer<InterfaceCS> {
	public static final SleepServer INSTANCE = new SleepServer();
	private SleepServer() {
		super(InterfaceCS.class);
	}
}