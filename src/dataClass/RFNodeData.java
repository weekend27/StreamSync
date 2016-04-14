package dataClass;

import java.io.Serializable;

public class RFNodeData implements Serializable{
	
	private static final long serialVersionUID = 5577489929299975884L;
	public int PackageNO = 0;           				/*数据包编号*/
    public short FrameNo = 0;              					/*帧号*/
    public short TBeamID = 0;              					/*发射波位编号*/
    public double time = 0.0;              					/*发射波位开始扫描时间*/
    public short Rid = 0;                  							/*接收节点ID*/
    public short Tid = 0;                  							/*发射节点ID*/
    public short RBeamNum = 0;             				/*接收波束数量*/
    public short PulseNum = 0;             					/*脉冲数量 1~1000*/
    public short dataType = 0;           					/*数据类型 1：信号级；2：读文件；3：点迹；
	                                   														4：干扰信号；5：检测统计量（无干扰）；
	                                   														6：检测统计量（抗干扰后）*/
    public short ajFlag = 0;             						/*抗干扰标志 0：不抗干扰；1：启用抗干扰措施*/
    public short Default1 = 0;          					/*预留字段1 发射波位序号(处于同一发射波位内数据包一致，从1开始，按序增加)*/
    public int Default2 = 0;            						/*预留字段2 标记在每个发射波位内，此接收节点产生的数据包个数（0-7）*/
    public int totalSize = 0;           					/*回波数据总长度*/
    public short[] waveData = new short[20000000];         				/*目标回波数据 保存20个通道、30个脉冲的数据*/
    
}
