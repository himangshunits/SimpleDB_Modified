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
	      SimpleDB.init("simpleDB");   
	      
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
	   }
}