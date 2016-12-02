# SimpleDB_Modified

### Unity ID : 
Himangshu Borah - hborah
Rahul Shah - rshah5
Akshat Shah - ashah7  
Akash Mehta - amehta7

### List of files Changed : 

[TODO - run ```git diff --name-only dedc91f3da1643e8802a4431a632e187f0f4acf3``` after final commit. ]

### Testing for Part 1 - Buffer Replacement Policy

Please check the testBufferReplacementPolicy() function in [TestFlow.java](https://github.com/himangshunits/SimpleDB_Modified/blob/master/simpledb2.10/src/simpledb/client/TestNewBufferManagementFlow.java)

A test suite is written which simulates a sample test scenario:  

1. A new buffer manager is created with 8 buffers and a list of file-blocks are created. 
2. Number of available buffers are confirmed.
3. All buffers are pinned with first 8 blocks and number of available buffers are printed at each stage.
4. Upon the next attempt of pinning a buffer, BufferAbortException is thrown as expected.
5. Couple of buffers are unpinned and again an attempt to pin a buffer is made. Buffer pinned successfully.
6. Replacement policy tested by correctly outputting which buffer is pinned next.


### Testing for Part 2 - LogRecordIterator and Recovery

[TODO - Add description]


Running Instruction:
Import this project into Eclipse and Run as Java Application -> simpledb.client programs.
                                                            
