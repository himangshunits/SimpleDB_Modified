package simpledb.client;

import java.util.ArrayList;
import java.util.Collections;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.tx.recovery.LogRecordIterator;
import simpledb.tx.recovery.RecoveryMgr;
/*
 	1. Create a block and pin it to a buffer.
	2. Create a recovery manager for a transaction ( txid=123)
	3. Use setInt and setString to set logs for the transaction with txid=123.
	4. Use LogRecordIterator it.next() to see the logs are read in forward manner and prints
	old and new value.
	5. Use multiple blocks and repeat the above steps.
	For eg: Consider there is a transaction with txid=123.
	Create two blocks blk1 and blk2. Use setInt() and setString() on both blocks.
	Use LogRecordIterator it.next() to see the logs are read in forward manner from one
	block to other block. 
  
 */
public class TestNewRecoveryFlow {
	static BufferMgr bfm;
	static RecoveryMgr rm_321;
	static RecoveryMgr rm_123;
	static Block blk[] = new Block[4];
	static Buffer buff[] = new Buffer[4];
	static int lsn[] = new int[4];
	public static void testLogIterator()
	{
		  int txid_123 = 123;
		  int txid_321 = 321;
		  // Create a RecoveryManager with id = 123.
		  rm_123 = new RecoveryMgr(txid_123);
		  rm_321 = new RecoveryMgr(txid_321);
	    
	      // Use setInt and setString to set logs for the transaction with txid=123.
	      lsn[0] = rm_123.setInt(buff[0],    40, 1234);
	      lsn[1] = rm_123.setString(buff[1], 50, "Himangshu");
	      
	      buff[0].setInt(40, 1234, txid_123, lsn[0]);
	      buff[1].setString(50, "Himangshu", txid_123, lsn[1]);      
	      
	      bfm.flushAll(txid_123);
	      
	      
	      // Use setInt and setString to set logs for the transaction with txid=321.
	      lsn[2] = rm_321.setInt(buff[2], 40, 4321);
	      lsn[3] = rm_321.setString(buff[3], 50, "Rahul");
	      
	      buff[0].setInt(40, 4321, txid_321, lsn[2]);
	      buff[1].setString(50, "Rahul", txid_321, lsn[3]);
	         
	      
	      bfm.flushAll(txid_321);
	      
	      ArrayList<String> forwardList =  new ArrayList<String>();
	      ArrayList<String> reverseList =  new ArrayList<String>();
	      //Using Log Record Iterator to print records.
	      LogRecordIterator it = new LogRecordIterator();
	      while(it.hasNext()){
	    	  reverseList.add(it.next().toString());
	      }
	      System.out.println("-------------------");
	      while(it.hasPrevious()){
	    	  forwardList.add(it.previous().toString());
	      }
	      Collections.reverse(reverseList);
	      System.out.println(forwardList);
	      System.out.println(reverseList);
	      System.out.println("The Equality Status = " + reverseList.equals(forwardList));
	}
	public static void testRecoveryFlow(){
		int txid_123 = 123;
		  int txid_321 = 321;
		  // Create a RecoveryManager with id = 123.
		  rm_123 = new RecoveryMgr(txid_123);
		  rm_321 = new RecoveryMgr(txid_321);
	    
	      // Use setInt and setString to set logs for the transaction with txid=123.
	      lsn[0] = rm_123.setInt(buff[0],    40, 1234);
	      lsn[1] = rm_123.setString(buff[1], 50, "Himangshu");
	      
	      buff[0].setInt(40, 1234, txid_123, lsn[0]);
	      buff[1].setString(50, "Himangshu", txid_123, lsn[1]);      
	      
	      bfm.flushAll(txid_123);
	      
	      rm_123.commit();
	      
	      // Use setInt and setString to set logs for the transaction with txid=321.
	      lsn[2] = rm_321.setInt(buff[2], 40, 4321);
	      lsn[3] = rm_321.setString(buff[3], 50, "Rahul");
	      
	      buff[0].setInt(40, 4321, txid_321, lsn[2]);
	      buff[1].setString(50, "Rahul", txid_321, lsn[3]);
	        
	      
	      bfm.flushAll(txid_321);
	      
	      rm_321.commit();
	      rm_321.recover();
	      
	      
	      
	      ArrayList<String> forwardList =  new ArrayList<String>();
	      ArrayList<String> reverseList =  new ArrayList<String>();
	      //Using Log Record Iterator to print records .
	      LogRecordIterator it = new LogRecordIterator();
	      while(it.hasNext()){
	    	  reverseList.add(it.next().toString());
	      }
	      System.out.println("The Equality Status = " + reverseList);
	}
	public static void main(String args[]) throws Exception {
	      // configure and initialize the database
	      SimpleDB.init(args[0]);   
	      
	      System.out.println("database server ready for Recovery Flow Testing");
	      //Get the buffer manager
	      bfm = SimpleDB.bufferMgr();
	      for(int i=0;i<4;i++)
	      {
	    	  blk[i] = new Block("block_file", i+1);
	    	  
	    	  buff[i] = bfm.pin(blk[i]);
	      }
	      rm_123 = new RecoveryMgr(123);
	      rm_321 = new RecoveryMgr(321);
	      testLogIterator();
	      testRecoveryFlow();
	}
}