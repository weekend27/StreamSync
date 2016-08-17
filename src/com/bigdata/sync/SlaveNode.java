package com.bigdata.sync;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;

import com.cetc.remote.*;

import dataClass.ConfData;
import dataClass.JNData;
import dataClass.JamSignalData;
import dataClass.NonJamRTSData;
import dataClass.Package;
import dataClass.RFNodeData;
import preprocess.MiddleClass;
import preprocess.RTSData;

@SuppressWarnings("unused")
public class SlaveNode implements Slave, HandleData {

	LinkedList<Package> packageList = new LinkedList<Package>();
	Set<Short> isDeletedSet = new HashSet<Short>();
	public static final int ARRAYLEN = ConfData.readProperties().arrayLen;

	public void save(short default1) {

		//System.out.println("**************SAVE START**************");

		synchronized(this) {

			isDeletedSet.add(default1);

			ListIterator<Package> lit = (ListIterator<Package>) packageList.iterator();
			int tempCount;
			int len;
			String key = null;
			Package tempPackage = null;

			System.out.println("[SAVE] save Default1--->" + default1);

			if (!packageList.isEmpty()) {    		// find the target data
				while (lit.hasNext()) {
					tempPackage = lit.next();
					if (default1 == tempPackage.Default1) {
						packageList.remove(tempPackage);  				// delete data in the linked list
						break;
					}
				}
			}

			// send data to Kafka
			if (tempPackage != null) {
				JNData[] tempDataArray = tempPackage.DataArray;
				tempCount = tempPackage.count;
				len = tempDataArray.length;
				for (int i = 0; i < len; i++) {
					if (tempDataArray[i] != null) {
						key = (default1 + "") + "|" + (tempCount + "");
						if (key != null) {
							//System.out.println("**************send to Kafka START**************");
							System.out.println("[SEND] send to Kafka: Key--->" + key);
							System.out.println("[SEND] send to Kafka: Default1--->" + tempDataArray[i].Default1);
							System.out.println("[SEND] send to Kafka: Default2--->" + tempDataArray[i].Default2);
							System.out.println("[SEND] send to Kafka: Rid--->" + tempDataArray[i].Rid);
							System.out.println("[SEND] send to Kafka: Tid--->" + tempDataArray[i].Tid);
							System.out.println("[SEND] send to Kafka: FrameNo--->" + tempDataArray[i].FrameNo);
							System.out.println("[SEND] send to Kafka: TBeamID--->" + tempDataArray[i].TBeamID);
							System.out.println("[SEND] send to Kafka: PackageNO--->" + tempDataArray[i].PackageNO);
							System.out.println("[SEND] send to Kafka: totalSize1--->" + tempDataArray[i].totalSize1);
							System.out.println("[SEND] send to Kafka: totalSize2--->" + tempDataArray[i].totalSize2);
//							System.out.println("waveData1[85] = " + tempDataArray[i].waveData1[85]);
//							System.out.println("waveData1[86] = " + tempDataArray[i].waveData1[86]);
							System.out.println("[SEND] send to Kafka: waveData1.length--->" + tempDataArray[i].waveData1.length);
							System.out.println("[SEND] send to Kafka: waveData2.length--->" + tempDataArray[i].waveData2.length);
							//System.out.println("**************send to Kafka END**************");
							KafkaProducer.producer(key, tempDataArray[i]);
						}
					}
				}
			}
		}
		//System.out.println("**************SAVE END**************");
	}

