package simpledb.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ListIterator;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.remote.RemoteDriver;
import simpledb.remote.RemoteDriverImpl;
import simpledb.tx.recovery.LogRecord;
import simpledb.tx.recovery.LogRecordIterator;
import simpledb.tx.recovery.RecoveryMgr;

public class TestNewRecoveryFlow {

	public static void main(String args[]) throws Exception {
	      // configure and initialize the database
	      SimpleDB.init(args[0]);
	      
	      // create a registry specific for the server on the default port
	      Registry reg = LocateRegistry.createRegistry(1099);
	      
	      // and post the server entry in it
	      RemoteDriver d = new RemoteDriverImpl();
	      reg.rebind("simpledb", d);
	      
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
	   }
}
