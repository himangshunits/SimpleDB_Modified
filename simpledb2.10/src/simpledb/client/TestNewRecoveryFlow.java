package simpledb.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ListIterator;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.remote.RemoteDriver;
import simpledb.remote.RemoteDriverImpl;
import simpledb.server.SimpleDB;
import simpledb.tx.recovery.LogRecord;
import simpledb.tx.recovery.LogRecordIterator;
import simpledb.tx.recovery.RecoveryMgr;

public class TestNewRecoveryFlow {

	public static void main(String args[]) throws Exception {
		// configure and initialize the database
		SimpleDB.init(args[0]);   
		
		testBufferReplacementPolicy();
		/*
		System.out.println("database server ready for Recovery Flow Testing");
		//Gget the buffer manager
		BufferMgr bufferMgr = SimpleDB.bufferMgr();
		// Creating a Block.
		Block blk1 = new Block("block_file", 1);
		Buffer buff = bufferMgr.pin(blk1);
		
		//Create a RecoveryManager with id = 123.
		int txid = 123;
		RecoveryMgr rm = new RecoveryMgr(txid);
		//Commit a transaction .
		rm.commit();
		//Recover a transaction.
		rm.recover();
		//Sample setInt
		int lsn1 = rm.setInt(buff, 4, 1234);
		int lsn2 = rm.setString(buff, 4, "Himangshu");
		
		buff.setInt(4, 1234, txid, lsn1);
		//Flushing all transactions
		bufferMgr.flushAll(txid);
		//Using Log Record Iterator to print records .
		LogRecordIterator it = new LogRecordIterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		System.out.println("-------------------");
		while(it.hasPrevious()){
			System.out.println(it.previous()); 
		}	   
		*/   
	}

	private static void testBufferReplacementPolicy() {
		// TODO Auto-generated method stub
		BufferMgr bufferMgr = new BufferMgr(8);
		
		System.out.println("---------TEST-1----------");
		// 1. Create a list of files-blocks.
		Block[] blks = new Block[12];
		for (int i = 0; i < blks.length; i++) {
			blks[i] = new Block("file-block", i+1);
		}
		System.out.println("Created File Blocks.");
		
		System.out.println();
		System.out.println("---------TEST-2----------");
		// 2. Check the number of available buffers initially. All should be available as none of them have been pinned yet.
		System.out.println(bufferMgr.available());
		
		System.out.println();
		System.out.println("---------TEST-3----------");
		Buffer[] buffers = new Buffer[8];
		// 3. Keep pinning buffers one by one and check the number of available buffers.
		for (int i = 0; i < buffers.length; i++) {
			buffers[i] = bufferMgr.pin(blks[i]);
			System.out.println("Available buffers : " + bufferMgr.available() + "\n");
		}
		
		System.out.println();
		System.out.println("---------TEST-4----------");
		// 4. When all buffers have been pinned, if pin request is made again, throw an exception
		try {
			// Should throw exception
			System.out.println("No available buffers. Still attempting to pin a block.");
			Buffer buff = bufferMgr.pin(blks[8]);
			System.out.println("Unexpected behavior : Exception Expected.");
		} catch (simpledb.buffer.BufferAbortException e) {
			// Expected behavior.
			System.out.println("****** Buffer Abort Exception thrown - TimedOut ******");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("---------TEST-5----------");
		// 5. Unpin a few buffers and see if you are still getting an exception or not.
		try {
			System.out.println("Unpinned buffer 7");
			bufferMgr.unpin(buffers[6]);
			System.out.println("Unpinned buffer 6");
			bufferMgr.unpin(buffers[5]);
			Buffer buff = bufferMgr.pin(blks[8]);
			System.out.println("Successfully pinned a buffer after unpinning couple of buffers");
		} catch (simpledb.buffer.BufferAbortException e) {
			// Unexpected behavior.
			System.out.println("****** Buffer Abort Exception thrown - TimedOut ******");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("---------TEST-6----------");
		// 6. Try to pin a new buffer again, and check your replacement policy while seeing which currently unpinned buffer is replaced.
		try {
			System.out.println("Unpinned buffer 3");
			bufferMgr.unpin(buffers[2]);
			System.out.println("Unpinned buffer 2");
			bufferMgr.unpin(buffers[1]);
			System.out.println("Available buffers : " + bufferMgr.available());
			System.out.println("Buffers 2, 3 and 7 are available. Unpinned in reverse order. Thus, according to replacement policy, Buffer 2 should be replaced first, followed by Buffer 3 and Buffer 7.");
			
			bufferMgr.pin(blks[9]);
			bufferMgr.pin(blks[10]);
			bufferMgr.pin(blks[11]);
			System.out.println("Replacement Policy validated.");
		} catch (simpledb.buffer.BufferAbortException e) {
			// Unexpected behavior.
			System.out.println("****** Buffer Abort Exception thrown - TimedOut ******");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}