package com.bigdata.DataClass;

import java.io.Serializable;

public class Package implements Serializable{
	
	public static final int ARRAYLEN = 50;
	
	private static final long serialVersionUID = 1L;
	
	private short Default1;
	
	private int count;
	
	private JNData[] DataArray = new JNData[ARRAYLEN];	

	public short getDefault1() {
		return Default1;
	}

	public void setDefault1(short default1) {
		Default1 = default1;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public JNData[] getDataArray() {
		return DataArray;
	}

	public void setDataArray(JNData[] dataArray) {
		DataArray = dataArray;
	}
	
}
