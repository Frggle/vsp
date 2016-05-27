package haw.vs.VSPraktikum.util.Bank;

import java.util.HashMap;
import java.util.Map;

/**
 * Eine Bank verwaltet seine Accounts und Transactions
 * @author Marc
 *
 */
public class Bank {
	/**
	 * AccountNumber -> Account
	 */
	private Map<Integer, Account> accountMap = new HashMap<>();
	/**
	 * TID -> Transaction
	 */
	private Map<Integer, Transaction> transactionMap = new HashMap<>();
	/**
	 * int -> Transfer
	 */
	private Map<Integer, Transfer> transferMap = new HashMap<>();
	/**
	 * unique banknumber
	 */
	private int bankNumber;
	
	private int accountNumberCounter = 0;
	private int transferNumberCounter = 0;
//	private String bankURI;
//	private String playerURI;
	
	public Bank(int bankNumber) { //, String playerURI, String bankURI) {
		this.bankNumber = bankNumber;
//		this.bankURI = bankURI;
//		this.playerURI = playerURI;
	}
	
	/**
	 * Die Bank erzeugt ein neues Konto und verwaltet dies
	 * @param saldo
	 * @throws NumberFormatException, wenn "saldo" ungueltig
	 * @return
	 */
	public Account createAccount(String saldoS) throws NumberFormatException {
		int saldo = Integer.parseInt(saldoS);
		Account account = new Account(accountNumberCounter++, saldo); //, bankURI, playerURI);
		accountMap.put(account.getAccountNumber(), account);
		return account;
	}
	
	/**
	 * Gibt einen Account zurueck
	 * @param accNumber
	 * @throws NumberFormatException, wenn "accNumber" ungueltig
	 * @return
	 */
	public Account getAccount(String accNumberS) throws NumberFormatException {
		int accNumber = Integer.parseInt(accNumberS);
		return accountMap.get(accNumber);
	}
	
	/**
	 * Gibt die Banknummer zurueck
	 * @return
	 */
	public String getBankNumber() {
		return String.valueOf(bankNumber);
	}
	
	/**
	 * Ueberweist einen Geldbetrag an ein Account
	 * @param to, Accountnummer als String
	 * @param amountS, Wert als String
	 * @throws NumberFormatException, wenn "amountS" oder "toS" ungueltig
	 * @return
	 */
	public boolean addAmount(String toS, String amountS) throws NumberFormatException {
		int amountI = Integer.parseInt(amountS);
		int to = Integer.parseInt(toS);
		
		if(accountMap.containsKey(to)) {
			Account acc = accountMap.get(to);
			Account bankAcc = new Account(accountNumberCounter++);
			Transfer transfer = new Transfer(bankAcc, acc, amountI);
			transferMap.put(transferNumberCounter++, transfer);
			return transfer.run();
		}
		return false;
	}
	
	/**
	 * Zieht einen Geldbetrag von einem Account ab (sofern ausreichender Saldo)
	 * @param from, Accountnummer als String
	 * @param amountS, Wert als String
	 * @throws NumberFormatException, wenn "amountS" oder "fromS" ungueltig
	 * @return
	 */
	public boolean subtractAmount(String fromS, String amountS) throws NumberFormatException {
		int amountI = Integer.parseInt(amountS);
		int from = Integer.parseInt(fromS);
		
		if(accountMap.containsKey(from)) {
			Account acc = accountMap.get(from);
			Account bankAcc = new Account(accountNumberCounter++);
			Transfer transfer = new Transfer(acc, bankAcc, amountI);
			transferMap.put(transferNumberCounter++, transfer);
			return transfer.run();
		}
		return false;
	}
	
