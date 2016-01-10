package com.bigdata.sync;

public interface Master {
	void processMsgFirst(short id);
	void processMsgLast(short id);
	void processMsgAwake(short id);
	void sleep(short id, long millis);
}