package com.bigdata.sync;
public interface Master {
	void processMsgFirst(short id, int default2);
	void processMsgLast(short id);
	void processMsgAwake(short id);
}