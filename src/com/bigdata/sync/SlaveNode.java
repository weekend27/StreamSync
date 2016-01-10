package com.bigdata.sync;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import com.bigdata.DataClass.ConfData;
import com.bigdata.DataClass.JNData;
import com.bigdata.DataClass.Package;
import com.cetc.remote.Agent;
import com.cetc.remote.AliasServer;

public class SlaveNode implements Slave {
	
	LinkedList<Package> packageList = new LinkedList<Package>();
	Set<Short> isDeletedSet = new HashSet<Short>();
	public static final int ARRAYLEN = ConfData.readProperties().arrayLen;
	
	public void save(short default1) {
		
		System.out.println("((((((((((((((((((((((SAVE))))))))))))))))))))))))");
		
		synchronized(this) {
			
			isDeletedSet.add(default1);
			
			ListIterator<Package> lit = (ListIterator<Package>) packageList.iterator();
			int tempCount;
			int len;
			String key = null;
			Package tempPackage = null;
			
			System.out.println("SAVE DEFAULT1--->" + default1);
			
			if (!packageList.isEmpty()) {    		// find the target data
				while (lit.hasNext()) {
					tempPackage = lit.next();
					if (default1 == tempPackage.getDefault1()) {
						packageList.remove(tempPackage);  				// delete data in the linked list
						break;
					}
				}
			}
			
			// send data to Kafka
			JNData[] tempDataArray = tempPackage.getDataArray();
			tempCount = tempPackage.getCount();
			len = tempDataArray.length;
			for (int i = 0; i < len; i++) {
				if (tempDataArray[i] != null) {
					key = (default1 + "") + "|" + (tempCount + "");
					if (key != null) {
						System.out.println("####SEND TO KAFKA: KEY--->" + key);
						System.out.println("####SEND TO KAFKA: DEFAULT1--->" + tempDataArray[i].getDefault1());
						KafkaProducer.producer(key, tempDataArray[i]);
					}
				}
			}
			
		}
	}
	
	public synchronized void processData(JNData inData) {
		ListIterator<Package> lit = (ListIterator<Package>) packageList.iterator();
		short tempDefault1;
		int tempCount;
		boolean signAdd = false;
		int tempIndex;
		
		System.out.println("<<<<<<<<<<<<<packageList.size() = " + packageList.size());
		
		if (isDeletedSet.isEmpty() || !isDeletedSet.contains(inData.getDefault1())) {    // make sure that this default1 has not been deleted before
			
			System.out.println("<<<<<<<<<<<<<isDeletedSet.size() = " + isDeletedSet.size());
			
			if (packageList.isEmpty()) {			// if the packageList is empty, add inData to packageList
				Package tempPackage = new Package();
				JNData[] tempDataArray = new JNData[ARRAYLEN];
				tempDataArray = tempPackage.getDataArray();
				tempPackage.setDefault1(inData.getDefault1());
				tempPackage.setCount(1);
				tempDataArray[0] = new JNData();
				tempDataArray[0] = inData;
				tempPackage.setDataArray(tempDataArray);
				packageList.add(tempPackage);
				signAdd = true;
				first(inData.getDefault1());			// send first message to control node
				if (inData.getDefault2() == 1) {			// check if it triggers the sending condition, if so, send last message
					last(inData.getDefault1());
				}
			}
			else {			// if the packageList is not empty, traverse packageList and find the inserting position
				while (lit.hasNext()) {
					Package tempPackage = new Package();
					JNData[] tempDataArray = new JNData[ARRAYLEN];
					tempPackage = lit.next();
					tempDataArray = tempPackage.getDataArray();
					tempDefault1 = tempPackage.getDefault1();
					if (tempDefault1 == inData.getDefault1()) {		// find the inserting position before traversal is over
						tempIndex = packageList.indexOf(tempPackage);		// record the inserting position
						tempCount = tempPackage.getCount();
						tempDataArray[tempCount] = inData;
						tempCount++;
						tempPackage.setCount(tempCount);
						tempPackage.setDataArray(tempDataArray);
						packageList.set(tempIndex, tempPackage);			// insert inData
						signAdd = true;
						if (tempCount == inData.getDefault2()) {			// check if it triggers the sending condition, if so, send last message
							last(inData.getDefault1());
						}
						break;
					}
				}
				if (signAdd == false) {			// if there is no position to insert inData, insert it to the tail of packageList
					Package tempPackage = new Package();
					JNData[] tempDataArray = new JNData[ARRAYLEN];
					tempDataArray = tempPackage.getDataArray();
					tempPackage.setDefault1(inData.getDefault1());
					tempPackage.setCount(1);
					tempDataArray[0] = inData;
					tempPackage.setDataArray(tempDataArray);
					packageList.add(tempPackage);
					signAdd = true;
					first(inData.getDefault1());    // send first message to control node
					if (inData.getDefault2() == 1) {    // check if it triggers the sending condition, if so, send last message
						last(inData.getDefault1());
					}
				}
			}
		}
	}
	
	public void first(short default1) {				// send first message to control node
		System.out.println("~~~~first default1 = " + default1);
		MasterServer.INSTANCE.remote("radar.master").data().processMsgFirst(default1);
	}
	
	public void last(short default1) {			// send last message to control node
		System.out.println("^^^^last default1 = " + default1);
		MasterServer.INSTANCE.remote("radar.master").data().processMsgLast(default1);
	}

}


class SlaveAgent extends Agent<Slave, SlaveNode> {
	public static final SlaveNode server = new SlaveNode();
	public static final SlaveAgent INSTANCE = new SlaveAgent();
	private SlaveAgent() {
		super(Slave.class, SlaveNode.class, server);
	}
}

class SlaveServer extends AliasServer<Slave> {
	public static final SlaveServer INSTANCE = new SlaveServer();
	private SlaveServer() {
		super(Slave.class);
	}
}		
