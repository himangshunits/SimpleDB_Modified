package simpledb.client;
import simpledb.server.SimpleDB;

public class TestCasesDriver {
	public static void main(String args[]) throws Exception {
		// configure and initialize the database
		SimpleDB.init(args[0]);
		TestNewBufferManagementFlow.testBufferReplacementPolicy();
		TestNewRecoveryFlow.testRecovery();
	}	
}