package com.bigdata.DataClass;

public class RFNodeData {
	public long PackageNO;           				/*数据包编号*/
    public int FrameNo;              					/*帧号*/
    public int TBeamID;              					/*发射波位编号*/
    public double time;              					/*发射波位开始扫描时间*/
    public int Rid;                  							/*接收节点ID*/
    public int Tid;                  							/*发射节点ID*/
    public int RBeamNum;             				/*接收波束数量*/
    public int PulseNum;             					/*脉冲数量 1~1000*/
    public short dataType;           					/*数据类型 1：信号级；2：读文件；3：点迹；
	                                   														4：干扰信号；5：检测统计量（无干扰）；
	                                   														6：检测统计量（抗干扰后）*/
    public short ajFlag;             						/*抗干扰标志 0：不抗干扰；1：启用抗干扰措施*/
    public short Default1;          					/*预留字段1 发射波位序号(处于同一发射波位内数据包一致，从1开始，按序增加)*/
    public int Default2;            						/*预留字段2 标记在每个发射波位内，此接收节点产生的数据包个数（0-7）*/
    public long totalSize;           					/*回波数据总长度*/
    public short[] waveData;         				/*目标回波数据 保存20个通道、30个脉冲的数据*/
    
    public RFNodeData() {
    	
    }
}
