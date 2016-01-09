package com.bigdata.DataClass;

public class JNData {
    public JamSignalData jamData;
    public NonJamRTSData nonJamData;
    public boolean hasJam;
    public boolean hasNonJam;
    private short Default1;
    private int Default2;
    public JNData(){ 
    	
    }
	public short getDefault1() {
		return Default1;
	}
	public void setDefault1(short default1) {
		Default1 = default1;
	}
	public int getDefault2() {
		return Default2;
	}
	public void setDefault2(int default2) {
		Default2 = default2;
	}

}
