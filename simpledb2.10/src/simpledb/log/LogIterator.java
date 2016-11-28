package simpledb.log;

import static simpledb.file.Page.INT_SIZE;
import simpledb.file.*;
import simpledb.server.SimpleDB;
import java.util.ListIterator;

/**
 * A class that provides the ability to move through the
 * records of the log file in reverse order.
 * 
 * @author Edward Sciore
 */
class LogIterator implements ListIterator<BasicLogRecord> {
   private Block blk;
   private Page pg = new Page();
   private int currentrec;
   
   /**
    * Creates an iterator for the records in the log file,
    * positioned after the last log record.
    * This constructor is called exclusively by
    * {@link LogMgr#iterator()}.
    */
   LogIterator(Block blk) {
      this.blk = blk;
      pg.read(blk);
      currentrec = pg.getInt(LogMgr.LAST_POS);
   }
   
   /**
    * Determines if the current log record
    * is the earliest record in the log file.
    * @return true if there is an earlier record
    */
   public boolean hasNext() {
      return currentrec > LogMgr.LAST_POS || blk.number() > 0;
   }
   
   /**
    * Moves to the next log record in reverse order.
    * If the current log record is the earliest in its block,
    * then the method moves to the next oldest block,
    * and returns the log record from there.
    * @return the next earliest log record
    */
   public BasicLogRecord next() {
      if (currentrec == LogMgr.LAST_POS)
         moveToNextBlock();
      currentrec = pg.getInt(currentrec);
      return new BasicLogRecord(pg, currentrec + 2*INT_SIZE);
   }
   
   
   
   @Override
	public boolean hasPrevious() {
		return pg.getInt(currentrec + INT_SIZE) != -1 || blk.number() < (SimpleDB.fileMgr().size(blk.fileName()) - 1);
	}
	
	@Override
	public BasicLogRecord previous() {
		if (pg.getInt(currentrec + INT_SIZE) == -1) 
			moveToPrevBlock();
		BasicLogRecord basicLogRecord = new BasicLogRecord(pg, currentrec + 2*INT_SIZE);
		currentrec = pg.getInt(currentrec + INT_SIZE);
		return basicLogRecord;
	}
	
   
   /**
    * Moves to the next log block in reverse order,
    * and positions it after the last record in that block.
    */
   private void moveToNextBlock() {
      blk = new Block(blk.fileName(), blk.number()-1);
      pg.read(blk);
      currentrec = pg.getInt(LogMgr.LAST_POS);
   }

   /**
    * Moves to the next log block in forward order,
    * and positions it after the first record in that block.
    */
   private void moveToPrevBlock() {
	   blk = new Block(blk.fileName(), blk.number()+1);
	   pg.read(blk);
	   currentrec = LogMgr.LAST_POS;
   }

	
	
	@Override
	public int nextIndex() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int previousIndex() {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void set(BasicLogRecord e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(BasicLogRecord e) {
		throw new UnsupportedOperationException();
		
	}
	@Override
	public void remove() {
	      throw new UnsupportedOperationException();
	}
}