	/**
	 * Ueberweist einen Geldbetrag von einem Account zum Anderen (sofern ausreichender Saldo)
	 * @param from, Accountnummer als String
	 * @param to, Accountnummer als String
	 * @param amountS, Wert als String
	 * @throws NumberFormatException, wenn "amountS", "fromS" oder "toS" ungueltig
	 * @return
	 */
	public boolean transfer(String fromS, String toS, String amountS) throws NumberFormatException {
		int amountI = Integer.parseInt(amountS);
		int from = Integer.parseInt(fromS);
		int to = Integer.parseInt(toS);
		
		if(accountMap.containsKey(from) && accountMap.containsKey(to)) {
			Account accFrom = accountMap.get(from);
			Account accTo = accountMap.get(to);
			Transfer transfer = new Transfer(accFrom, accTo, amountI);
			transferMap.put(transferNumberCounter++, transfer);
			return transfer.run();
		}
		return false;
	}
	
	/**
	 * Erzeugt eine Transaction und verwaltet diese
	 * @param tid, Transaction ID
	 * @throws NumberFormatException, wenn "tid" ungueltig
	 */
	public void createTransaction(String tid) throws NumberFormatException {
		int tidI = Integer.parseInt(tid);
		
		if(!transactionMap.containsKey(tidI)) {
			Transaction transaction = new Transaction(tidI);
			transactionMap.put(tidI, transaction);
		}
	}
	
	/**
	 * Fuegt einen Transfer zu einer Transaction hinzu
	 * Transfer wird an dieser Stelle nicht ausgefuehrt
	 * Wenn Accountnumber gleich 0 -> wird als Bank interpretiert
	 * @param tid, Transaction ID
	 * @param from, Accountnumber
	 * @param to, Accountnumber
	 * @param amountS
	 * @throws NumberFormatException, wenn "tidS", "fromS", "toS" oder "amountS" ungueltig
	 * @return boolean ob Transfer erfolgreich zur Transaction hinzugefuegt wurde
	 */
	public boolean addTransferToTransaction(String tidS, String fromS, String toS, String amountS) throws NumberFormatException {
		int amount = Integer.parseInt(amountS);
		int tid = Integer.parseInt(tidS);
		int from = Integer.parseInt(fromS);
		int to = Integer.parseInt(toS);
		Transaction transaction = null;
		
		if(from == to) {
			return false;
		}
		if(!transactionMap.containsKey(tid)) {
			return false;
		} else {
			transaction = transactionMap.get(tid);
		}
		if(from == 0 && accountMap.containsKey(to)) {
			Account bankAcc = new Account(accountNumberCounter++);
			Account accTo = accountMap.get(to);
			Transfer transfer = new Transfer(bankAcc, accTo, amount);
			transaction.addTransfer(transfer);
			return true;
		} else if(to == 0 && accountMap.containsKey(from)) {
			Account bankAcc = new Account(accountNumberCounter++);
			Account fromAcc = accountMap.get(from);
			Transfer transfer = new Transfer(fromAcc, bankAcc, amount);
			transaction.addTransfer(transfer);
			return true;
		}
		if(accountMap.containsKey(from) && accountMap.containsKey(to)) {
			Account accFrom = accountMap.get(from);
			Account accTo = accountMap.get(to);
			Transfer transfer = new Transfer(accFrom, accTo, amount);
			transaction.addTransfer(transfer);
			return true;
		}
		return false;
	}
	
	/**
	 * Fuehrt alle einzelnen Transfers einer Transaction aus
	 * @param tidS
	 * @return boolean, ob alle Transfer erfolgreich ausgefuehrt
	 */
	public boolean commitTransaction(String tidS) throws NumberFormatException {
		int tid = Integer.parseInt(tidS);
		
		if(transactionMap.containsKey(tid)) {
			Transaction transaction = transactionMap.get(tid);
			if(transaction.commit()) {
				transactionMap.remove(tid);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Entfernt eine Transaction, mitsamt aller enthaltenen Transfers
	 * @param tid, Transaction ID
	 */
	public void deleteTransaction(String tidS) throws NumberFormatException {
		int tid = Integer.parseInt(tidS);
		transactionMap.remove(tid);
	}
}