	public synchronized void processData(JNData inData) {
		ListIterator<Package> lit = (ListIterator<Package>) packageList.iterator();
		short tempDefault1;
		int tempCount;
		boolean signAdd = false;
		int tempIndex;

		//System.out.println("OUTSIDE");
		//System.out.println("<<<<<<<<<<<<<packageList.size() = " + packageList.size());
		System.out.println("[SYNC] synchronization process: default1 = " + inData.Default1);
		System.out.println("[SYNC] synchronization process: default2 = " + inData.Default2);

		if (isDeletedSet.isEmpty() || !isDeletedSet.contains(inData.Default1)) {    // make sure that this default1 has not been deleted before

			//System.out.println("INSIDE");
			//System.out.println("<<<<<<<<<<<<<isDeletedSet.size() = " + isDeletedSet.size());

			if (packageList.isEmpty()) {			// if the packageList is empty, add inData to packageList
				Package tempPackage = new Package();
				JNData[] tempDataArray = new JNData[ARRAYLEN];
				tempDataArray = tempPackage.DataArray;
				tempPackage.Default1 = inData.Default1;
				tempPackage.count = 1;
				tempDataArray[0] = new JNData();
				tempDataArray[0] = inData;
				tempPackage.DataArray = tempDataArray;
				packageList.add(tempPackage);
				signAdd = true;
				first(inData.Default1);			// send first message to control node
				System.out.println("[FRST] first default2 = " + inData.Default2);
				if (inData.Default2 == 1) {			// check if it triggers the sending condition, if so, send last message
					last(inData.Default1);
				}
			}
			else {			// if the packageList is not empty, traverse packageList and find the inserting position
				while (lit.hasNext()) {
					Package tempPackage = new Package();
					JNData[] tempDataArray = new JNData[ARRAYLEN];
					tempPackage = lit.next();
					tempDataArray = tempPackage.DataArray;
					tempDefault1 = tempPackage.Default1;
					if (tempDefault1 == inData.Default1) {		// find the inserting position before traversal is over
						tempIndex = packageList.indexOf(tempPackage);		// record the inserting position
						tempCount = tempPackage.count;
						tempDataArray[tempCount] = inData;
						tempCount++;
						tempPackage.count= tempCount;
						tempPackage.DataArray = tempDataArray;
						packageList.set(tempIndex, tempPackage);			// insert inData
						signAdd = true;
						if (tempCount == inData.Default2) {			// check if it triggers the sending condition, if so, send last message
							last(inData.Default1);
							System.out.println("[LAST] last default2 = " + inData.Default2);
						}
						break;
					}
				}
				if (signAdd == false) {			// if there is no position to insert inData, insert it to the tail of packageList
					Package tempPackage = new Package();
					JNData[] tempDataArray = new JNData[ARRAYLEN];
					tempDataArray = tempPackage.DataArray;
					tempPackage.Default1 = inData.Default1;
					tempPackage.count = 1;
					tempDataArray[0] = inData;
					tempPackage.DataArray = tempDataArray;
					packageList.add(tempPackage);
					signAdd = true;
					first(inData.Default1);    // send first message to control node
					System.out.println("[FRST] first default2 = " + inData.Default2);
					if (inData.Default2 == 1) {    // check if it triggers the sending condition, if so, send last message
						last(inData.Default1);
						System.out.println("[LAST] last default2 = " + inData.Default2);
					}
				}
			}
		}
	}


	public void handleData(final dataClass.RFNodeData nodeData) {
		// record raw data
		if(nodeData.TBeamID == 1) {
			writeData(nodeData);
		}

		processData(SlaveNode.preProcessData(nodeData));

		/*new Thread(new Runnable() {
			@Override
			public void run() {
				processData(SlaveNode.preProcessData(nodeData));
			}
		}).start();*/

	}


