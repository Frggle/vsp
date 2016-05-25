package haw.vs.VSPraktikum.util.Bank;

import java.util.HashMap;
import java.util.Map;

public class Bank {
	private Map<String, Account> accountMap = new HashMap<>();
	private String bankNumber;
	private Map<Integer, Transaction> transactionMap = new HashMap<>();
	private int accountNumberCounter = 0;
//	private String bankURI;
//	private String playerURI;
	
	public Bank(int bankNumber) { //, String playerURI, String bankURI) {
		this.bankNumber = String.valueOf(bankNumber);
//		this.bankURI = bankURI;
//		this.playerURI = playerURI;
	}
	
	public Account createAccount(int saldo) {
		Account account = new Account(accountNumberCounter++, saldo); //, bankURI, playerURI);
		accountMap.put(account.getAccountNumber(), account);
		return account;
	}
	
	public Account getAccount(int accNumber) {
		return accountMap.get(accNumber);
	}
	
	public String getBankNumber() {
		return bankNumber;
	}
	
	public boolean addAmount(int to, String amountS) {
		int amountI = convertAmountToInt(amountS);
		if(amountI == Integer.MAX_VALUE) {
			return false;
		}
		if(accountMap.containsKey(String.valueOf(to))) {
			Account acc = accountMap.get(String.valueOf(to));
			acc.addAmount(amountI);
			return true;
		}
		return false;
	}
	
	public boolean subtractAmount(int from, String amountS) {
		int amountI = convertAmountToInt(amountS);
		if(amountI == Integer.MAX_VALUE) {
			return false;
		}
		if(accountMap.containsKey(String.valueOf(from))) {
			Account acc = accountMap.get(String.valueOf(from));
			if(acc.subtractAmount(amountI)) {
				return true;	
			}
		}
		return false;
	}
	
	public boolean transfer(int from, int to, String amountS) {
		int amountI = convertAmountToInt(amountS);
		if(amountI == Integer.MAX_VALUE) {
			return false;
		}
		if(accountMap.containsKey(String.valueOf(from)) && accountMap.containsKey(String.valueOf(to))) {
			Account accFrom = accountMap.get(String.valueOf(from));
			Account accTo = accountMap.get(String.valueOf(to));
			if(accFrom.subtractAmount(amountI)) {
				accTo.addAmount(amountI);
				return true;
			}
		} else {
			return false;
		}
		return false;
	}
	
	/**
	 * Convert a String to int
	 * invalid int -> MAX_VALUE
	 * @param amount
	 * @return converted String
	 */
	private int convertAmountToInt(String amount) {
		int amountAsInt = 0;
		try {
			amountAsInt = Integer.parseInt(amount);
		} catch(NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
		return amountAsInt;
	}

	public void createTransaction(int tid) {
		if(!transactionMap.containsKey(tid)) {
			Transaction transaction = new Transaction(tid);
			transactionMap.put(tid, transaction);
		}
	}
	
	// TODO: wie wird kenntlich gemacht, dass es sich um einen Transfer zwischen Bank und Player handelt?
	public void addTransfer(int tid, int from, int to, int amount) {
		
	}
	
	public void commitTransaction(int tid) {
		if(transactionMap.containsKey(tid)) {
			Transaction transaction = transactionMap.get(tid);
			// TODO: einzelnen Transfers ausfuehren
		}
	}
	
	public void deleteTransaction(int tid) {
		transactionMap.remove(tid);
	}
}
