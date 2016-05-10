package haw.vs.VSPraktikum.services;

import java.util.HashMap;
import java.util.Map;
import haw.vs.VSPraktikum.util.Bank.Transaction;

public class TransactionService {
	
	private static TransactionService transService = new TransactionService();
	private static int uriCounter = 0;
	private static Map<String, Transaction> transactionMap = new HashMap<>();	// Trans-URI -> Transaction
	
	private TransactionService() { }
	
	public static TransactionService getInstance() {
		return transService;
	}
	
	public void createTransaction(String bankuri, String reason, String player) {
		Transaction newTrans = new Transaction(uriCounter++, bankuri, reason, player);
		transactionMap.put(newTrans.getUri(), newTrans);
	}
	
	public static Map<String, Transaction> getTransactionMap() {
		return transactionMap;
	}
}
