package com.bigdata.DataClass;

import java.io.Serializable;

public class MasterTable implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private short Default1;
	
	private long recvTime;
	
	private int finishNum;
	
	private int startNum;
	
	public short getDefault1() {
		return Default1;
	}

	public void setDefault1(short default1) {
		Default1 = default1;
	}
	
	public long getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(long recvTime) {
		this.recvTime = recvTime;
	}

	public int getFinishNum() {
		return finishNum;
	}

	public void setFinishNum(int finishNum) {
		this.finishNum = finishNum;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	
}
