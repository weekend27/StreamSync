package com.bigdata.sync;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import com.bigdata.DataClass.*;
import com.cetc.remote.Agent;
import com.cetc.remote.AliasServer;

public class ControlMessage implements Master  {
	
	LinkedList<MasterTable> mlist = new LinkedList<MasterTable>();
	public static final long T = ConfData.readProperties().allNodesWaitTime;			// SLEEP Threshold is 5 seconds
	private String[] servers = {"radar.slave1"};   //, "radar.slave2"
	private volatile Boolean flag = false;
	
	
	public void processMsgFirst(short default1) {		// message: FIRST
		ListIterator<MasterTable> lit = (ListIterator<MasterTable>) mlist.iterator();
		long currTime;
		long gapTime;
		MasterTable tempMT = new MasterTable();
		int tempSN;
		int tempIndex;
		boolean signAdd = false;
		
		
		if (mlist.isEmpty()) {
			currTime = new Date().getTime();
			tempMT.setDefault1(default1);
			tempMT.setRecvTime(currTime);
			tempMT.setStartNum(1);
			tempMT.setFinishNum(0);
			mlist.add(tempMT);
			signAdd = true;
			sleep(default1, T);		// trigger the sleep process
		} else {
			while (lit.hasNext()) {
				tempMT = lit.next();
				tempIndex = mlist.indexOf(tempMT);
				if (default1 == tempMT.getDefault1()) {
					currTime = new Date().getTime();
					gapTime = currTime - tempMT.getRecvTime();
					System.out.println("***New gapTime = " + gapTime);
					if (gapTime >= T) {
						save(default1);
						System.out.println("MASTER SAVE===OVERTIME--->" + default1);
						mlist.remove(tempMT);
						signAdd = true;
						break;
					} else {
						tempSN = tempMT.getStartNum();
						tempSN++;
						tempMT.setStartNum(tempSN);
						mlist.set(tempIndex, tempMT);
						signAdd = true;
						break;
					}
				}
			}
		}
		if (signAdd == false) {
			currTime = new Date().getTime();
			tempMT.setDefault1(default1);
			tempMT.setRecvTime(currTime);
			tempMT.setStartNum(1);
			tempMT.setFinishNum(0);
			mlist.add(tempMT);
			signAdd = true;
			sleep(default1, T);		// trigger the sleep process
		}
	}

	public void processMsgLast(short default1) {		// message: LAST
		ListIterator<MasterTable> lit = (ListIterator<MasterTable>) mlist.iterator();
		long currTime;
		long gapTime;
		MasterTable tempMT = new MasterTable();
		int tempFN;
		int tempIndex;
		
		while (lit.hasNext()) {
			tempMT = lit.next();
			tempIndex = mlist.indexOf(tempMT);
			if (default1 == tempMT.getDefault1()) {
				currTime = new Date().getTime();
				gapTime = currTime - tempMT.getRecvTime();
				if (gapTime >= T) {
					save(default1);
					System.out.println("MASTER SAVE===OVERTIME--->" + default1);
					mlist.remove(tempMT);
					break;
				} else {
					tempFN = tempMT.getFinishNum();
					tempFN++;
					if (tempFN == servers.length) {			// all servers send LAST message
						save(default1);
						System.out.println("MASTER SAVE===FINISH--->" + default1);
						mlist.remove(tempMT);
						break;
					} else {
						tempMT.setFinishNum(tempFN);
						mlist.set(tempIndex, tempMT);
						break;
					}
				}
			}
		}
	}
	
	public void processMsgAwake(short default1) {		// message: AWAKE
		ListIterator<MasterTable> lit = (ListIterator<MasterTable>) mlist.iterator();
		long currTime;
		long gapTime;
		long sleepTime;
		MasterTable tempMT = new MasterTable();
		short retDefault1;
		boolean signAwake = false;
		
		while (lit.hasNext()) {
			tempMT = lit.next();
			if (default1 == tempMT.getDefault1()) {
				save(default1);
				System.out.println("MASTER SAVE===AWAKE--->" + default1);
				mlist.remove(tempMT);
				signAwake = true;
				break;
			}
		}
		if (signAwake == false && !mlist.isEmpty()) {
			retDefault1 = mlist.getFirst().getDefault1();
			currTime = new Date().getTime();
			gapTime = currTime - mlist.getFirst().getRecvTime();
			sleepTime = T - gapTime;
			sleep(retDefault1, sleepTime);
		}
	}
	
	public void sleep(final short default1, final long sleepTime) {
		
		synchronized(this) {
			if (flag)
				return;
			flag = true;
		}
		System.out.printf("Master receives SLEEP(%d) for (%d) milliseconds.\n", default1, sleepTime);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				} finally {
					MasterServer.INSTANCE.remote("radar.master").data().processMsgAwake(default1);
					flag = false;
				}
			}
		}).start();
		
	}

	private void save(short id) {
		for (String server : servers) {
			SlaveServer.INSTANCE.remote(server).data().save(id);
		}
	}
	
}

class MasterAgent extends Agent<Master, ControlMessage> {
	public static final MasterAgent INSTANCE = new MasterAgent();
	private MasterAgent() {
		super(Master.class, ControlMessage.class);
	}
}

class MasterServer extends AliasServer<Master> {
	public static final MasterServer INSTANCE = new MasterServer();
	private MasterServer() {
		super(Master.class);
	}
}