package com.bigdata.sync;

import java.util.Date;
import java.util.Random;

import com.cetc.remote.*;
import com.bigdata.DataClass.*;

@SuppressWarnings("unused")
public class Test {
	static Random r = new Random();
	public static void main(String[] args) throws InterruptedException {     // run control first, then run server1, server2, server3 ......
//		control();
//		server("radar.slave1");
		server("radar.slave2");
	}
	
	
	private static void control() {
		BDPSystem.init(new BDPSystem.SystemConfig("radar.master"));
		MasterAgent.INSTANCE.local().data();
		SleepAgent.INSTANCE.local().data();
	}
	
	private static void server(String key) throws InterruptedException {
		
		BDPSystem.init(new BDPSystem.SystemConfig(key));
		SlaveAgent.INSTANCE.local().data();
		ServerMessage s = SlaveAgent.server;
		long PackageNO = Math.abs(r.nextLong());												/*数据包编号*/
		int FrameNo = Math.abs(r.nextInt());														/*帧号*/
		int TBeamID;																								/*发射波位编号*/
		double time = Math.abs(r.nextDouble());												/*发射波位开始扫描时间*/
		int Rid;																										/*接收节点ID*/
		int Tid;																										/*发射节点ID*/
		int RBeamNum = Math.abs(r.nextInt());													/*接收波束数量*/
		int PulseNum = Math.abs(r.nextInt() % 1001);   										/*脉冲数量 1~1000*/
		short dataType = (short) (Math.abs(r.nextInt() % 6) + 1);						/*数据类型 1-6*/
		short ajFlag;																								/*抗干扰标志 0 or 1*/
		long totalSize = 100;																					/*回波数据总长度*/
		short[] waveData = {1,3,5,7,9,2,4,6,8,0};													/*目标回波数据 保存20个通道、30个脉冲的数据*/
		short Default1;																							/*预留字段1: increasing send number*/
		int Default2;																								/*预留字段2: 7 packages at most*/
		
		for (short i = 1; i <= 20; i++) {
			int total = r.nextInt(7) + 1;
//			int total = 7;
			for (int j = 1; j <= total; j++) {
				int millis = r.nextInt(200);
				Thread.sleep((long)millis);
				
				System.out.printf("i = %s, j = %s\n", i, j);
				ajFlag = (short) Math.abs(r.nextInt() % 2);
				Rid = j;
				Tid = Math.abs(r.nextInt());
				TBeamID  = Math.abs(r.nextInt());
				
				RFNodeData rData = new RFNodeData();
				rData.ajFlag = ajFlag;
				rData.Rid = Rid;
				rData.Tid = Tid;
				rData.TBeamID = TBeamID;
				rData.Default1 = i;
				rData.Default2 = total;
				
				s.processData(preProcessData(rData));
			}
		}
		
	}
	
	public static JNData preProcessData(RFNodeData preData){
	    JNData jnData = new JNData();
	    JamSignalData jam = new JamSignalData();
	    NonJamRTSData nonJam = new NonJamRTSData();
	    jam = process1(preData);
	    nonJam = process2(preData);
	    int flag = Math.abs(r.nextInt() % 3);    // produce random number: 0,1,2
//	    int flag = 2;
	    if (flag == 0) {						// jnData has only jam
	    	jnData.jamData = jam;
	        jnData.hasJam = true;
	        jnData.nonJamData = null;
	        jnData.hasNonJam = false;
	        jnData.setDefault1(jam.Default1);
	        jnData.setDefault2(jam.Default2);
	    } else if (flag == 1) {			// jnData has only nonJam
	    	jnData.jamData = null;
	        jnData.hasJam = false;
	        jnData.nonJamData = nonJam;
	        jnData.hasNonJam = true;
	        jnData.setDefault1(jam.Default1);
	        jnData.setDefault2(jam.Default2);
	    } else {   								// jnData has both of jam and nonJam
	    	jnData.jamData = jam;
	        jnData.hasJam = true;
	        jnData.nonJamData = nonJam;
	        jnData.hasNonJam = true;
	        jnData.setDefault1(jam.Default1);
	        jnData.setDefault2(jam.Default2);
	    }
	    
	    return jnData;
	}
	
	public static JamSignalData process1 (RFNodeData data) {
	    JamSignalData jam = new JamSignalData();
	    jam.ajFlag = data.ajFlag;
	    jam.Rid = data.Rid;
	    jam.Tid = data.Tid;
	    jam.TBeamID = data.TBeamID;
	    jam.Default1 = data.Default1;
	    jam.Default2 = data.Default2;
	    return jam;
	}
	
	public static NonJamRTSData process2 (RFNodeData data) {
		NonJamRTSData nonJam = new NonJamRTSData();
		nonJam.ajFlag = data.ajFlag;
		nonJam.Rid = data.Rid;
		nonJam.Tid = data.Tid;
		nonJam.TBeamID = data.TBeamID;
		nonJam.Default1 = data.Default1;
		nonJam.Default2 = data.Default2;
	    return nonJam;
	}
	
}