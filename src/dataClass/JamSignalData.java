package dataClass;

/**
 * Created by cloud on 15-12-29.
 * 受干扰信号数据包格式
 */
public class JamSignalData {
	
	public int PackageNO = 0;
    public short FrameNo = 0;
    public short TBeamID = 0;
    public double	time = 0.0;
    public short	Rid  = 0;
    public short	Tid = 0;
    public short	RBeamNum = 0;
    public short	PulseNum = 0;
    public short	dataType = 0;
    public short ajFlag = 0;
    public short Default1 = 0;
    public int	Default2 = 0;
    public int	totalSize = 0;
    public short[] waveData = new short[20000000];

}