	public static void deleteFile() {
		File file = new File("BeforePrepData");
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				f.delete();
			}
		}
	}

	public static void writeData(RFNodeData data) {
		File file;
		FileWriter fw;

		try {
			file = new File(System.getProperty("user.home" ) + "/BeforePrepData/");
			if (!file.exists() || !file.isDirectory()) {
				file.mkdirs();
			}
			file = new File(System.getProperty("user.home" ) + "/BeforePrepData/" + "beforePrepData-FrameNo" + data.FrameNo + "-TBeamID" + data.TBeamID
					+ "-Rid" + data.Rid + "-Tid" + data.Tid);
			fw = new FileWriter(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			fw.write(data.PackageNO + "\r\n");
			fw.write(data.FrameNo + "\r\n");
			fw.write(data.TBeamID + "\r\n");
			fw.write(data.time + "\r\n");
			fw.write(data.Rid + "\r\n");
			fw.write(data.Tid + "\r\n");
			fw.write(data.RBeamNum + "\r\n");
			fw.write(data.PulseNum + "\r\n");
			fw.write(data.dataType + "\r\n");
			fw.write(data.ajFlag + "\r\n");
			fw.write(data.Default1 + "\r\n");
			fw.write(data.Default2 + "\r\n");
			fw.write(data.totalSize + "\r\n");
			for (int i = 0; i < data.waveData.length; i++) {
				fw.write(data.waveData[i] + " ");
			}
			fw.close();
			System.out.println("写文件完成！！！");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@SuppressWarnings("static-access")
	public static JNData preProcessData(RFNodeData nodeData){
		MiddleClass middle = new MiddleClass();
		middle.setPackageNO(nodeData.PackageNO);
		middle.setFrameNo(nodeData.FrameNo);
		middle.setTBeamID(nodeData.TBeamID);
		middle.settime(nodeData.time);
		middle.setRid(nodeData.Rid);
		middle.setTid(nodeData.Tid);
		middle.setRBeamNum(nodeData.RBeamNum);
		middle.setPulseNum(nodeData.PulseNum);
		middle.setdataType(nodeData.dataType);
		middle.setajFlag(nodeData.ajFlag);
		middle.setDefault1(nodeData.Default1);
		middle.setDefault2(nodeData.Default2);
		middle.settotalSize(nodeData.totalSize);
		middle.setwaveData(nodeData.waveData);

		middle.execProcess();

		JNData result = new JNData();
		result.PackageNO = middle.getPackageNO();
		result.FrameNo =  middle.getFrameNo();
		result.TBeamID =  middle.getTBeamID();
		result.time =  middle.gettime();
		result.Rid =  middle.getRid();
		result.Tid =  middle.getTid();
		result.RBeamNum = middle.getRBeamNum();
		result.PulseNum = middle.getPulseNum();
		result.dataType = middle.getdataType();
		result.ajFlag = middle.getajFlag();
		result.Default1 = middle.getDefault1();
		result.Default2 = middle.getDefault2();
		result.totalSize1 = middle.gettotalSize1();
		result.totalSize2 = middle.gettotalSize2();
		result.waveData1 = middle.getwaveData1();
		result.waveData2 = middle.getwaveData2();

	    return result;
	}

	public void first(short default1) {				// send first message to control node
		System.out.println("[FRST] first default1 = " + default1);
		// 调用远程Master的处理方法processMsgFirst
		MasterServer.INSTANCE.remote(ConfData.readProperties().master).data().processMsgFirst(default1);
	}

	public void last(short default1) {			// send last message to control node
		System.out.println("[LAST] last default1 = " + default1);
		// 调用远程Master的处理方法processMsgFirst
		MasterServer.INSTANCE.remote(ConfData.readProperties().master).data().processMsgLast(default1);
	}

}

class SlaveAgent extends BaseAgent<Slave, SlaveNode> {
	public static final SlaveNode server = new SlaveNode();
	public static final SlaveAgent INSTANCE = new SlaveAgent();
	private SlaveAgent() {
		super(Slave.class, SlaveNode.class, server);
	}
}

class HandleDataAgent extends BaseAgent<HandleData, SlaveNode> {
	public static final HandleDataAgent INSTANCE = new HandleDataAgent();
	private HandleDataAgent() {
		super(HandleData.class, SlaveNode.class, SlaveAgent.server);
	}
}

class SlaveServer extends BaseServer<Slave> {
	public static final SlaveServer INSTANCE = new SlaveServer();
	private SlaveServer() {
		super(Slave.class);
	}
}
