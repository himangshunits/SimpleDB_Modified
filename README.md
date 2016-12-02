# SimpleDB_Modified

### Unity ID : 

Himangshu Ranjan Borah - hborah

Rahul Shah - rshah5

Akshat Shah - ashah7  

Akash Mehta - amehta7


### List of files Changed : 

[Github Repo - SimpleDB_Modified](https://github.com/himangshunits/SimpleDB_Modified)

*Note* : We have made some changes in the directory structure of the simpledb server and simpledbclient so that it's convenient to import the structure to eclipse or any other IDE and start using it instantly. For importing into Eclipse,

1. Unzip the Files in te ZIP archive.
2. In Eclipse, import from exsiting source, and point to the unzipped folder above, it will show 2 projects, import both.
3. Open simpledb.client package, and right click on TestCasesDriver.java, add a run configuration with arguments of your choice, this argument value will be the folder in your home directory where SimpleDB's control files will be stored.
4. Run or Debug with this configuration, this will simulate all the required Test Flows.

```
simpledb2.10/src/simpledb/buffer/BasicBufferMgr.java
simpledb2.10/src/simpledb/buffer/Buffer.java
simpledb2.10/src/simpledb/buffer/BufferMgr.java
simpledb2.10/src/simpledb/client/CreateStudentDB.java
simpledb2.10/src/simpledb/client/TestCasesDriver.java
simpledb2.10/src/simpledb/client/TestNewBufferManagementFlow.java
simpledb2.10/src/simpledb/client/TestNewRecoveryFlow.java
simpledb2.10/src/simpledb/log/LogIterator.java
simpledb2.10/src/simpledb/log/LogMgr.java
simpledb2.10/src/simpledb/server/Startup.java
simpledb2.10/src/simpledb/tx/recovery/CheckpointRecord.java
simpledb2.10/src/simpledb/tx/recovery/CommitRecord.java
simpledb2.10/src/simpledb/tx/recovery/LogRecord.java
simpledb2.10/src/simpledb/tx/recovery/LogRecordIterator.java
simpledb2.10/src/simpledb/tx/recovery/RecoveryMgr.java
simpledb2.10/src/simpledb/tx/recovery/RollbackRecord.java
simpledb2.10/src/simpledb/tx/recovery/SetIntRecord.java
simpledb2.10/src/simpledb/tx/recovery/SetStringRecord.java
simpledb2.10/src/simpledb/tx/recovery/StartRecord.java
```

## Testing

The entry point for testing the functionality is [TestCasesDriver.java](https://github.com/himangshunits/SimpleDB_Modified/blob/master/simpledb2.10/src/simpledb/client/TestCasesDriver.java) which has two children : [TestNewBufferManagementFlow.java](https://github.com/himangshunits/SimpleDB_Modified/blob/master/simpledb2.10/src/simpledb/client/TestNewBufferManagementFlow.java) for Buffer Replacement Policy and [TestNewRecoveryFlow.java](https://github.com/himangshunits/SimpleDB_Modified/blob/master/simpledb2.10/src/simpledb/client/TestNewRecoveryFlow.java) for LogRecordIterator and Recovery.

### Testing for Part 1 - Buffer Replacement Policy

Please check the testBufferReplacementPolicy() function in [TestNewBufferManagementFlow.java](https://github.com/himangshunits/SimpleDB_Modified/blob/master/simpledb2.10/src/simpledb/client/TestNewBufferManagementFlow.java)

A test suite is written which simulates a sample test scenario:  

1. A new buffer manager is created with 8 buffers and a list of file-blocks are created. 
2. Number of available buffers are confirmed.
3. All buffers are pinned with first 8 blocks and number of available buffers are printed at each stage.
4. Upon the next attempt of pinning a buffer, BufferAbortException is thrown as expected.
5. Couple of buffers are unpinned and again an attempt to pin a buffer is made. Buffer pinned successfully.
6. Replacement policy tested by correctly outputting which buffer is pinned next.

```
---------TEST-1----------
Created File Blocks.

---------TEST-2----------
8

---------TEST-3----------
New buffer allocated
Buffer used is 1
Available buffers : 7

New buffer allocated
Buffer used is 2
Available buffers : 6

New buffer allocated
Buffer used is 3
Available buffers : 5

New buffer allocated
Buffer used is 4
Available buffers : 4

New buffer allocated
Buffer used is 5
Available buffers : 3

New buffer allocated
Buffer used is 6
Available buffers : 2

New buffer allocated
Buffer used is 7
Available buffers : 1

New buffer allocated
Buffer used is 8
Available buffers : 0


---------TEST-4----------
No available buffers. Still attempting to pin a block.
****** Buffer Abort Exception thrown - TimedOut ******

---------TEST-5----------
Unpinned buffer 7
Unpinned buffer 6
Buffer used is 6
Successfully pinned a buffer after unpinning couple of buffers

---------TEST-6----------
Unpinned buffer 3
Unpinned buffer 2
Available buffers : 3
Buffers 2, 3 and 7 are available. Unpinned in reverse orders. Thus, according to replacement policy, Buffer 2 should be replaced first, followed by Buffer 3 and Buffer 7.
Buffer used is 2
Buffer used is 3
Buffer used is 7
Replacement Policy validated.
```

### Testing for Part 2 - LogRecordIterator and Recovery

We have made some significant changes in the structure of the code. Upon realization of the limitation of the Iterator Class - which has the capability of going only in forward direction, we changed it to the new ListIterator Class - which has the capabilities of moving in both forward and backward direction.


The test scenario is as follows:

1. The transaction 1 is committed and the transaction 2 is not committed and thus, the changes made by transaction 1 are restored. 
2. The transaction 1 is committed and the transaction 2 is also committed and thus, the changes made by transaction 2 persists. 

```
Output for LogIterator:
--------
The Equality for reverse and forward logs = true

Output for Recovery:
--------
The Equality Status = true
Flow1 :: Contents of Block before uncommitted transaction
The INTEGER is = 1234
The STRING is = Hello
Flow1 :: Contents of Block after uncommitted transaction
The INTEGER is = 1234
The STRING is = Hello
Flow2 :: Contents of Block before committed transaction
The INTEGER is = 1234
The STRING is = Hello
Flow2 :: Contents of Block after committed transaction
The INTEGER is = 4321
The STRING is = World
```
