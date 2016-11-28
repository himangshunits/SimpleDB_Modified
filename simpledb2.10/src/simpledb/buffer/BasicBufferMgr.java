package simpledb.buffer;

import java.util.HashMap;
import java.util.LinkedList;
//import java.util.HashSet;
import java.util.Queue;

import simpledb.file.Block;
import simpledb.file.FileMgr;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
	private HashMap<Block, Buffer> bufferPoolMap;
	private LinkedList<Buffer> freeBuffers;
	private Queue<Buffer> bufferQ;
	private int numAvailable;
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots and initializes data structures to map blocks to buffer.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
    BasicBufferMgr(int numbuffs) {
	   bufferPoolMap = new HashMap<Block, Buffer>();
	   freeBuffers = new LinkedList<Buffer>();
	   numAvailable = numbuffs;
	   // Initially none of the buffers are allocated
	   for(int i=0;i<numbuffs;i++)
	   {
		   freeBuffers.add(new Buffer(i+1));
	   }
	   bufferQ = new LinkedList<Buffer>();
	   
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      for (Buffer buff : bufferPoolMap.values())
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   private synchronized void assignToBlock(Buffer buff, Block blk)
   {
	   // When assign this new block, we flush the page's data to disk and add this page to this buffer
	   buff.assignToBlock(blk);
	   bufferPoolMap.put(blk, buff);
	   bufferQ.add(buff);
   }
   synchronized Buffer pin(Block blk) 
   {
     // Try to find the block in the buffer-pool
	 Buffer buff = findExistingBuffer(blk);
     if( buff != null)
     {
    	  System.out.println("Block already in buffer at: " + buff.getIndex());
     }
     else 
     {
    	 // Choose a buffer from the buffer-pool using replacement policy 
    	 buff = chooseUnpinnedBuffer();
    	 if(buff == null)
    		  	return buff;
    	 System.out.println("Buffer used is " + buff.getIndex());
         assignToBlock(buff, blk); 
	     
	      
	     if (!buff.isPinned())
	        numAvailable--;
     }
     buff.pin();
     return buff;
   }
   
   private synchronized void assignToNew(Buffer buff, String filename, PageFormatter fmtr)
   {
	   buff.assignToNew(filename, fmtr);
	   bufferPoolMap.put(buff.block(), buff);
	   bufferQ.add(buff);
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
	   
	   Buffer buff = chooseUnpinnedBuffer();
	   if(buff == null)
		  	return buff;

	   System.out.println("Buffer used is " + buff.getIndex());
	   assignToNew(buff, filename, fmtr);
	   numAvailable--;
	   buff.pin();
       return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
      {
    	  numAvailable++;
      }
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private Buffer findExistingBuffer(Block blk) {
	   return getMapping(blk);
   }
   
   /**
    * Returns the the buffer which is not pinned and has stayed longest in the bufferpool. All the buffers 
    * are pinned and not free the function returns null. 
    * @return a unpinned buffer
    */
   private Buffer chooseUnpinnedBuffer() 
   {
	   for(Buffer buff: bufferQ)
	   {
		   if(!buff.isPinned())
		   {
			   // Remove the previous entry of the this buffer's block in the bufferPoolMap
			   bufferPoolMap.remove(buff.block());
			   bufferQ.remove(buff);
			   return buff;
		   }
	    }
		if(freeBuffers.isEmpty())
			return null;
		else
		{
			System.out.println("New buffer allocated");
			return freeBuffers.removeFirst();
		}    
   }
   /**
   * Determines whether the map has a mapping from
   * the block to some buffer.
   * @param the block to use as a key
   * @returns true if there is a mapping; false otherwise
   */
   boolean containsMapping(Block blk) {
   		return bufferPoolMap.containsKey(blk);
   }
   /**
   * Returns the buffer that the map maps the specified block to.
   * @param the block to use as a key
   * @return the buffer mapped to if there is a mapping; null otherwise
   */
   Buffer getMapping(Block blk) 
   {
   		return bufferPoolMap.get(blk);
   }
}


