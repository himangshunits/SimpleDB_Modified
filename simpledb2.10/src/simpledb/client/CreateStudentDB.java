package simpledb.client;
import java.util.Iterator;
import java.util.ListIterator;

import simpledb.file.Block;
import simpledb.server.SimpleDB;
import simpledb.tx.recovery.*;

public class CreateStudentDB {
    public static void main(String[] args) {
    	String database_name = "simpleDB";
    	SimpleDB.init(database_name);
    	
    	// Testing Buffer Management
    	/*
    	Block[] blks = new Block[9];
    	for(int i=0;i<9;i++)
    		blks[i] = new Block("filename", i+1);
    	BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
    	Buffer[] buffers = new Buffer[8];
    	for(int i=0;i<8;i++)
    		buffers[i] = basicBufferMgr.pin(blks[i]);
    	try
    	{
    		basicBufferMgr.pin(blks[8]);
    	}
    	catch(BufferAbortException e)
    	{
    		basicBufferMgr.unpin(buffers[1]);
    		basicBufferMgr.unpin(buffers[2]);
    		basicBufferMgr.pin(blks[8]);	
    	}*/
    	//Block blk1 = new Block("filename", 1);
    	//
    	//rm.commit();
    	//rm.recover();
    	
    	RecoveryMgr rm = new RecoveryMgr(123);
    	// Testing forward iterator
    	ListIterator<LogRecord> iter = new LogRecordIterator();
    	while(iter.hasNext())
    	{
    		System.out.println(iter.next());
    	}
    	System.out.println("-------------");
    	while(iter.hasPrevious())
    	{
    		System.out.println(iter.previous());
    	}
	}
}
