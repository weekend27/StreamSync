package dataClass;

import java.io.Serializable;

public class Package implements Serializable{
	
	public static final int ARRAYLEN = 50;
	
	private static final long serialVersionUID = 1L;
	
	public short Default1;
	
	public int count;
	
	public JNData[] DataArray = new JNData[ARRAYLEN];	

}
