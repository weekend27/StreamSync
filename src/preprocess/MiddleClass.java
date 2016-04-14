package preprocess;

import dataClass.ConfData;

public class MiddleClass {

	static
	{
		System.load(ConfData.readProperties().libpreprocessPath);	
	}
	public static native void setPackageNO(int i);
	public static native void setFrameNo(short i);
	public static native void setTBeamID(short i);
	public static native void settime(double i);
	public static native void setRid(short i);
	public static native void setTid(short i);
	public static native void setRBeamNum(short i);
	public static native void setPulseNum(short i);
	public static native void setdataType(short i);
	public static native void setajFlag(short i);
	public static native void setDefault1(short i);
	public static native void setDefault2(int i);
	public static native void settotalSize(int i);
	public static native void setwaveData(short[] i);
	
	public static native void execProcess();
	
	public static native int getPackageNO();
	public static native short getFrameNo();
	public static native short getTBeamID();
	public static native double gettime();
	public static native short getRid();
	public static native short getTid();
	public static native short getRBeamNum();
	public static native short getPulseNum();
	public static native short getdataType();
	public static native short getajFlag();
	public static native short getDefault1();
	public static native int getDefault2();
	public static native int gettotalSize1();
	public static native int gettotalSize2();
	public static native short[] getwaveData1();
	public static native short[] getwaveData2();
	
}
