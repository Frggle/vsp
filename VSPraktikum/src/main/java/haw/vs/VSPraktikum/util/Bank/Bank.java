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
	private Map<String, Account> accountMap = new HashMap<>();
	/**
	 * TID -> Transaction
	 */
	private Map<Integer, Transaction> transactionMap = new HashMap<>();
	/**
	 * Int -> Transfer
	 */
	private Map<Integer, Transfer> transferMap = new HashMap<>();
	/**
	 * unique banknumber
	 */
	private String bankNumber;
	
	private int accountNumberCounter = 0;
	private int transferNumberCounter = 0;
//	private String bankURI;
//	private String playerURI;
	
	public Bank(int bankNumber) { //, String playerURI, String bankURI) {
		this.bankNumber = String.valueOf(bankNumber);
//		this.bankURI = bankURI;
//		this.playerURI = playerURI;
	}
	
	/**
	 * Die Bank erzeugt ein neues Konto und verwaltet dies
	 * @param saldo
	 * @return
	 */
	public Account createAccount(int saldo) {
		Account account = new Account(accountNumberCounter++, saldo); //, bankURI, playerURI);
		accountMap.put(account.getAccountNumber(), account);
		return account;
	}
	
	/**
	 * Gibt einen Account zurueck
	 * @param accNumber
	 * @return
	 */
	public Account getAccount(int accNumber) {
		return accountMap.get(accNumber);
	}
	
	/**
	 * Gibt die Banknummer zurueck
	 * @return
	 */
	public String getBankNumber() {
		return bankNumber;
	}
	
	/**
	 * Ueberweist einen Geldbetrag an ein Account
	 * @param to, Accountnummer als String
	 * @param amountS, Wert als String
	 * @throws NumberFormatException, wenn "amountS" ungueltig
	 * @return
	 */
	public boolean addAmount(String to, String amountS) throws NumberFormatException {
		int amountI = Integer.parseInt(amountS);
		
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
	 * @throws NumberFormatException, wenn "amountS" ungueltig
	 * @return
	 */
	public boolean subtractAmount(String from, String amountS) throws NumberFormatException {
		int amountI = Integer.parseInt(amountS);
		
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
	 * @throws NumberFormatException, wenn "amountS" ungueltig
	 * @return
	 */
	public boolean transfer(String from, String to, String amountS) throws NumberFormatException {
		int amountI = Integer.parseInt(amountS);
		
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
	 */
	public void createTransaction(int tid) {
		if(!transactionMap.containsKey(tid)) {
			Transaction transaction = new Transaction(tid);
			transactionMap.put(tid, transaction);
		}
	}
	
	/**
	 * Fuegt ein Transfer zu einer Transaction hinzu
	 * Transfer wird an dieser Stelle nicht ausgefuehrt
	 * Wenn Accountnumber gleich 0 -> wird als Bank interpretiert
	 * @param tid, Transaction ID
	 * @param from, Accountnumber
	 * @param to, Accountnumber
	 * @param amountS
	 */
	// TODO: ueberarbeiten -> Transfer Klasse fuehrt eines Transfer durch
	public boolean addTransfer(int tid, int from, int to, String amountS) {
		if(from == to) {
			return false;
		}
		if(from == 0) {
			Account bankAcc = new Account(accountNumberCounter++, Integer.MAX_VALUE/2);
			// foo
		} else if(to == 0) {
			Account bankAcc = new Account(accountNumberCounter++, Integer.MAX_VALUE/2);
			// bar
		}
		int amount = convertAmountToInt(amountS);
		if(amount == Integer.MIN_VALUE) {
			return false;
		}
		if(!transactionMap.containsKey(tid)) {
			return false;
		}
		if(accountMap.containsKey(String.valueOf(from)) && accountMap.containsKey(String.valueOf(to))) {
			Account accFrom = accountMap.get(String.valueOf(from));
			Account accTo = accountMap.get(String.valueOf(to));
			Transfer transfer = new Transfer(accFrom, accTo, amount);
//			transactionMap.put(tid, ??);
		}
		return false;
	}
	
	/**
	 * Fuehrt alle einzelnen Transfers einer Transaction aus
	 * @param tid
	 * @return boolean, ob alle Transfer erfolgreich ausgefuehrt
	 */
	public boolean commitTransaction(int tid) {
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
	public void deleteTransaction(int tid) {
		transactionMap.remove(tid);
	}
}
