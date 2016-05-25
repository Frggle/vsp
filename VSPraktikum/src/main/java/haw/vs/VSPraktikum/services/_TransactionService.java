package haw.vs.VSPraktikum.services;

import java.util.HashMap;
import java.util.Map;
import haw.vs.VSPraktikum.util.Bank._Transaction;

public class _TransactionService {
	
	private static _TransactionService transService = new _TransactionService();
	private static int uriCounter = 0;
	private static Map<String, _Transaction> transactionMap = new HashMap<>();	// Trans-URI -> Transaction
	
	private _TransactionService() { }
	
	public static _TransactionService getInstance() {
		return transService;
	}
	
	public void createTransaction(String bankuri, String reason, String player) {
		_Transaction newTrans = new _Transaction(uriCounter++, bankuri, reason, player);
		transactionMap.put(newTrans.getUri(), newTrans);
	}
	
	public static Map<String, _Transaction> getTransactionMap() {
		return transactionMap;
	}
}
